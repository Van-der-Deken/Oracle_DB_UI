package ui;

import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Y500 on 17.04.2016.
 */
public abstract class UiElements {
    public static JFrame createFrame(Dimension size) {
        JFrame output = new JFrame();
        //Convert inputs
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //Configure frame
        output.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        output.setMinimumSize(size);
        output.setPreferredSize(size);
        output.setMaximumSize(size);
        output.setSize(size);
        output.setResizable(false);
        output.setLocation(screenSize.width/2-output.getSize().width/2, screenSize.height/2-output.getSize().height/2);
        return output;
    }

    public static JPanel createPanel(Dimension size, String[] specs) {
        JPanel output = new JPanel();
        //Convert inputs
        FormLayout layout = new FormLayout(specs[0],specs[1]);
        //Configure panel
        output.setLayout(layout);
        output.setMinimumSize(size);
        output.setPreferredSize(size);
        output.setMaximumSize(size);
        output.setSize(size);
        output.setBackground(UiConfiguration.background);
        output.setForeground(UiConfiguration.foreground);
        return output;
    }

    public static JTextField createTextField(Dimension size) {
        JTextField output = new JTextField();
        //Configure textfield
        output.setMinimumSize(size);
        output.setPreferredSize(size);
        output.setMaximumSize(size);
        output.setSize(size);
        output.setBackground(UiConfiguration.fieldBackground);
        output.setForeground(UiConfiguration.foreground);
        output.setDisabledTextColor(UiConfiguration.disabledTextColor);
        output.setSelectedTextColor(UiConfiguration.selectedTextColor);
        output.setSelectionColor(UiConfiguration.selectionColor);
        output.setCaretColor(UiConfiguration.caretColor);
        output.setCaretPosition(0);
        output.setHorizontalAlignment(SwingConstants.CENTER);
        return output;
    }

    public static JPasswordField createPasswordField(Dimension size) {
        JPasswordField output = new JPasswordField();
        //Configure passwordfield
        output.setMinimumSize(size);
        output.setPreferredSize(size);
        output.setMaximumSize(size);
        output.setSize(size);
        output.setBackground(UiConfiguration.fieldBackground);
        output.setForeground(UiConfiguration.foreground);
        output.setDisabledTextColor(UiConfiguration.disabledTextColor);
        output.setSelectedTextColor(UiConfiguration.selectedTextColor);
        output.setSelectionColor(UiConfiguration.selectionColor);
        output.setCaretColor(UiConfiguration.caretColor);
        output.setCaretPosition(0);
        output.setHorizontalAlignment(SwingConstants.CENTER);
        return output;
    }

    public static JButton createButton(Dimension size, String title) {
        JButton output = new JButton();
        //Configure button
        output.setMinimumSize(size);
        output.setPreferredSize(size);
        output.setMaximumSize(size);
        output.setSize(size);
        output.setBackground(UiConfiguration.fieldBackground);
        output.setForeground(UiConfiguration.foreground);
        output.setText(title);
        return output;
    }

    public static JLabel createLabel(Dimension size, String text) {
        JLabel output = new JLabel();
        //Configure label
        output.setMinimumSize(size);
        output.setPreferredSize(size);
        output.setMaximumSize(size);
        output.setSize(size);
        output.setBackground(UiConfiguration.fieldBackground);
        output.setForeground(UiConfiguration.foreground);
        output.setHorizontalAlignment(SwingConstants.CENTER);
        output.setVerticalAlignment(SwingConstants.CENTER);
        output.setText(text);
        return output;
    }

    public static JComboBox createComboBox(Dimension size) {
        JComboBox output = new JComboBox();
        //Configure combo box
        output.setMinimumSize(size);
        output.setPreferredSize(size);
        output.setMaximumSize(size);
        output.setSize(size);
        output.setBackground(UiConfiguration.fieldBackground);
        output.setForeground(UiConfiguration.foreground);
        return output;
    }

    public static JTable createTable(TableAdapter model) {
        JTable output = new JTable(model);
        //Configure table
        output.setDefaultRenderer(String.class, model.getRenderer());
        output.getTableHeader().setBackground(Color.CYAN);
        output.setBackground(UiConfiguration.foreground);
        output.setForeground(UiConfiguration.fieldBackground);
        return output;
    }

    public static JScrollPane createScrollPane(Dimension size, Component in) {
        JScrollPane output = new JScrollPane(in);
        //Configure pane
        output.createHorizontalScrollBar();
        output.createVerticalScrollBar();
        output.setMinimumSize(size);
        output.setPreferredSize(size);
        output.setMaximumSize(size);
        output.setSize(size);
        output.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        output.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return output;
    }

    public static JCheckBox createCheckBox(Dimension size) {
        JCheckBox output = new JCheckBox("Show",true);
        output.setMinimumSize(size);
        output.setPreferredSize(size);
        output.setMaximumSize(size);
        output.setSize(size);
        output.setBackground(UiConfiguration.background);
        output.setForeground(UiConfiguration.foreground);
        return output;
    }

    public static JMenuBar createMenuBar() {
        JMenuBar output = new JMenuBar();
        output.setBackground(UiConfiguration.background);
        output.setForeground(UiConfiguration.foreground);
        return output;
    }

    public static JMenu createMenu(String title) {
        JMenu output = new JMenu(title);
        output.setBackground(UiConfiguration.background);
        output.setForeground(UiConfiguration.foreground);
        return output;
    }

    public static JMenuItem createMenuItem(String title) {
        JMenuItem output = new JMenuItem(title);
        output.setBackground(UiConfiguration.background);
        output.setForeground(UiConfiguration.foreground);
        return output;
    }
}
