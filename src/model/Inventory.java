package model;

public record Inventory(
        int inventoryId,
        int storeId,
        int productId,
        String expirationDate,
        String status // 전시(display), 배송(shipping), 판매(sold), 폐기(disposed)
) {}
