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
        balance = Money.parse("EUR 00.00");
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
}
