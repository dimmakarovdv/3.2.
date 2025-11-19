package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class LoginPage {
    private SelenideElement usernameInput = $("[data-test-id=login] input");
    private SelenideElement passwordInput = $("[data-test-id=password] input");
    private SelenideElement actionLoginButton = $("button[data-test-id='action-login']");

    public void fillLoginForm(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        actionLoginButton.click();
    }

    public VerificationPage submitAndProceedToVerification() {
        handleHiddenElementIfNeeded();
        return new VerificationPage();
    }

    private void handleHiddenElementIfNeeded() {
        SelenideElement hiddenElement = $("[data-hidden-dialog]");
        if (hiddenElement.exists()) {
            hiddenElement.shouldBe(Condition.visible);
            hiddenElement.$("[data-hidden-dialog]").click();
        }
    }
}