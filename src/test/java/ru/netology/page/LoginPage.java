package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private SelenideElement loginPageHeader = $("h2");

    public void waitForPageToLoad() {
        loginPageHeader.shouldBe(visible, Duration.ofSeconds(20));
        loginField.shouldBe(visible, Duration.ofSeconds(20));
        passwordField.shouldBe(visible, Duration.ofSeconds(20));
        loginButton.shouldBe(visible, Duration.ofSeconds(20));
    }

    public void login(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();
    }

    public void verifyStandardError() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(20))
                .shouldHave(text("Неверно указан логин или пароль"), Duration.ofSeconds(20));
    }

    public void verifyBlockedMessage() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(20))
                .shouldHave(text("заблокирован"));
    }
}