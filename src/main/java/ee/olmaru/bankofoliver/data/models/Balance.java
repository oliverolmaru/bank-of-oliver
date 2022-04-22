package ee.olmaru.bankofoliver.data.models;

import com.fasterxml.jackson.annotation.*;
import ee.olmaru.bankofoliver.data.exceptions.InvalidAmountException;
import ee.olmaru.bankofoliver.data.exceptions.InvalidCurrencyException;
import ee.olmaru.bankofoliver.data.models.enums.Currency;
import ee.olmaru.bankofoliver.data.models.enums.TransactionDirection;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Balance {
    private UUID id;
    private Currency currencyCode;
    private BigDecimal amount;
    @JsonBackReference
    private Account account;
    @JsonIgnore
    private List<Transaction> transactions;

    public void AddTransactionToAmount(Transaction transaction) throws InvalidAmountException, InvalidCurrencyException {
        if(transaction.getAmount().compareTo(BigDecimal.ZERO) == -1) throw new InvalidAmountException();
        BigDecimal newAmount;
        if(transaction.getDirection() == TransactionDirection.IN){
            newAmount = this.amount.add(transaction.getAmount());
        }
        else if(transaction.getDirection() == TransactionDirection.OUT){
            newAmount = this.amount.subtract(transaction.getAmount());
        }
        else{
            throw new InvalidCurrencyException();
        }

        if(newAmount.compareTo(BigDecimal.ZERO) == -1) throw new InvalidAmountException();

        this.amount = newAmount;
    }

}
