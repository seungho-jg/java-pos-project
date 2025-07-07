package model;

public record Orders(
        int orderId,
        int productId,
        int customerId,
        int price,
        String payMethod, // 1: 현금 2: 카드
        int quantity,
        String timeStamp // 오라클에서 디폴트로 생성
) {}
