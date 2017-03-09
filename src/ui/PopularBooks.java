package ui;

import com.jgoodies.forms.layout.CellConstraints;
import engine.EngineConfiguration;
import engine.SQLActions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Created by Y500 on 18.05.2016.
 */
public class PopularBooks extends UiWindow {
    private JButton get;
    private Vector<JTextField> output = new Vector<JTextField>();
    private Vector<JLabel> outputLabels = new Vector<JLabel>();

    public PopularBooks() {
        String[] specs = {"10dlu, center:pref, 5dlu, center:pref, 10dlu, center:pref, 10dlu",
                            "10dlu, center:pref, 10dlu, center:pref, 5dlu, center:pref, 5dlu, center:pref, 10dlu"};
        mainFrame = UiElements.createFrame(new Dimension(450,200));
        framePanel = UiElements.createPanel(new Dimension(450,200), specs);
        Dimension labelSize = new Dimension(20,20);
        Dimension textFieldSize = new Dimension(200,20);
        JLabel inputTitle = UiElements.createLabel(new Dimension(150,20), "Рейтинг книг");
        get = UiElements.createButton(new Dimension(150,20), "Получить данные");
        for(int i = 0; i < 3; ++i) {
            output.add(UiElements.createTextField(textFieldSize));
            output.get(i).setEditable(false);
            outputLabels.add(UiElements.createLabel(labelSize, Integer.toString(i+1)+"."));
        }
        CellConstraints cc = new CellConstraints();
        framePanel.add(inputTitle, cc.xy(4,2));
        framePanel.add(get, cc.xy(6,4));
        for(int i = 0; i < 3; ++i) {
            framePanel.add(outputLabels.get(i), cc.xy(2,4+i*2));
            framePanel.add(output.get(i), cc.xy(4,4+i*2));
        }
        mainFrame.setContentPane(framePanel);
        mainFrame.setTitle("3 самых популярных книги");
    }

    public void show() {
        mainFrame.pack();
        mainFrame.setVisible(true);

        get.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<String> result = SQLActions.callProcedureWithDBMS("{call APP_THREE_TOP_BOOKS()}", 3);
                if (result.size() > 0) {
                    output.get(0).setText(result.get(0).replace(EngineConfiguration.BOOKS_RESPONSE_REPLACE, ""));
                    if (result.size() > 1) {
                        output.get(1).setText(result.get(1).replace(EngineConfiguration.BOOKS_RESPONSE_REPLACE, ""));
                        if (result.size() > 2) {
                            output.get(2).setText(result.get(2).replace(EngineConfiguration.BOOKS_RESPONSE_REPLACE, ""));
                        }
                    }
                }
            }
        });
    }
}
