package ru.netology.data;

import ru.netology.db.DatabaseHelper;
import java.sql.SQLException;

public class DataHelper {
    public static String getValidLogin() {
        return "vasya";
    }

    public static String getValidPassword() {
        return "qwerty123";
    }

    public static String getInvalidPassword() {
        return "invalid_password";
    }

    public static String getAuthCodeWithRetry() throws SQLException {
        int maxAttempts = 5;
        int delay = 5000;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            String code = DatabaseHelper.getLastAuthCodeForUser(getValidLogin());
            if (code != null && code.length() == 6) {
                return code;
            }

            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        throw new RuntimeException("Не удалось получить код после " + maxAttempts + " попыток");
    }

    public static void setupTestData() throws SQLException {
        DatabaseHelper.clearAllTables();
        DatabaseHelper.createTestUser();
    }

    public static void cleanUp() throws SQLException {
        DatabaseHelper.clearAllTables();
    }
}