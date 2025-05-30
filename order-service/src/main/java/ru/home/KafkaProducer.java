package ru.home;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.home.dto.OrderEvent;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Value("${order.created.topic}")
    private String orderCreatedTopic;

    public KafkaProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(OrderEvent event) {
        kafkaTemplate.send(orderCreatedTopic, event);
    }
}
