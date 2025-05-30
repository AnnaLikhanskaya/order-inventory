package ru.home;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.home.dto.InventoryResponse;
import ru.home.model.Order;
import ru.home.repository.OrderRepository;

@Service
public class KafkaConsumer {

    private final OrderRepository orderRepository;

    public KafkaConsumer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "${inventory.reserved.topic}", groupId = "order-group")
    public void handleInventoryResponse(InventoryResponse response) {
        Order order = orderRepository.findByOrderId(response.getOrderId());
        if (order != null) {
            if (response.isSuccess()) {
                order.setStatus("CONFIRMED");
            } else {
                order.setStatus("REJECTED");
            }
            orderRepository.save(order);
        }
    }
}