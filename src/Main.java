import controller.*;
import db.ConnectionManager;
import service.ProductService;
import service.StaffService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConnectionManager.driverLoad(); // 드라이버 로드
        StaffService staffService = new StaffService(); // StaffService 생성
        ProductService productService = new ProductService(); // ProductService 생성
        Scanner scanner = new Scanner(System.in);

        boolean isLoggedIn = false;

        while (true) {
            if (isLoggedIn) {
                System.out.println("== 메뉴선택 ==");
                System.out.println("1. 매장제품조회"); // 매장에 있는 제품 조회
                System.out.println("2. 매장제품판매");
                System.out.println("== 상품관련 ==");
                System.out.println("5. 상품등록");
                System.out.println("6. 상품목록");
                System.out.println("7. 상품주문");  // 상품주문 -> inventory
                System.out.println("============");
                System.out.println("0. 종료(퇴근하기)");
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice){
                    case 0:
                        System.exit(0);
                        break;
                    case 2:
                        System.out.println("2");
                        break;
                    case 5:
                        new AddProduct(scanner, productService).run();
                        break;
                    case 6:
                        new getProductAll(scanner, productService).run();
                        break;
                }
            } else {
                System.out.print("아이디 입력: ");
                String id = scanner.nextLine();
                System.out.print("비밀번호 입력: ");
                String pw = scanner.nextLine();
                String name = staffService.login(id, pw);
                if (!name.isEmpty()){
                    isLoggedIn = true;
                    System.out.println("**로그인 완료**");
                    System.out.println("직원: " + name + "님 안녕하세요!");
                    System.out.println("============");
                }
            }
        }
    }
}
