import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.home.KafkaProducer;
import ru.home.dto.OrderEvent;
import ru.home.dto.OrderRequest;
import ru.home.dto.OrderResponse;
import ru.home.model.Order;
import ru.home.repository.OrderRepository;
import ru.home.service.OrderService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testCreateOrder() {
        OrderRequest request = new OrderRequest("1", 5);


        Order savedOrder = new Order();
        savedOrder.setOrderId("d885eba1-8090-498d-9ea1-2fa5dccf8b4b");
        savedOrder.setProductId("1");
        savedOrder.setQuantity(5);
        savedOrder.setStatus("PENDING");

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId("d885eba1-8090-498d-9ea1-2fa5dccf8b4b");
            return order;
        });

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals("d885eba1-8090-498d-9ea1-2fa5dccf8b4b", response.getOrderId());
        assertEquals("1", response.getProductId());
        assertEquals(5, response.getQuantity());
        assertEquals("PENDING", response.getStatus());

        verify(kafkaProducer, times(1)).sendOrderCreatedEvent(any(OrderEvent.class));
    }

    @Test
    public void testGetOrderStatus() {
        Order order = new Order();
        order.setOrderId("order1");
        order.setProductId("1");
        order.setQuantity(5);
        order.setStatus("PENDING");

        when(orderRepository.findByOrderId("order1")).thenReturn(order);

        OrderResponse response = orderService.getOrderStatus("order1");

        assertNotNull(response);
        assertEquals("order1", response.getOrderId());
        assertEquals("1", response.getProductId());
        assertEquals(5, response.getQuantity());
        assertEquals("PENDING", response.getStatus());
    }
}