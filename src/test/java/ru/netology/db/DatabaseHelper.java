package ru.netology.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final DataSource dataSource;
    private static final QueryRunner runner = new QueryRunner();

    static {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/app");
        ds.setUsername("app");
        ds.setPassword("pass");
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setInitialSize(1);
        dataSource = ds;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void clearAllTables() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            runner.update(conn, "DELETE FROM auth_codes");
            runner.update(conn, "DELETE FROM card_transactions");
            runner.update(conn, "DELETE FROM cards");
            runner.update(conn, "DELETE FROM users");
        }
    }

    public static void createTestUser() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String checkUserSql = "SELECT COUNT(*) as count FROM users WHERE login = ?";
            Long count = runner.query(conn, checkUserSql, new ScalarHandler<>(), "vasya");
            if (count == null || count == 0) {
                String sql = "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, 'active')";
                runner.update(conn, sql,
                        "123e4567-e89b-12d3-a456-426614174000",
                        "vasya",
                        "$2a$10$Y3oH7lY5vDgGZ1n8QY3j.eXcXyZ5Z3Z3Z3Z3Z3Z3Z3Z3Z3Z3Z");
            }
        }
    }

    public static String getLastAuthCodeForUser(String username) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT ac.code " +
                    "FROM auth_codes ac " +
                    "JOIN users u ON ac.user_id = u.id " +
                    "WHERE u.login = ? " +
                    "ORDER BY ac.created DESC " +
                    "LIMIT 1";
            return runner.query(conn, query, new ScalarHandler<>(), username);
        } catch (SQLException e) {
            System.err.println("Ошибка при получении кода: " + e.getMessage());
            return null;
        }
    }
}