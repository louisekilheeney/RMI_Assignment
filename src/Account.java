import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class Account {
    private int accountNumber;
    private String username;
    private String password;
    private Money balance;

    public Account(int accountNumber, String username, String password)
    {
        setAccountNumber(accountNumber);
        setUsername(username);
        setPassword(password);
        CurrencyUnit eur = CurrencyUnit.of("EUR");
        balance = Money.of(eur, 0);
    }

    public void deposit(Money amount) {
        balance = balance.plus(amount);
    }

    public void withdraw(Money amount) throws InvalidTransaction{
        if(amount.compareTo(balance) > 0) {
            // If the amount is more than the balance
            throw new InvalidTransaction("Not enough funds");
        } else {
            balance = balance.minus(amount);
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

    public Money getBalance()
    {
        return balance;
    }

    public void setBalance(Money balance)
    {
        this.balance = balance;
    }
}
