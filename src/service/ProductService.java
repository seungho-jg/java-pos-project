package service;

import db.ProductDao;
import model.Product;

import java.util.ArrayList;

public class ProductService {
    ProductDao productDao = null;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public boolean addProduct(Product product) {
        return productDao.insertProduct(product);
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
