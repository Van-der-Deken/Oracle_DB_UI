package ui;

import com.jgoodies.forms.layout.CellConstraints;
import engine.SQLActions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

/**
 * Created by Y500 on 13.11.2015.
 */
public class LogIn extends UiWindow {
    private JLabel messagesLabel;
    private JTextField login;
    private JPasswordField password;
    private JButton enter;

    public LogIn() {
        Dimension frameSize = new Dimension(343,200);
        Dimension fieldsSize = new Dimension(200,30);
        Dimension buttonSize = new Dimension(100,30);
        Dimension labelSize = new Dimension(75,30);
        String[] panelSpecs = {"10dlu, center:pref, 5dlu, center:pref, 10dlu",
                                "center:pref, center:pref, 10dlu, center:pref, 10dlu, center:pref, 10dlu"};
        mainFrame = UiElements.createFrame(frameSize);
        framePanel = UiElements.createPanel(frameSize, panelSpecs);
        messagesLabel = UiElements.createLabel(fieldsSize,"");
        JLabel loginLabel = UiElements.createLabel(labelSize,"Логин:");
        loginLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel passwordLabel = UiElements.createLabel(labelSize,"Пароль:");
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        login = UiElements.createTextField(fieldsSize);
        password = UiElements.createPasswordField(fieldsSize);
        enter = UiElements.createButton(buttonSize, "ОК");

        CellConstraints cc = new CellConstraints();
        framePanel.add(messagesLabel, cc.xy(4,1));
        framePanel.add(loginLabel, cc.xy(2,2));
        framePanel.add(passwordLabel, cc.xy(2, 4));
        framePanel.add(login, cc.xy(4,2));
        framePanel.add(password, cc.xy(4, 4));
        framePanel.add(enter, cc.xy(4, 6));
        mainFrame.setContentPane(framePanel);
    }

    public void show() {
        mainFrame.setTitle("Log in");
        mainFrame.pack();
        mainFrame.setVisible(true);

        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messagesLabel.setText("");
                String userLogin = login.getText();
                try {
                    MessageDigest cryptor = MessageDigest.getInstance("MD5");
                    byte[] crypt = cryptor.digest(userLogin.getBytes());
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                }
                if(SQLActions.connectToDB(userLogin, new String(password.getPassword()))) {
                    SQLActions.enableDBMSOutput(1024);
                    close();
                    MainView view = new MainView("JOURNAL");
                    view.show();
                } else {
                    messagesLabel.setText("Неверный логин или пароль");
                }
            }
        });

        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                messagesLabel.setText("");
            }
        });

        password.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                messagesLabel.setText("");
            }
        });
    }
}
