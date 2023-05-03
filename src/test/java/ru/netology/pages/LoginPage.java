package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage {
    private SelenideElement loginInput = $("[data-test-id=login] input");
    private SelenideElement passInput = $("[data-test-id=password] input");
    private SelenideElement nextButton = $("[data-test-id=action-login]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification] .notification__content");


    public VerificationPage validLogin(DataHelper.UserInfo info) {
        loginInput.setValue(info.getLogin());
        passInput.setValue(info.getPassword());
        nextButton.click();
        return new VerificationPage();
    }

    public void invalidLogin(DataHelper.UserInfo info) {
        loginInput.setValue(info.getLogin());
        passInput.setValue(info.getPassword());
        nextButton.click();
    }

    public void existNotificationOnLoginPage() {
        errorNotification.shouldBe(Condition.visible).shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}
