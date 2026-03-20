package pl.piomin.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import pl.piomin.base.domain.Order;
import pl.piomin.order.service.OrderGeneratorService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    private AtomicLong id = new AtomicLong();
    private RabbitTemplate template;
    private OrderGeneratorService orderGeneratorService;

    public OrderController(RabbitTemplate template,
                           OrderGeneratorService orderGeneratorService) {
        this.template = template;
        this.orderGeneratorService = orderGeneratorService;
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        order.setId(id.incrementAndGet());
        template.convertAndSend("orders", order);
        LOG.info("Sent: {}", order);
        return order;
    }

    @PostMapping("/generate")
    public boolean create() {
        orderGeneratorService.generate();
        return true;
    }

    @RabbitListener(queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue(name = "orders", durable = "true"))
    public void listen(Order order) {
        LOG.info("Received: {}", order);
    }

    @GetMapping
    public List<Order> all() {
        // Dummy implementation since RabbitMQ does not support direct state queries like Kafka Streams
        return new ArrayList<>();
    }
}