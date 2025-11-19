package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private SelenideElement dashboardTitle = $("h2");

    public void waitForDashboardVisible() {
        dashboardTitle.shouldBe(visible);
    }

}