import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {
    private Bank bank;

    @BeforeEach
    public void setUp() {
        try {
            bank = new Bank();
        } catch (Exception e){}
    }

    @Test
    @DisplayName("Testing login functionality")
    public void testLogin() {
        long sessionID;

        // Not an account
        try{
            sessionID = bank.login("Not a user", "Incorrect password");
        } catch (Exception e) {
            assertEquals("Username not found", e.getMessage());
        }

        // Invalid password
        try{
            sessionID = bank.login("Jessica Haugh", "Incorrect password");
        } catch (Exception e) {
            assertEquals("Incorrect password", e.getMessage());
        }

        // Successful
        try{
            // Assert no exception thrown
            sessionID = bank.login("Jessica Haugh", "airplane45");
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @DisplayName("Testing getting balance")
    public void testBalanceRetrieval() {
        long sessionID;

        // Successful
        try{
            // Assert no exception thrown
            sessionID = bank.login("Jessica Haugh", "airplane45");

            // Invalid session ID
            try {
                bank.getBalance(123, 000);
            } catch (InvalidSession e) {
                assertEquals("Session not found", e.getMessage());
            }

            // Incorrect account number
            try {
                bank.getBalance(000, sessionID);
            } catch (InvalidSession e) {
                assertEquals("Incorrect account number", e.getMessage());
            }

            // Successful
            try{
                // Assert no exception thrown
                Money initialBalance = bank.getBalance(123, sessionID);
                assertEquals(Money.parse("EUR 00.00"), initialBalance);
            } catch (Exception e) {
                throw new AssertionError(e.getMessage());
            }
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @DisplayName("Testing deposits")
    public void testDeposits() {
        long sessionID;
        int accountnum = 123;

        // Successful login
        try{
            // Assert no exception thrown
            sessionID = bank.login("Jessica Haugh", "airplane45");

            try{
                // Assert no exception thrown
                float expectedBalance = 0.0f;
                Money balance = bank.getBalance(accountnum, sessionID);
                assertEquals(Money.parse("EUR " + expectedBalance), balance);

                float amount = 100.00f;
                expectedBalance += amount;
                Money amountToDeposit = Money.parse("EUR " + amount);
                bank.deposit(accountnum, amountToDeposit, sessionID);

                balance = bank.getBalance(accountnum, sessionID);
                assertEquals(Money.parse("EUR " + expectedBalance), balance);

                amount = 4.00f;
                expectedBalance += amount;
                amountToDeposit = Money.parse("EUR " + amount);
                bank.deposit(accountnum, amountToDeposit, sessionID);

                balance = bank.getBalance(accountnum, sessionID);
                assertEquals(Money.parse("EUR " + expectedBalance), balance);
            } catch (Exception e) {
                throw new AssertionError(e.getMessage());
            }
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }
}
