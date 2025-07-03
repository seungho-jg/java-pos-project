package model;

public record Order(
        int orderId,
        int productId,
        int customerId,
        int price
) {}
