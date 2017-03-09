package ui;

import javax.swing.*;

/**
 * Created by Y500 on 17.04.2016.
 */
public abstract class UiWindow {
    protected JFrame mainFrame;
    protected JPanel framePanel;

    public void showWindow() {
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public abstract void show();

    public void close() {
            mainFrame.dispose();
        }
}
