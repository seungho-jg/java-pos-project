package controller;


import model.InventoryCount;
import model.InventoryDetail;
import service.StoreService;

import java.util.ArrayList;
import java.util.Scanner;

public class GetStockAll implements Controller {
    Scanner sc = null;
    StoreService storeService = null;

    public GetStockAll(Scanner scanner, StoreService storeService) {
        this.sc = scanner;
        this.storeService = storeService;
    }

    @Override
    public void run(){
        ArrayList<InventoryCount> res =  storeService.checkStockCountList();
        for (InventoryCount inv : res) {
            System.out.print("[" + inv.productId() + "] ");
            System.out.print(inv.productName() + ": ");
            for (int i = 0; i < inv.totalEA(); i ++){
                System.out.print("*");
            }
            System.out.println("(" + inv.totalEA() + " 개"+ ")");
            System.out.println();
        }
    }
}
