package model;

public record Product(
        int productId,
        String name,
        int category, // 제품 유형
        String company,
        int price,
        int expirationDate, // 유통기한
        String isAdult // 19금 물품인지
) {}
