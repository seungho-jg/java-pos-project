package controller;

import service.ProductService;
import service.StoreService;

import java.io.BufferedReader;
import java.io.IOException;

public class AddStockProduct implements Controller {
    private final BufferedReader reader;
    private final StoreService storeService;
    private final ProductService productService;

    public AddStockProduct(BufferedReader reader, StoreService storeService, ProductService productService) {
        this.reader = reader;
        this.storeService = storeService;
        this.productService = productService;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.print("주문할 제품이름: ");
                String prodName = reader.readLine().trim();

                if (!productService.isValidProdName(prodName)) {
                    System.out.println("해당 제품이 존재하지 않습니다.");
                    continue;
                }

                System.out.print("주문할 수량(최소 1): ");
                String qtyLine = reader.readLine().trim();
                int quantity;
                try {
                    quantity = Integer.parseInt(qtyLine);
                    if (quantity < 1) {
                        System.out.println("최소 1개 이상 입력해야 합니다.");
                        continue;
                    }

                    storeService.stockProduct(prodName, quantity);
                    System.out.printf("'%s' %d개 주문이 완료되었습니다.%n", prodName, quantity);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("숫자로 입력해 주세요.");
                }
            }
        } catch (IOException e) {
            System.out.println("입력 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
