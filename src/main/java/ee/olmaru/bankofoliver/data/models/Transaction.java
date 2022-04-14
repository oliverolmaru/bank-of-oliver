package ee.olmaru.bankofoliver.data.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private TransactionDirection direction;
    private String currencyCode;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
}
