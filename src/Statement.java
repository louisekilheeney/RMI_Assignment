import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface Statement extends Serializable {
    public int getAccountnum();  // returns account number associated with this statement

    public Date getStartDate(); // returns start Date of Statement

    public Date getEndDate(); // returns end Date of Statement

    public String getAccountName(); // returns name of account holder

    public List<Transaction> getTransactions(); // return list of transactions included in this statement
}