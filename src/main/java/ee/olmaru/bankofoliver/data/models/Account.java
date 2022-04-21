package ee.olmaru.bankofoliver.data.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Setter
@Getter
public class Account {
    private UUID id;
    private String countryCode;
    @JsonBackReference
    private Customer customer;
    @JsonManagedReference
    private List<Balance> balances;
}
