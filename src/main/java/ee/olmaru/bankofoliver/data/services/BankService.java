package ee.olmaru.bankofoliver.data.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.olmaru.bankofoliver.data.exceptions.GenericApiException;
import ee.olmaru.bankofoliver.data.exceptions.InsufficientFundsException;
import ee.olmaru.bankofoliver.data.exceptions.InvalidAmountException;
import ee.olmaru.bankofoliver.data.mappers.Mapper;
import ee.olmaru.bankofoliver.data.messaging.Amqp;
import ee.olmaru.bankofoliver.data.models.Account;
import ee.olmaru.bankofoliver.data.models.Balance;
import ee.olmaru.bankofoliver.data.models.Customer;
import ee.olmaru.bankofoliver.data.models.Error;
import ee.olmaru.bankofoliver.data.models.Transaction;
import ee.olmaru.bankofoliver.data.models.enums.Currency;
import ee.olmaru.bankofoliver.data.models.enums.ErrorCode;
import ee.olmaru.bankofoliver.data.models.enums.TransactionDirection;
import ee.olmaru.bankofoliver.data.requests.AccountCreateRequest;
import ee.olmaru.bankofoliver.data.requests.TransactionCreateRequest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BankService {
    private Mapper mapper;
    private final Amqp amqp;
    private ObjectMapper objectMapper;

    public BankService(Mapper mapper, Amqp amqp) {
        this.mapper = mapper;
        this.amqp = amqp;
        this.objectMapper = Jackson2ObjectMapperBuilder.json().build();


    }

    public Account getAccount(String id) throws GenericApiException{
        UUID accountId;
        try{
            accountId = UUID.fromString(id);
        }
        catch (IllegalArgumentException ex){
            throw new GenericApiException(new Error(ErrorCode.VAL400,"Account ID is not valid"));
        }
        Account account = mapper.getAccount(accountId).orElseThrow(() -> new GenericApiException(new Error(ErrorCode.ACC404,"No account exists with ID: " + id.toString())));
        return account;
    }

    @Transactional
    public Account createAccount(AccountCreateRequest createRequest) throws NoSuchElementException{
        Customer customer = getCustomer(createRequest.getCustomerId());

        if(createRequest.getCountryCode().isBlank() || createRequest.getCountryCode().length() != 3){
            throw new GenericApiException(new Error(ErrorCode.VAL400,"Country Code has ti be 3-character value"));
        }
        Account newAccount = new Account();
        newAccount.setId(UUID.randomUUID());
        newAccount.setCountryCode(createRequest.getCountryCode());
        newAccount.setCustomer(customer);
        mapper.insertAccount(newAccount);

        for (String currencyStr:
             createRequest.getCurrencies()) {
            Currency currency;
            try {
                currency = Currency.valueOf(currencyStr);
            } catch (IllegalArgumentException ex) {
                String allowedCurrencies = String.join(", ", Arrays.stream(Currency.values()).map(Enum::name).toList());
                throw new GenericApiException(new Error(ErrorCode.VAL400, currencyStr + " is invalid currency value. Accepted values include: " + allowedCurrencies));
            }
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

    public Customer getCustomer(String id) throws GenericApiException{
        UUID customerId;
        try{
            customerId = UUID.fromString(id);
        }
        catch (IllegalArgumentException ex){
            throw new GenericApiException(new Error(ErrorCode.VAL400,"Customer ID is not valid"));
        }
        Customer customer = mapper.getCustomer(customerId).orElseThrow(() -> new GenericApiException(new Error(ErrorCode.CUS404, "No customer exists with ID: " + id.toString())));
        return customer;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = mapper.getCustomers();
        return customers;
    }

    public Balance getBalance(String id) throws GenericApiException{
        UUID balanceId;
        try{
            balanceId = UUID.fromString(id);
        }
        catch (IllegalArgumentException ex){
            throw new GenericApiException(new Error(ErrorCode.VAL400,"Balance ID is not valid"));
        }
        Balance balance = mapper.getBalance(balanceId).orElseThrow(() -> new GenericApiException(new Error(ErrorCode.BAL404,"No balance exists with ID: " + id)));
        return balance;
    }

    public Transaction getTransaction(String id) throws GenericApiException{
        UUID transactionId;
        try{
            transactionId = UUID.fromString(id);
        }
        catch (IllegalArgumentException ex){
            throw new GenericApiException(new Error(ErrorCode.VAL400,"Transaction ID is not valid"));
        }
        Transaction transaction = mapper.getTransaction(transactionId).orElseThrow(() -> new GenericApiException(new Error(ErrorCode.TRA404,"No transaction exists with ID: " + id)));
        return transaction;
    }

    @Transactional
    public Transaction createTransaction(TransactionCreateRequest request) throws GenericApiException {
        Currency currency;
        TransactionDirection direction;
        try {
            currency = Currency.valueOf(request.getCurrency());
        } catch (IllegalArgumentException ex) {
            String allowedCurrencies = String.join(", ", Arrays.stream(Currency.values()).map(Enum::name).toList());
            throw new GenericApiException(new Error(ErrorCode.VAL400, request.getCurrency() + " is invalid currency value. Accepted values include: " + allowedCurrencies));
        }

        try {
            direction = TransactionDirection.valueOf(request.getDirection());
        } catch (IllegalArgumentException ex) {
            String allowedDirections = String.join(", ", Arrays.stream(TransactionDirection.values()).map(Enum::name).toList());
            throw new GenericApiException(new Error(ErrorCode.VAL400, request.getDirection() + " is invalid transaction direction. Accepted values include: " + allowedDirections));
        }
        Account account = getAccount(request.getAccountId());

        Balance balance = account.getBalances().stream().filter(bal -> bal.getCurrencyCode() == currency).findFirst().orElseThrow(() -> new GenericApiException(new Error(ErrorCode.BAL404, "This account does not have balance for " + request.getCurrency())));

        if(request.getDescription().isBlank()){
            throw new GenericApiException(new Error(ErrorCode.VAL400,"Description is required"));
        }

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setAmount(request.getAmount());
        transaction.setBalance(balance);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setDirection(direction);
        transaction.setCurrencyCode(currency);
        transaction.setDescription(request.getDescription());

        try {
            balance.AddTransactionToAmount(transaction);
        } catch (InvalidAmountException ex) {
            throw new GenericApiException(new Error(ErrorCode.VAL400, "Amount has to be a positive number"));
        } catch (InsufficientFundsException ex){
            throw new GenericApiException(new Error(ErrorCode.TRA405, "Insufficient funds available to perform this transaction"));
        }

        mapper.insertTransaction(transaction);
        mapper.updateBalance(balance);

        try{
            String serialized = objectMapper.writeValueAsString(transaction);
            amqp.getTemplate().convertAndSend("bank-of-olmaru-exchange","banking.transactions.insert", serialized);
        } catch (JsonProcessingException ex){
            System.out.println("JSON processing failed - " + ex.getMessage());
        }

        return transaction;
    }

}
