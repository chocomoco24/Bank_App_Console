package service.impl;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import repository.AccountRepository;
import repository.TransactionRepository;
import repository.CustomerRepository;
import domain.Account;
import domain.Customer;
import domain.Transaction;
import service.BankService;
import domain.Type;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;



public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository CustomerRepository = new CustomerRepository();
    
    public String openAccount(String name, String email, String accountType){

        String customerId = UUID.randomUUID().toString();

        Customer customer = new Customer(email, customerId, name);
        CustomerRepository.save(customer);

        String accountNumber = getAccountNumber();

        Account account = new Account(accountNumber, accountType,(double)0,customerId);

        accountRepository.save(account);

        return accountNumber;
    }

    @Override
    public List<Account> listAccounts(){
        return accountRepository.findAll().stream().sorted(Comparator.comparing(Account :: getAccountNumber)).collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String note){
        Account account = accountRepository.findByNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account Not Found: " + accountNumber));

        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction(account.getAccountNumber(), amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.DEPOSIT);

        transactionRepository.add(transaction);

    }

    @Override
    public void withdraw(String accountNumber, Double amount, String note){
        Account account = accountRepository.findByNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account Not Found: " + accountNumber));

        if(account.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("Insufficient Balance");


        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction(account.getAccountNumber(), amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.WITHDRAW);

        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String fromAcc, String toAcc, Double amount, String note) {
        if(fromAcc.equals(toAcc))
            throw new RuntimeException("Cannot transfer to your own account:");

        Account from = accountRepository.findByNumber(fromAcc).orElseThrow(() -> new AccountNotFoundException("Account Not Found: " + fromAcc));

        Account to = accountRepository.findByNumber(toAcc).orElseThrow(() -> new AccountNotFoundException("Account Not Found: " + toAcc));

        if(from.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("Insufficient Balance");

        from.setBalance(from.getBalance() - amount);
        to.setBalance(from.getBalance() + amount);

        Transaction fromTransaction = new Transaction(from.getAccountNumber(), amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.TRANSFER_OUT);

        Transaction toTransaction = new Transaction(to.getAccountNumber(), amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.TRANSFER_IN);

        transactionRepository.add(fromTransaction);
    }

    @Override
    public List<Transaction> getStatement(String account){
        return transactionRepository.findByAccount(account).stream().sorted(Comparator.comparing(Transaction :: getTimestamp)).collect(Collectors.toList());
    }

    @Override
    public List<Account> searchAccountsByCustomerName(String name) {
        String query = (name == null) ? "" : name.toLowerCase();
        List<Account> result = new ArrayList<>();

        for(Customer c : CustomerRepository.findAll()){
            if(c.getName().toLowerCase().contains(query))
                result.addAll(accountRepository.findByCustomerId(c.getId()));
        }
        result.sort(Comparator.comparing(Account::getAccountNumber));
        return result;
    }


     private String getAccountNumber() {
        int size = accountRepository.findAll().size() +1;
        String accountNumber = String.format("AC%06d", size); // AC000001
        return accountNumber;
    }


}