package controller;

import dto.ProdCategory;
import model.Product;
import service.ProductService;

import java.util.Scanner;

public class AddProduct implements Controller {
    Scanner sc = null;
    ProductService productService = null;

    public AddProduct(Scanner scanner, ProductService ps) {
        sc = scanner;
        productService = ps;
    }

    @Override
    public void run() {
        System.out.print("상품이름: ");
        String prodName = sc.nextLine().trim();
        int category = 0;
        while (category == 0) {
            System.out.print("카테고리(food/alcohol): ");
            category = ProdCategory.categoryToInt(sc.nextLine().trim());
            if (category == 0) {
                System.out.println("없는 카테고리입니다.");
            }
        }
        System.out.print("회사명: ");
        String company = sc.nextLine().trim();
        System.out.print("가격: ");
        int price = Integer.parseInt(sc.nextLine().trim());
        System.out.print("유통기한(일): ");
        int expDate = Integer.parseInt(sc.nextLine().trim());
        System.out.print("19세 제한여부(y/n): ");
        String res = sc.nextLine().trim();
        boolean isAdult = false;
        if (res.equals("Y") || res.equals("y")) {
            isAdult = true;
            System.out.println("++19세 제한으로 설정되었습니다.++");
        }
        boolean insetResult = productService.addProduct(new Product(0, prodName, category, company, price, expDate, isAdult ? "1" : "0"));
        if (insetResult) {
            System.out.println("제품등록에 성공했습니다.");
        } else {
            System.out.println("제품등록 실패");
        }
    }
}
