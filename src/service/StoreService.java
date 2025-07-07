package service;

import db.InventoryDao;
import db.ProductDao;
import db.StoreDao;
import db.OrdersDao;
import dto.*;
import model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class StoreService {
    LocalDate today;
    ProductDao productDao;
    StoreDao storeDao;
    OrdersDao ordersDao;
    InventoryDao inventoryDao;
    ArrayList<Cart> cartList = new ArrayList<>();


    public StoreService(ProductDao productDao, StoreDao storeDao, OrdersDao ordersDao, InventoryDao inventoryDao, LocalDate today){
        this.productDao = productDao;
        this.storeDao = storeDao;
        this.ordersDao = ordersDao;
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

    // 제품 이름으로 인벤토리 가져오기
    public Inventory getInventoryByName(String name){
        Inventory result = inventoryDao.getInventoryOneByProdId(productDao.getProductByName(name).productId(), "display");
        if (result == null) {
            System.out.println("getInventoryByName() 실패");
            return null;
        }
        return result;
    }

    // 물품 입고
    public void stockProduct(String productName, int quantity){
        // 이름으로 product 검색
        Product findProd = productDao.getProductByName(productName);
        String expDate = null;
        if (findProd.category() == 1) {
            expDate = String.valueOf(today.plusDays(findProd.expirationDate()));
        }

        for (int i = 0; i < quantity; i++){
            boolean result = inventoryDao.insertInventory(storeDao.getStoreId(), findProd, expDate);
            if (result) {
                System.out.println("성공");
            } else {
                System.out.println("실패");
            }
        }
    }
    // 결제할 제품 추가: 성공(1), 19세(2), 유통기한(3), 제품없음(4)
    public int addCart(Inventory inventory, boolean isAdult) {
        // 제품이 없을때
        if (inventory == null) {
            return 4;
        }
        // 제품정보 가져오기
        Product product = productDao.getProductById(inventory.productId());

        // 19세 제한물품일때
        if (product.isAdult().equals("1") && !isAdult) {
            return 2;
        }

        // 유통기한 처리
        if (product.category() == 1) {
            // 유통기한이 지났을때
            if (today.isAfter(Date.valueOf(inventory.expirationDate()).toLocalDate())) {
                return 3;
            }
        }
        boolean addSameInven = false;
        for (Cart cart : cartList) {
            if (cart.getProductId() == product.productId()) {
                cart.increaseQuantity();
                addSameInven = true;
            }
        }
        if (!addSameInven) {
            cartList.add(
                    new Cart(
                            inventory.inventoryId(),
                            inventory.productId(),
                            product.name(),
                            product.price(),
                            1
                    )
            );
        }
        // 전체 금액
        int totalPrice = 0;
        for (Cart cart : cartList) {
            totalPrice += cart.getPrice() * cart.getQuantity();
        }

        // print cartList 나중에 분리
        System.out.print("[");
        for (Cart c : cartList) {
            System.out.print(c.getProductName()+ "x" + c.getQuantity() + " ");
        }
        System.out.println("] : " + totalPrice);
        return 1;
    }


    // 제품 판매
    public OrderResult processSale(String method, int cashAmount) {
        // 카트에 아무것도 없을 때
        if (cartList.isEmpty()) {
            return new OrderResult("장바구니가 비어있습니다.");
        }
        // 전체 금액
        int totalPrice = 0;
        for (Cart cart : cartList) {
            totalPrice += cart.getPrice() * cart.getQuantity();
        }

        // 결제수단
        if (method.equals("cash")) {
            if (cashAmount < totalPrice) {
                return new OrderResult("현금이 부족합니다.");
            }
            int change = cashAmount - totalPrice;
            storeDao.increaseBalance(totalPrice);
            System.out.printf("현금 결제 완료, 거스름돈: %,d원%n", change);
        } else if (method.equals("card")) {
            storeDao.increaseBalance(totalPrice);
            System.out.printf("카드 결제 완료: %,d원%n", totalPrice);
        } else {
            return new OrderResult("결제수단 오류");
        }

        for (Cart cart : cartList) {
            ordersDao.insertOrder(
                    new Orders(
                            0,
                            cart.getProductId(),
                            0,
                            cart.getPrice(),
                            method.equals("card") ? "1" : "0", // 0: cash, 1: cart
                            cart.getQuantity(),
                            "default"
                    ));
            // 재고 차감
            inventoryDao.updateInventoryStatus(cart.getInventoryId(), "sold");

            // 장바구니 비우기
            cartList.clear();

            // 성공
            Orders successOrder = new Orders(
                    0, 0, 0, totalPrice,
                    method.equals("card") ? "1" : "0",
                    0,
                    "default"
            );
            return new OrderResult(successOrder);
        }
        return new OrderResult("결제 실패");
    }


}
