package ee.olmaru.bankofoliver.data.services;


import ee.olmaru.bankofoliver.data.mappers.Mapper;
import ee.olmaru.bankofoliver.data.models.Account;
import ee.olmaru.bankofoliver.data.models.Balance;
import ee.olmaru.bankofoliver.data.models.Customer;
import ee.olmaru.bankofoliver.data.models.Transaction;
import org.springframework.stereotype.Service;

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

    public Customer getCustomer(UUID id) throws NullPointerException{
        Customer customer = mapper.getCustomer(id);
        return customer;
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
