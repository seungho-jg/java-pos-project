package controller;

import service.StoreService;

public class CheckBalance implements Controller {
    private final StoreService storeService;

    public CheckBalance(StoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    public void run() {
        int balance = storeService.getStoreBalance();
        System.out.printf("현재 잔고는 %,d원입니다.%n", balance);
    }
}
