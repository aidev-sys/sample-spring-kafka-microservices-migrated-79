package pl.piomin.stock.domain;

import jakarta.persistence.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int availableItems;
    private int reservedItems;

    public Product() {
    }

    public Product(Long id, String name, int availableItems, int reservedItems) {
        this.id = id;
        this.name = name;
        this.availableItems = availableItems;
        this.reservedItems = reservedItems;
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

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    public int getReservedItems() {
        return reservedItems;
    }

    public void setReservedItems(int reservedItems) {
        this.reservedItems = reservedItems;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", availableItems=" + availableItems +
                ", reservedItems=" + reservedItems +
                '}';
    }

    @Component
    public static class ProductMessageHandler {

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @RabbitListener(queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue(name = "product.queue", durable = "true"))
        public void handleProductMessage(Object message) {
            // Process the message
            System.out.println("Received message: " + message);
        }

        public void sendProductMessage(Object message) {
            rabbitTemplate.convertAndSend("product.queue", message);
        }
    }
}