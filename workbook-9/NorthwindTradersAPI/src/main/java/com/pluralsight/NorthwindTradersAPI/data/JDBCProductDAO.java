package com.pluralsight.NorthwindTradersAPI.data;

import com.pluralsight.NorthwindTradersAPI.interfaces.ProductDAO;
import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
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

        // TO-DO: Update query to not include the JOIN
        // TO-DO: Update products to take in a CategoryID as an int
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

        // TO-DO: Update products to take in a CategoryID as an int
        String query = "SELECT ProductID, ProductName, CategoryID, UnitPrice " +
                "FROM products " +
                "WHERE ProductID = ?;";
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

    @Override
    public Product addProduct(Product product){
        String query = "INSERT INTO products (ProductName, CategoryID, UnitPrice) " +
                "VALUES (?, ?, ?);";

        try {
            Connection connection = dataSource.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, product.getName());
                statement.setString(2, product.getCategory());
                statement.setDouble(3, product.getPrice());

                int rowsAffected = statement.executeUpdate();

                ResultSet rs = statement.getGeneratedKeys();

                if (rs.next()) product.setProductID(rs.getInt(1));

                if (rowsAffected > 0) {
                    System.out.println("Product added successfully!");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }
}
