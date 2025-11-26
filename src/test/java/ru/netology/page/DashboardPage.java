package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private final SelenideElement dashboardHeader = $("h2");

    public void verifyDashboardIsVisible() {
        dashboardHeader.shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(text("Интернет Банк"));
    }
}