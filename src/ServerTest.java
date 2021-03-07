import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {
    private Bank bank;
    private BigDecimal eur;

    
    @BeforeEach
    public void setUp()
    {
        try {
            bank = new Bank();
            eur = new BigDecimal("EUR");
        } catch (Exception e) {
        }
    }

    @Test
    @DisplayName("Testing login functionality")
    public void testLogin()
    {
        long sessionID;

        // Not an account
        try {
            sessionID = bank.login("Not a user", "Incorrect password");
        } catch (Exception e) {
            assertEquals("Username not found", e.getMessage());
        }

        // Invalid password
        try {
            sessionID = bank.login("jeshaugh", "Incorrect password");
        } catch (Exception e) {
            assertEquals("Incorrect password", e.getMessage());
        }

        // Successful
        try {
            // Assert no exception thrown
            sessionID = bank.login("jeshaugh", "airplane45");
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @DisplayName("Testing getting balance")
    public void testBalanceRetrieval()
    {
        long sessionID;

        // Successful
        try {
            // Assert no exception thrown
            sessionID = bank.login("jeshaugh", "airplane45");

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
            try {
                // Assert no exception thrown
                BigDecimal initialBalance = bank.getBalance(123, sessionID);
                assertEquals( new BigDecimal(0), initialBalance);
            } catch (Exception e) {
                throw new AssertionError(e.getMessage());
            }
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @DisplayName("Testing deposits")
    public void testDeposits()
    {
        long sessionID;
        int accountnum = 456;

        // Successful login
        try {
            // Assert no exception thrown
            sessionID = bank.login("ciaherne", "bluesky36");

            try {
                // Assert no exception thrown
                float expectedBalance = 0.0f;
                BigDecimal balance = bank.getBalance(accountnum, sessionID);
                assertEquals(new BigDecimal(expectedBalance), balance);

                float amount = 100.00f;
                expectedBalance += amount;
                BigDecimal amountToDeposit =  new BigDecimal( amount);
                bank.deposit(accountnum, amountToDeposit, sessionID);

                balance = bank.getBalance(accountnum, sessionID);
                assertEquals( new BigDecimal( expectedBalance), balance);

                amount = 4.00f;
                expectedBalance += amount;
                amountToDeposit = new BigDecimal( amount);
                bank.deposit(accountnum, amountToDeposit, sessionID);

                balance = bank.getBalance(accountnum, sessionID);
                assertEquals(new BigDecimal(expectedBalance), balance);
            } catch (Exception e) {
                throw new AssertionError(e.getMessage());
            }
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @DisplayName("Testing withdrawals")
    public void testWithdrawals()
    {
        long sessionID;
        int accountnum = 789;

        // Successful login
        try {
            // Assert no exception thrown
            sessionID = bank.login("jacallaghan", "cloudy09");

            try {
                // Assert no exception thrown
                float expectedBalance = 0.0f;
                BigDecimal balance = bank.getBalance(accountnum, sessionID);
                assertEquals(new BigDecimal( expectedBalance), balance);

                float amount = 100.00f;
                expectedBalance += amount;
                BigDecimal changeAmount = new BigDecimal( expectedBalance);
                bank.deposit(accountnum, changeAmount, sessionID);

                balance = bank.getBalance(accountnum, sessionID);
                assertEquals(new BigDecimal( expectedBalance), balance);

                // Invalid withdrawal - not enough funds
                amount = 200.00f;
                changeAmount = new BigDecimal( amount);
                try {
                    bank.withdraw(accountnum, changeAmount, sessionID);
                } catch (InvalidTransaction e) {
                    // Expected balance does not change
                    balance = bank.getBalance(accountnum, sessionID);
                    assertEquals(new BigDecimal( expectedBalance), balance);
                    assertEquals("Not enough funds", e.getMessage());
                }

                // Valid withdrawal
                amount = 50.00f;
                expectedBalance -= amount;
                changeAmount = new BigDecimal( amount);
                bank.withdraw(accountnum, changeAmount, sessionID);

                // Expected balance does not change
                balance = bank.getBalance(accountnum, sessionID);
                assertEquals(new BigDecimal(expectedBalance), balance);

                // Valid withdrawal
                amount = 50.00f;
                expectedBalance -= amount;
                changeAmount = new BigDecimal( amount);
                bank.withdraw(accountnum, changeAmount, sessionID);

                // Expected balance does not change
                balance = bank.getBalance(accountnum, sessionID);
                assertEquals(new BigDecimal( expectedBalance), balance);

                // Invalid withdrawal - not enough funds
                amount = 50.00f;
                changeAmount = new BigDecimal( amount);
                try {
                    bank.withdraw(accountnum, changeAmount, sessionID);
                } catch (InvalidTransaction e) {
                    // Expected balance does not change
                    balance = bank.getBalance(accountnum, sessionID);
                    assertEquals( new BigDecimal( expectedBalance), balance);
                    assertEquals("Not enough funds", e.getMessage());
                }
            } catch (Exception e) {
                throw new AssertionError(e.getMessage());
            }
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @DisplayName("Testing statements")
    public void testStatements()
    {
        long sessionID;
        int accountnum = 1011;

        // Successful login
        try {
            // Assert no exception thrown
            sessionID = bank.login("neoconnor", "rainfall76");

            try {
                ArrayList<Transaction> expectedTransactions = new ArrayList();

                BigDecimal changeAmount = new BigDecimal( 100);
                bank.deposit(accountnum, changeAmount, sessionID);
                expectedTransactions.add(new Transaction(changeAmount, Transaction.TransactionType.DEPOSIT));

                changeAmount =  new BigDecimal( 25);
                bank.withdraw(accountnum, changeAmount, sessionID);
                expectedTransactions.add(new Transaction(changeAmount, Transaction.TransactionType.WITHDRAWAL));

                changeAmount = new BigDecimal( 75);
                bank.deposit(accountnum, changeAmount, sessionID);
                expectedTransactions.add(new Transaction(changeAmount, Transaction.TransactionType.DEPOSIT));

                changeAmount = new BigDecimal(50);
                bank.withdraw(accountnum, changeAmount, sessionID);
                expectedTransactions.add(new Transaction(changeAmount, Transaction.TransactionType.WITHDRAWAL));

                Comparator<Transaction> compareByDate = Comparator.comparing(Transaction::getDate);
                Collections.sort(expectedTransactions, compareByDate);

                Date yesterday = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
                Date tomorrow = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

                Statement statement = bank.getStatement(accountnum, yesterday, tomorrow, sessionID);
                List<Transaction> actualTransactions = statement.getTransactions();

                // Avoiding an index out of bounds exception
                assertEquals(expectedTransactions.size(), actualTransactions.size());

                Comparator<Transaction> compareUsingApproxDate = new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction transaction1, Transaction transaction2)
                    {
                        // Compare types
                        String type1 = transaction1.getDescription().split("\t")[0];
                        String type2 = transaction2.getDescription().split("\t")[0];
                        if(type1.compareTo(type2) != 0) return -1;

                        // Compare dates (to the hour)
                        Date date1 = transaction1.getDate();
                        Date date2 = transaction2.getDate();
                        if(date1.getYear() != date2.getYear()) return -1;
                        if(date1.getMonth() != date2.getMonth()) return -1;
                        if(date1.getDate() != date2.getDate()) return -1;
                        if(date1.getHours() != date2.getHours()) return -1;

                        // Return comparison of amounts
                        return transaction1.getAmount().compareTo(transaction2.getAmount());
                    }
                };


                for(int i = 0; i < actualTransactions.size(); i++) {
                    Transaction expectedTransaction = expectedTransactions.get(i);
                    Transaction actualTransaction = actualTransactions.get(i);
                    // Dont compare objects, because the time the transaction objects were instantiated
                    // would differ by a few milliseconds

                    int comparison = compareUsingApproxDate.compare(expectedTransaction, actualTransaction);
                    assertEquals(0, comparison);
                }

                System.out.print(statement);
            } catch (Exception e) {
                throw new AssertionError(e.getMessage());
            }
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

}