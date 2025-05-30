package ru.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.home.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(String orderId);
}
