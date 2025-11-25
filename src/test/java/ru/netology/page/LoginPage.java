package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement usernameField = $("[data-test-id=login] input");
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private final SelenideElement loginPageHeader = $("h1");

    public void waitForPageToLoad() {
        loginPageHeader.shouldBe(visible, Duration.ofSeconds(20));
        usernameField.shouldBe(visible, Duration.ofSeconds(15));
        passwordField.shouldBe(visible, Duration.ofSeconds(15));
        loginButton.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void login(String username, String password) {
        usernameField.setValue(username);
        passwordField.setValue(password);
        loginButton.click();
    }

    public boolean isErrorNotificationVisible() {
        try {
            errorNotification.shouldBe(visible, Duration.ofSeconds(10));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public String getErrorNotificationText() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(10));
        return errorNotification.getText();
    }
}