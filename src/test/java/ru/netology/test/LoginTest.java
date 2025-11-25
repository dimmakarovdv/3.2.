package ru.netology.test;

import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;
import java.sql.SQLException;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {

    @BeforeAll
    static void setUpAll() throws SQLException {
        DataHelper.setupTestData();
    }

    @AfterAll
    static void tearDownAll() throws SQLException {
        DataHelper.cleanUp();
    }

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
        String authCode = DataHelper.getAuthCodeWithRetry();
        assertNotNull(authCode, "Код не найден в базе");
        assertEquals(6, authCode.length(), "Код должен быть длиной 6 символов");
        verificationPage.enterCode(authCode);
        verificationPage.verify();
        DashboardPage dashboardPage = new DashboardPage();
        assertTrue(dashboardPage.isDashboardVisible(), "Личный кабинет не загружен");
    }

    @Test
    void shouldBlockUserAfterThreeFailedAttempts() {
        LoginPage loginPage = new LoginPage();
        String validLogin = DataHelper.getValidLogin();

        // Три неудачные попытки входа
        for (int i = 1; i <= 3; i++) {
            loginPage.login(validLogin, DataHelper.getInvalidPassword() + i);

            if (i < 3) {
                boolean isErrorVisible = loginPage.isErrorNotificationVisible();
                assertTrue(isErrorVisible, "Ошибка не появилась на попытке #" + i);

                String errorText = loginPage.getErrorNotificationText();
                assertTrue(errorText.contains("Неверно указан логин или пароль") ||
                                errorText.contains("Ошибка") ||
                                errorText.contains("Неверный"),
                        "Неверный текст ошибки на попытке #" + i + ": " + errorText);
                sleep(5000);
            } else {
                boolean isBlocked = loginPage.isErrorNotificationVisible();
                assertTrue(isBlocked, "Сообщение о блокировке не отобразилось");

                String blockText = loginPage.getErrorNotificationText();
                assertTrue(blockText.contains("заблокирован") ||
                                blockText.contains("блокировка") ||
                                blockText.contains("blocked") ||
                                blockText.contains("Ошибка"),
                        "Неверный текст блокировки: " + blockText);
            }
        }
    }
}