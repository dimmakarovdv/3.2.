package ru.netology.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLHelper {
    public static void clearAllTables() throws SQLException {
        Connection conn = DatabaseConnector.connect();
        try {
            execute(conn, "DELETE FROM auth_codes");
            execute(conn, "DELETE FROM card_transactions");
            execute(conn, "DELETE FROM cards");
            execute(conn, "DELETE FROM users");
        } finally {
            DatabaseConnector.close(conn);
        }
    }

    public static void createUser(String username, String password) throws SQLException {
        Connection conn = DatabaseConnector.connect();
        try {
            String uuid = java.util.UUID.randomUUID().toString();
            execute(conn, "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, 'active')", uuid, username, password);
        } finally {
            DatabaseConnector.close(conn);
        }
    }

    public static String getLastAuthCode() throws SQLException {
        Connection conn = DatabaseConnector.connect();
        try {
            String sql = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
            try (PreparedStatement statement = conn.prepareStatement(sql);
                 ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getString("code");
                }
                return null;
            }
        } finally {
            DatabaseConnector.close(conn);
        }
    }

    public static void generateAuthCode(String username) throws SQLException {
        Connection conn = DatabaseConnector.connect();
        try {
            // Получаем user_id
            String userId = null;
            String selectSql = "SELECT id FROM users WHERE login = ?";
            try (PreparedStatement statement = conn.prepareStatement(selectSql)) {
                statement.setString(1, username);
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        userId = result.getString("id");
                    }
                }
            }

            if (userId != null) {
                // Генерируем код
                int randomCode = (int) (Math.random() * 900000 + 100000);
                String uuid = java.util.UUID.randomUUID().toString();

                // Вставляем код
                String insertSql = "INSERT INTO auth_codes(id, user_id, code, created) VALUES (?, ?, ?, NOW())";
                try (PreparedStatement statement = conn.prepareStatement(insertSql)) {
                    statement.setString(1, uuid);
                    statement.setString(2, userId);
                    statement.setString(3, String.valueOf(randomCode));
                    statement.executeUpdate();
                }
            }
        } finally {
            DatabaseConnector.close(conn);
        }
    }

    private static void execute(Connection conn, String sql, Object... params) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            setParameters(statement, params);
            statement.executeUpdate();
        }
    }

    private static void setParameters(PreparedStatement statement, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}