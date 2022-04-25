package ee.olmaru.bankofoliver.data.controllers;


import ee.olmaru.bankofoliver.data.exceptions.GenericApiException;
import ee.olmaru.bankofoliver.data.models.Account;
import ee.olmaru.bankofoliver.data.models.Balance;
import ee.olmaru.bankofoliver.data.models.Customer;
import ee.olmaru.bankofoliver.data.models.Error;
import ee.olmaru.bankofoliver.data.models.Transaction;
import ee.olmaru.bankofoliver.data.models.enums.ErrorCode;
import ee.olmaru.bankofoliver.data.requests.AccountCreateRequest;
import ee.olmaru.bankofoliver.data.requests.TransactionCreateRequest;
import ee.olmaru.bankofoliver.data.responses.ErrorResponse;
import ee.olmaru.bankofoliver.data.responses.TransactionCreateResponse;
import ee.olmaru.bankofoliver.data.services.BankService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequestMapping(value = "/api", produces = {"application/json"})
@RestController
@Tag(name = "Bank API", description = "Base API for customers, accounts, balances and transactions")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
public class BankController {

    private BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping(path = "/customers/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer successfully found",
                    content = @Content(schema = @Schema(implementation = Customer.class))) })
    public ResponseEntity<?> customerGet(@PathVariable String id){
        Customer customer = this.bankService.getCustomer(id);
        return ResponseEntity.ok(customer);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of customers found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Customer.class)))) })
    @GetMapping(path = "/customers")
    public ResponseEntity<?> customerGetAll(){
        List<Customer> customers = this.bankService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }


    @PostMapping(path = "/accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully created",
                    content = @Content(schema = @Schema(implementation = Account.class))) })
    public ResponseEntity<?> accountCreate(@RequestBody @Valid AccountCreateRequest request){
        Account result = this.bankService.createAccount(request);

        return ResponseEntity.ok(result);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully found",
                    content = @Content(schema = @Schema(implementation = Account.class))) })
    @GetMapping(path = "/accounts/{id}")
    public ResponseEntity<?> accountGet(@PathVariable String id){
        Account account = this.bankService.getAccount(id);
        return ResponseEntity.ok(account);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully found",
                    content = @Content(schema = @Schema(implementation = Balance.class))) })
    @GetMapping(path = "/balances/{id}")
    public ResponseEntity<?> balanceGet(@PathVariable String id){
        Balance balance = this.bankService.getBalance(id);
        return ResponseEntity.ok(balance);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction created successfully",
                    content = @Content(schema = @Schema(implementation = Transaction.class))) })
    @PostMapping(path = "/transactions")
    public ResponseEntity<?> transactionCreate(@RequestBody @Valid TransactionCreateRequest request){
        Transaction result = this.bankService.createTransaction(request);
        TransactionCreateResponse response = new TransactionCreateResponse(result, request.getAccountId());
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction successfully found",
                    content = @Content(schema = @Schema(implementation = Transaction.class))) })
    @GetMapping(path = "/transactions/{id}")
    public ResponseEntity<?> transactionGet(@PathVariable String id) {
        Transaction transaction = this.bankService.getTransaction(id);
        return ResponseEntity.ok(transaction);
    }
}
