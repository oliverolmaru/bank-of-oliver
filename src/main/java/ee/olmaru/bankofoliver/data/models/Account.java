package ee.olmaru.bankofoliver.data.models;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {
    private UUID id;
    private String countryCode;
    @JsonBackReference
    private Customer customer;
    @JsonManagedReference
    private List<Balance> balances;

    public Account() {
        balances = new ArrayList<>();
    }

    public void addBalance(Balance balance){
        balances.add(balance);
    }
}
