package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private SelenideElement dashboardHeader = $("h2");

    public boolean isLoaded() {
        return dashboardHeader.getText().contains("личный кабинет");
    }
}