package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private final SelenideElement dashboardHeader = $("h2");

    public boolean isDashboardVisible() {
        return dashboardHeader.shouldBe(visible, Duration.ofSeconds(30))
                .getText()
                .toLowerCase()
                .contains("личный кабинет");
    }
}