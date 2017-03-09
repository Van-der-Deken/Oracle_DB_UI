package engine;

import java.sql.*;
import java.util.Vector;

/**
 * Created by Y500 on 22.04.2016.
 */
public abstract class SQLActions {
    public static Vector<String> callProcedureWithDBMS(String callQuery, int amount) {
        try {
            CallableStatement procedure = Resources.connection.prepareCall(callQuery);
            procedure.execute();
            return getLinesFromDBMS(amount);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long callProcedureWithNumOut(String callQuery) {
        try {
            CallableStatement procedure = Resources.connection.prepareCall(callQuery);
            procedure.registerOutParameter(1, Types.NUMERIC);
            procedure.execute();
            return procedure.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long callProcedureWithNumInNumOut(String callQuery, int inParam) {
        try {
            CallableStatement procedure = Resources.connection.prepareCall(callQuery);
            procedure.setInt(1, inParam);
            procedure.registerOutParameter(2, Types.NUMERIC);
            procedure.execute();
            return procedure.getLong(2);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean enableDBMSOutput(int buffer_size) {
        try {
            CallableStatement stmt = Resources.connection.prepareCall("{call sys.dbms_output.enable(?) }");
            stmt.setInt(1,buffer_size);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Vector<String> getLinesFromDBMS(int amount) {
        Vector<String> lines = new Vector<String>();
        try {
            CallableStatement stmt = Resources.connection.prepareCall("{call sys.dbms_output.get_line(?,?)}");
            stmt.registerOutParameter(1,java.sql.Types.VARCHAR);
            stmt.registerOutParameter(2,java.sql.Types.NUMERIC);
            while (amount > 0) {
                stmt.execute();
                if(stmt.getInt(2) != 0) {
                    break;
                } else {
                    lines.add(stmt.getString(1));
                }
            }
            return lines;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean connectToDB(String login, String password) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", login, password);
            Resources.connection = con;
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Vector<Vector<String>> getTableData(String tableName, Vector<String> columns) {
        try {
            Statement statement = Resources.connection.createStatement();
            String query = "SELECT * FROM "+tableName;
            ResultSet result = statement.executeQuery(query);
            Vector<Vector<String>> data = new Vector<Vector<String>>();
            while (result.next()) {
                data.add(new Vector<String>());
                for(int i = 0; i < columns.size(); ++i) {
                    if(columns.get(i).contains("DATE")) {
                        Timestamp current = result.getTimestamp(i + 1);
                        if(current == null) {
                            data.lastElement().add("null");
                        } else {
                            data.lastElement().add(result.getTimestamp(i + 1).toString());
                        }
                    } else {
                        data.lastElement().add(result.getString(i+1));
                    }
                }
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String update(String tableName, Vector<String> columns, Vector<String> oldValues, Vector<String> newValues) {
        String command = "UPDATE "+tableName+" SET";
        for(int i = 0; i < columns.size(); ++i) {
            if(!newValues.get(i).isEmpty()) {
                command += " "+columns.get(i)+"=";
                try {
                    Long.parseLong(newValues.get(i));
                    command += newValues.get(i)+",";
                } catch (NumberFormatException ex) {
                    if(columns.get(i).contains("DATE")) {
                        command += "TO_TIMESTAMP('" + newValues.get(i) + "', 'YYYY-MM-DD HH24:MI:SS.FF'),";
                    } else {
                        command += "'" + newValues.get(i) + "',";
                    }
                }
            }
        }
        command = command.substring(0, command.length()-1);
        command += " WHERE ";
        for(int i = 0; i < columns.size(); ++i) {
            command += columns.get(i)+"=";
            try {
                Long.parseLong(oldValues.get(i));
                command += oldValues.get(i)+" AND ";
            } catch (NumberFormatException ex) {
                if(columns.get(i).contains("DATE")) {
                    if(oldValues.get(i).equals("null")) {
                        command = command.substring(0, command.length()-columns.get(i).length());
                    } else {
                        command += "TO_TIMESTAMP('" + oldValues.get(i) + "', 'YYYY-MM-DD HH24:MI:SS.FF') AND ";
                    }
                } else {
                    command += "'" + oldValues.get(i) + "' AND ";
                }
            }
        }
        command = command.substring(0, command.length()-5);
        System.out.println(command);
        try {
            Statement statement = Resources.connection.createStatement();
            statement.executeUpdate(command);
            return "";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage().split("\n")[0].split(":")[1];
        }
    }

    public static String insert(String tableName, Vector<String> columns, Vector<String> newValues) {
        String command = "INSERT INTO "+tableName+"(";
        String values = "";
        String titles = "";
        for(int i = 0; i < columns.size(); ++i) {
            if(!newValues.get(i).isEmpty()) {
                titles += columns.get(i)+",";
                try {
                    Long.parseLong(newValues.get(i));
                    values += newValues.get(i)+",";
                } catch (NumberFormatException ex) {
                    if(columns.get(i).contains("DATE")) {
                        values += "TO_TIMESTAMP('" + newValues.get(i) + "', 'YYYY-MM-DD HH24:MI:SS.FF'),";
                    } else {
                        values += "'" + newValues.get(i) + "',";
                    }
                }
            }
        }
        command += titles.substring(0,titles.length()-1)+") VALUES ("+values.substring(0,values.length()-1)+")";
        System.out.println(command);
        try {
            Statement statement = Resources.connection.createStatement();
            statement.executeUpdate(command);
            return "";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage().split("\n")[0].split(":")[1];
        }
    }

    public static String delete(String tableName, Vector<String> columns, Vector<String> oldValues) {
        String command = "DELETE FROM "+tableName+" WHERE ";
        for(int i = 0; i < columns.size(); ++i) {
            command += columns.get(i)+"=";
            try {
                Long.parseLong(oldValues.get(i));
                command += oldValues.get(i)+" AND ";
            } catch (NumberFormatException ex) {
                if(columns.get(i).contains("DATE")) {
                    if(!oldValues.get(i).equals("null")) {
                        command += "TO_TIMESTAMP('" + oldValues.get(i) + "', 'YYYY-MM-DD HH24:MI:SS.FF') AND ";
                    } else {
                        command = command.substring(0, command.length() - columns.get(i).length());
                    }
                } else {
                    command += "'" + oldValues.get(i) + "' AND ";
                }
            }
        }
        command = command.substring(0, command.length()-5);
        System.out.println(command);
        try {
            Statement statement = Resources.connection.createStatement();
            statement.executeUpdate(command);
            return "";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage().split("\n")[0].split(":")[1];
        }
    }

    public static Vector<String> getColumnNames(String tableName) {
        String command = "SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME='"+tableName+"'";
        try {
            Statement statement = Resources.connection.createStatement();
            ResultSet result = statement.executeQuery(command);
            Vector<String> buffer = new Vector<String>();
            Vector<String> output = new Vector<String>();
            while (result.next()) {
                buffer.add(result.getString(1));
            }
            for(int i = buffer.size()-1; i >= 0; --i) {
                output.add(Resources.guiTransformer.get(buffer.get(i)));
            }
            return output;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Vector<String> getClientInfo(int id) {
        Vector<String> output = new Vector<String>();
        String command = "SELECT * FROM CLIENTS WHERE ID="+Integer.toString(id);
        try {
            Statement statement = Resources.connection.createStatement();
            ResultSet result = statement.executeQuery(command);
            result.next();
            output.add(result.getString(3));
            output.add(result.getString(2));
            output.add(result.getString(4));
            output.add(result.getString(5));
            output.add(result.getString(6));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static void initGUIHash() {
        Resources.guiTransformer.put("ID", "№");
        Resources.guiTransformer.put("CLIENT_ID","№ билета");
        Resources.guiTransformer.put("BOOK_ID","№ книги");
        Resources.guiTransformer.put("DATE_BEG","Взята");
        Resources.guiTransformer.put("DATE_END","Вернуть до");
        Resources.guiTransformer.put("DATE_RET","Возвращена");
        Resources.guiTransformer.put("FIRST_NAME","Имя");
        Resources.guiTransformer.put("LAST_NAME","Фамилия");
        Resources.guiTransformer.put("PATHER_NAME","Отчество");
        Resources.guiTransformer.put("PASSPORT_SERIA","Серия пасспорта");
        Resources.guiTransformer.put("PASSPORT_NUM","№ пасспорта");
        Resources.guiTransformer.put("NAME","Название");
        Resources.guiTransformer.put("CNT","Количество");
        Resources.guiTransformer.put("TYPE_ID","№ типа");

    }
}
