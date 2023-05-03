package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;
import java.util.Random;

import static ru.netology.data.SQLHelper.getAuthCode;

public class DataHelper {
    private DataHelper(){}

    private static final Faker faker = new Faker(new Locale("en"));

    @Value
    public static class UserInfo {
        String login;
        String password;
    }

    @Value
    public static class AuthCode {
        String authCode;
    }

    public static UserInfo getValidUser() {
        return new UserInfo("vasya", "qwerty123");
    }

    public static UserInfo getInvalidUser() {
        return new UserInfo(faker.name().username(), getInvalidPassword());
    }

    public static String getInvalidPassword() {
        return faker.internet().password();
    }

    public static UserInfo getValidUserNameWithInvalidPass() {
        return new UserInfo(getValidUser().getLogin(), getInvalidPassword());
    }

    public static AuthCode getValidAuthCode() {
        return SQLHelper.getAuthCode();
    }

    public static AuthCode getInvalidAuthCode() {
        return new AuthCode(faker.numerify("111111"));
    }

    public static int generateValidAmount(int balance) {
        return new Random().nextInt(balance) + 1;
    }

    public static int generateInvalidAmount(int balance) {
        return Math.abs(balance) + new Random().nextInt(10000);
    }
}
