package ee.olmaru.bankofoliver.data.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Component
public class Amqp {

    private AmqpAdmin amqpAdmin;

    public Amqp(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void declareAfterStartup() {

        amqpAdmin.declareExchange(new TopicExchange("bank-of-olmaru-exchange"));
        amqpAdmin.declareQueue(new Queue("banking.transactions"));
        amqpAdmin.declareBinding(new Binding("banking.transactions", Binding.DestinationType.QUEUE, "bank-of-olmaru-exchange","banking.transactions.#",null));
        amqpAdmin.declareQueue(new Queue("banking.accounts"));
        amqpAdmin.declareBinding(new Binding("banking.accounts", Binding.DestinationType.QUEUE, "bank-of-olmaru-exchange","banking.accounts.#",null));
        amqpAdmin.declareQueue(new Queue("banking.balances"));
        amqpAdmin.declareBinding(new Binding("banking.balances", Binding.DestinationType.QUEUE, "bank-of-olmaru-exchange","banking.balances.#",null));
        amqpAdmin.declareQueue(new Queue("banking.customers"));
        amqpAdmin.declareBinding(new Binding("banking.customers", Binding.DestinationType.QUEUE, "bank-of-olmaru-exchange","banking.customers.#",null));
    }
}