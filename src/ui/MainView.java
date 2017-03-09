package ui;

import com.jgoodies.forms.layout.CellConstraints;
import engine.SQLActions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Created by Y500 on 20.04.2016.
 */
public class MainView extends AbstractTableView {
    private JMenuBar menuBar;
    private JMenuItem clients;
    private JMenuItem books;
    private JMenuItem clientData;
    private JMenuItem maxFine;
    private JMenuItem popBooks;

    public MainView(String tableName) {
        menuBar = UiElements.createMenuBar();
        JMenu menu = UiElements.createMenu("Дополнительно");
        menu.setFont(UiConfiguration.titleFont);
        clients = UiElements.createMenuItem("Читатели");
        books = UiElements.createMenuItem("Книги");
        clientData = UiElements.createMenuItem("Данные о читателе");
        maxFine = UiElements.createMenuItem("Максимальный штраф");
        popBooks = UiElements.createMenuItem("3 самых популярных книги");
        menu.add(clients);
        menu.add(books);
        menu.add(clientData);
        menu.add(maxFine);
        menu.add(popBooks);
        menuBar.add(menu);

        name = tableName;
        Vector<String> columns = SQLActions.getColumnNames(name);
        Vector<Vector<String>> data = SQLActions.getTableData(name, columns);
        buttonSize = new Dimension(100, 20);
        Dimension labelSize = new Dimension(300, 20);
        Dimension tableSize = new Dimension(50+100*columns.size(), 300);
        Dimension frameSize = new Dimension(tableSize.width+200, 450);
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
        mainFrame.setJMenuBar(menuBar);
        setFrameTitle();
    }

    public void show() {
        enableListeners();
        showWindow();

        clients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableView view = new TableView("CLIENTS");
                view.show();
            }
        });

        books.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableView view = new TableView("BOOKS");
                view.show();
            }
        });

        clientData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientInfo info = new ClientInfo();
                info.show();
            }
        });

        maxFine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MaxFine fine = new MaxFine();
                fine.show();
            }
        });

        popBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PopularBooks popularBooks = new PopularBooks();
                popularBooks.show();
            }
        });
    }
}
