package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement verificationCodeInput = $("input[name='code']");
    private SelenideElement actionVerifyButton = $("[data-test-id=action-verify]");

    public void fillVerificationForm(String code) {
        verificationCodeInput.setValue(code);
        actionVerifyButton.click();
    }

    public DashboardPage verifyAndProceedToDashboard() {
        return new DashboardPage();
    }
}