package ee.olmaru.bankofoliver.data.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountCreateRequest {
    private String customerId;
    private String countryCode;
    private List<String> currencies;
}
