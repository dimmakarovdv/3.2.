package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private SelenideElement loginPageHeader = $("h2");

    public void waitForPageToLoad() {
        loginPageHeader.shouldBe(visible, Duration.ofSeconds(20));
        loginField.shouldBe(visible, Duration.ofSeconds(15));
        passwordField.shouldBe(visible, Duration.ofSeconds(15));
        loginButton.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void login(String login, String password) {
        // Очищаем поля перед вводом новых данных
        loginField.clear();
        passwordField.clear();

        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();

        // Даем время на обработку формы
        sleep(2000);
    }

    // Добавляем недостающие методы
    public boolean isErrorVisible() {
        try {
            errorNotification.shouldBe(visible, Duration.ofSeconds(10));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public String getErrorMessage() {
        return errorNotification.shouldBe(visible, Duration.ofSeconds(10)).getText();
    }

    public boolean isBlockedMessageVisible() {
        try {
            errorNotification.shouldBe(visible, Duration.ofSeconds(10));
            String errorMessage = errorNotification.getText().toLowerCase();
            return errorMessage.contains("заблокирован");
        } catch (Throwable e) {
            return false;
        }
    }
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}