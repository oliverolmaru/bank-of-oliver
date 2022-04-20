package ee.olmaru.bankofoliver;


import ee.olmaru.bankofoliver.data.models.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiControllerTests {
    @LocalServerPort
    private int testPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void GetAccountFromDefaultData() throws Exception {
        Account account = this.restTemplate.getForObject("http://localhost:" + testPort + "/api/accounts/" + DefaultDataIds.accountId, Account.class);
        assertThat(account).isNotNull();
        assertThat(account.getCountryCode()).isEqualTo("EST");
    }

}
