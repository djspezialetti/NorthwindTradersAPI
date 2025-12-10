package com.pluralsight.NorthwindTradersAPI.interfaces;

import com.pluralsight.NorthwindTradersAPI.models.Product;

import java.util.List;

public interface ProductDAO {
    public List<Product> getProductByID(int id);
    public List<Product> getAllProducts();
    public Product addProduct(Product product);
}
