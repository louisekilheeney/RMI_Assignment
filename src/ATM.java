
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ATM {

    public static void main(String args[]) throws Exception {

        int registryport = 20345;
        //BankInterface bankServer;
        ATM atm = null;
        Long _sessionId = null;

        if (args.length > 0){
            registryport = Integer.parseInt(args[0]);
        }

        System.out.println("RMI_assignment port = " + registryport);
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
            System.out.println("Security manager set");
        }
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            System.out.println("Got it");
            String name = "Bank";
            BankInterface bankServer = (BankInterface) registry.lookup(name);
            System.out.println("Got the bank too");

            atm = new ATM(bankServer);
            System.out.println("Got the atm too");
        } catch (Exception e) {
            System.err.println("ATM exception: " +e.toString());
            e.printStackTrace();
        }
        while(true){
            atm.login();
            atm.atmActions();
        }

    }

    private long sessionId;
    private BankInterface bankServer;
    private Scanner sc;
    private int accNumber;

    public ATM(BankInterface _bankServer) {
        this.bankServer = _bankServer;
        this.sc = new Scanner(System.in);
        this.accNumber = -1;
        this.sessionId = -1L;
    }

    public void login()
    {
        while(sessionId == -1L){
            System.out.println("Login to access Account: Please Enter Username: ");
            String username = this.sc.next();
            System.out.println("Please Enter Password: ");
            String password = this.sc.next();

            try {
                sessionId = this.bankServer.login(username, password);
                System.out.println("Successful login for " + username + ": session ID " + sessionId + " is valid for 5 minutes");
            } catch (InvalidLogin | RemoteException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            this.accNumber = bankServer.getAccountNumber(this.sessionId);
            this.sessionId = sessionId;
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
        }
    }

    private void logout(){
        this.sessionId = -1L;
    }

    private void atmActions() {
        boolean logout = false;
        while(!logout) {
            System.out.println("Choose from the following options: ");
            System.out.println(" W)ithdraw, D)eposit, B)alance, S)tatement or L)ogout ");
            String option = this.sc.next();
            switch (option.toLowerCase()) {
                case "withdraw":
                case "w":
                    this.withdraw();
                    break;

                case "deposit":
                case "d":
                    this.deposit();
                    break;

                case "balance":
                case "b":
                    this.balance();
                    break;
                case "statement":
                case "s":
                    this.statement();
                    break;

                case "logout":
                case "l":
                    System.out.println("Goodbye");
                    logout = true;
                    break;
                default:
                    System.out.println("invalid option selected, please try again");
            }
        }
    }


    private void withdraw() {
        try {
            System.out.println("Enter amount to withdraw: ");
            String amountStr = this.sc.next();
            double amountValue = Double.parseDouble(amountStr);
            BigDecimal amountToWithdraw = new BigDecimal(amountValue);
            bankServer.withdraw(this.accNumber, amountToWithdraw, this.sessionId);
            System.out.println("Successfully withdrew " + amountToWithdraw + " Euro from account" + accNumber);

        } catch (InvalidTransaction | RemoteException | InvalidSession e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to main menu");
            return;
        }
    }

    private void deposit() {
        try {
            System.out.println("Please Enter deposit account: ");
            String depositStr = this.sc.next();
            double depostitValue = Double.parseDouble(depositStr);
            BigDecimal amountToDeposit = new BigDecimal(depostitValue);
            bankServer.deposit(this.accNumber, amountToDeposit, this.sessionId);
            System.out.println("Successfully deposited " + amountToDeposit + " Euro into account" + this.accNumber);

        } catch (InvalidTransaction | RemoteException | InvalidSession e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to main menu");
            return;
        }
    }

    private void balance() {
        try {
            BigDecimal balance = bankServer.getBalance(this.accNumber, this.sessionId);
            System.out.println("The current balance of account " + this.accNumber + " is " + balance.toString()+ "Euro");
        } catch (InvalidSession | RemoteException e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to main menu");
            return;
        }
    }

    private void statement() {
        try {

            System.out.println("Please Enter startDate: ");
            String startDate = this.sc.next();
            Date start=new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
            System.out.print("Please Enter endDate: ");
            String endDate = sc.next();
            Date end=new SimpleDateFormat("dd/MM/yyyy").parse(endDate);

            Statement statement = bankServer.getStatement(this.accNumber, start, end, this.sessionId);
            System.out.println("The statement for account" + this.accNumber + " for the period " + start + " to " + end);
            System.out.println(statement.toString());

        } catch (InvalidTransaction | ParseException | RemoteException | InvalidSession e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to main menu");
            return;
        }
    }
}

