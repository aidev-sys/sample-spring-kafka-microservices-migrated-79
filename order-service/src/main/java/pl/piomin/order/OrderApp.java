package pl.piomin.order;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import pl.piomin.base.domain.Order;
import pl.piomin.order.controller.OrderController;
import pl.piomin.order.service.OrderGeneratorService;
import pl.piomin.order.service.OrderManageService;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableRabbit
@EnableAsync
public class OrderApp {

    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class, args);
    }

    @Bean
    public Queue ordersQueue() {
        return new Queue("orders", true);
    }

    @Bean
    public Queue paymentOrdersQueue() {
        return new Queue("payment-orders", true);
    }

    @Bean
    public Queue stockOrdersQueue() {
        return new Queue("stock-orders", true);
    }

    @RabbitListener(queues = "payment-orders")
    public void handlePaymentOrder(Order order) {
        // Handle payment order logic
    }

    @RabbitListener(queues = "stock-orders")
    public void handleStockOrder(Order order) {
        // Handle stock order logic
    }

    @RabbitListener(queues = "orders")
    public void handleOrder(Order order) {
        // Handle final order logic
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setThreadNamePrefix("rabbitSender-");
        executor.initialize();
        return executor;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate();
    }
}