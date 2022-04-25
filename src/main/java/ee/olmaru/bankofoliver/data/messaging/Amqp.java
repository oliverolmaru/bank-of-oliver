package ee.olmaru.bankofoliver.data.messaging;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Amqp {

    private RabbitTemplate template;

    public Amqp() {
        ConnectionFactory connectionFactory = new CachingConnectionFactory();

        this.template = new RabbitTemplate(connectionFactory);
        //String foo = (String) template.receiveAndConvert("myqueue");
    }

    public RabbitTemplate getTemplate(){
        return template;
    }
}