package pl.piomin.stock.repository;

import jakarta.persistence.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue(name = "product_queue", durable = "true"))
    public void listenProductMessage(Object message) {
        // Handle incoming product messages
        System.out.println("Received product message: " + message);
    }

    public Optional<Product> findById(Long id) {
        // Simulate fetching from PostgreSQL
        return Optional.of(new Product(id, "Sample Product"));
    }

    public List<Product> findAll() {
        // Simulate fetching all products from PostgreSQL
        return List.of(new Product(1L, "Product 1"), new Product(2L, "Product 2"));
    }

    public Product save(Product product) {
        // Simulate saving to PostgreSQL
        rabbitTemplate.convertAndSend("product_queue", product);
        return product;
    }

    public void deleteById(Long id) {
        // Simulate deleting from PostgreSQL
        rabbitTemplate.convertAndSend("product_queue", "DELETE:" + id);
    }
}