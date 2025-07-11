package db;

import model.Inventory;
import dto.InventoryCount;
import dto.InventoryDetail;
import model.Product;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class InventoryDao {
    Random rand = new Random();

    public boolean insertInventory(int storeId, Product product, String expDate) {
        int inventoryId = Math.abs(rand.nextInt());
        final String insert_sql = """
            INSERT INTO inventory(inventoryId, storeId, productId, expirationDate, status)
            VALUES(?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)
            """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insert_sql);
            preparedStatement.setInt(1, inventoryId);
            preparedStatement.setInt(2, storeId);
            preparedStatement.setInt(3, product.productId());
            preparedStatement.setString(4, expDate);
            preparedStatement.setString(5, "shipping"); // 입고될 떄 바로 배송중 상태
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
                        result.getString(9),
                        result.getString(10)
                );
                inventoryDetails.add(detail);
            }
            preparedStatement.close();

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
                        result.getString("ExpirationDate"),
                        result.getString("Status")
                );
                inventories.add(inventory);
            }
            preparedStatement.close();

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
    public ArrayList<Inventory> getInventoryListByProductId(int productId, String status) {
        final String sql = """
            SELECT inventoryId, storeId, productId, TO_CHAR(expirationDate, 'YYYY-MM-DD') AS expirationDate, status
            FROM inventory
            WHERE productId = ? AND status = ?
            ORDER BY expirationDate
        """;
        ArrayList<Inventory> list = new ArrayList<>();
        Connection connnection = null;
        try {
            connnection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connnection.prepareStatement(sql);
            preparedStatement.setInt(1, productId);
            preparedStatement.setString(2, status);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                list.add(new Inventory(
                        result.getInt("inventoryId"),
                        result.getInt("storeId"),
                        result.getInt("productId"),
                        result.getString("expirationDate"),
                        result.getString("status")
                ));
            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connnection != null)
                try {
                    connnection.close();
                } catch (SQLException e) {
                    System.out.println("close() 실패" + e.getMessage());
                }
        }
        return list;
    }

    public Inventory getInventoryOneByProdId(int productId, String status) {
        final String select_sql = """
                SELECT inventoryId, storeId, productId, TO_CHAR(expirationDate, 'YYYY-MM-DD') AS expirationDate, status\s
                FROM inventory\s
                WHERE productId = ? AND status = ?\s
                ORDER BY expirationDate
                """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(select_sql);
            preparedStatement.setInt(1, productId);
            preparedStatement.setString(2, status);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return new Inventory(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getString(4),
                        result.getString(5)
                );
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
        return null;
    }

    public boolean updateInventoryStatus(int inventoryId, String status) {
        final String update_sql = """
            UPDATE inventory
            SET status = ?
            WHERE inventoryId = ?
        """;

        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(update_sql);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, inventoryId);

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
                    System.out.println("close() 실패: " + e.getMessage());
                }
            }
        }
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


    public ArrayList<InventoryCount> getInventoryCount(String status) {
        ArrayList<InventoryCount> inventoryCounts = new ArrayList<>();
        final String select_sql = """
                SELECT p.productId, p.productName, COUNT(*) as count\s
                FROM inventory i\s
                JOIN product p\s
                ON i.productid = p.productid\s
                WHERE i.status = ?\s
                GROUP BY p.productId, p.productName
                """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(select_sql);
            preparedStatement.setString(1, status);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                InventoryCount inventoryCount = new InventoryCount(
                    result.getInt(1),
                    result.getString(2),
                    result.getInt(3)
                );
                inventoryCounts.add(inventoryCount);
            }
            preparedStatement.close();

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
        return inventoryCounts;
    }
}