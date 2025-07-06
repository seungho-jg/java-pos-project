package model;

public record InventoryDetail(
        int inventoryId,
        int storeId,
        int productId,
        String name,
        int category, // 제품 유형
        String company,
        int price,
        String isAdult, // 19금 물품인지
        String expirationDate
) {}
