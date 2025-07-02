package model;

public record Product(
        int productId,
        String Name,
        String Company,
        int price,
        String expirationDate, // 유통기한
        boolean isAdult, // 19금 물품인지
        boolean isDisplay // 진열되어 있는지
) {}
