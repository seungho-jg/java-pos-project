package dto;

public record InventoryCount(
        int productId,
        String productName,
        int totalEA
) {}
