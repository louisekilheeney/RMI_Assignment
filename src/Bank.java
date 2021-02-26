import org.joda.money.Money;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Bank implements BankInterface {
    private List<Account> accounts; // users accounts


    public Bank() throws RemoteException
    {
        super();    // call the parent constructor

        Account[] sampleAccounts = {
                new Account(123, "Jessica Haugh", "airplane45"),
                new Account(456, "Cian Aherne", "bluesky36"),
                new Account(789, "James Callaghan", "cloudy09")
        };

        accounts = new ArrayList<>();
        Collections.addAll(accounts, sampleAccounts);
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

    /**
     * Main method for the Bank server
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception
    {
        // initialise Bank server - see sample code in the notes and online RMI tutorials for details
        try {
            // First reset our Security manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
                System.out.println("Security manager set");
            }

            // Create an instance of the local object
            BankInterface bank = new Bank();
            System.out.println("Instance of the Bank server created");
            BankInterface stub = (BankInterface) UnicastRemoteObject.exportObject(bank, 0);

            // Put the server object into the Registry
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Bank", stub);
            System.out.println("Name rebind completed");
            System.out.println("Server ready for requests!");
        } catch (Exception exc) {
            System.out.println("Error in main - " + exc.toString());
        }
    }

    @Override
    public long login(String username, String password) throws RemoteException, InvalidLogin
    {
        String message = "Username not found";

        for (Account account : accounts) {
            if (username.compareTo(account.getUsername()) == 0) {
                if (password.compareTo(account.getPassword()) == 0) {
                    Session session = new Session(account);
                    return session.getId();
                } else {
                    message = "Incorrect password";
                    throw new InvalidLogin(message);
                }
            }
        }

        throw new InvalidLogin(message);
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