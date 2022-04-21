package ee.olmaru.bankofoliver.data.services;


import ee.olmaru.bankofoliver.data.mappers.Mapper;
import ee.olmaru.bankofoliver.data.models.Account;
import ee.olmaru.bankofoliver.data.models.Balance;
import ee.olmaru.bankofoliver.data.models.Customer;
import ee.olmaru.bankofoliver.data.models.Transaction;
import ee.olmaru.bankofoliver.data.models.enums.Currency;
import ee.olmaru.bankofoliver.data.requests.AccountCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ApiService {
    private Mapper mapper;

    public ApiService(Mapper mapper) {
        this.mapper = mapper;
    }

    public Account GetAccount(UUID id) throws NullPointerException{
        Account account = mapper.getAccount(id);
        return account;
    }

    @Transactional
    public Account CreateAccount(AccountCreateRequest createRequest) {
        Customer customer = mapper.getCustomer(UUID.fromString(createRequest.getCustomerId())
        );
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

    public Customer getCustomer(UUID id) throws NullPointerException{
        Customer customer = mapper.getCustomer(id);
        return customer;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = mapper.getCustomers();
        return customers;
    }

    public Transaction GetTransaction(UUID id) throws NullPointerException{
        Transaction transaction = mapper.getTransaction(id);
        return transaction;
    }

    public Balance GetBalance(UUID id) throws NullPointerException{
        Balance balance = mapper.getBalance(id);
        return balance;
    }


    public List<Transaction> getAccountTransactions(UUID accountId) throws NullPointerException {
        //TODO
        return new ArrayList<>();
    }
}
