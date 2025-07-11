package controller;

import dto.OrderResult;
import service.StoreService;

import java.io.BufferedReader;
import java.io.IOException;

public class SaleProduct implements Controller {
    private final BufferedReader reader;
    private final StoreService storeService;

    public SaleProduct(BufferedReader reader, StoreService storeService) {
        this.reader = reader;
        this.storeService = storeService;
    }

    @Override
    public void run() {
        boolean isAdult = false;
        try {
            while (true) {
                System.out.print("구매할 제품의 이름을 입력해주세요(완료:0): ");
                String prodName = reader.readLine().trim();

                if ("0".equals(prodName)) {
                    System.out.print("결제수단을 입력해주세요(cash/card): ");
                    String payMethod = reader.readLine().trim();

                    int cashAmount = 0;
                    if ("cash".equalsIgnoreCase(payMethod)) {
                        System.out.print("현금 얼마를 넣으시겠습니까?: ");
                        cashAmount = Integer.parseInt(reader.readLine().trim());
                    }

                    OrderResult result = storeService.processSale(payMethod, cashAmount);
                    if (result.isSuccess()) {
                        System.out.println("결제가 완료되었습니다. 감사합니다!");
                        System.out.printf("현재 잔고: %,d원%n", storeService.getStoreBalance());
                    } else {
                        System.out.println("오류: " + result.getErrorMsg());
                    }
                    break;
                }

                // 장바구니 처리: 성공(1), 성인인증 필요(2), 유통기한 만료(3), 제품없음(4)
                int code = storeService.addCart(storeService.getInventoryByName(prodName), isAdult);
                switch (code) {
                    case 1:
                        System.out.println("장바구니에 추가되었습니다: " + prodName);
                        break;
                    case 2:
                        System.out.println("성인 인증이 필요합니다.");
                        System.out.print("주민번호 앞 6자리: ");
                        reader.readLine(); // 인증 로직 (추후 구현)
                        isAdult = true;
                        break;
                    case 3:
                        System.out.println("유통기한이 지난 상품입니다.");
                        break;
                    case 4:
                        System.out.println("제품이 없습니다.");
                        break;
                    default:
                        System.out.println("알 수 없는 오류(코드: " + code + ")");
                }
            }
        } catch (IOException e) {
            System.out.println("입력 처리 중 오류가 발생했습니다: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("숫자 형식이 올바르지 않습니다: " + e.getMessage());
        }
    }
}
