package com.j2zromero.pointofsale.repositories.category;
import com.j2zromero.pointofsale.models.categories.Category;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    // Insert a new category (returns true if already exists)
    public boolean add(Category category) throws SQLException {
        String sql = "{ CALL AddCategory(?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getSlug());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("alreadyExists");
                }
            }
        }
        return false;
    }

    // Retrieve all categories
    public List<Category> getAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "{ CALL GetAllCategories() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getLong("id"));
                c.setName(rs.getString("name"));
                c.setSlug(rs.getString("slug"));
                Timestamp ct = rs.getTimestamp("created_at");
                Timestamp ut = rs.getTimestamp("updated_at");
                c.setCreated_at(ct != null ? new Date(ct.getTime()) : null);
                c.setUpdated_at(ut != null ? new Date(ut.getTime()) : null);
                list.add(c);
            }
        }
        return list;
    }

    // Update an existing category
    public void update(Category category) throws SQLException {
        String sql = "{ CALL UpdateCategory(?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, category.getId());
            stmt.setString(2, category.getName());
            stmt.setString(3, category.getSlug());
            stmt.execute();
        }
    }

    // Delete by ID
    public void delete(long id) throws SQLException {
        String sql = "{ CALL DeleteCategory(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, id);
            stmt.execute();
        }
    }
}