package ee.olmaru.bankofoliver.data.controllers;


import ee.olmaru.bankofoliver.data.models.Account;
import ee.olmaru.bankofoliver.data.models.Customer;
import ee.olmaru.bankofoliver.data.requests.AccountCreateRequest;
import ee.olmaru.bankofoliver.data.requests.TransactionCreateRequest;
import ee.olmaru.bankofoliver.data.services.ApiService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@CrossOrigin
@RequestMapping("/api")
@Controller
public class ApiServiceController {

    private ApiService apiService;

    public ApiServiceController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping(path = "/customers/{id}")
    public ResponseEntity<?> customerGet(@PathVariable String id){
        UUID customerId;
        try{
            customerId = UUID.fromString(id);
        }
        catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body("Wrong UUID!");
        }
        Customer customer = this.apiService.getCustomer(customerId);
        return ResponseEntity.ok(customer);
    }


    @PostMapping(path = "/accounts")
    public ResponseEntity<?> accountCreate(@RequestBody @Valid AccountCreateRequest request){
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/accounts/{id}")
    public ResponseEntity<?> accountGet(@PathVariable String id){
        UUID accountId;
        try{
            accountId = UUID.fromString(id);
        }
        catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body("Wrong UUID!");
        }
        Account account = this.apiService.GetAccount(accountId);
        return ResponseEntity.ok(account);
    }

    @PostMapping(path = "/transactions")
    public ResponseEntity<?> transactionCreate(@RequestBody @Valid TransactionCreateRequest request){
        return ResponseEntity.notFound().build();
    }
}
