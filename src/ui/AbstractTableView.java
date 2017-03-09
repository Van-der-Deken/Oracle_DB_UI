package ui;

import com.jgoodies.forms.layout.CellConstraints;
import engine.Resources;
import engine.SQLActions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by Y500 on 23.04.2016.
 */
public abstract class AbstractTableView extends UiWindow {
    protected String name;
    protected JLabel messages;
    protected JScrollPane tablePane;
    protected JTable table;
    protected TableAdapter tableModel;
    protected JButton add;
    protected JButton delete;
    protected JButton rollback;
    protected JButton commit;
    protected JPanel buttonsPanel;
    protected Dimension buttonSize;

    protected void enableListeners() {
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messages.setText("");
                Vector<String> empty = new Vector<String>();
                for(int i = 0; i < tableModel.getColumnCount(); ++i) {
                    empty.add("");
                }
                tableModel.addRow(empty);
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messages.setText("");
                int rowIndex = table.getSelectedRow();
                if(rowIndex == -1) {
                    messages.setText("Choose row for delete before");
                }
                tableModel.markForDelete(rowIndex);
            }
        });

        rollback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.clearDeleted();
                tableModel.clearInserted();
                tableModel.clearEdited();
                tableModel.setData(SQLActions.getTableData(name, tableModel.getColumnNames()));
                tableModel.fireTableDataChanged();
            }
        });

        commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messages.setText("");
                if(tableModel.editedEmpty() && tableModel.insertedEmpty() && tableModel.deletedEmpty()) {
                    messages.setText("Nothing to commit");
                } else {
                    if(update() && insert() && delete()) {
                        messages.setText("Transaction commited");
                    }
                    tableModel.clearDeleted();
                    tableModel.clearInserted();
                    tableModel.clearEdited();
                    tableModel.setData(SQLActions.getTableData(name, tableModel.getColumnNames()));
                    tableModel.fireTableDataChanged();
                }
            }
        });

        mainFrame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
                messages.setText("");
            }
        });
    }

    protected void setFrameTitle() {
        mainFrame.setTitle(name.charAt(0)+name.substring(1).toLowerCase());
    }

    protected void createButtonPanel() {
        String[] specs = {"10dlu, center:pref, 5dlu", "10dlu, center:pref, 10dlu, center:pref, 10dlu, center:pref, 10dlu, center:pref, 10dlu"};
        Dimension panelSize = new Dimension(200, 200);
        add = UiElements.createButton(buttonSize, "Добавить");
        delete = UiElements.createButton(buttonSize, "Удалить");
        rollback = UiElements.createButton(buttonSize, "Откатить");
        commit = UiElements.createButton(buttonSize, "ОК");
        buttonsPanel = UiElements.createPanel(panelSize, specs);
        CellConstraints cc = new CellConstraints();
        buttonsPanel.add(add, cc.xy(2, 2));
        buttonsPanel.add(delete, cc.xy(2, 4));
        buttonsPanel.add(rollback, cc.xy(2, 6));
        buttonsPanel.add(commit, cc.xy(2, 8));
    }

    protected boolean update() {
        Vector<Vector<String>> newData = tableModel.getData();
        Vector<Vector<String>> oldData = SQLActions.getTableData(name, tableModel.getColumnNames());
        for(String current : tableModel.getEditedCells()) {
            Vector<String> cords = new Vector<String>(Arrays.asList(current.split("#")));
            Vector<String> newValues = new Vector<String>();
            int rowIndex = Integer.parseInt(cords.get(0));
            Vector<String> row = newData.get(rowIndex);
            cords.remove(0);
            for(int i = 0; i < tableModel.getColumnCount(); ++i) {
                if(cords.contains(Integer.toString(i))) {
                    newValues.add(row.get(i));
                } else {
                    newValues.add("");
                }
            }
            String retcode = SQLActions.update(name, tableModel.getColumnNames(), oldData.get(rowIndex), newValues);
            if(!retcode.isEmpty()) {
                messages.setText(retcode);
                try {
                    Resources.connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        try {
            Resources.connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean insert() {
        Vector<Vector<String>> newData = tableModel.getData();
        for(Integer current : tableModel.getInserted()) {
            String retcode = SQLActions.insert(name, tableModel.getColumnNames(), newData.get(current));
            if(!retcode.isEmpty()) {
                messages.setText(retcode);
                try {
                    Resources.connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        try {
            Resources.connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean delete() {
        Vector<Vector<String>> oldData = SQLActions.getTableData(name, tableModel.getColumnNames());
        for(Integer current : tableModel.getDeleted()) {
            String retcode = SQLActions.delete(name, tableModel.getColumnNames(), oldData.get(current));
            if(!retcode.isEmpty()) {
                messages.setText(retcode);
                try {
                    Resources.connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        try {
            Resources.connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
