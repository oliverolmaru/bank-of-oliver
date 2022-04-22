package ee.olmaru.bankofoliver.data.controllers;


import ee.olmaru.bankofoliver.data.models.Account;
import ee.olmaru.bankofoliver.data.models.Balance;
import ee.olmaru.bankofoliver.data.models.Customer;
import ee.olmaru.bankofoliver.data.models.Transaction;
import ee.olmaru.bankofoliver.data.requests.AccountCreateRequest;
import ee.olmaru.bankofoliver.data.requests.TransactionCreateRequest;
import ee.olmaru.bankofoliver.data.responses.TransactionCreateResponse;
import ee.olmaru.bankofoliver.data.services.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
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

    @GetMapping(path = "/customers")
    public ResponseEntity<?> customerGetAll(){
        List<Customer> customers = this.apiService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }


    @PostMapping(path = "/accounts")
    public ResponseEntity<?> accountCreate(@RequestBody @Valid AccountCreateRequest request){
        Account result = this.apiService.createAccount(request);

        //TODO - Format response based on challenge requirements

        return ResponseEntity.ok(result);
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
        Account account = this.apiService.getAccount(accountId);
        return ResponseEntity.ok(account);
    }

    @GetMapping(path = "/balances/{id}")
    public ResponseEntity<?> balanceGet(@PathVariable String id){
        UUID balanceId;
        try{
            balanceId = UUID.fromString(id);
        }
        catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body("Wrong UUID!");
        }
        Balance balance = this.apiService.GetBalance(balanceId);
        return ResponseEntity.ok(balance);
    }

    @PostMapping(path = "/transactions")
    public ResponseEntity<?> transactionCreate(@RequestBody @Valid TransactionCreateRequest request){
        Transaction result = this.apiService.CreateTransaction(request);
        //TransactionCreateResponse response = (TransactionCreateResponse) result;
        //response.setAccountId(request.getAccountId());
        return ResponseEntity.ok(result);
    }
}
