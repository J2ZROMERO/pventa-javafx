package com.j2zromero.pointofsale.repositories.brand;

import com.j2zromero.pointofsale.models.brands.Brand;
import com.j2zromero.pointofsale.utils.MariaDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BrandRepository {

    public void add(Brand brand) throws SQLException {
        String sql = "{ CALL AddBrand(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, brand.getName());
            stmt.execute();
        }
    }

    public List<Brand> getAll() throws SQLException {
        List<Brand> brands = new ArrayList<>();
        String sql = "{ CALL GetBrands() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Brand brand = new Brand();
                brand.setId(rs.getInt("id"));
                brand.setName(rs.getString("name"));
                brands.add(brand);
            }
        }
        return brands;
    }

    public void update(Brand brand) throws SQLException {
        String sql = "{ CALL UpdateBrand(?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setInt(1, brand.getId());
            stmt.setString(2, brand.getName());
            stmt.execute();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "{ CALL DeleteBrand(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        }
    }}
