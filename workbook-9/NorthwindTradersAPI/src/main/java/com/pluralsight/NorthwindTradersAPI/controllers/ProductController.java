package com.pluralsight.NorthwindTradersAPI.controllers;

import com.pluralsight.NorthwindTradersAPI.interfaces.ProductDAO;
import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    private ProductDAO productDAO;

    @Autowired
    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @RequestMapping(path = "/products", method = RequestMethod.GET)
    public List<Product> getAllProducts() {
        List<Product> products = productDAO.getAllProducts();
        return products;
    }

    @RequestMapping(path = "/products/{productID}", method = RequestMethod.GET)
    public List<Product> getProductByID(@PathVariable int productID) {
        List<Product> products = productDAO.getProductByID(productID);
        return products;
    }
}
