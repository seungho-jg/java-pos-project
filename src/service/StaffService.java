package service;

import db.StaffDao;
import db.WorklogDao;
import model.Staff;
import model.WorkLog;

import java.sql.Timestamp;

public class StaffService {
    StaffDao staffDao = null;
    WorklogDao worklogDao = null;

    public StaffService(StaffDao staffDao, WorklogDao worklogDao) {
        this.staffDao = staffDao;
        this.worklogDao = worklogDao;
    }

    public Staff login(String id, String pw){
        Staff result = staffDao.getStaff(id, pw);

        if (result == null) {
            System.out.println("아이디나 비밀번호가 틀렸습니다.");
            return null;
        }
        // 출근 기록 추가
        boolean workLogInsert = worklogDao.insertWorkLog(
                result.staffId(),
                1, // 매장 ID
                new Timestamp(System.currentTimeMillis())
        );
        if (workLogInsert) {
            System.out.println(result.name() + " 출근 기록 완료");
        } else {
            System.out.println(result.name() + " 출근 기록 실패");
        }

        return result;
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

    public int calculateWage(int staffId) {
        // 최근 worklog 조회
        WorkLog workLog = worklogDao.getLatestWorkLog(staffId);
        if (workLog == null || workLog.endDate() == null) {
            System.out.println("근무 기록이 올바르지 않습니다.");
            return 0;
        }

        long minutes = java.time.Duration.between(
                workLog.startDate().toLocalDateTime(),
                workLog.endDate().toLocalDateTime()
        ).toMinutes();

        int wagePerMinute = 11_000;
        int totalPay = (int)minutes * wagePerMinute;

        return totalPay;
    }
}
