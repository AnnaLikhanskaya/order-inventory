package ru.home.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.KafkaProducer;
import ru.home.dto.OrderEvent;
import ru.home.dto.OrderRequest;
import ru.home.dto.OrderResponse;
import ru.home.model.Order;
import ru.home.repository.OrderRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setProductId(orderRequest.getProductId());
        order.setQuantity(orderRequest.getQuantity());
        order.setStatus("PENDING");
        orderRepository.save(order);

        // Send event to Kafka
        OrderEvent event = new OrderEvent();
        event.setOrderId(order.getOrderId());
        event.setProductId(order.getProductId());
        event.setQuantity(order.getQuantity());
        kafkaProducer.sendOrderCreatedEvent(event);

        return mapToOrderResponse(order);
    }

    public OrderResponse getOrderStatus(String orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        return mapToOrderResponse(order);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setProductId(order.getProductId());
        response.setQuantity(order.getQuantity());
        response.setStatus(order.getStatus());
        return response;
    }
}
