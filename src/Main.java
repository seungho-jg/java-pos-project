import controller.*;
import db.*;
import dto.InventoryTuple;
import model.Staff;
import service.ProductService;
import service.StaffService;
import service.StoreService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws Exception {
        // 드라이버 및 서비스 초기화
        ConnectionManager.driverLoad();
        LocalDate today = LocalDate.now();

        StaffDao staffDao = new StaffDao();
        ProductDao productDao = new ProductDao();
        StoreDao storeDao = new StoreDao();
        WorklogDao worklogDao = new WorklogDao();
        InventoryDao inventoryDao = new InventoryDao();
        OrdersDao ordersDao = new OrdersDao();

        StaffService staffService = new StaffService(staffDao, worklogDao);
        ProductService productService = new ProductService(productDao);
        StoreService storeService = new StoreService(productDao, storeDao, ordersDao, inventoryDao, today);

        // 논블로킹 입력을 위한 BufferedReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // 블로킹 로그인 처리
        System.out.print("아이디 입력: ");
        String id = reader.readLine().trim();
        System.out.print("비밀번호 입력: ");
        String pw = reader.readLine().trim();

        Staff currentStaff = staffService.login(id, pw);
        if (currentStaff == null) {
            System.out.println("로그인 실패");
            return;
        }
        System.out.println("**로그인 완료**");
        System.out.println("직원: " + currentStaff.name() + "님 안녕하세요!");
        boolean running = true;

        // 시간 기준 틱 계산
        long startTime = System.currentTimeMillis();
        boolean menuShown = false;

        while (running) {
            long now = System.currentTimeMillis();
            StoreService.globalTick = (now - startTime) / 100;  // 1 tick = 10ms 기준

            // 예약 입고 및 슬립리스트 처리
            Iterator<InventoryTuple> iter = StoreService.sleepList.iterator();
            while (iter.hasNext()) {
                InventoryTuple tuple = iter.next();
                if (tuple.getOverLocalTick(StoreService.globalTick)) {
                    System.out.printf("✔ 깨움 → ID: %d / 현재Tick: %d\n",
                            tuple.getInventoryId(), StoreService.globalTick);
                    inventoryDao.updateInventoryStatus(tuple.getInventoryId(), "display");
                    iter.remove();
                }
            }

            if (!menuShown) {
                System.out.println("== 메뉴선택 ==");
                System.out.println("1. 매장재고조회");
                System.out.println("2. 상품판매");
                System.out.println("3. 잔고확인");
                System.out.println("4. 금일매출");
                System.out.println("5. 상품등록");
                System.out.println("6. 상품목록");
                System.out.println("7. 상품주문");
                System.out.println("0. 종료(퇴근하기)");
                menuShown = true;
            }

            // 논블로킹으로 사용자 입력 처리
            if (reader.ready()) {
                String line = reader.readLine().trim();
                switch (line) {
                    case "0":
                        staffService.loggout(currentStaff.staffId());
                        int wage = staffService.calculateWage(currentStaff.staffId());
                        System.out.printf("사원: %s 일당: %,d원\n", currentStaff.name(), wage);
                        running = false;
                        break;
                    case "1":
                        new GetStockAll(reader, storeService).run();
                        menuShown = false;
                        break;
                    case "2":
                        new SaleProduct(reader, storeService).run();
                        menuShown = false;
                        break;
                    case "3":
                        new CheckBalance(storeService).run();
                        menuShown = false;
                        break;
                    case "4":
                        new ShowTodaySales(storeService).run();
                        menuShown = false;
                        break;
                    case "5":
                        new AddProduct(reader, productService).run();
                        menuShown = false;
                        break;
                    case "6":
                        new GetProductAll(reader, productService).run();
                        menuShown = false;
                        break;
                    case "7":
                        new AddStockProduct(reader, storeService, productService).run();
                        menuShown = false;
                        break;
                    default:
                        System.out.println("알 수 없는 명령어입니다.");
                }
            }

            // 루프 과부하 방지
            Thread.sleep(200);
        }

        System.out.println("프로그램 종료.");
    }
}
