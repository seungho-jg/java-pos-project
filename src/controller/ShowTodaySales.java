// ShowTodaySales.java

package controller;

import model.Orders;
import service.StoreService;

import java.util.ArrayList;

public class ShowTodaySales implements Controller {

    StoreService storeService;

    public ShowTodaySales(StoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    public void run() {
        System.out.println("📅 오늘의 매출 내역입니다:");

        ArrayList<Orders> orderList = storeService.getTodayOrderList();
        if (orderList.isEmpty()) {
            System.out.println("오늘 매출이 없습니다.");
            return;
        }

        for (Orders order : orderList) {
            String method = order.payMethod().equals("1") ? "현금" : "카드";
            System.out.printf("- 상품ID: %d | 수량: %d개 | 단가: %,d원 | 결제: %s | 시간: %s%n",
                    order.productId(),
                    order.quantity(),
                    order.price(),
                    method,
                    order.timeStamp()
            );
        }

        int total = storeService.getTodaySalesTotal();
        System.out.printf("💰 총 매출 합계: %,d원%n", total);
    }
}
