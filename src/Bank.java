import org.joda.money.Money;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public class Bank implements BankInterface {
    private List<Account> accounts; // users accounts

    public Bank() throws RemoteException
    {
    }

    public void deposit(int account, Money amount) throws RemoteException, InvalidSession
    {
// implementation code
    }

    public void withdraw(int account, Money amount) throws RemoteException, InvalidSession
    {
// implementation code
    }

    public Money getBalance(int account) throws RemoteException, InvalidSession
    {
// implementation code
        return null;
    }

    public Statement getStatement(Date from, Date to) throws RemoteException, InvalidSession
    {
// implementation code
        return null;
    }

    public static void main(String args[]) throws Exception
    {
// initialise Bank server - see sample code in the notes and online RMI tutorials for details
    }

    @Override
    public long login(String username, String password) throws RemoteException, InvalidLogin
    {
        return 0;
    }

    @Override
    public void deposit(int accountnum, Money amount, long sessionID) throws RemoteExcept, InvalidSession
    {

    }

    @Override
    public void withdraw(int accountnum, Money amount, long sessionID) throws RemoteException, InvalidSession
    {

    }

    @Override
    public Money getBalance(int accountnum, long sessionID) throws RemoteException, InvalidSession
    {
        return null;
    }

    @Override
    public Statement getStatement(Date from, Date to, long sessionID) throws RemoteException, InvalidSession
    {
        return null;
    }
}