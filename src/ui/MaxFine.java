package ui;

import com.jgoodies.forms.layout.CellConstraints;
import engine.SQLActions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by Y500 on 18.05.2016.
 */
public class MaxFine extends UiWindow {
    private JTextField maxFine;
    private JButton compute;

    public MaxFine() {
        String[] specs = {"10dlu, center:pref, 10dlu, center:pref, 10dlu", "10dlu, center:pref, 10dlu, center:pref, 10dlu"};
        mainFrame = UiElements.createFrame(new Dimension(400,150));
        framePanel = UiElements.createPanel(new Dimension(400,150), specs);
        Dimension labelSize = new Dimension(200,20);
        Dimension textFieldSize = new Dimension(100,20);
        JLabel inputTitle = UiElements.createLabel(labelSize, "Максимальный штраф");
        maxFine = UiElements.createTextField(textFieldSize);
        maxFine.setEditable(false);
        compute = UiElements.createButton(new Dimension(100,20), "Вычислить");
        CellConstraints cc = new CellConstraints();
        framePanel.add(inputTitle, cc.xy(2,2));
        framePanel.add(maxFine, cc.xy(2,4));
        framePanel.add(compute, cc.xy(4,4));
        mainFrame.setContentPane(framePanel);
        mainFrame.setTitle("Максимальный штраф");
    }

    public void show() {
        mainFrame.pack();
        mainFrame.setVisible(true);

        compute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long result = SQLActions.callProcedureWithNumOut("{call APP_MAX_FINE(?)}");
                if (result != -1) {
                    maxFine.setText(Long.toString(result));
                } else {
                    maxFine.setText("Error");
                }
            }
        });
    }
}
