package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id=login] input");
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private final SelenideElement loginPageHeader = $("h2");

    public void waitForPageToLoad() {
        loginPageHeader.shouldBe(visible, Duration.ofSeconds(20));
        loginField.shouldBe(visible, Duration.ofSeconds(20));
        passwordField.shouldBe(visible, Duration.ofSeconds(20));
        loginButton.shouldBe(visible, Duration.ofSeconds(20));
    }

    public void login(String login, String password) {
        loginField.clear();
        passwordField.clear();
        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();
    }

    public void verifyErrorNotification(String expectedText) {
        errorNotification.shouldBe(visible, Duration.ofSeconds(20))
                .shouldHave(text(expectedText));
    }
}