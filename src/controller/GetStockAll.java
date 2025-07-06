package controller;


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
        ArrayList<InventoryDetail> res =  storeService.showStockList();
        for (InventoryDetail i : res) {
            System.out.println("=============");
            System.out.println(i.inventoryId());
            System.out.println(i.name());
            System.out.println(i.price());
            System.out.println("=============");
        }
    }
}
