package at.htlhl;

import java.sql.DriverManager;

public class Connection {

    public static boolean checkConnection(){
        try {
            java.sql.Connection con = DriverManager.getConnection(
                    "jdbc:mysql://branmark.ddns.net:3306/snake", "snake", "python");
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
