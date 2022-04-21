package ee.olmaru.bankofoliver.data.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ee.olmaru.bankofoliver.data.models.enums.Currency;
import ee.olmaru.bankofoliver.data.models.enums.TransactionDirection;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
public class Transaction {
    private UUID id;
    private TransactionDirection direction;
    private Currency currencyCode;
    private BigDecimal amount;
    private String description;
    @JsonManagedReference
    private Balance balance;
    private LocalDateTime createdAt;

}
