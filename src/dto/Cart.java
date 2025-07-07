package dto;

import java.util.ArrayList;

public class Cart {
    final private ArrayList<Integer> inventoryIdList = new ArrayList<>();
    final private int productId;
    final private String productName;
    final private int price;
    private int quantity;

    public Cart(int inventoryId, int productId, String productName, int price, int quantity) {
        this.inventoryIdList.add(inventoryId);
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public ArrayList<Integer> getInventoryIdList() {
        return inventoryIdList;
    }

    public boolean hasInventoryId(int inventoryId) {
        return inventoryIdList.contains(inventoryId);
    }


    public void addInventoryId(int inventoryId) {
        this.inventoryIdList.add(inventoryId);
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
