package db;

import model.Inventory;
import model.InventoryDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class InventoryDao {
    static Random rand = new Random();
    static int inventoryId = Math.abs(rand.nextInt());

    public boolean insertInventory(Inventory inventory){
        final String insert_sql = """
                INSERT INTO inventory(storeId, productId, expirationDate)\s
                VALUES(?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))
                """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insert_sql);
            preparedStatement.setInt(1, inventoryId);
            preparedStatement.setInt(2, inventory.storeId());
            preparedStatement.setInt(3, inventory.productId());
            preparedStatement.setString(4, inventory.expirationDate());
            int result = preparedStatement.executeUpdate();
            return result == 1;

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

    public boolean updateInventory(Inventory inventory){
        final String update_sql = """
                UPDATE inventory\s
                SET storeId = ?, productId = ?, expirationDate = TO_DATE(?, 'YYYY-MM-DD')\s
                WHERE inventoryId = ?
                """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(update_sql);
            preparedStatement.setInt(1, inventory.storeId());
            preparedStatement.setInt(2, inventory.productId());
            preparedStatement.setString(3, inventory.expirationDate());
            preparedStatement.setInt(4, inventory.inventoryId());
            int res = preparedStatement.executeUpdate();

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

    public ArrayList<InventoryDetail> getInventoryAll() {
        final String select_sql = """
                SELECT inventoryId, storeId, productId, category, price\s
                productName, company, isAdult, expirationDate\s
                FROM inventory JOIN product\s
                ON inventory.productId = product.productId
                """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(select_sql);
            ResultSet result = preparedStatement.executeQuery();
            ArrayList<InventoryDetail> inventories = new ArrayList<>();
            while (result.next()) {
                inventories.add(
                        new InventoryDetail(
                                result.getInt(1),
                                result.getInt(2),
                                result.getInt(3),
                                result.getInt(4),
                                result.getInt(5),
                                result.getString(6),
                                result.getString(7),
                                result.getString(8),
                                String.valueOf(result.getDate(9))
                        )
                );
            }
            return inventories;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<InventoryDetail>();
    }

    public boolean deleteInventoryById(int inventoryId) {
        final String delete_sql ="""
                DELETE FROM inventory\s
                WHERE inventoryId = ?
                """;
        Connection connection = null;
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(delete_sql);
            preparedStatement.setInt(1, inventoryId);
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result == 1;
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

    public Inventory getInventoryById(int id) {
        final String select_sql = """
                SELECT * FROM inventory\s
                WHERE inventoryId = ?
                """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(select_sql);
            ResultSet result = preparedStatement.executeQuery();
            //preparedStatement.close();
            if (result.next()) {
                return new Inventory(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getString(4)
                );
            }
            return null;
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
