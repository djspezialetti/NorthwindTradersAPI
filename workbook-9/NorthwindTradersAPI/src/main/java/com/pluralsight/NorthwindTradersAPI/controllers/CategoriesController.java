package com.pluralsight.NorthwindTradersAPI.controllers;

import com.pluralsight.NorthwindTradersAPI.models.Category;
import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoriesController {
    private DataSource dataSource;

    @Autowired
    public CategoriesController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @RequestMapping(path = "/categories", method = RequestMethod.GET)
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        String query = "SELECT  CategoryID, CategoryName " +
                "FROM categories " +
                "ORDER BY CategoryID;";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rows = statement.executeQuery();

            while (rows.next()) {
                categories.add(new Category(rows.getInt(1), rows.getString(2)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @RequestMapping(path = "/categories/{categoryID}", method = RequestMethod.GET)
    public List<Category> getCategoryByID(@PathVariable int categoryID) {
        List<Category> categories = new ArrayList<>();

        String query = "SELECT  CategoryID, CategoryName " +
                "FROM categories " +
                "WHERE CategoryID = ?;";

        try {
            Connection connection = dataSource.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, categoryID);

                try (ResultSet rows = statement.executeQuery()) {
                    while (rows.next()) {
                        categories.add(new Category(rows.getInt(1), rows.getString(2)));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }
}
