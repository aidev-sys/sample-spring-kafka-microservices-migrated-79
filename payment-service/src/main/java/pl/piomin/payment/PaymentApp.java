package pl.piomin.payment;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.piomin.base.domain.Order;
import pl.piomin.payment.domain.Customer;
import pl.piomin.payment.repository.CustomerRepository;
import pl.piomin.payment.service.OrderManageService;

import java.util.Random;

@SpringBootApplication
public class PaymentApp {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentApp.class);

    public static void main(String[] args) {
        SpringApplication.run(PaymentApp.class, args);
    }

    @Autowired
    OrderManageService orderManageService;

    @RabbitListener(queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue(name = "orders", durable = "true"))
    public void onEvent(Order o) {
        LOG.info("Received: {}", o);
        if (o.getStatus().equals("NEW"))
            orderManageService.reserve(o);
        else
            orderManageService.confirm(o);
    }

    @Autowired
    private CustomerRepository repository;

    @PostConstruct
    public void generateData() {
        Random r = new Random();
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            int count = r.nextInt(100, 1000);
            Customer c = new Customer(null, faker.name().fullName(), count, 0);
            repository.save(c);
        }
    }

    @Bean
    public Queue ordersQueue() {
        return new Queue("orders", true);
    }
}