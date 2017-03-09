package ui;

import com.jgoodies.forms.layout.CellConstraints;
import engine.SQLActions;

import java.awt.*;
import java.util.Vector;

/**
 * Created by Y500 on 23.04.2016.
 */
public class TableView extends AbstractTableView {
    public TableView(String tableName) {
        name = tableName;
        Vector<String> columns = SQLActions.getColumnNames(name);
        Vector<Vector<String>> data = SQLActions.getTableData(name, columns);
        buttonSize = new Dimension(100, 20);
        Dimension labelSize = new Dimension(300, 20);
        Dimension tableSize = new Dimension(50+100*columns.size(), 300);
        Dimension frameSize = new Dimension(tableSize.width+200, 410);
        String[] framePanelSpecs = {"20dlu, center:pref, 5dlu, center:pref, 20dlu", "5dlu, center:pref, 10dlu, center:pref, 20dlu"};
        mainFrame = UiElements.createFrame(frameSize);
        messages = UiElements.createLabel(labelSize, "");
        CellConstraints cc = new CellConstraints();
        createButtonPanel();
        framePanel = UiElements.createPanel(frameSize, framePanelSpecs);
        tableModel = new TableAdapter(data, columns);
        tableModel.enableCellEdit();
        table = UiElements.createTable(tableModel);
        tablePane = UiElements.createScrollPane(tableSize, table);
        framePanel.add(messages, cc.xy(2, 2));
        framePanel.add(tablePane, cc.xy(2, 4));
        framePanel.add(buttonsPanel, cc.xy(4, 4));
        mainFrame.setContentPane(framePanel);
        setFrameTitle();
    }

    public void show() {
        enableListeners();
        showWindow();
    }
}
