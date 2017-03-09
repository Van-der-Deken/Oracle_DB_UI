package ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Vector;

/**
 * Created by Y500 on 17.04.2016.
 */
public class TableAdapter extends AbstractTableModel {
    private Vector<Vector<String>> data = new Vector<Vector<String>>();
    private Vector<String> columnNames = new Vector<String>();
    private boolean editable = false;
    private Vector<String> edited = new Vector<String>();
    private Vector<Integer> inserted = new Vector<Integer>();
    private Vector<Integer> deleted = new Vector<Integer>();

    public class CellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setVerticalAlignment(CENTER);
            label.setHorizontalAlignment(CENTER);
            String cords = Integer.toString(row)+"#"+Integer.toString(column);
            if(inserted.contains(row)) {
                label.setBackground(Color.YELLOW);
                label.setForeground(Color.BLACK);
            } else if(deleted.contains(row)) {
                label.setBackground(Color.RED);
                label.setForeground(Color.WHITE);
            } else if(edited.contains(cords)) {
                label.setBackground(Color.BLUE);
                label.setForeground(Color.WHITE);
            } else {
                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
            }
            return label;
        }
    }

    public TableAdapter(Vector<Vector<String>> data, Vector<String> columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    public TableAdapter(String[][] data, String[] columnNames) {
        for(String[] row : data) {
            this.data.add(new Vector<String>());
            for(String current : row) {
                this.data.lastElement().add(current);
            }
        }
        for(String current : columnNames) {
            this.columnNames.add(current);
        }
    }

    public int getRowCount() {
        return data.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).get(columnIndex);
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public String getColumnName(int columnIndex) {
        return columnNames.get(columnIndex);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if(!value.equals(getValueAt(rowIndex, columnIndex))) {
            String cords = Integer.toString(rowIndex)+"#"+Integer.toString(columnIndex);
            if(!inserted.contains(rowIndex) && !deleted.contains(rowIndex)) {
                edited.add(cords);
            }
            data.get(rowIndex).set(columnIndex, (String) value);
            fireTableDataChanged();
        }
    }

    public void enableCellEdit() {
        editable = true;
    }

    public void disableCellEdit() {
        editable = false;
    }

    public void addRow(Vector<String> row) {
        if(row.size() > getColumnCount()) {
            //TODO Logger
            System.out.println("Too much elements");
        } else {
            data.add(row);
            inserted.add(data.size()-1);
            fireTableDataChanged();
        }
    }

    public void markForDelete(int index) {
        deleted.add(index);
        fireTableDataChanged();
    }

    public void clearInserted() {
        inserted.clear();
        fireTableDataChanged();
    }

    public void clearDeleted() {
        deleted.clear();
        fireTableDataChanged();
    }

    public void clearEdited() {
        edited.clear();
        fireTableDataChanged();
    }

    public boolean insertedEmpty() {
        return inserted.isEmpty();
    }

    public boolean editedEmpty() {
        return edited.isEmpty();
    }

    public boolean deletedEmpty() {
        return deleted.isEmpty();
    }

    public CellRenderer getRenderer() {
        return new CellRenderer();
    }

    public Vector<String> getColumnNames() {
        return columnNames;
    }

    public Vector<Vector<String>> getData() {
        return data;
    }

    public void setData(Vector<Vector<String>> newData) {
        data = newData;
    }

    public Vector<String> getEditedCells() {
        Vector<String> output = new Vector<String>();
        Vector<Integer> rows = new Vector<Integer>();
        for(String current : edited) {
            String[] cords = current.split("#");
            int index  = rows.indexOf(Integer.parseInt(cords[0]));
            if(index != -1) {
                output.setElementAt(output.get(index)+"#"+cords[1], index);
            } else {
                output.add(current);
                rows.add(Integer.parseInt(cords[0]));
            }
        }
        return output;
    }

    public Vector<Integer> getInserted() {
        return inserted;
    }

    public Vector<Integer> getDeleted() {
        return deleted;
    }
}