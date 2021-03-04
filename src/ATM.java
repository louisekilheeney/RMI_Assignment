
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ATM {

    public static void main(String args[]) throws Exception {
// get user’s input, and perform the operations
        int registryport = 20345;
        Bank bankServer = null;
        ATM atm = null;
        Long _sessionId = null;

        if (args.length > 0){
            registryport = Integer.parseInt(args[0]);
        }

        System.out.println("RMIassignment port = " + registryport);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Registry registry = LocateRegistry.getRegistry(registryport);
            bankServer = (Bank) registry.lookup("//localhost/Bank");
            atm = new ATM(bankServer);
        } catch (Exception e) {
            System.err.println("ATM exception:");
            e.printStackTrace();
        }
        while(true){
            atm.login();
            atm.atmActions();
        }

    }

    private Long sessionId;
    private Bank bankServer;
    private Scanner sc;
    private int accNumber;

    public ATM(Bank _bankServer) {
        this.bankServer = _bankServer;
        this.sc = new Scanner(System.in);
        this.accNumber = -1;
        this.sessionId = -1L;
    }

    public void login()
    {
        while(sessionId == -1L){
            System.out.print("Login to access Account: Please Enter Username: ");
            String username = this.sc.next();
            System.out.print("Please Enter Password: ");
            String password = this.sc.next();

            try {
                sessionId = this.bankServer.login(username, password);
                System.out.print("Successful login for" + username + ": session ID" + sessionId + "is valid for 5 minutes");
            } catch (InvalidLogin | RemoteException e) {
                System.out.println(e.getMessage());
            }
        }
        this.accNumber = bankServer.getAccountNumber(this.sessionId);
        this.sessionId = sessionId;
    }

    private void logout(){
        this.sessionId = null;
    }

    private void atmActions() {
        boolean logout = false;
        while(!logout) {
            System.out.print("Choose from the following options:");
            System.out.print("W)ithdraw, D)eposit, B)alance, S)tatement or L)ogout");
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
            System.out.print("Enter amount to withdraw: ");
            String amountStr = this.sc.next();
            double amountValue = Double.parseDouble(amountStr);
            BigDecimal amountToWithdraw = new BigDecimal(amountValue);
            bankServer.withdraw(this.accNumber, amountToWithdraw, this.sessionId);
            System.out.println("Successfully withdrew €" + amountToWithdraw + " from account" + accNumber);

        } catch (InvalidTransaction | RemoteException | InvalidSession e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to main menu");
            return;
        }
    }

    private void deposit() {
        try {
            System.out.print("Please Enter deposit account: ");
            String depositStr = sc.next();
            double depostitValue = Double.parseDouble(depositStr);
            BigDecimal amountToDeposit = new BigDecimal(depostitValue);
            bankServer.deposit(this.accNumber, amountToDeposit, this.sessionId);
            System.out.println("Successfully deposited €" + amountToDeposit + " into account" + this.accNumber);

        } catch (InvalidTransaction | RemoteExcept | InvalidSession e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to main menu");
            return;
        }
    }

    private void balance() {
        try {
            BigDecimal balance = bankServer.getBalance(this.accNumber, this.sessionId);
            System.out.println("The current balance of account" + this.accNumber + "is " + balance.toString());
        } catch (InvalidSession | RemoteException e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to main menu");
            return;
        }
    }

    private void statement() {
        try {
            System.out.print("Please Enter startDate: ");
            String startDate = sc.nextLine();
            Date start=new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
            System.out.print("Please Enter endDate: ");
            String endDate = sc.nextLine();
            Date end=new SimpleDateFormat("dd/MM/yyyy").parse(endDate);

            bankServer.getStatement(this.accNumber, start, end, this.sessionId);
            System.out.println("The statement for account" + this.accNumber + "for the period " + start + "to" + end);

        } catch (InvalidTransaction | ParseException | RemoteException | InvalidSession e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to main menu");
            return;
        }
    }
}

