package ru.netology.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.APIHelper;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;

public class APINegativeTest {

    @BeforeAll
    static void setup() {
        APIHelper.login();
        APIHelper.verify();
    }

    @Test
    void shouldInvalidTransfer() {
        var from = APIHelper.firstCard().getNumber();
        var to = APIHelper.secondCard().getNumber();
        var amount = DataHelper.generateInvalidAmount(APIHelper.firstCard().getBalance());

        APIHelper.invalidTransfer(new APIHelper.Transfer(from, to, amount));
        SQLHelper.clearAuthCodes();
    }
}
