package app;

import java.util.Scanner;

import service.BankService;

import service.impl.BankServiceImpl;

public class main {
    public static void main(String [] args) {

        Scanner sc = new Scanner(System.in);
        BankService bankService = new BankServiceImpl();
        Boolean running = true;

        System.out.println("Welcome to Console Bank");
        while(running){
            System.out.println("""
                1. Open Account
                2. Deposit
                3. Withdraw
                4. Transfer
                5. Account Statement
                6. List Accounts
                7. Search by Customer name
                0. Exit
                """);

        System.out.print("CHOOSE:");
        String choice = sc.nextLine().trim();
        System.out.println("CHOICE: "+ choice);
        if(choice.equals("0"))
            running = false;

        switch (choice) {
            case "1" -> openAccount(sc, bankService);
            case "2" -> deposit(sc,bankService);
            case "3" -> withdraw(sc, bankService);
            case "4" -> transfer(sc, bankService);
            case "5" -> statement(sc, bankService);
            case "6" -> listAccounts(sc, bankService);
            case "7" -> searchAccounts(sc, bankService);
            case "0" -> running = false;
            default -> System.out.println("Invalid choice");
        }

        }
        
    }


    private static void openAccount(Scanner sc, BankService bankService) {

        System.out.println("Customer name: ");
        String name = sc.nextLine().trim();

        System.out.println("Customer email: ");
        String email = sc.nextLine().trim();

        System.out.println("Account type(SAVINGS/CURRENT): ");
        String type = sc.nextLine().trim();

        System.out.println("Initial deposit(optional, blank for 0): ");
        String amountStr = sc.nextLine().trim();
        Double initial = Double.valueOf(amountStr);
        
        String accountNumber = bankService.openAccount(name, email, type);
        if(initial > 0)
            bankService.deposit(accountNumber, initial, "Initial Deposit");

        System.out.println("Account opened: " + accountNumber);
    }

    private static void deposit(Scanner sc, BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber = sc.nextLine().trim();
        System.out.println("Amount: ");
        Double amount = Double.valueOf(sc.nextLine().trim());
        bankService.deposit(accountNumber, amount, "Deposit");
        System.out.println("Deposited");
    }

    private static void withdraw(Scanner sc, BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber = sc.nextLine().trim();
        System.out.println("Amount: ");
        Double amount = Double.valueOf(sc.nextLine().trim());
        bankService.withdraw(accountNumber, amount, "Withdrawl");
        System.out.println("Withdrawn");
        
    }

    private static void transfer(Scanner sc, BankService bankService) {
        System.out.println("From account number: ");
        String from = sc.nextLine().trim();
        System.out.println("To account number: ");
        String to = sc.nextLine().trim();
        System.out.println("Amount:");
        Double amount = Double.valueOf(sc.nextLine().trim());
        bankService.transfer(from,to,amount,"Transfer");
        System.out.println("Transferred");
        
    }

    private static void statement(Scanner sc, BankService bankService) {
        System.out.println("Account number: ");
        String account = sc.nextLine().trim();

        bankService.getStatement(account).forEach(t -> {
            System.out.println(t.getTimestamp() + " | " + t.getType() + " | " + t.getAmount() + " | " + t.getNote());
        } );
        
    }

    private static void listAccounts(Scanner sc, BankService bankService) {
        bankService.listAccounts().forEach(a -> {
            System.out.println(a.getAccountNumber() + " | " + a.getAccountType() + " | " + a.getBalance());
        });
    }

    private static void searchAccounts(Scanner sc, BankService bankService) {
        System.out.println("Customer name: ");
        String name = sc.nextLine().trim();

        bankService.searchAccountsByCustomerName(name).forEach(account -> 
            System.out.println(account.getAccountNumber() + " | " + account.getAccountType() + " | " + account.getBalance())
        );
    } 
        
}


