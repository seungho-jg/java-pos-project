package controller;

import model.ProdCategory;
import model.Product;
import service.ProductService;

import java.util.ArrayList;
import java.util.Scanner;

public class getProductAll implements Controller {
    Scanner sc = null;
    ProductService productService = null;

    public getProductAll(Scanner scanner, ProductService ps) {
        sc = scanner;
        productService = ps;
    }
    @Override
    public void run() {
        ArrayList<Product> result = productService.showProductAll();
        if (result.isEmpty()) {
            System.out.println("가져오기 실패");
            return;
        }
        for (Product r : result) {
            System.out.println();
            System.out.println("ID : " + r.productId());
            if(r.isAdult().equals("1")) {
                System.out.println("[ 19세이하 제한품목 ]");
            }
            System.out.println("( " + ProdCategory.intToCategory(r.category()) + " )");
            System.out.println("제품명   : " + r.name());
            System.out.println("제조사   : " + r.company());
            System.out.println("가격(원) : " + r.price());
            System.out.println("유통기한 - " + r.expirationDate());

            System.out.println();
        }
    };
}
