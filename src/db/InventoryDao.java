package db;

import model.Inventory;
import model.InventoryDetail;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class InventoryDao {
    static Random rand = new Random();
    static int inventoryId = Math.abs(rand.nextInt());
    LocalDate today = LocalDate.now();

    public boolean insertInventory(int storeId, Product product) {
        final String insert_sql = """
            INSERT INTO inventory(inventoryId, storeId, productId, expirationDate)
            VALUES(?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))
            """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insert_sql);
            String expDate = String.valueOf(today.plusDays(product.expirationDate()));
            preparedStatement.setInt(1, inventoryId);
            preparedStatement.setInt(2, storeId);
            preparedStatement.setInt(3, product.productId());
            preparedStatement.setString(4, expDate);
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result == 1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("close() 실패: " + e.getMessage());
                }
            }
        }
    }

    public ArrayList<InventoryDetail> getInventoryDetail() {
        ArrayList<InventoryDetail> inventoryDetails = new ArrayList<>();
        final String select_sql = """
                SELECT
                    i.inventoryId,
                    i.storeId,
                    i.productId,
                    p.productName,
                    p.category,
                    p.company,
                    p.price,
                    p.isAdult,
                    i.expirationDate
                FROM
                    inventory i
                JOIN
                    product p
                ON
                    i.productId = p.productId
                """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(select_sql);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                InventoryDetail detail = new InventoryDetail(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getString(4),
                        result.getInt(5),
                        result.getString(6),
                        result.getInt(7),
                        result.getString(8),
                        result.getString(9)
                );
                inventoryDetails.add(detail);
            }

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
        return inventoryDetails;
    }

    public ArrayList<Inventory> getInventoryAll() {
        final String select_sql = "SELECT * FROM inventory";
        ArrayList<Inventory> inventories = new ArrayList<>();
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(select_sql);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                Inventory inventory = new Inventory(
                        result.getInt("inventoryId"),
                        result.getInt("storeId"),
                        result.getInt("productId"),
                        result.getString("ExpirationDate")
                );
                inventories.add(inventory);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("close() 실패: " + e.getMessage());
                }
            }
        }
        return inventories;
    }

    public boolean deleteInventoryById(int inventoryId) {
        final String delete_sql = "DELETE FROM inventory WHERE inventoryId = ?";
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(delete_sql);
            preparedStatement.setLong(1, inventoryId);

            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result == 1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("close() 실패 " + e.getMessage());
                }
            }
        }
    }
}