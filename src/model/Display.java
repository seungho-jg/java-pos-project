package model;

public record Display(
        int displayId,
        int storeId,
        int productId,
        int totalEA,
        String ExpirationDate
) {}
