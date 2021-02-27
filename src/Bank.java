import org.joda.money.Money;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Bank implements BankInterface {
    private List<Account> accounts; // users accounts
    private List<Session> sessions;

    public Bank() throws RemoteException
    {
        super();    // call the parent constructor

        Account[] sampleAccounts = {
                new Account(123, "Jessica Haugh", "airplane45"),
                new Account(456, "Cian Aherne", "bluesky36"),
                new Account(789, "James Callaghan", "cloudy09"),
                new Account(1011, "Noel O'Connor", "rainfall76")
        };

        accounts = new ArrayList<>();
        sessions = new ArrayList<>();
        Collections.addAll(accounts, sampleAccounts);
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
                    sessions.add(session);
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
    public void deposit(int accountnum, Money amount, long sessionID) throws RemoteExcept, InvalidSession, InvalidTransaction
    {
        Account currentAccount = getAssociatedAccount(accountnum, sessionID);
        currentAccount.deposit(amount);
    }

    private Account getAssociatedAccount(int accountnum, long sessionID) throws InvalidSession {
        // Check does the session exist
        for (Session session : sessions) {
            if (session.getId() == sessionID) {
                // Check is the session active
                if(session.isActive()) {
                    // Check does the account number match
                    if(session.getAccount().getAccountNumber() == accountnum) {
                        return session.getAccount();
                    } else {
                        throw new InvalidSession("Incorrect account number");
                    }
                } else {
                    throw new InvalidSession("Session timed out");
                }
            }
        }

        // If we reach here without finding a session with the matching session ID
        throw new InvalidSession("Session not found");
    }

    @Override
    public void withdraw(int accountnum, Money amount, long sessionID) throws RemoteException, InvalidSession, InvalidTransaction
    {
        Account currentAccount = getAssociatedAccount(accountnum, sessionID);
        currentAccount.withdraw(amount);
    }

    @Override
    public Money getBalance(int accountnum, long sessionID) throws RemoteException, InvalidSession
    {
        Account currentAccount = getAssociatedAccount(accountnum, sessionID);
        return currentAccount.getBalance();
    }

    @Override
    public Statement getStatement(int accountnum, Date from, Date to, long sessionID) throws RemoteException, InvalidSession, InvalidTransaction
    {
        Account currentAccount = getAssociatedAccount(accountnum, sessionID);
        return new AccountStatement(from, to, currentAccount);
    }
}