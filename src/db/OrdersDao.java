package db;

import model.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class OrdersDao {
    Random random = new Random();

    public boolean insertOrder(Orders orders) {
        int orderId = Math.abs(random.nextInt());
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
}
