package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;
import ru.netology.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.clearDataBaseForSQLTest;

public class SQLTest {
    LoginPage loginPage;
    DashboardPage dashboardPage;
    VerificationPage verificationPage;

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @AfterAll
    static void clearDB() {
        clearDataBaseForSQLTest();
    }

    @Test
    void successLogin() {
        var validUser = getValidUser();
        verificationPage = loginPage.validLogin(validUser);
        var authCode = getValidAuthCode();
        dashboardPage = verificationPage.validConfirm(authCode);
    }

    @Test
    void shouldLoginWithWrongPassword() {
        var invalidUser = new DataHelper.UserInfo(getValidUser().getLogin(), getInvalidPassword());
        loginPage.invalidLogin(invalidUser);
        loginPage.existNotificationOnLoginPage();
    }

    @Test
    void shouldConfirmWrongCode() {
        var validUser = getValidUser();
        verificationPage = loginPage.validLogin(validUser);
        var authCode = getInvalidAuthCode();
        verificationPage.invalidConfirm(authCode);
        verificationPage.existNotificationOnVerificationPage();
    }
}
