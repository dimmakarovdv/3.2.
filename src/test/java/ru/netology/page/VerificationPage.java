package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class VerificationPage {
    private final SelenideElement codeField = $("[data-test-id=code] input");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private final SelenideElement pageTitle = $("h2");

    public void waitForPageToLoad() {
        pageTitle.shouldBe(visible, Duration.ofSeconds(30));
        codeField.shouldBe(visible, Duration.ofSeconds(25));
        verifyButton.shouldBe(visible, Duration.ofSeconds(25));
    }

    public void enterCode(String code) {
        codeField.shouldBe(visible, Duration.ofSeconds(20)).setValue(code);
    }

    public void verify() {
        verifyButton.shouldBe(visible, Duration.ofSeconds(20)).click();
        sleep(20000);
    }
}