package controller;

import dto.ProdCategory;
import model.Product;
import service.ProductService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class GetProductAll implements Controller {
    private final BufferedReader reader;
    private final ProductService productService;

    public GetProductAll(BufferedReader reader, ProductService productService) {
        this.reader = reader;
        this.productService = productService;
    }

    @Override
    public void run() {
        try {
            ArrayList<Product> result = productService.showProductAll();
            if (result.isEmpty()) {
                System.out.println("가져오기 실패");
                return;
            }

            for (Product r : result) {
                System.out.println();
                System.out.println("ID : " + r.productId());
                if ("1".equals(r.isAdult())) {
                    System.out.println("[ 19세이하 제한품목 ]");
                }
                System.out.println("( " + ProdCategory.intToCategory(r.category()) + " )");
                System.out.println("제품명   : " + r.name());
                System.out.println("제조사   : " + r.company());
                System.out.println("가격(원) : " + r.price());
                System.out.println("유통기한 - " + r.expirationDate());
                System.out.println();
            }

            System.out.print("아무키나 입력하세요: ");
            reader.readLine();
        } catch (IOException e) {
            System.out.println("입력 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
