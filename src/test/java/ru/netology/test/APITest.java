package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.APIHelper;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;

import java.util.List;

import static io.restassured.RestAssured.given;


public class APITest {
    @BeforeAll
    static void setup() {
        APIHelper.login();
        APIHelper.verify();
    }

    @AfterAll
    static void clearDB() {
        SQLHelper.clearAuthCodesAndCardTransaction();
    }
    @Test
    void showCardBalance() {
        List<APIHelper.CardInfo> cardsInfo = given()
                .spec(APIHelper.requestSpecificationWithToken)
                .when()
                .get("/cards")
                .then()
                .spec(APIHelper.responseSpecificationWithoutContentType)
                .extract().body().jsonPath().getList("", APIHelper.CardInfo.class);
        Assertions.assertEquals(cardsInfo.get(0).getBalance(), APIHelper.firstCard().getBalance());

        SQLHelper.clearAuthCodes();
    }

    @Test
    void shouldValidTransfer() {
        var from = APIHelper.firstCard().getNumber();
        var to = APIHelper.secondCard().getNumber();
        var amount = DataHelper.generateValidAmount(APIHelper.firstCard().getBalance());
        var tmpAmount = APIHelper.firstCard().getBalance();

        APIHelper.transfer(new APIHelper.Transfer(from, to, amount));

        var expected = tmpAmount - amount;

        Assertions.assertEquals(expected, APIHelper.firstCard().getBalance());

        APIHelper.transfer(new APIHelper.Transfer(to, from, amount));
        SQLHelper.clearAuthCodes();
    }
}
