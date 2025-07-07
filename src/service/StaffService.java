package service;

import db.StaffDao;
import db.WorklogDao;
import model.Staff;

import java.sql.Timestamp;

public class StaffService {
    StaffDao staffDao = null;
    WorklogDao worklogDao = null;

    public StaffService(StaffDao staffDao, WorklogDao worklogDao) {
        this.staffDao = staffDao;
        this.worklogDao = worklogDao;
    }

    public String login(String id, String pw){
        Staff result = staffDao.getStaff(id, pw);

        if (result == null) {
            System.out.println("아이디나 비밀번호가 틀렸습니다.");
            return "";
        }
        // 출근 기록 추가
        boolean workLogInserte = worklogDao.insertWorkLog(
                result.staffId(),
                1, // 매장 ID
                new Timestamp(System.currentTimeMillis())
        );
        if (workLogInserte) {
            System.out.println(result.name() + " 출근 기록 완료");
        } else {
            System.out.println(result.name() + " 출근 기록 실패");
        }

        return result.name();
    }

    public void loggout(int staffId) {
        // 퇴근 기록 업데이트
        boolean workLogUpdate = worklogDao.updateWorkLogEnd(
                staffId,
                new Timestamp(System.currentTimeMillis())
        );
        if (workLogUpdate) {
            System.out.println("퇴근 기록 완료");
        } else {
            System.out.println("퇴근 기록 실패");
        }
    }
}
