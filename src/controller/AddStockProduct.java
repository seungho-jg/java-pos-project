package controller;

import service.ProductService;
import service.StoreService;

import java.util.Scanner;

public class AddStockProduct implements Controller{
    Scanner sc = null;
    StoreService storeService = null;
    ProductService productService = null;

    public AddStockProduct(Scanner sc, StoreService storeservice, ProductService productService){
        this.sc = sc;
        this.storeService = storeservice;
        this.productService = productService;
    }
    @Override
    public void run() {
        while (true) {
            System.out.print("주문할 제품이름: ");
            String prodName = sc.nextLine();

            if (!productService.isValidProdName(prodName)) {
                System.out.println("해당 제품이 존재하지 않습니다.");
                continue;
            }

            System.out.print("주문할 수량(최소 10): ");
            try {
                int quantity = Integer.parseInt(sc.nextLine());
                if (quantity < 1) {
                    System.out.println("최소 0개 이상 입력해야 합니다.");
                    continue;
                }

                storeService.stockProduct(prodName, quantity);
                System.out.printf("'%s' %d개 주문이 완료되었습니다.%n", prodName, quantity);
                break;

            } catch (NumberFormatException e) {
                System.out.println("숫자로 입력해 주세요.");
            }
        }
    }

}
