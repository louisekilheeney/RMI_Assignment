import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AccountStatement implements Statement {
    private int accountnum;
    private Date startDate;
    private Date endDate;
    private String accountName;
    private List<Transaction> transactions;

    public AccountStatement(Date startDate, Date endDate, Account account) throws InvalidTransaction
    {
        setAccountnum(account.getAccountNumber());
        setStartDate(startDate);
        setEndDate(endDate);
        setAccountName(account.getUsername());

        // Create a list to hold the transactions for this statement
        transactions = new ArrayList<>();

        // Iterate through the transactions in the account
        for (Transaction transaction : account.getTransactions()) {
            try {
                // If the transaction date is within the specified range
                // (we ignore the transaction time)
                if (transaction.getDateWithoutTime().compareTo(startDate) >= 0 && transaction.getDateWithoutTime().compareTo(endDate) <= 0) {
                    // Add the transaction to the statement
                    transactions.add(transaction);
                }
            } catch (ParseException e) {
                throw new InvalidTransaction("Invalid transaction found");
            } catch (NullPointerException e) {
                throw new InvalidTransaction("Invalid transaction found");
            }
        }

        // Sort the transactions by date
        Comparator<Transaction> compareByDate = Comparator.comparing(Transaction::getDate);
        Collections.sort(transactions, compareByDate);
    }

    @Override
    public String toString()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String output = "Statement for " + accountName
                + "\nAccount number: " + accountnum
                + "\nFrom: " + dateFormat.format(startDate)
                + "\nTo: " + dateFormat.format(endDate);

        for (Transaction transaction : transactions) {
            output += "\n" + transaction.getDescription();
        }

        return output;
    }

    public int getAccountnum()
    {
        return accountnum;
    }

    public void setAccountnum(int accountnum)
    {
        this.accountnum = accountnum;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public List<Transaction> getTransactions()
    {
        return transactions;
    }
}
