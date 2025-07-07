import controller.*;
import db.*;
import model.Staff;
import service.ProductService;
import service.StaffService;
import service.StoreService;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConnectionManager.driverLoad(); // 드라이버 로드
        LocalDate today = LocalDate.now(); // 로컬 데이트
        /* Dao 객체 생성 */
        StaffDao staffDao = new StaffDao();
        ProductDao productDao = new ProductDao();
        StoreDao storeDao = new StoreDao();
        WorklogDao worklogDao = new WorklogDao();
        InventoryDao inventoryDao = new InventoryDao();
        OrdersDao ordersDao = new OrdersDao();

        /* 서비스에 Dao 주입 */
        StaffService staffService = new StaffService(staffDao, worklogDao);
        ProductService productService = new ProductService(productDao);
        StoreService storeService = new StoreService(productDao, storeDao, ordersDao, inventoryDao, today);
        Scanner scanner = new Scanner(System.in);

        // 세션
        boolean isLoggedIn = false;
        Staff currentStaff = null;

        while (true) {
            if (isLoggedIn) {
                System.out.println("== 메뉴선택 ==");
                System.out.println("1. 매장재고조회"); // 매장에 있는 제품 조회
                System.out.println("2. 상품판매");
                System.out.println("3. 잔고확인");
                System.out.println("4. 금일매출");
                System.out.println("5. 상품등록");
                System.out.println("6. 상품목록");
                System.out.println("7. 상품주문");  // 상품주문 -> inventory
                System.out.println("============");
                System.out.println("0. 종료(퇴근하기)");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice){
                    case 0:
                        staffService.loggout(currentStaff.staffId());
                        int wage = staffService.calculateWage(currentStaff.staffId());
                        System.out.printf("사원: " + currentStaff.name() + " 일당 :  %,d원 \n", wage);
                        System.exit(0);
                        break;
                    case 1:
                        new GetStockAll(scanner, storeService).run();
                        break;
                    case 2:
                        new SaleProduct(scanner, storeService).run();
                        break;
                    case 3:
                        new CheckBalance(storeService).run();
                        break;
                    case 4:
                        new ShowTodaySales(storeService).run();
                        break;
                    case 5:
                        new AddProduct(scanner, productService).run();
                        break;
                    case 6:
                        new GetProductAll(scanner, productService).run();
                        break;
                    case 7:
                        new AddStockProduct(scanner, storeService, productService).run();
                        break;
                }
            } else {
                System.out.print("아이디 입력: ");
                String id = scanner.nextLine();
                System.out.print("비밀번호 입력: ");
                String pw = scanner.nextLine();
                Staff findStaff = staffService.login(id, pw);
                if (findStaff != null){
                    isLoggedIn = true;
                    currentStaff = findStaff;
                    System.out.println("**로그인 완료**");
                    System.out.println("직원: " + currentStaff.name() + "님 안녕하세요!");
                    System.out.println("============");
                }
            }
        }
    }
}
