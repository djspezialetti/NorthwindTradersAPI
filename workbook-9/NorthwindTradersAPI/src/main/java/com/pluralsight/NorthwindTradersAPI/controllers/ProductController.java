package com.pluralsight.NorthwindTradersAPI.controllers;

import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @RequestMapping(path="/products", method = RequestMethod.GET)
    public List<Product> getAllProducts(){

        List<Product> products = new ArrayList<>();
        String query = "SELECT P.ProductID, P.ProductName, C.CategoryName, P.UnitPrice " +
                "FROM products AS P " +
                "JOIN categories AS C ON (C.CategoryID = P.CategoryID) " +
                "ORDER BY P.ProductID;";
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rows = statement.executeQuery();

            while (rows.next()) {
                this.products.add(new Product(rows.getInt(1), rows.getString(2),
                        rows.getString(3), rows.getDouble(4)));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

//    @RequestMapping(path="/", method = RequestMethod.GET)
//    public List<Product> getProductByID(@RequestParam(required = false) int productID){
//        List<Product> products = new ArrayList<>();
//
//
//        return products;
//    }

}
