package ru.netology.data;

import ru.netology.db.SQLHelper;
import java.sql.SQLException;

public class DataHelper {
    public static void clearAllTables() throws SQLException {
        SQLHelper.clearAllTables();
    }

    public static void createUser(String username, String password) throws SQLException {
        SQLHelper.createUser(username, password);
    }

    public static String getLastAuthCode() throws SQLException {
        return SQLHelper.getLastAuthCode();
    }

    public static void generateAuthCode(String username) throws SQLException {
        SQLHelper.generateAuthCode(username);
    }
}
