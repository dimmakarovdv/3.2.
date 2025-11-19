package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;
import java.sql.SQLException;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @BeforeEach
    void setup() throws SQLException {
        DataHelper.cleanDatabase();
        DataHelper.createUser("vasya", "$2a$10$Gaeh2B/1CgvQW.2a10Qh.eSzjlsmks7kK9qSPyhAogS2gOSWJ.1K2");
    }

    @Test
    void testSuccessfulUserLogin() throws SQLException {
        DataHelper.generateAuthenticationCode("vasya");
        var loginPage = open("http://localhost:9999/", LoginPage.class);
        loginPage.fillLoginForm("vasya", "qwerty123");
        var verificationPage = loginPage.submitAndProceedToVerification();
        var generatedCode = DataHelper.getLastGeneratedCode();
        verificationPage.fillVerificationForm(generatedCode);
        var dashboardPage = verificationPage.verifyAndProceedToDashboard();
        dashboardPage.waitForDashboardVisible();
    }

    @Test
    void testBlockUserAfterThreeFailures() {
        var loginPage = open("http://localhost:9999/", LoginPage.class);
        loginPage.fillLoginForm("vasya", "Pass1");
        loginPage.fillLoginForm("vasya", "Pass2");
        loginPage.fillLoginForm("vasya", "Pass3");
        $(byAttribute("data-test-id", "error-message"))
                .shouldBe(visible)
                .shouldHave(text("Заблокирован"));
    }
}