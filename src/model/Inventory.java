package model;

public record Inventory(
        int inventoryId,
        int storeId,
        int productId,
        String expirationDate
) {}
