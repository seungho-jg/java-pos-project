import db.ConnectionManager;
import service.StaffService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConnectionManager.driverLoad(); // 드라이버 로드
        StaffService staffService = new StaffService(); // 서비스객체 생성
        Scanner sc = new Scanner(System.in);

        boolean isLoggedIn = false;

        while (true) {
            if (isLoggedIn) {
                System.out.println("== 메뉴선택 ==");
                System.out.println("1. 제품조회");
                System.out.println("2. 제품판매");
                System.out.println("3. 제품등록");
                System.out.println("4. 제품주문");
                System.out.println("0. 종료(퇴근하기)");
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice){
                    case 0:
                        System.exit(0);
                        break;
                    case 1:
                        System.out.println("hi");
                }
            } else {
                System.out.print("아이디 입력: ");
                String id = sc.nextLine();
                System.out.print("비밀번호 입력: ");
                String pw = sc.nextLine();
                String name = staffService.login(id, pw);
                if (!name.isEmpty()){
                    isLoggedIn = true;
                    System.out.println("**로그인 완료**");
                    System.out.println("직원: " + name + "님 안녕하세요!");
                    System.out.println("-----------------------------");
                }
            }
        }
    }
}
