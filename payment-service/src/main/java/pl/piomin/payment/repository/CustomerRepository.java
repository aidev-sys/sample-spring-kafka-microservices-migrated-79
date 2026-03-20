package pl.piomin.payment.repository;

import jakarta.persistence.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue(name = "customer.created", durable = "true"))
    public void handleCustomerCreated(Object message) {
        // Process customer created event
        System.out.println("Received customer created message: " + message);
    }

    @RabbitListener(queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue(name = "customer.updated", durable = "true"))
    public void handleCustomerUpdated(Object message) {
        // Process customer updated event
        System.out.println("Received customer updated message: " + message);
    }

    public void sendCustomerCreatedEvent(Object customer) {
        rabbitTemplate.convertAndSend("customer.created", customer);
    }

    public void sendCustomerUpdatedEvent(Object customer) {
        rabbitTemplate.convertAndSend("customer.updated", customer);
    }
}