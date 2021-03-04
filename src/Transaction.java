
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Transaction implements Serializable {
    private BigDecimal amount;
    private Date date;
    private String description;
    public enum TransactionType {DEPOSIT, WITHDRAWAL}

    public Transaction(BigDecimal amount, TransactionType transactionType) throws InvalidTransaction
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

    public BigDecimal getAmount()
    {
        return amount;
    }

    public Date getDate()
    {
        return date;
    }

    public void setAmount(BigDecimal amount)
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