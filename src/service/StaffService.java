package service;

import db.StaffDao;
import model.Staff;

public class StaffService {
    StaffDao staffDao = null; // 싱글톤

    public StaffService(StaffDao staffDao) {
        this.staffDao = staffDao;
    }

    public String login(String id, String pw){
        Staff result = staffDao.getStaff(id, pw);

        if (result == null) {
            System.out.println("아이디나 비밀번호가 틀렸습니다.");
            return "";
        }
        return result.name();
    }
}
