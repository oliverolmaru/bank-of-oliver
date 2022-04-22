package ee.olmaru.bankofoliver.data.mappers;

import ee.olmaru.bankofoliver.data.models.*;
import ee.olmaru.bankofoliver.data.models.enums.TransactionDirection;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.apache.ibatis.annotations.Mapper
public interface Mapper {

    //CUSTOMER

    @Results(id = "customerResult", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "address", column = "address"),
            @Result(property = "accounts", column = "id", javaType = List.class, many = @Many(select = "getCustomerAccounts"))
    })
    @Select("SELECT * FROM CUSTOMERS WHERE id = #{id}")
    Optional<Customer> getCustomer(@Param("id") UUID id);

    @Select("SELECT * FROM CUSTOMERS")
    @ResultMap(value = "customerResult")
    List<Customer> getCustomers();

    @Insert("INSERT INTO customers(id, first_name, last_name, address) " +
            " VALUES (#{id}, #{firstName}, #{lastName}, #{address})")
    int insertCustomer(Customer customer);

    //ACCOUNT

    @Results(id = "accountResult", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "countryCode", column = "country_code"),
            @Result(property = "customer", column = "customer_id", one = @One(select = "ee.olmaru.bankofoliver.data.mappers.Mapper.getCustomer")),
            @Result(property = "balances", column = "id", javaType = List.class, many = @Many(select = "getAccountBalances"))
    })
    @Select("SELECT * FROM ACCOUNTS WHERE id = #{id}")
    Optional<Account> getAccount(@Param("id") UUID id);

    @Select("SELECT * FROM ACCOUNTS WHERE customer_id = #{customer_id}")
    @ResultMap(value = "accountResult")
    List<Account> getCustomerAccounts(@Param("customer_id") UUID customerId);

    @Insert("INSERT INTO accounts(id, country_code, customer_id) " +
            " VALUES (#{id}, #{countryCode}, #{customer.id})")
    int insertAccount(Account account);

    // BALANCE
    @Insert("INSERT INTO balances(id, currency_code, amount, account_id) " +
            " VALUES (#{id}, #{currencyCode}, #{amount}, #{account.id})")
    int insertBalance(Balance balance);

    @Update("UPDATE balances SET currency_code=#{currencyCode}, amount=#{amount} WHERE id=#{id}")
    int updateBalance(Balance balance);

    @Results(id = "balanceResult", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "currencyCode", column = "currency_code"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "account", column = "account_id", one = @One(select = "ee.olmaru.bankofoliver.data.mappers.Mapper.getAccount")),
            @Result(property = "transactions", column = "id", javaType = List.class, many = @Many(select = "getBalanceTransactions"))
    })
    @Select("SELECT * FROM BALANCES WHERE id = #{id}")
    Optional<Balance> getBalance(@Param("id") UUID id);

    @Select("SELECT * from BALANCES WHERE account_id = #{account_id}")
    @ResultMap(value = "balanceResult")
    List<Balance> getAccountBalances(@Param("account_id") UUID accountId);

    //TRANSACTION

    @Insert("INSERT INTO transactions(id, direction, currency_code, description, amount, created_at, balance_id) " +
            " VALUES (#{id}, #{direction}, #{currencyCode}, #{description}, #{amount}, #{createdAt}, #{balance.id})")
    int insertTransaction(Transaction transaction);

    @Results(id = "transactionResult", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "direction", column = "direction", javaType = TransactionDirection.class),
            @Result(property = "currencyCode", column = "currency_code"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "description", column = "description"),
            @Result(property = "balance", column = "balance_id", one = @One(select = "ee.olmaru.bankofoliver.data.mappers.Mapper.getBalance")),
            @Result(property = "createdAt", column = "created_at")
    })
    @Select("SELECT * FROM TRANSACTIONS WHERE id = #{id}")
    Optional<Transaction> getTransaction(@Param("id") UUID id);

    @ResultMap(value = "transactionResult")
    @Select("SELECT * FROM TRANSACTIONS WHERE balance_id = #{balance_id}")
    List<Transaction> getBalanceTransactions(@Param("balance_id") UUID balanceId);



}
