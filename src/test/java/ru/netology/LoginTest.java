package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class LoginTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        $("[data-test-id=login]").shouldBe(Condition.visible);
        clearDatabase();
        addTestUser();
    }

    @Test
    void testLogin() {
        $("[data-test-id=login] input").setValue("vasya");
        $("[data-test-id=password] input").setValue("qwerty123");
        $("[data-test-id=action-login]").click();
        $("[data-test-id=code] input").shouldBe(Condition.visible, Duration.ofSeconds(30));
        String code = getCodeFromDatabase();
        $("[data-test-id=code] input").setValue(code);
        $("[data-test-id=action-verify]").click();
        $("h2").shouldHave(Condition.text("Интернет Банк"));
    }

    void clearDatabase() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "app", "pass");

            connection.prepareStatement("DELETE FROM auth_codes").executeUpdate();
            connection.prepareStatement("DELETE FROM card_transactions").executeUpdate();
            connection.prepareStatement("DELETE FROM cards").executeUpdate();
            connection.prepareStatement("DELETE FROM users").executeUpdate();

            connection.close();
            System.out.println("База данных очищена");
        } catch (Exception e) {
            System.out.println("Ошибка при очистке базы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void addTestUser() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "app", "pass");

            String userId = java.util.UUID.randomUUID().toString();

            String sql = "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, 'active')";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, userId);
            statement.setString(2, "vasya");
            statement.setString(3, "$2a$10$Gaeh2B/1CgvQW.2a10Qh.eSzjlsmks7kK9qSPyhAogS2gOSWJ.1K2");

            statement.executeUpdate();
            statement.close();

            ResultSet check = connection.prepareStatement("SELECT * FROM users WHERE login = 'vasya'").executeQuery();
            if (check.next()) {
                System.out.println("Пользователь vasya успешно добавлен в базу");
            } else {
                System.out.println("ОШИБКА: Пользователь не был добавлен в базу!");
            }
            check.close();

            connection.close();
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }

    String getCodeFromDatabase() {
        sleep(5000);

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "app", "pass");

            String sql = "SELECT code FROM auth_codes ac " +
                    "JOIN users u ON ac.user_id = u.id " +
                    "WHERE u.login = 'vasya' ORDER BY ac.created DESC LIMIT 1";

            ResultSet result = connection.prepareStatement(sql).executeQuery();

            if (result.next()) {
                String code = result.getString("code");
                connection.close();
                return code;
            } else {
                System.out.println("Код не найден в базе данных!");
                ResultSet allCodes = connection.prepareStatement(
                                "SELECT u.login, ac.code FROM auth_codes ac JOIN users u ON ac.user_id = u.id")
                        .executeQuery();
                while (allCodes.next()) {
                    System.out.println("Найден код: " + allCodes.getString("code") +
                            " для пользователя: " + allCodes.getString("login"));
                }
                allCodes.close();
            }

            connection.close();
        } catch (Exception e) {
            System.out.println("Ошибка при получении кода: " + e.getMessage());
            e.printStackTrace();
        }

        return "000000";
    }
}