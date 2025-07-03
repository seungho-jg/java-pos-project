package model;

enum prodCategory {
    food,   // 음식
    alcohol // 술
}

public record Product(
        int productId,
        String name,
        prodCategory category, // 제품 유형
        String company,
        int price,
        String expirationDate, // 유통기한
        boolean isAdultOnly // 19금 물품인지
) {}
