package ee.olmaru.bankofoliver.data.requests;

import ee.olmaru.bankofoliver.data.models.enums.Currency;
import ee.olmaru.bankofoliver.data.models.enums.TransactionDirection;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionCreateRequest {
    private String accountId;
    private BigDecimal amount;
    private String currency;
    private String direction;
    private String description;
}
