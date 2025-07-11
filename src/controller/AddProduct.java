package controller;

import dto.ProdCategory;
import model.Product;
import service.ProductService;

import java.io.BufferedReader;
import java.io.IOException;

public class AddProduct implements Controller {
    private final BufferedReader reader;
    private final ProductService productService;

    public AddProduct(BufferedReader reader, ProductService productService) {
        this.reader = reader;
        this.productService = productService;
    }

    @Override
    public void run() {
        try {
            System.out.print("상품이름: ");
            String prodName = reader.readLine().trim();

            int category = 0;
            while (category == 0) {
                System.out.print("카테고리(food/alcohol): ");
                category = ProdCategory.categoryToInt(reader.readLine().trim());
                if (category == 0) {
                    System.out.println("없는 카테고리입니다.");
                }
            }

            System.out.print("회사명: ");
            String company = reader.readLine().trim();

            System.out.print("가격: ");
            int price = Integer.parseInt(reader.readLine().trim());

            System.out.print("유통기한(일): ");
            int expDate = Integer.parseInt(reader.readLine().trim());

            System.out.print("19세 제한여부(y/n): ");
            String res = reader.readLine().trim();
            boolean isAdult = false;
            if (res.equalsIgnoreCase("y")) {
                isAdult = true;
                System.out.println("++19세 제한으로 설정되었습니다.++");
            }

            boolean insertResult = productService.addProduct(
                    new Product(0, prodName, category, company, price, expDate, isAdult ? "1" : "0")
            );
            if (insertResult) {
                System.out.println("제품등록에 성공했습니다.");
            } else {
                System.out.println("제품등록 실패");
            }
        } catch (IOException e) {
            System.out.println("입력 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
