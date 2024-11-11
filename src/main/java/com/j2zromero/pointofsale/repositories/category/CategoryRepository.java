package com.j2zromero.pointofsale.repositories.category;
import com.j2zromero.pointofsale.models.categories.Category;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoryRepository {

    public void add(Category category) throws SQLException {
        String sql = "{ CALL AddCategory(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, category.getName());
            stmt.execute();
        }
    }

    public List<Category> getAll() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "{ CALL GetCategory() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }
        }
        return categories;
    }

    public void update(Category category) throws SQLException {
        String sql = "{ CALL UpdateCategory(?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setInt(1, category.getId());
            stmt.setString(2, category.getName());
            stmt.execute();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "{ CALL DeleteCategory(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        }
    }}
