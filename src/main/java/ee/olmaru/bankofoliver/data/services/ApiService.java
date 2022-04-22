package ee.olmaru.bankofoliver.data.services;


import ee.olmaru.bankofoliver.data.exceptions.InvalidCurrencyException;
import ee.olmaru.bankofoliver.data.exceptions.InvalidTransactionDirectionException;
import ee.olmaru.bankofoliver.data.mappers.Mapper;
import ee.olmaru.bankofoliver.data.models.Account;
import ee.olmaru.bankofoliver.data.models.Balance;
import ee.olmaru.bankofoliver.data.models.Customer;
import ee.olmaru.bankofoliver.data.models.Transaction;
import ee.olmaru.bankofoliver.data.models.enums.Currency;
import ee.olmaru.bankofoliver.data.models.enums.TransactionDirection;
import ee.olmaru.bankofoliver.data.requests.AccountCreateRequest;
import ee.olmaru.bankofoliver.data.requests.TransactionCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ApiService {
    private Mapper mapper;

    public ApiService(Mapper mapper) {
        this.mapper = mapper;
    }

    public Account getAccount(UUID id) throws NoSuchElementException{
        Account account = mapper.getAccount(id).orElseThrow(() -> new NoSuchElementException("No account exists with ID: " + id.toString()));
        return account;
    }

    @Transactional
    public Account createAccount(AccountCreateRequest createRequest) throws NoSuchElementException{
        Customer customer = getCustomer(UUID.fromString(createRequest.getCustomerId()));
        Account newAccount = new Account();
        newAccount.setId(UUID.randomUUID());
        newAccount.setCountryCode(createRequest.getCountryCode());
        newAccount.setCustomer(customer);
        mapper.insertAccount(newAccount);

        for (String currencyStr:
             createRequest.getCurrencies()) {
            Currency currency = Currency.valueOf(currencyStr);
            Balance balance = new Balance();

            Balance newBalance = new Balance();
            newBalance.setId(UUID.randomUUID());
            newBalance.setCurrencyCode(currency);
            newBalance.setAmount(BigDecimal.valueOf(0));
            newBalance.setAccount(newAccount);
            newAccount.addBalance(newBalance);

            mapper.insertBalance(newBalance);

        }
        return  newAccount;
    }

    public Customer getCustomer(UUID id) throws NoSuchElementException{
        Customer customer = mapper.getCustomer(id).orElseThrow(() -> new NoSuchElementException("No customer exists with ID: " + id.toString()));;
        return customer;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = mapper.getCustomers();
        return customers;
    }

    public Transaction GetTransaction(UUID id) throws NoSuchElementException{
        Transaction transaction = mapper.getTransaction(id).orElseThrow(() -> new NoSuchElementException("No transaction exists with ID: " + id.toString()));
        return transaction;
    }

    public Balance GetBalance(UUID id) throws NoSuchElementException{
        Balance balance = mapper.getBalance(id).orElseThrow(() -> new NoSuchElementException("No balance exists with ID: " + id.toString()));
        return balance;
    }

    @Transactional
    public Transaction CreateTransaction(TransactionCreateRequest request) throws InvalidCurrencyException, InvalidTransactionDirectionException, NoSuchElementException {
        Currency currency;
        TransactionDirection direction;
        try { currency = Currency.valueOf(request.getCurrency()); } catch (IllegalArgumentException ex) { throw new InvalidCurrencyException();}
        try { direction = TransactionDirection.valueOf(request.getDirection()); } catch (IllegalArgumentException ex) { throw new InvalidTransactionDirectionException();}
        Account account = getAccount(UUID.fromString(request.getAccountId()));
        Balance balance = account.getBalances().stream().filter(bal -> bal.getCurrencyCode() == currency).findFirst().orElseThrow(() -> new NoSuchElementException("This account does not have balance for " + request.getCurrency()));

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setAmount(request.getAmount());
        transaction.setBalance(balance);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setDirection(direction);
        transaction.setCurrencyCode(currency);
        transaction.setDescription(request.getDescription());

        balance.AddTransactionToAmount(transaction);

        mapper.insertTransaction(transaction);
        mapper.updateBalance(balance);

        return transaction;
    }


    public List<Transaction> getAccountTransactions(UUID accountId) throws NullPointerException {
        //TODO
        return new ArrayList<>();
    }
}
