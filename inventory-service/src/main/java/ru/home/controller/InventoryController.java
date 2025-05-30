package ru.home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.home.dto.InventoryRequest;
import ru.home.dto.InventoryResponse;
import ru.home.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse addInventory(@RequestBody InventoryRequest request) {
        return inventoryService.createOrUpdateInventory(request);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getInventory(@PathVariable String productId) {
        return inventoryService.getAvailableQuantity(productId);
    }
}