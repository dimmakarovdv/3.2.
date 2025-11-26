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
        return "pass1";
    }

    public static String getAuthCode() throws SQLException {
        return DatabaseHelper.getLastAuthCodeForUser(getValidLogin());
    }
}