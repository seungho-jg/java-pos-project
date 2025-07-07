package db;

import model.WorkLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

public class WorklogDao {
    Random rand = new Random();
    // 근무시작
    public boolean insertWorkLog(int staffId, int storeId, Timestamp startDate) {
        int workLogId = Math.abs(rand.nextInt());
        final String insert_sql = """
            INSERT INTO worklog(workLogId, staffId, storeId, startDate, endDate)
            VALUES(?, ?, ?, ?, null)
        """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insert_sql);
            preparedStatement.setInt(1, workLogId);
            preparedStatement.setInt(2, staffId);
            preparedStatement.setInt(3, storeId);
            preparedStatement.setTimestamp(4, startDate);
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("close() 실패" + e.getMessage());
            }
        }
        return false;
    }
    // endDate 업데이트
    public boolean updateWorkLogEnd(int staffId, Timestamp endDate) {
        final String update_sql = """
            UPDATE workLog
            SET endDate = ?
            WHERE staffId = ?
            AND endDate IS NULL
        """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(update_sql);
            preparedStatement.setTimestamp(1, endDate);
            preparedStatement.setInt(2, staffId);
            int res = preparedStatement.executeUpdate();
            preparedStatement.close();
            return res == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("close() 실패" + e.getMessage());
            }
        }
        return false;
    }
    // 가장 최근 근무기록 조회
    public WorkLog getLatestWorkLog(int staffId) {
        final String select_sql = """
            SELECT *
            FROM workLog
            WHERE staffId = ?
            ORDER BY startDate DESC
            FETCH FIRST 1 ROWS ONLY
        """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(select_sql);
            pstmt.setInt(1, staffId);
            ResultSet result = pstmt.executeQuery();
            if (result.next()) {
                return new WorkLog(
                        result.getInt("worklogid"),
                        result.getInt("staffid"),
                        result.getInt("storeid"),
                        result.getTimestamp("startdate"),
                        result.getTimestamp("enddate")
                );
            }
            result.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("close() 실패" + e.getMessage());
            }
        }
        return null;
    }

    // 전체 근무기록 조회
    public ArrayList<WorkLog> getAllWorkLogs(int staffId) {
        ArrayList<WorkLog> list = new ArrayList<>();
        final String select_sql = """
            SELECT *
            FROM workLog
            WHERE staffId = ?
            ORDER BY startDate DESC
        """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(select_sql);
            preparedStatement.setInt(1, staffId);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                list.add(new WorkLog(
                        result.getInt("worklogid"),
                        result.getInt("staffid"),
                        result.getInt("storeid"),
                        result.getTimestamp("startdate"),
                        result.getTimestamp("enddate")
                ));
            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("close() 실패");
            }
        }
        return list;
    }
}
