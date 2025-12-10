package com.pluralsight.NorthwindTradersAPI.data;

import com.pluralsight.NorthwindTradersAPI.interfaces.CategoryDAO;
import com.pluralsight.NorthwindTradersAPI.models.Category;
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
public class JDBCCategoryDAO implements CategoryDAO {
    private List<Category> categories;
    private DataSource dataSource;

    @Autowired
    public JDBCCategoryDAO(DataSource dataSource){
        this.dataSource = dataSource;
        this.categories = new ArrayList<>();
    }

    @Override
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

    @Override
    public List<Category> getCategoryByID(int id) {
        List<Category> categories = new ArrayList<>();

        String query = "SELECT  CategoryID, CategoryName " +
                "FROM categories " +
                "WHERE CategoryID = ?;";

        try {
            Connection connection = dataSource.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);

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
