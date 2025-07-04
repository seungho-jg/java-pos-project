package db;

import model.ProdCategory;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class ProductDao {
    static Random rand = new Random();
    static int productId = Math.abs(rand.nextInt());

    public boolean insertProduct(String productName, int category, String company, int price, int expDate, String isAdult) {
        final String insert_sql = """
                    INSERT INTO product(productId, productName, category, company, price, expirationDate, isAdult)\s
                    VALUES(?, ?, ?, ?, ?, ?, ?)
                """;
        Connection connection = null;
        try {
            System.out.println(productId);
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insert_sql);
            preparedStatement.setInt(1, productId);
            preparedStatement.setString(2, productName);
            preparedStatement.setInt(3, category);
            preparedStatement.setString(4, company);
            preparedStatement.setInt(5, price);
            preparedStatement.setInt(6, expDate);
            preparedStatement.setString(7, isAdult);
            final int res = preparedStatement.executeUpdate();
            System.out.println("Insert: " + res);
            preparedStatement.close();
            return res == 1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("로그인 실패");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("close() 실패");
            }
        }
        return false;
    }

    public Product getProductByName(String productName){
        final String select_sql = """
                SELECT * FROM product\s
                WHERE productName = ?
                """;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(select_sql);
            preparedStatement.setString(1, productName);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                int prodId = result.getInt(1);
                String prodName = result.getString(2);
                int category = result.getInt(3);
                String company = result.getString(4);
                int price = result.getInt(5);
                int expDate = result.getInt(6);
                boolean isAdultOnly = result.getString(7).equals("1");
                return new Product(
                        prodId,
                        prodName,
                        category,
                        company,
                        price,
                        expDate,
                        isAdultOnly
                );
            }
            return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("로그인 실패");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("close() 실패");
            }
        }
        return null;
    }

//    public boolean updateProduct() {
//
//    }
//
//
//
//    public boolean removeProductByName(String productName){
//
//    }
}
