package model;

public record InventoryDetail(
        int inventoryId,
        int storeId,
        int productId,
        int category,
        int price,
        String productName,
        String company,
        String isAdult,
        String expirationDate
) {}
