package ru.netology.test;

import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;
import java.sql.SQLException;
import static com.codeborne.selenide.Selenide.*;

public class LoginTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfullyLoginWithAuthCodeFromDatabase() throws SQLException {
        LoginPage loginPage = new LoginPage();
        loginPage.login(DataHelper.getValidLogin(), DataHelper.getValidPassword());
        VerificationPage verificationPage = new VerificationPage();
        verificationPage.waitForPageToLoad();
        String authCode = DataHelper.getAuthCode();
        verificationPage.enterCode(authCode);
        verificationPage.verify();
        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.verifyDashboardIsVisible();
    }

    @Test
    void shouldBlockUserAfterThreeFailedAttempts() {
        LoginPage loginPage = new LoginPage();

        for (int i = 1; i <= 2; i++) {
            loginPage.login(DataHelper.getValidLogin(), DataHelper.getInvalidPassword());
            loginPage.verifyStandardError();
            sleep(5000);
        }
        loginPage.login(DataHelper.getValidLogin(), DataHelper.getInvalidPassword());
        loginPage.verifyBlockedMessage();
    }
}