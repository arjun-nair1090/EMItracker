package com.microfinance.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton JDBC connection to the microfinance MySQL database.
 * Update DB_URL, DB_USER, and DB_PASSWORD to match your environment.
 */
public class DBConnection {

    private static final String DB_URL      = "jdbc:mysql://localhost:3306/microfinance_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "root";  // ← change to your MySQL password

    private static Connection connection = null;

    private DBConnection() {}

    /**
     * Returns the shared Connection instance, creating it if necessary.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Add it to the classpath.", e);
            }
        }
        return connection;
    }

    /** Closes the connection (call on app exit). */
    public static void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ignored) {}
        }
    }
}
