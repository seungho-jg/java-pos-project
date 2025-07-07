package service;

import db.InventoryDao;
import db.ProductDao;
import db.StoreDao;
import db.OrderDao;
import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class StoreService {
    LocalDate today;
    ProductDao productDao;
    StoreDao storeDao;
    OrderDao orderDao;
    InventoryDao inventoryDao;

    public StoreService(ProductDao productDao, StoreDao storeDao, OrderDao orderDao, InventoryDao inventoryDao, LocalDate today){
        this.productDao = productDao;
        this.storeDao = storeDao;
        this.orderDao = orderDao;
        this.inventoryDao = inventoryDao;
        this.today = today;
    }

    // 전체 재고 확인(InventoryDetail)
    public ArrayList<InventoryDetail> checkStockList() {
        ArrayList<InventoryDetail> result = inventoryDao.getInventoryDetail();
        if (result == null) {
            System.out.println("조회 실패");
            return null;
        }
        return result;
    }
    // 재고 품목 카운팅-1
    public ArrayList<InventoryCount> checkStockCountList(){
        // 디폴트 오버로드 매소드 display로 조회
        ArrayList<InventoryCount> result = inventoryDao.getInventoryCount("display");
        if (result == null) {
            System.out.println("조회 실패");
            return null;
        }
        return result;
    }
    // 재고 품목 카운팅-2
    public ArrayList<InventoryCount> checkStockCountList(String status) {
        ArrayList<InventoryCount> result = inventoryDao.getInventoryCount(status);
        if (result == null) {
            System.out.println("조회 실패");
            return null;
        }
        return result;
    }

    // 품목이 존재하는지 체크
    public boolean isValidProdName(String productName) {
        Product result = productDao.getProductByName(productName);
        return result != null;
    }

    // 물품 입고
    public void stockProduct(String productName, int quantity){
        // 이름으로 product 검색
        Product findProd = productDao.getProductByName(productName);
        String expDate = null;
        if (findProd.category() == 1) {
            expDate = String.valueOf(today.plusDays(findProd.expirationDate()));
        }

        for (int i = 0; i < quantity; i ++){
            boolean result = inventoryDao.insertInventory(storeDao.getStoreId(), findProd, expDate);
            if (result) {
                System.out.println("성공");
            } else {
                System.out.println("실패");
            }
        }
    }

    // 제품 판매
    public Order processSale(Long staffId, Map<Long, Integer> productQuantities) {

        // 1. 새로운 도메인 객체(Order)를 생성합니다.
        return new Order(1,1,1,1);
    }

    //
}
