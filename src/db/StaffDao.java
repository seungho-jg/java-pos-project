package db;

import model.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class StaffDao {
    static Random rand = new Random();
    static int staffId = Math.abs(rand.nextInt());

    public boolean insertStaff(String id, String pw, String name) {
        final String insert_sql = """
                    INSERT INTO staff(staffId, accountId, accountPw, name)\s
                    VALUES(?, ?, ?, ?)
                """;
        Connection connection = null;
        try {
            System.out.println(staffId);
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insert_sql);
            preparedStatement.setInt(1, staffId);
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, pw);
            preparedStatement.setString(4, name);
            final int res = preparedStatement.executeUpdate();
            preparedStatement.close();
            return res == 1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("close() 실패");
            }
        }
        return false;
    }

    public Staff getStaff(String id, String pw){
        final String select_sql = """
                    SELECT staffId,name FROM staff\s
                    WHERE accountId=? AND accountPw=?
                """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement =  connection.prepareStatement(select_sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, pw);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()){
                return new Staff(
                        result.getInt(1),
                        "***",
                        "***",
                        result.getString(2)
                );
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("close() 실패");
            }
        }
        return null;
    }
}
