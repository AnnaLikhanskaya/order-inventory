package ru.home;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.home.dto.InventoryResponse;
import ru.home.dto.OrderEvent;
import ru.home.service.InventoryService;

@Service
public class KafkaConsumer {

    private final InventoryService inventoryService;
    private final KafkaProducer kafkaProducer;

    public KafkaConsumer(InventoryService inventoryService, KafkaProducer kafkaProducer) {
        this.inventoryService = inventoryService;
        this.kafkaProducer = kafkaProducer;
    }

    @KafkaListener(topics = "${order.created.topic}", groupId = "inventory-group")
    public void handleOrderCreated(OrderEvent event) {
        try {
            boolean isAvailable = inventoryService.checkAndUpdateInventory(
                    event.getProductId(),
                    event.getQuantity()
            );

            InventoryResponse response = new InventoryResponse();
            response.setOrderId(event.getOrderId());
            response.setProductId(event.getProductId());
            response.setSuccess(isAvailable);
            response.setMessage(isAvailable ? "Inventory reserved" : "Not enough inventory");

            kafkaProducer.sendInventoryResponse(response);
        } catch (Exception e) {
            InventoryResponse response = new InventoryResponse();
            response.setOrderId(event.getOrderId());
            response.setProductId(event.getProductId());
            response.setSuccess(false);
            response.setMessage("Error processing inventory: " + e.getMessage());
            kafkaProducer.sendInventoryResponse(response);
        }
    }
}