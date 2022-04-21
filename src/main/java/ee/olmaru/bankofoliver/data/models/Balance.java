package ee.olmaru.bankofoliver.data.models;

import com.fasterxml.jackson.annotation.*;
import ee.olmaru.bankofoliver.data.models.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Setter
@Getter
public class Balance {
    private UUID id;
    private Currency currencyCode;
    private BigDecimal amount;
    @JsonBackReference
    private Account account;
    @JsonIgnore
    private List<Transaction> transactions;

}
