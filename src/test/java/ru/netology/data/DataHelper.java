package ru.netology.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataHelper {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/app";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "pass";

    public static void cleanDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.prepareStatement("DELETE FROM auth_codes").executeUpdate();
            conn.prepareStatement("DELETE FROM card_transactions").executeUpdate();
            conn.prepareStatement("DELETE FROM cards").executeUpdate();
            conn.prepareStatement("DELETE FROM users").executeUpdate();
        }
    }

    public static void createUser(String username, String hashedPassword) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String uuid = java.util.UUID.randomUUID().toString();
            String sql = "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, 'active')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid);
            stmt.setString(2, username);
            stmt.setString(3, hashedPassword);
            stmt.executeUpdate();
        }
    }

    public static String getLastGeneratedCode() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
            var result = conn.prepareStatement(sql).executeQuery();
            if (result.next()) {
                return result.getString("code");
            }
        }
        throw new IllegalStateException("No codes found in database");
    }

    public static void generateAuthenticationCode(String username) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String uuid = java.util.UUID.randomUUID().toString();
            String sql = "INSERT INTO auth_codes(id, user_id, code) VALUES (?, ?, ?)";
            String selectSql = "SELECT id FROM users WHERE login=?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setString(1, username);
            var rs = selectStmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Пользователь не найден.");
            }
            String userId = rs.getString("id");
            int randomCode = (int) (Math.random() * 900000 + 100000);

            PreparedStatement insertStmt = conn.prepareStatement(sql);
            insertStmt.setString(1, uuid);
            insertStmt.setString(2, userId);
            insertStmt.setInt(3, randomCode);
            insertStmt.executeUpdate();
        }
    }
}