package Functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=WAREHOUSE_MANAGEMENT;encrypt=true;trustServerCertificate=true";
    private static final String USER = "thienphu";
    private static final String PASSWORD = "0963810449";

    // Method to get a database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
