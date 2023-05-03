package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static QueryRunner runner = new QueryRunner();

    private SQLHelper() {}

    @SneakyThrows
    private static Connection getConnection () {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app","app", "pass");
    }

    public static DataHelper.AuthCode getAuthCode() {
        var authCode = "SELECT code FROM auth_codes order by created DESC Limit 1;";
        try (var conn = getConnection()) {
            var code = runner.query(conn, authCode, new ScalarHandler<String>());
            return new DataHelper.AuthCode(code);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public static int getCardBalanceByID(String id) {
        var balance = "SELECT balance_in_kopecks FROM cards WHERE id ='" + id + "'";
        try (var conn = getConnection()) {
            var cardBalance = runner.query(conn, balance, new ScalarHandler<Integer>());
            return cardBalance / 100;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static void clearDataBaseForSQLTest() {
        var connection = getConnection();
        runner.execute(connection, "DELETE FROM auth_codes");

        runner.execute(connection, "DELETE FROM cards");
        runner.execute(connection, "DELETE FROM users");
    }

    @SneakyThrows
    public static void clearDataBase() {
        var connection = getConnection();
        runner.execute(connection, "DELETE FROM auth_codes");
        runner.execute(connection, "DELETE FROM card_transaction");
        runner.execute(connection, "DELETE FROM cards");
        runner.execute(connection, "DELETE FROM users");
    }

    @SneakyThrows
    public static void clearAuthCodesAndCardTransaction() {
        var connection = getConnection();
        runner.execute(connection, "DELETE FROM auth_codes");
        runner.execute(connection, "DELETE FROM card_transaction");
    }

    @SneakyThrows
    public static void clearAuthCodes() {
        var connection = getConnection();
        runner.execute(connection, "DELETE FROM auth_codes");
    }
}
