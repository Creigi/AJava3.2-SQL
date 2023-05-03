package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement inputCode = $("[data-test-id=code] input");
    private SelenideElement nextButton = $("[data-test-id=action-verify]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification] .notification__content");

    public void invalidConfirm(DataHelper.AuthCode authCode) {
        inputCode.setValue(authCode.getAuthCode());
        nextButton.click();
    }

    public DashboardPage validConfirm(DataHelper.AuthCode authCode) {
        inputCode.setValue(authCode.getAuthCode());
        nextButton.click();
        return new DashboardPage();
    }

    public void existNotificationOnVerificationPage() {
        errorNotification.shouldBe(Condition.visible).shouldHave(Condition.text("Ошибка! Неверно указан код! Попробуйте ещё раз."));
    }
}
