import engine.SQLActions;
import ui.LogIn;

/**
 * Created by Y500 on 15.04.2016.
 */
public class Main {
    public static void main(String[] args) {
        SQLActions.initGUIHash();
        LogIn logView = new LogIn();
        logView.show();
        System.out.println();
    }
}
