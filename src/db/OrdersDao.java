package db;

import model.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class OrdersDao {
    Random rand= new Random();

    public boolean insertOrder(Orders orders) {
        int orderId = Math.abs(rand.nextInt());
        final String insert_sql ="""
                INSERT INTO orders(orderId, productId, price, payMethod, customerId, quantity)\s
                VALUES(?, ?, ?, ?, ?, ?)
                """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insert_sql);
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, orders.productId());
            preparedStatement.setInt(3, orders.price());
            preparedStatement.setString(4, orders.payMethod());
            preparedStatement.setInt(5, orders.customerId());
            preparedStatement.setInt(6, orders.quantity());
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();

            return result == 1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("close() 실패" + e.getMessage());
                }
            }
        }
        return false;
    }

    public ArrayList<Orders> getTodayOrders() {
        ArrayList<Orders> list = new ArrayList<>();
        final String select_sql = """
            SELECT orderId, productId, customerId, price, payMethod, quantity, TO_CHAR(orderDate, 'YYYY-MM-DD HH24:MI:SS')
            FROM orders
            WHERE TRUNC(orderDate) = TRUNC(SYSDATE)
        """;
        Connection connection = null;

        try  {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(select_sql);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Orders o = new Orders(
                        result.getInt("orderId"),
                        result.getInt("productId"),
                        result.getInt("customerId"),
                        result.getInt("price"),
                        result.getString("payMethod"),
                        result.getInt("quantity"),
                        result.getString(7)  // timestamp 문자열
                );
                list.add(o);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("close() 실패 " + e.getMessage());
                }
            }
        }
        return list;
    }

    public int getTodaySalesTotal() {
        final String sql = "SELECT SUM(price * quantity) AS total FROM orders WHERE TRUNC(orderDate) = TRUNC(SYSDATE)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("getTodaySalesTotal 오류: " + e.getMessage());
        }
        return 0;
    }

}
