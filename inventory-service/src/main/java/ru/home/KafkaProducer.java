package ru.home;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.home.dto.InventoryResponse;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, InventoryResponse> kafkaTemplate;

    @Value("${inventory.reserved.topic}")
    private String inventoryReservedTopic;

    public KafkaProducer(KafkaTemplate<String, InventoryResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendInventoryResponse(InventoryResponse response) {
        kafkaTemplate.send(inventoryReservedTopic, response);
    }
}
