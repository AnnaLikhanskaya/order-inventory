package ru.home.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.dto.InventoryRequest;
import ru.home.dto.InventoryResponse;
import ru.home.model.Inventory;
import ru.home.repository.InventoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional
    public boolean checkAndUpdateInventory(String productId, Integer quantity) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);

        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            if (inventory.getAvailableQuantity() >= quantity) {
                inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
                inventoryRepository.save(inventory);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public InventoryResponse createOrUpdateInventory(InventoryRequest request) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(request.getProductId());

        Inventory inventory;
        if (inventoryOptional.isPresent()) {
            inventory = inventoryOptional.get();
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() + request.getQuantity());
        } else {
            inventory = new Inventory();
            inventory.setProductId(request.getProductId());
            inventory.setName(request.getName());
            inventory.setAvailableQuantity(request.getQuantity());
        }

        inventory = inventoryRepository.save(inventory);

        InventoryResponse response = new InventoryResponse();
        response.setProductId(inventory.getProductId());
        response.setName(inventory.getName());
        response.setAvailableQuantity(inventory.getAvailableQuantity());
        return response;
    }

    public Integer getAvailableQuantity(String productId) {
        return inventoryRepository.findByProductId(productId)
                .map(Inventory::getAvailableQuantity)
                .orElse(0);
    }
}