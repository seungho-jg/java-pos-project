package model;

public record Product(
        int productId,
        String name,
        ProdCategory category, // 제품 유형
        String company,
        int price,
        String expirationDate, // 유통기한
        boolean isAdultOnly // 19금 물품인지
) {}
