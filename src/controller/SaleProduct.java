package controller;

import dto.OrderResult;
import service.StoreService;

import java.util.Scanner;

public class SaleProduct implements Controller{
    Scanner scanner = null;
    StoreService storeService = null;
    private boolean isAdult = false;
    public SaleProduct(Scanner scanner, StoreService storeService) {
        this.scanner = scanner;
        this.storeService = storeService;
    }
    @Override
    public void run() {
        while (true) {
            System.out.println("구매할 제품의 이름을 입력해주세요(완료:0)");
            String prodName = scanner.nextLine();
            if (prodName.equals("0")) {
                System.out.println("결제수단을 입력해주세요(cash/card): ");
                String payMethod = scanner.nextLine();

                int cashAmount = 0;
                if (payMethod.equals("cash")) {
                    System.out.print("현금 얼마를 넣으시겠습니까?: ");
                    cashAmount = Integer.parseInt(scanner.nextLine());
                }
                OrderResult result = storeService.processSale(payMethod, cashAmount);
                if (result.isSuccess()) {
                    System.out.println("결제가 완료되었습니다. 감사합니다!");
                } else {
                    System.out.println("오류: " + result.getErrorMsg());
                }
                break;
            }
            // 성공(1), 19세(2), 유통기한(3), 제품없음(4)
            int result = storeService.addCart(storeService.getInventoryByName(prodName), isAdult);
            switch (result) {
                case 2:
                    System.out.println("성인 인증이 필요합니다.");
                    System.out.print("주민번호 앞 6자리: ");
                    scanner.nextLine(); // 나이검증 나중에 구현
                    isAdult = true;
                    break;
                case 3:
                    System.out.println("유통기한이 지난 상품입니다.");
                    // 폐기하기
                    break;
                case 4:
                    System.out.println("제품이 없습니다.");
                    break;
            }
        }
    }
}
