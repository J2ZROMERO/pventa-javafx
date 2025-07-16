package com.j2zromero.pointofsale.repositories.brand;

import com.j2zromero.pointofsale.models.brands.Brand;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrandRepository {

    // Insert a new brand (returns true if already exists)
    public boolean add(Brand brand) throws SQLException {
        String sql = "{ CALL AddBrand(?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, brand.getName());
            stmt.setString(2, brand.getSlug());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("alreadyExists");
                }
            }
        }
        return false;
    }

    // Retrieve all brands
    public List<Brand> getAll() throws SQLException {
        List<Brand> list = new ArrayList<>();
        String sql = "{ CALL GetAllBrands() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Brand b = new Brand();
                b.setId(rs.getLong("id"));
                b.setName(rs.getString("name"));
                b.setSlug(rs.getString("slug"));
                Timestamp ct = rs.getTimestamp("created_at");
                Timestamp ut = rs.getTimestamp("updated_at");
                b.setCreated_at(ct != null ? new Date(ct.getTime()) : null);
                b.setUpdated_at(ut != null ? new Date(ut.getTime()) : null);
                list.add(b);
            }
        }
        return list;
    }

    // Update an existing brand
    public void update(Brand brand) throws SQLException {
        String sql = "{ CALL UpdateBrand(?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, brand.getId());
            stmt.setString(2, brand.getName());
            stmt.setString(3, brand.getSlug());
            stmt.execute();
        }
    }

    // Delete by ID
    public void delete(long id) throws SQLException {
        String sql = "{ CALL DeleteBrand(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, id);
            stmt.execute();
        }
    }
}