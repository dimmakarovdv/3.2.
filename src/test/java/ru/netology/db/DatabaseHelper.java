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

    public static String getLatestAuthCode(String username) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT ac.code " +
                    "FROM auth_codes ac " +
                    "JOIN users u ON ac.user_id = u.id " +
                    "WHERE u.login = ? " +
                    "ORDER BY ac.created DESC " +
                    "LIMIT 1";
            return runner.query(conn, query, new ScalarHandler<>(), username);
        }
    }
}