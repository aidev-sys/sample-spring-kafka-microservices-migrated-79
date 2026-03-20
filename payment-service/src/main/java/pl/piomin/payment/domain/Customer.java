package pl.piomin.payment.domain;

import jakarta.persistence.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(name = "amount_available")
    private int amountAvailable;
    
    @Column(name = "amount_reserved")
    private int amountReserved;

    public Customer() {
    }

    public Customer(Long id, String name, int amountAvailable, int amountReserved) {
        this.id = id;
        this.name = name;
        this.amountAvailable = amountAvailable;
        this.amountReserved = amountReserved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public int getAmountReserved() {
        return amountReserved;
    }

    public void setAmountReserved(int amountReserved) {
        this.amountReserved = amountReserved;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amountAvailable=" + amountAvailable +
                ", amountReserved=" + amountReserved +
                '}';
    }
    
    @Component
    public static class CustomerMessageHandler {
        
        @Autowired
        private RabbitTemplate rabbitTemplate;
        
        @RabbitListener(queues = "customer.created")
        public void handleCustomerCreated(Object message) {
            // Process customer created message
        }
        
        @RabbitListener(queues = "customer.updated")
        public void handleCustomerUpdated(Object message) {
            // Process customer updated message
        }
        
        public void sendCustomerCreated(Customer customer) {
            rabbitTemplate.convertAndSend("customer.created", customer);
        }
        
        public void sendCustomerUpdated(Customer customer) {
            rabbitTemplate.convertAndSend("customer.updated", customer);
        }
    }
}