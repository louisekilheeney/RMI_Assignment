
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Bank implements BankInterface {
    private List<Account> accounts;
    private List<Session> sessions;

    public Bank() throws RemoteException
    {
        super();

        // Sample accounts
        Account[] sampleAccounts = {
                new Account(123, "jeshaugh", "airplane45"),
                new Account(456, "ciaherne", "bluesky36"),
                new Account(789, "jacallaghan", "cloudy09"),
                new Account(1011, "neoconnor", "rainfall76")
        };

        accounts = new ArrayList<>();
        Collections.addAll(accounts, sampleAccounts);

        sessions = new ArrayList<>();
    }

    public static void main(String args[]) throws Exception
    {
        try {
            // Set security manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
                System.out.println("Security manager set");
            }

            // Create an instance of the bank server and export it
            BankInterface bank = new Bank();
            System.out.println("Instance of the Bank server created");
            BankInterface stub = (BankInterface) UnicastRemoteObject.exportObject(bank, 0);

            // Bind the bank server to the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Bank", stub);
            System.out.println("Name rebind completed");
            System.out.println("Server ready for requests!");
        } catch (Exception e) {
            System.out.println("Bank server error: " + e.getMessage());
        }
    }

    @Override
    public long login(String username, String password) throws RemoteException, InvalidLogin
    {
        // Iterate through the accounts
        System.out.println("Login attempt for username: " + username);
        for (Account account : accounts) {
            // Check if the username is there
            if (username.compareTo(account.getUsername()) == 0) {
                // Check if the password matches
                if (password.compareTo(account.getPassword()) == 0) {
                    System.out.println("Login successful:"
                            + "\nUsername: " + username
                            + "\nAccount number: " + account.getAccountNumber()
                    );

                    // Create a new session and return the session ID
                    Session session = new Session(account);
                    sessions.add(session);

                    System.out.println("New session (ID = " + session.getId() + ") created. Time left: " + session.getTimeLeft());

                    return session.getId();
                } else {
                    System.out.println("Login unsuccessful");
                    throw new InvalidLogin("Incorrect password");
                }
            }
        }

        System.out.println("Login unsuccessful");
        throw new InvalidLogin("Username not found");
    }

    @Override
    public void deposit(int accountnum, BigDecimal amount, long sessionID) throws RemoteException, InvalidSession, InvalidTransaction
    {
        Session currentSession = getAssociatedSession(accountnum, sessionID);
        Account currentAccount = currentSession.getAccount();
        currentAccount.deposit(amount);
        System.out.println("Deposit to account " + accountnum + ":"
                + "\nAmount: €" + amount
                + "\nBalance: €" + currentAccount.getBalance()
                + "\nSession time left: " + currentSession.getTimeLeft() + " s"
        );
    }

    private Session getAssociatedSession(int accountnum, long sessionID) throws InvalidSession
    {
        // Check does the session exist
        for (Session session : sessions) {
            if (session.getId() == sessionID) {
                // Check is the session active
                if (session.isActive()) {
                    // Check does the account number match
                    if (session.getAccount().getAccountNumber() == accountnum) {
                        return session;
                    } else {
                        throw new InvalidSession("Incorrect account number");
                    }
                } else {
                    System.out.println("Session timed out");
                    throw new InvalidSession("Session timed out");
                }
            }
        }

        // If we reach here without finding a session with the matching session ID
        throw new InvalidSession("Session not found");
    }

    @Override
    public void withdraw(int accountnum, BigDecimal amount, long sessionID) throws RemoteException, InvalidSession, InvalidTransaction
    {
        Session currentSession = getAssociatedSession(accountnum, sessionID);
        Account currentAccount = currentSession.getAccount();
        currentAccount.withdraw(amount);
        System.out.println("Withdrawal from account " + accountnum + ":"
                + "\nAmount: €" + amount
                + "\nBalance: €" + currentAccount.getBalance()
                + "\nSession time left: " + currentSession.getTimeLeft() + " s"
        );
    }

    @Override
    public BigDecimal getBalance(int accountnum, long sessionID) throws RemoteException, InvalidSession
    {
        Session currentSession = getAssociatedSession(accountnum, sessionID);
        Account currentAccount = currentSession.getAccount();
        System.out.println("Balance request for account " + accountnum + ":"
                + "\nBalance: €" + currentAccount.getBalance()
                + "\nSession time left: " + currentSession.getTimeLeft() + " s"
        );
        return currentAccount.getBalance();
    }

    @Override
    public Statement getStatement(int accountnum, Date from, Date to, long sessionID) throws RemoteException, InvalidSession, InvalidTransaction
    {
        Session currentSession = getAssociatedSession(accountnum, sessionID);
        Account currentAccount = currentSession.getAccount();
        System.out.println("Statement request for account " + accountnum + ":"
                + "\nFrom: " + from
                + "\nTo: " + to
                + "\nSession time left: " + currentSession.getTimeLeft() + " s"
        );
        return new AccountStatement(from, to, currentAccount);
    }

    @Override
    public int getAccountNumber(long sessionID) throws RemoteException
    {
        for (Session session : sessions) {
            if (session.getId() == sessionID) {
                // Check is the session active
                if (session.isActive()) {
                    // Check does the account number match
                    return session.getAccount().getAccountNumber();
                }
            }
        }

        return -1;
    }
}