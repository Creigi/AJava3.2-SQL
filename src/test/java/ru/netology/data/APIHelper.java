package ru.netology.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.SneakyThrows;
import lombok.Value;

import java.sql.SQLException;
import java.util.List;

import static io.restassured.RestAssured.given;

public class APIHelper {
    private APIHelper() {}

    private static final String URL = "http://localhost";

    @Value
    public static class Verification {
        String login;
        String code;
    }

    @Value
    public static class Token {
        String token;
    }

    @Value
    public static class Transfer {
        String from;
        String to;
        Integer amount;
    }

    @Value
    public static class CardInfo {
        String id;
        String number;
        Integer balance;
    }

    @SneakyThrows
    public static APIHelper.CardInfo firstCard() {
        return new CardInfo("0f3f5c2a-249e-4c3d-8287-09f7a039391d", "5559 0000 0000 0002",
                SQLHelper.getCardBalanceByID("0f3f5c2a-249e-4c3d-8287-09f7a039391d"));
    }

    @SneakyThrows
    public static APIHelper.CardInfo secondCard() {
        return new CardInfo("92df3f1c-a033-48e6-8390-206f6b1f56c0", "5559 0000 0000 0001",
                SQLHelper.getCardBalanceByID("92df3f1c-a033-48e6-8390-206f6b1f56c0"));
    }
    public static Verification verificationWithValidUser() {
        return new Verification(DataHelper.getValidUser().getLogin(), DataHelper.getValidAuthCode().getAuthCode());
    }

    public static RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri(URL)
            .setPort(9999)
            .setBasePath("/api")
            .setContentType(ContentType.JSON)
            .build();

    public static RequestSpecification requestSpecificationWithToken = new RequestSpecBuilder()
            .setBaseUri(URL)
            .setPort(9999)
            .setBasePath("/api")
            .setContentType(ContentType.JSON)
            .build().header("authorization", "bearer " + APIHelper.getValidToken().getToken());

    public static Token getValidToken() {
        return  given()
                .spec(requestSpecification)
                .body(verificationWithValidUser())
                .when()
                .post("/auth/verification")
                .then()
                .extract().as(APIHelper.Token.class);
    }

    public static ResponseSpecification responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

    public static ResponseSpecification responseSpecificationWithoutContentType = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .expectStatusCode(200)
                .build();

    public static ResponseSpecification responseSpecificationInvalidTransfer = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .expectStatusCode(400)
            .build();

    public static void login() {
        given()
                .spec(requestSpecification)
                .body(DataHelper.getValidUser())
                .when()
                .post("/auth")
                .then()
                .spec(responseSpecificationWithoutContentType);
    }

    public static void verify() {
        given()
                .spec(requestSpecification)
                .body(verificationWithValidUser())
                .when()
                .post("/auth/verification")
                .then()
                .spec(responseSpecification);
    }

    public static void transfer(Transfer transfer) {
        given()
                .spec(requestSpecificationWithToken)
                .body(transfer)
                .when()
                .post("/transfer")
                .then()
                .spec(responseSpecificationWithoutContentType);
    }

    public static void invalidTransfer(Transfer transfer) {
        given()
                .spec(requestSpecificationWithToken)
                .body(transfer)
                .when()
                .post("/transfer")
                .then()
                .spec(responseSpecificationInvalidTransfer);
    }
}
