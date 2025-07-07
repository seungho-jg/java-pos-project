package dto;

public class Cart {
    final private int productId;
    final private String productName;
    final private int price;
    private int quantity;

    public Cart(int productId, String productName, int price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
    public String getProductName() {
        return productName;
    }

    public void increaseQuantity() {
        this.quantity += 1;
    }
}
