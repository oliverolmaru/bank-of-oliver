package ee.olmaru.bankofoliver;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.olmaru.bankofoliver.data.DefaultDataIds;
import ee.olmaru.bankofoliver.data.exceptions.GenericApiException;
import ee.olmaru.bankofoliver.data.messaging.Amqp;
import ee.olmaru.bankofoliver.data.models.*;
import ee.olmaru.bankofoliver.data.models.enums.Currency;
import ee.olmaru.bankofoliver.data.models.enums.ErrorCode;
import ee.olmaru.bankofoliver.data.requests.AccountCreateRequest;
import ee.olmaru.bankofoliver.data.requests.TransactionCreateRequest;
import ee.olmaru.bankofoliver.data.responses.ErrorResponse;
import ee.olmaru.bankofoliver.data.responses.TransactionCreateResponse;
import ee.olmaru.bankofoliver.data.services.BankService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankIntegrationTests {
    @LocalServerPort
    private int testPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BankService bankService;


    @BeforeEach
    public void resetRabbitQueue(){
        //amqp.getAdmin().purgeQueue("banking.transactions");
        //amqp.getAdmin().purgeQueue("banking.accounts");
        //amqp.getAdmin().purgeQueue("banking.balances");
        //amqp.getAdmin().purgeQueue("banking.customers");
    }

    // Customers

    @Test
    public void getCustomer_shouldReturnDefaultCustomer() {
        Customer customer = bankService.getCustomer(DefaultDataIds.customerId);
        assertThat(customer.getId()).isEqualTo(UUID.fromString(DefaultDataIds.customerId));
    }
    @Test
    public void getCustomer_shouldThrowValidationException() {
        GenericApiException ex =  Assertions.assertThrows(GenericApiException.class,  () -> this.bankService.getCustomer("1234"));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.VAL400);
    }
    @Test
    public void getCustomer_shouldThrowNoElementException() {
        GenericApiException ex =  Assertions.assertThrows(GenericApiException.class,  () -> this.bankService.getCustomer("99564bea-2886-4937-a226-34421af76522"));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.CUS404);
    }

    @Test
    public void getCustomers_shouldReturnAllCustomers() {
        List<Customer> customers = this.bankService.getAllCustomers();
        assertThat(customers.size()).isGreaterThan(0);
    }

    @Test
    public void getCustomerHttp_shouldReturnDefaultCustomer() {
        Customer customer = this.restTemplate.getForObject("http://localhost:" + testPort + "/api/customers/" + DefaultDataIds.customerId, Customer.class);
        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isEqualTo(UUID.fromString(DefaultDataIds.customerId));
    }

    
    // Accounts
    @Test
    public void getAccount_shouldReturnDefaultAccount() {
        Account account = bankService.getAccount(DefaultDataIds.accountId);
        assertThat(account.getId()).isEqualTo(UUID.fromString(DefaultDataIds.accountId));
    }
    @Test
    public void getAccount_shouldThrowValidationException() {
        GenericApiException ex =  Assertions.assertThrows(GenericApiException.class,  () -> this.bankService.getAccount("1234"));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.VAL400);
    }
    @Test
    public void getAccount_shouldThrowNoElementException() {
        GenericApiException ex =  Assertions.assertThrows(GenericApiException.class,  () -> this.bankService.getAccount("99564bea-2886-4937-a226-34421af76522"));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.ACC404);
    }

    @Test
    public void createAccount_shouldReturnNewAccount() {
        AccountCreateRequest createRequest = new AccountCreateRequest();
        createRequest.setCustomerId(DefaultDataIds.customerId);
        createRequest.setCountryCode("EST");
        createRequest.setCurrencies(Arrays.stream(Currency.values()).map(Enum::name).toList());

        Account account = this.bankService.createAccount(createRequest);
        assertThat(account.getId()).isNotNull();
    }

    @Test
    public void createAccount_shouldThrowInvalidCurrencyException() {
        AccountCreateRequest createRequest = new AccountCreateRequest();
        createRequest.setCustomerId(DefaultDataIds.customerId);
        createRequest.setCountryCode("EST");
        List<String> currencies = new ArrayList<>();
        currencies.add("ERROR_CURRENCY");
        createRequest.setCurrencies(currencies);
        GenericApiException ex = Assertions.assertThrows(GenericApiException.class, () -> this.bankService.createAccount(createRequest));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.VAL400);
    }

    @Test
    public void getAccountHttp_shouldReturnDefaultAccount() {
        Account account = this.restTemplate.getForObject("http://localhost:" + testPort + "/api/accounts/" + DefaultDataIds.accountId, Account.class);
        assertThat(account).isNotNull();
        assertThat(account.getId()).isEqualTo(UUID.fromString(DefaultDataIds.accountId));
    }

    //Balance
    @Test
    public void getBalance_shouldReturnDefaultBalance() {
        Balance balance = bankService.getBalance(DefaultDataIds.balanceId);
        assertThat(balance.getId()).isEqualTo(UUID.fromString(DefaultDataIds.balanceId));
    }
    @Test
    public void getBalance_shouldThrowValidationException() {
        GenericApiException ex =  Assertions.assertThrows(GenericApiException.class,  () -> this.bankService.getBalance("1234"));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.VAL400);
    }
    @Test
    public void getBalance_shouldThrowNoElementException() {
        GenericApiException ex =  Assertions.assertThrows(GenericApiException.class,  () -> this.bankService.getBalance("99564bea-2886-4937-a226-34421af76522"));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.BAL404);
    }
    
    // Transactions
    @Test
    public void getTransaction_shouldReturnDefaultTransaction() {
        Transaction transaction = bankService.getTransaction(DefaultDataIds.transactionId);
        assertThat(transaction.getId()).isEqualTo(UUID.fromString(DefaultDataIds.transactionId));
    }
    @Test
    public void getTransaction_shouldThrowValidationException() {
        GenericApiException ex =  Assertions.assertThrows(GenericApiException.class,  () -> this.bankService.getTransaction("1234"));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.VAL400);
    }
    @Test
    public void getTransaction_shouldThrowNoElementException() {
        GenericApiException ex =  Assertions.assertThrows(GenericApiException.class,  () -> this.bankService.getTransaction("99564bea-2886-4937-a226-34421af76522"));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.TRA404);
    }

    private TransactionCreateRequest newValidTransactionCreateRequest(){
        TransactionCreateRequest createRequest = new TransactionCreateRequest();
        createRequest.setAmount(BigDecimal.valueOf(1));
        createRequest.setDescription("Description");
        createRequest.setDirection("IN");
        createRequest.setCurrency("EUR");
        createRequest.setAccountId(DefaultDataIds.accountId);
        
        return createRequest;
    }

    @Test
    public void createTransaction_shouldReturnNewTransaction() throws InterruptedException, JsonProcessingException {
        TransactionCreateRequest createRequest = newValidTransactionCreateRequest();
        Transaction transaction = this.bankService.createTransaction(createRequest);
        assertThat(transaction.getId()).isNotNull();

        String queueMessage = (String) rabbitTemplate.receiveAndConvert("banking.transactions",1000);
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        Transaction receivedTransaction = objectMapper.readValue(queueMessage,Transaction.class);
        assertThat(receivedTransaction.getId()).isEqualTo(transaction.getId());

    }
    
    @Test
    public void createTransaction_shouldThrowInvalidCurrencyException() {
        TransactionCreateRequest createRequest = newValidTransactionCreateRequest();
        createRequest.setCurrency("ERROR_CURRENCY");

        GenericApiException ex = Assertions.assertThrows(GenericApiException.class, () -> this.bankService.createTransaction(createRequest));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.VAL400);
    }
    
    @Test
    public void createTransaction_shouldThrowInvalidAmountException() {
        TransactionCreateRequest createRequest = newValidTransactionCreateRequest();
        createRequest.setAmount(BigDecimal.valueOf(-1));

        GenericApiException ex = Assertions.assertThrows(GenericApiException.class, () -> this.bankService.createTransaction(createRequest));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.VAL400);
    }
    
    @Test
    public void createTransaction_shouldThrowInvalidDirectionException() {
        TransactionCreateRequest createRequest = newValidTransactionCreateRequest();
        createRequest.setDirection("ERROR_DIRECTION");

        GenericApiException ex = Assertions.assertThrows(GenericApiException.class, () -> this.bankService.createTransaction(createRequest));
        assertThat(ex.getError().getCode()).isEqualTo(ErrorCode.VAL400);
    }

    @Test
    public void getTransactionHttp_shouldReturnDefaultTransaction() {
        Transaction transaction = this.restTemplate.getForObject("http://localhost:" + testPort + "/api/transactions/" + DefaultDataIds.transactionId, Transaction.class);
        assertThat(transaction).isNotNull();
        assertThat(transaction.getId()).isEqualTo(UUID.fromString(DefaultDataIds.transactionId));
    }

    @Test
    public void postTransactionHttp_shouldReturnErrorResponse() {
        TransactionCreateRequest createRequest = newValidTransactionCreateRequest();
        createRequest.setAccountId("1234");
        ErrorResponse errorResponse = this.restTemplate.postForObject("http://localhost:" + testPort + "/api/transactions/", createRequest, ErrorResponse.class);
        assertThat(errorResponse.getError()).isNotNull();
        assertThat(errorResponse.getError().getCode()).isEqualTo(ErrorCode.VAL400);
    }
}
