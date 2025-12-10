package com.pluralsight.NorthwindTradersAPI.interfaces;

import com.pluralsight.NorthwindTradersAPI.models.Category;

import java.util.List;

public interface CategoryDAO {
    public void getProductByID(int id);
    public List<Category> getAllProducts();
}
