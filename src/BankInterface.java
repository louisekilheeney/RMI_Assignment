
import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface BankInterface extends Remote {
    public long login(String username, String password) throws RemoteException, InvalidLogin;

    public void deposit(int accountnum, BigDecimal amount, long sessionID) throws RemoteException, InvalidSession, InvalidTransaction;

    public void withdraw(int accountnum, BigDecimal amount, long sessionID) throws RemoteException, InvalidSession, InvalidTransaction;

    public BigDecimal getBalance(int accountnum, long sessionID) throws RemoteException, InvalidSession;

    public Statement getStatement(int accountnum, Date from, Date to, long sessionID) throws RemoteException, InvalidSession, InvalidTransaction;

    public int getAccountNumber(long sessionID) throws RemoteException;
}