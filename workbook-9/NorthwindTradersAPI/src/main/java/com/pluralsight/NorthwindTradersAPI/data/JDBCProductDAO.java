package com.pluralsight.NorthwindTradersAPI.data;

import com.pluralsight.NorthwindTradersAPI.interfaces.ProductDAO;
import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JDBCProductDAO implements ProductDAO {
    private List<Product> products;
    private DataSource dataSource;

    @Autowired
    public JDBCProductDAO(DataSource dataSource){
        this.dataSource = dataSource;
        this.products = new ArrayList<>();
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        String query = "SELECT P.ProductID, P.ProductName, C.CategoryName, P.UnitPrice " +
                "FROM products AS P " +
                "JOIN categories AS C ON (C.CategoryID = P.CategoryID) " +
                "ORDER BY P.ProductID;";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rows = statement.executeQuery();

            while (rows.next()) {
                products.add(new Product(rows.getInt(1), rows.getString(2),
                        rows.getString(3), rows.getDouble(4)));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    @Override
    public List<Product> getProductByID(int id) {
        List<Product> products = new ArrayList<>();

        String query = "SELECT P.ProductID, P.ProductName, C.CategoryName, P.UnitPrice " +
                "FROM products AS P " +
                "JOIN categories AS C ON (C.CategoryID = P.CategoryID) " +
                "WHERE P.ProductID = ?;";
        try {
            Connection connection = dataSource.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);

                try (ResultSet rows = statement.executeQuery()) {
                    while (rows.next()) {
                        products.add(new Product(rows.getInt(1), rows.getString(2),
                                rows.getString(3), rows.getDouble(4)));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }
}
