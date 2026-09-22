package jdbcc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection doConnectDB() {

        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");
            String dbHost = System.getenv("DB_HOST");

            if (dbUser == null || dbPassword == null) {
                throw new Exception("Set DB_USER and DB_PASSWORD env variables!");
            }

            con = DriverManager.getConnection("jdbc:mysql://" + dbHost + "/javaproject", dbUser, dbPassword);
            System.out.println("Connected to DataBase....................");
        }
        catch (Exception exp) {
            exp.printStackTrace();
        }

        return con;
    }

    /*
    public static void main(String[] args) {
        doConnectDB();
    }
    */
}
