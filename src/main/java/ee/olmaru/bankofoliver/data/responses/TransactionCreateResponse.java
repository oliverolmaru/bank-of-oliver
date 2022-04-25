package ee.olmaru.bankofoliver.data.responses;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ee.olmaru.bankofoliver.data.models.Account;
import ee.olmaru.bankofoliver.data.models.Transaction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionCreateResponse {
    private Transaction transaction;
    private String accountId;

    public TransactionCreateResponse(Transaction transaction, String accountId) {
        this.transaction = transaction;
        this.accountId = accountId;
    }
}
