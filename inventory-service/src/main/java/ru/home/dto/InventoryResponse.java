package ru.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    private String orderId;
    private String productId;
    private String name;
    private Integer availableQuantity;
    private boolean success;
    private String message;
}