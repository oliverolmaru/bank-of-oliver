package ee.olmaru.bankofoliver;


import ee.olmaru.bankofoliver.data.mappers.Mapper;
import ee.olmaru.bankofoliver.data.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AccountIntegrationTests {
    @Autowired
    Mapper mapper;

    @Test
    public void createNewAccount_shouldReturnCustomersName() {
        Customer customer = mapper.getCustomer(UUID.fromString("713024c6-615f-466c-9a14-95ba2f147cd3"));
        Account newAccount = new Account();
        UUID id = UUID.randomUUID();
        newAccount.setCountryCode("EST");
        newAccount.setId(id);
        newAccount.setCustomer(customer);
        mapper.insertAccount(newAccount);

        Account account = mapper.getAccount(id);
        assertThat(account).isNotNull();
        assertThat(account.getCustomer().getFirstName()).isEqualTo("Fixed");
    }

    @Test
    public void createNewBalance_shouldReturnEmptyBalance() {
        Account account = mapper.getAccount(UUID.fromString("713024c6-615f-466c-9a14-95ba2f147cd4"));

        UUID id = UUID.randomUUID();
        Balance newBalance = new Balance();
        newBalance.setId(id);
        newBalance.setCurrencyCode("EUR");
        newBalance.setAmount(BigDecimal.valueOf(99.99));
        newBalance.setAccount(account);
        mapper.insertBalance(newBalance);

        Balance balance = mapper.getBalance(id);
        assertThat(balance).isNotNull();
        assertThat(balance.getAccount().getCustomer().getFirstName()).isEqualTo("Fixed");
    }

    @Test
    public void createNewTransaction_shouldReturnTransaction() {
        Balance balance = mapper.getBalance(UUID.fromString("713024c6-615f-466c-9a14-95ba2f147cd5"));

        UUID id = UUID.randomUUID();
        Transaction newTransaction = new Transaction();
        newTransaction.setId(id);
        newTransaction.setCurrencyCode("EUR");
        newTransaction.setAmount(BigDecimal.valueOf(99.99));
        newTransaction.setDescription("My test transaction");
        newTransaction.setDirection(TransactionDirection.IN);
        newTransaction.setCreatedAt(LocalDateTime.now());
        newTransaction.setBalance(balance);
        mapper.insertTransaction(newTransaction);

        Transaction transaction = mapper.getTransaction(id);
        assertThat(transaction).isNotNull();
        assertThat(transaction.getBalance().getAccount().getCustomer().getFirstName()).isEqualTo("Fixed");
    }
}
