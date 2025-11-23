package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.*;
import java.sql.SQLException;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    @BeforeEach
    void setup() throws SQLException {
        DataHelper.clearAllTables();
        DataHelper.createUser("vasya", "qwerty123");
        sleep(3000);
    }

    @Test
    void successfulLoginTest() throws SQLException {
        open("http://localhost:9999");

        LoginPage loginPage = new LoginPage();
        loginPage.login("vasya", "qwerty123");
        sleep(3000);

        DataHelper.generateAuthCode("vasya");
        String code = DataHelper.getLastAuthCode();

        VerificationPage verificationPage = new VerificationPage();
        verificationPage.enterCode(code);
        verificationPage.verify();
        sleep(3000);

        DashboardPage dashboardPage = new DashboardPage();
        assertTrue(dashboardPage.isLoaded(), "Должен отображаться личный кабинет");
    }

    @Test
    void blockedUserTest() {
        open("http://localhost:9999");
        LoginPage loginPage = new LoginPage();

        for (int i = 1; i <= 3; i++) {
            loginPage.login("vasya", "wrong" + i);
            sleep(2000);
            loginPage = new LoginPage();
        }

        $("[data-test-id=error-notification]").shouldHave(text("заблокирован"));
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}