package ee.olmaru.bankofoliver.data.models;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Account {
    private UUID id;
    private String countryCode;
    private Customer customer;
    private List<Balance> balances;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
