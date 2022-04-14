package ee.olmaru.bankofoliver;


import ee.olmaru.bankofoliver.data.mappers.Mapper;
import ee.olmaru.bankofoliver.data.models.Account;
import ee.olmaru.bankofoliver.data.models.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
