import org.joda.money.Money;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Transaction implements Serializable {
    private Money amount;
    private Date date;
    private String description;
    public enum TransactionType {DEPOSIT, WITHDRAWAL}

    public Transaction(Money amount, TransactionType transactionType) throws InvalidTransaction
    {
        if(transactionType == null) {
            throw new InvalidTransaction("Unknown transaction type");
        } else {
            setAmount(amount);
            // Set the date to be the date the transaction was created
            setDate(new Date());
            // Auto-generate description
            DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
            String formattedDate = dateFormat.format(date);
            setDescription(transactionType.toString() + "\t" + formattedDate + ": " + amount);
        }
    }

    public Money getAmount()
    {
        return amount;
    }

    public Date getDate()
    {
        return date;
    }

    public void setAmount(Money amount)
    {
        this.amount = amount;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}