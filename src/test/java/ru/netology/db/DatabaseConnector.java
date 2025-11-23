package ru.netology.db;

import java.sql.*;

public class DatabaseConnector {
    private static Connection connection;

    public static Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
        }
        return connection;
    }

    public static void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Ошибка закрытия соединения: " + e.getMessage());
            }
        }
    }
}
