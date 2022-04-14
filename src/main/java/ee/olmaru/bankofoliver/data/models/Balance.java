package ee.olmaru.bankofoliver.data.models;

import java.math.BigDecimal;
import java.util.UUID;

public class Balance {
    private UUID id;
    private String currencyCode;
    private BigDecimal amount;
}
