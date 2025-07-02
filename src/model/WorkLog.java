package model;

public record WorkLog(
        int staffId,
        int storeId,
        String startDate, // 출근
        String endDate  // 퇴근
) {}
