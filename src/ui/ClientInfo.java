package ui;

import com.jgoodies.forms.layout.CellConstraints;
import engine.SQLActions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

/**
 * Created by Y500 on 18.05.2016.
 */
public class ClientInfo extends UiWindow {
    private JTextField input;
    private JButton ok;
    private Vector<JTextField> output = new Vector<JTextField>();
    private Vector<JLabel> outputLabels = new Vector<JLabel>();

    public ClientInfo() {
        String[] specs = {"10dlu, center:pref, 10dlu, center:pref, 10dlu", "10dlu, center:pref, 10dlu, center:pref, 20dlu, center:pref, 10dlu," +
                                "center:pref, 5dlu, center:pref, 5dlu, center:pref, 5dlu, center:pref, 5dlu, center:pref, 5dlu, center:pref, 5dlu," +
                                "center:pref, 10dlu"};
        mainFrame = UiElements.createFrame(new Dimension(330,400));
        framePanel = UiElements.createPanel(new Dimension(330,400), specs);
        Dimension labelSize = new Dimension(170,20);
        Dimension textFieldSize = new Dimension(100,20);
        JLabel inputTitle = UiElements.createLabel(labelSize, "Введите № пользователя");
        JLabel outputTitle = UiElements.createLabel(labelSize, "Данные о пользователе");
        input = UiElements.createTextField(textFieldSize);
        ok = UiElements.createButton(new Dimension(70,20), "OK");
        for(int i = 0; i < 7; ++i) {
            output.add(UiElements.createTextField(textFieldSize));
            output.get(i).setEditable(false);
        }
        outputLabels.add(UiElements.createLabel(labelSize, "Фамилия"));
        outputLabels.add(UiElements.createLabel(labelSize, "Имя"));
        outputLabels.add(UiElements.createLabel(labelSize, "Отчество"));
        outputLabels.add(UiElements.createLabel(labelSize, "Серия паспорта"));
        outputLabels.add(UiElements.createLabel(labelSize, "Номер паспорта"));
        outputLabels.add(UiElements.createLabel(labelSize, "Штраф"));
        outputLabels.add(UiElements.createLabel(labelSize, "Книг на руках"));
        CellConstraints cc = new CellConstraints();
        framePanel.add(inputTitle, cc.xy(2,2));
        framePanel.add(input, cc.xy(2,4));
        framePanel.add(ok, cc.xy(4,4));
        framePanel.add(outputTitle, cc.xy(2,6));
        for(int i = 0; i < 7; ++i) {
            framePanel.add(outputLabels.get(i), cc.xy(2,8+i*2));
            framePanel.add(output.get(i), cc.xy(4,8+i*2));
        }
        mainFrame.setContentPane(framePanel);
        mainFrame.setTitle("О пользователе");
    }

    public void show() {
        mainFrame.pack();
        mainFrame.setVisible(true);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = input.getText();
                if(!value.isEmpty()) {
                    try {
                        int id = Integer.parseInt(value);
                        Vector<String> result = SQLActions.getClientInfo(id);
                        if(!result.isEmpty()) {
                            for(int i = 0; i < 5; ++i) {
                                output.get(i).setText(result.get(i));
                            }
                            output.get(6).setText(Long.toString(SQLActions.callProcedureWithNumInNumOut("{call APP_CLIENT_BOOKS_AMOUNT(?,?)}", id)));
                            output.get(5).setText(Long.toString(SQLActions.callProcedureWithNumInNumOut("{call APP_CLIENT_FINE(?,?)}", id)));
                        } else {
                            for(int i = 0; i < 7; ++i) {
                                output.get(i).setText("Wrong input");
                            }
                        }
                    } catch (NumberFormatException exp) {
                        for(int i = 0; i < 7; ++i) {
                            output.get(i).setText("Wrong input");
                        }
                    }
                }
            }
        });

        input.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                for(int i = 0; i < 7; ++i) {
                    output.get(i).setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
    }
}
