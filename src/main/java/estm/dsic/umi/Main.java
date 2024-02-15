package estm.dsic.umi;

import java.util.List;
import java.util.Scanner;

import estm.dsic.umi.beans.Account;
import estm.dsic.umi.beans.Transaction;
import estm.dsic.umi.beans.User;
import estm.dsic.umi.business.AccountService;
import estm.dsic.umi.business.AuthService;
import estm.dsic.umi.business.TransactionService;

import java.rmi.Naming;

public class Main {
    static User user;
    static AuthService defaultAuthService;
    static AccountService accountService;
    static TransactionService transactionService;
    static List<Account> accounts;
    static Account account;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("************* Authentication ***********");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        // Prompt for password
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // TODO: REMOVE THIS TWO LINES
        email = "mesbahi.ed@gmail.com";
        password ="123456";

        try {
            // Look up the remote object in the RMI registry
            defaultAuthService = (AuthService) Naming.lookup("rmi://localhost/defaultAuthService");

            // Invoke the remote method
            user = defaultAuthService.authenticate(email,password);
            if (user != null) {
                System.out.println("Authentication successful!");
                accountService = (AccountService) Naming.lookup("rmi://localhost/accountService");
                transactionService = (TransactionService) Naming.lookup("rmi://localhost/transactionService");

                boolean finished = false;

                do {
                    // Display menu
                    System.out.println("************* MENU ***********");
                    System.out.println("Choose a service:");
                    System.out.println("1. Deposit");
                    System.out.println("2. Withdraw");
                    System.out.println("3. Transfer");
                    System.out.println("4. Bank Account Statement");
                    System.out.println("5. Finish");
                    System.out.println("******************************");


                    // Get user input
                    int choice = scanner.nextInt();

                    if (choice == 1){
                        System.out.println("Service: Deposit");
                        accounts=accountService.getUserAccounts(user);
                        user.setAccounts(accounts);
                        for (Account account:
                                user.getAccounts()) {
                            // show accounts without transactions
                            System.out.println("Account ID: "+account.getId()+" Balance: "+account.getBalance());
                        }
                        System.out.println("Choose an account to deposit to (ID) : ");
                        int accountId = scanner.nextInt();
                        account = user.getAccounts().stream().filter(a->a.getId()==accountId).findFirst().get();
                        System.out.println("Enter the amount to deposit: ");
                        double amount = scanner.nextDouble();
                        System.out.println(accountService.deposit(account,amount));
                    } else if(choice == 2){
                        System.out.println("Service: Withdraw");
                        accounts=accountService.getUserAccounts(user);
                        user.setAccounts(accounts);
                        for (Account account:
                                user.getAccounts()) {
                            // show accounts without transactions
                            System.out.println("Account ID: "+account.getId()+" Balance: "+account.getBalance());
                        }
                        System.out.println("Choose an account to withdraw from (ID) : ");
                        int accountId = scanner.nextInt();
                        Account account = user.getAccounts().stream().filter(a->a.getId()==accountId).findFirst().get();
                        System.out.println("Enter the amount you want to withdraw : ");
                        Double amount = scanner.nextDouble();
                        System.out.println(accountService.withdraw(account,amount));
                    } else if (choice == 3){
                        System.out.println("Service: Transfer");
                        System.out.println("Chooses an account (ID) to transfer from : ");
                        user.setAccounts(accountService.getUserAccounts(user));
                        for (Account account_:
                                user.getAccounts()) {
                            // show accounts without transactions
                            System.out.println("Account ID: "+account_.getId()+" Balance: "+account_.getBalance());
                        }
                        int srcAccountId = scanner.nextInt();
                        Account srcAccount = user.getAccounts().stream().filter(a->a.getId()==srcAccountId).findFirst().get();
                        System.out.println("Chooses an account (ID) to transfer to : ");
                        Integer destAccountId = scanner.nextInt();
                        Account destAccount = new Account();
                        destAccount.setId(destAccountId);
                        System.out.println("Enter the amount to transfer : ");
                        Double amount = scanner.nextDouble();
                        System.out.println(accountService.transfer(srcAccount,destAccount,amount));
                    } else if (choice == 4){
                        System.out.println("Service: Bank Account Statement");
                        System.out.println("Choose an account to show its statement (ID) : ");
                        user.setAccounts(accountService.getUserAccounts(user));
                        for (Account account:
                                user.getAccounts()) {
                            // show accounts without transactions
                            System.out.println("Account ID: "+account.getId()+" Balance: "+account.getBalance());
                        }
                        int accountId = scanner.nextInt();
                        Account account = user.getAccounts().stream().filter(a->a.getId()==accountId).findFirst().get();
                        List<Transaction> transactions = transactionService.getTransactionsOfAnAccount(account);
                        for (Transaction transaction:
                                transactions) {
                            System.out.println("Transaction ID: "
                                    +transaction.getId()+" Amount: "
                                    +transaction.getAmount()+
                                    " Source Account: "+transaction.getSrcAccount()+
                                    " Destination Account: "+transaction.getDestAccount()+
                                    " Type: "+transaction.getTransactionType()+
                                    " Date: "+transaction.getDate()
                            );
                        }
                    } else if (choice == 5){
                        System.out.println("Service: Finish");
                        finished = true;
                    } else {
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                    }
                    // Process user choice using switch statement
                    /*switch (choice) {
                        case 1:
                            System.out.println("Service: Deposit");
                            accounts=accountService.getUserAccounts(user);
                            user.setAccounts(accounts);
                            for (Account account:
                                 user.getAccounts()) {
                                // show accounts without transactions
                                System.out.println("Account ID: "+account.getId()+" Balance: "+account.getBalance());
                            }
                            System.out.println("Choose an account to deposit to (ID) : ");
                            int accountId = scanner.nextInt();
                            account = user.getAccounts().stream().filter(a->a.getId()==accountId).findFirst().get();
                            System.out.println("Enter the amount to deposit: ");
                            double amount = scanner.nextDouble();
                            System.out.println(accountService.deposit(account,amount));
                            break;

                        case 2:
                            System.out.println("Service: Withdraw");
                            accounts=accountService.getUserAccounts(user);
                            user.setAccounts(accounts);
                            for (Account account:
                                    user.getAccounts()) {
                                // show accounts without transactions
                                System.out.println("Account ID: "+account.getId()+" Balance: "+account.getBalance());
                            }
                            System.out.println("Choose an account to withdraw from (ID) : ");
                            accountId = scanner.nextInt();
                            Account account = user.getAccounts().stream().filter(a->a.getId()==accountId).findFirst().get();
                            System.out.println("Enter the amount you want to withdraw : ");
                            amount = scanner.nextDouble();
                            System.out.println(accountService.withdraw(account,amount));
                            break;

                        case 3:
                            System.out.println("Service: Transfer");
                            System.out.println("Chooses an account (ID) to transfer from : ");
                            for (Account account_:
                                    user.getAccounts()) {
                                // show accounts without transactions
                                System.out.println("Account ID: "+account_.getId()+" Balance: "+account_.getBalance());
                            }
                            int srcAccountId = scanner.nextInt();
                            Account srcAccount = user.getAccounts().stream().filter(a->a.getId()==srcAccountId).findFirst().get();
                            System.out.println("Chooses an account (ID) to transfer to : ");
                            Integer destAccountId = scanner.nextInt();
                            Account destAccount = new Account();
                            destAccount.setId(destAccountId);
                            System.out.println("Enter the amount to transfer : ");
                            amount = scanner.nextDouble();
                            System.out.println(accountService.transfer(srcAccount,destAccount,amount));
                            break;

                        case 4:
                            System.out.println("Service: Bank Account Statement");
                            // Add statement logic here
                            break;

                        case 5:
                            System.out.println("Service: Finish");
                            finished = true;
                            break;

                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                    }*/

                } while (!finished);

                System.out.println("Thank you for using our banking services!");

            } else {
                System.out.println("Authentication failed. Invalid email or password.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}