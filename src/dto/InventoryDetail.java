package dto;

public record InventoryDetail(
        int inventoryId,
        int storeId,
        int productId,
        String productName,
        int category,
        String company,
        int price,
        String isAdult,
        String expirationDate,
        String status
) {}
