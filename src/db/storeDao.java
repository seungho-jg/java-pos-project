package db;

public class StoreDao {
    final private int storeId = 1;
    private int balance = 1234000;

    public int getStoreId() {
        return this.storeId;
    }

    public int getBalance() {
        return this.balance;
    }

    public void increaseBalance(int amount) {
        this.balance += amount;
    }

    public void decreaseBalance(int amount) {
        this.balance -= amount;
    }
}
