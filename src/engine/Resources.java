package engine;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Created by Y500 on 22.04.2016.
 */
public abstract class Resources {
    public static Connection connection = null;
    public static HashMap<String, String> guiTransformer = new HashMap<String, String>();
}
