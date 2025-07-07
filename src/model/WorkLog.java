package model;

import java.sql.Timestamp;

public record WorkLog(
        int workLogId,
        int staffId,
        int storeId,
        Timestamp startDate, // 출근
        Timestamp endDate  // 퇴근
) {}
