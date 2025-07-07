package service;

import db.InventoryDao;
import db.ProductDao;
import db.StoreDao;
import db.OrderDao;
import model.InventoryDetail;
import model.Order;
import model.Product;

import java.util.ArrayList;
import java.util.Map;

public class StoreService {
    ProductDao productDao;
    StoreDao storeDao;
    OrderDao orderDao;
    InventoryDao inventoryDao;

    public StoreService(ProductDao productDao, StoreDao storeDao, OrderDao orderDao, InventoryDao inventoryDao){
        this.productDao = productDao;
        this.storeDao = storeDao;
        this.orderDao = orderDao;
        this.inventoryDao = inventoryDao;
    }

    // 재고 확인
    public ArrayList<InventoryDetail> showStockList() {
        ArrayList<InventoryDetail> result = inventoryDao.getInventoryDetail();
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

        for (int i = 0; i < quantity; i ++){
            boolean result = inventoryDao.insertInventory(storeDao.getStoreId(), findProd);
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
