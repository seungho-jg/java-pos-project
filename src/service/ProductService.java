package service;

import db.ProductDao;
import model.ProdCategory;

public class ProductService {
    ProductDao productDao = null;

    public ProductService() {
        productDao = new ProductDao();
    }

    public boolean addProduct(String prodName, int category, String company, int price, int expDate, boolean isAdultOnly) {
        return productDao.insertProduct(prodName, category, company, price, expDate, isAdultOnly ? "1" : "0");
    }
}
