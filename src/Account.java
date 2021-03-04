

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private int accountNumber;
    private String username;
    private String password;
    private BigDecimal balance;
    private List<Transaction> transactions;
    private Session session;

    public Account(int accountNumber, String username, String password)
    {
        setAccountNumber(accountNumber);
        setUsername(username);
        setPassword(password);
        balance = new BigDecimal(0);
        transactions = new ArrayList<>();
    }

    public void deposit(BigDecimal amount) throws InvalidTransaction {
        balance = balance.add(amount);
        transactions.add(new Transaction(amount, Transaction.TransactionType.DEPOSIT));
    }

    public void withdraw(BigDecimal amount) throws InvalidTransaction {
        if(amount.compareTo(balance) > 0) {
            // If the amount is more than the balance
            throw new InvalidTransaction("Not enough funds");
        } else {
            balance = balance.subtract(amount);
            transactions.add(new Transaction(amount, Transaction.TransactionType.WITHDRAWAL));
        }
    }

    public int getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    public List<Transaction> getTransactions()
    {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions)
    {
        this.transactions = transactions;
    }
}
