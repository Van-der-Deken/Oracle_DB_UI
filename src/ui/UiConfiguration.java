package ui;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Y500 on 17.04.2016.
 */
public interface UiConfiguration {
    //GUI constants
    public final int COMBOBOX = 1;
    public final int FIELD = 2;
    //Colors
    public final Color background = new Color(60,63,65);
    public final Color foreground = new Color(187,187,187);
    public final Color fieldBackground = new Color(69,73,74);
    public final Color caretColor = new Color(187, 187, 187);
    public final Color disabledTextColor = new Color(153,153,153);
    public final Color selectedTextColor = new Color(187,187,187);
    public final Color selectionColor = new Color(75,110,175);
    //Fonts
    public static Font titleFont = new Font("Segoe UI 12",Font.BOLD,14);
    //GUI Strings
    HashMap<String, String> UiGemini = new HashMap<String, String>();
}
