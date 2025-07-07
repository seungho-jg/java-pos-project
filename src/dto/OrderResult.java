package dto;

import model.Orders;

public class OrderResult {
    private Orders orders;
    private String errorMsg;

    public OrderResult(Orders orders) {
        this.orders = orders;
    }

    public OrderResult(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return this.errorMsg == null;
    }

    public Orders getOrders() {
        return this.orders;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}
