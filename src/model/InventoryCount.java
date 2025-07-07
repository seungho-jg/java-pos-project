package model;

public record InventoryCount(
        int productId,
        String productName,
        int totalEA
) {}
