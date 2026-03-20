package pl.piomin.order.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.piomin.base.domain.Order;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderGeneratorService {

    private static Random RAND = new Random();
    private AtomicLong id = new AtomicLong();
    private Executor executor;
    private RabbitTemplate template;

    public OrderGeneratorService(Executor executor, RabbitTemplate template) {
        this.executor = executor;
        this.template = template;
    }

    @Async
    public void generate() {
        for (int i = 0; i < 10000; i++) {
            int x = RAND.nextInt(5) + 1;
            Order o = new Order(id.incrementAndGet(), RAND.nextLong(100) + 1, RAND.nextLong(100) + 1, "NEW");
            o.setPrice(100 * x);
            o.setProductCount(x);
            template.convertAndSend("orders", o);
        }
    }
}