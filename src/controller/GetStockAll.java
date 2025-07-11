package controller;

import dto.InventoryCount;
import service.StoreService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class GetStockAll implements Controller {
    private final BufferedReader reader;
    private final StoreService storeService;

    public GetStockAll(BufferedReader reader, StoreService storeService) {
        this.reader = reader;
        this.storeService = storeService;
    }

    @Override
    public void run() {
        try {
            ArrayList<InventoryCount> res = storeService.checkStockCountList();
            if (res == null || res.isEmpty()) {
                System.out.println("가져올 재고가 없습니다.");
            } else {
                for (InventoryCount inv : res) {
                    System.out.print("[" + inv.productId() + "] ");
                    System.out.print(inv.productName() + ": ");
                    for (int i = 0; i < inv.totalEA(); i++) {
                        System.out.print("*");
                    }
                    System.out.println(" (" + inv.totalEA() + " 개)");
                    System.out.println();
                }
            }
            System.out.print("아무키나 입력하세요: ");
            reader.readLine();
        } catch (IOException e) {
            System.out.println("입력 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
