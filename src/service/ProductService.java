package service;

import db.ProductDao;
import model.Product;

import java.util.ArrayList;

public class ProductService {
    ProductDao productDao = null;

    public ProductService() {
        productDao = new ProductDao();
    }

    public boolean addProduct(String prodName, int category, String company, int price, int expDate, boolean isAdultOnly) {
        return productDao.insertProduct(prodName, category, company, price, expDate, isAdultOnly ? "1" : "0");
    }

    public ArrayList<Product> showProductAll(){
        ArrayList<Product> result = productDao.getProductAll();
        // null 처리
        if (result == null) {
            return new ArrayList<Product>();
        } else {
            return result;
        }
    }
}
