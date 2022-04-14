package ee.olmaru.bankofoliver.data.mappers;

import ee.olmaru.bankofoliver.data.models.Account;
import ee.olmaru.bankofoliver.data.models.Customer;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@org.apache.ibatis.annotations.Mapper
public interface Mapper {
    @Results(id = "accountResult", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "countryCode", column = "country_code"),
            @Result(property = "customer", column = "customer_id", one = @One(select = "ee.olmaru.bankofoliver.data.mappers.Mapper.getCustomer"))
    })
    @Select("SELECT * FROM ACCOUNTS WHERE id = #{id}")
    Account getAccount(@Param("id") UUID id);

    @Insert("INSERT INTO accounts(id, country_code, customer_id) " +
            " VALUES (#{id}, #{countryCode}, #{customer.id})")
    int insertAccount(Account account);

    @Results(id = "customerResult", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "address", column = "address"),
    })
    @Select("SELECT * FROM CUSTOMERS WHERE id = #{id}")
    Customer getCustomer(@Param("id") UUID id);

    @Insert("INSERT INTO customers(id, first_name, last_name, address) " +
            " VALUES (#{id}, #{firstName}, #{lastName}, #{address})")
    int insertCustomer(Customer customer);
}
