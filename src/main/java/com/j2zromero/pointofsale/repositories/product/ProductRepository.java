package com.j2zromero.pointofsale.repositories.product;

import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.utils.MariaDB;
import com.j2zromero.pointofsale.utils.UnitType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    // Method to add a new product
    public void add(Product product) throws SQLException {
        String sql = "{ CALL AddProduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getCode());
            stmt.setInt(4, product.getUnitMeasurement());
            stmt.setDouble(5, product.getUnitPrice());
            stmt.setObject(6, product.getVolumePrice(), Types.DOUBLE); // Nullable field
            stmt.setObject(7, product.getStock(), Types.DOUBLE);       // Stock, nullable
            stmt.setString(8, product.getCategory());
            stmt.setString(9, product.getBrand());
            stmt.setObject(10, product.getFkSupplier(), Types.BIGINT);
            stmt.execute();
        }
    }

    // Method to get all products
    public List<Product> getAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "{ CALL GetProducts() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setCode(rs.getString("code"));
                product.setUnitMeasurement(rs.getInt("unit_measurement"));
                product.setUnitPrice(rs.getDouble("unit_price"));
                product.setVolumePrice((Double) rs.getObject("volume_price")); // Nullable field
                product.setStock(rs.getDouble("stock"));
                product.setCategory(rs.getString("category"));
                product.setBrand(rs.getString("brand"));
                product.setFkSupplier(rs.getLong("fk_supplier"));
                products.add(product);
            }
        }
        return products;
    }

    // Method to update an existing product
    public void update(Product product) throws SQLException {
        String sql = "{ CALL UpdateProduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setString(4, product.getCode());
            stmt.setInt(5, product.getUnitMeasurement());
            stmt.setDouble(6, product.getUnitPrice());
            stmt.setObject(7, product.getVolumePrice(), Types.DOUBLE); // Nullable field
            stmt.setDouble(8, product.getStock());
            stmt.setString(9, product.getCategory());
            stmt.setString(10, product.getBrand());
            stmt.setLong(11, product.getFkSupplier());
            stmt.execute();
        }
    }

    // Method to delete a product by ID
    public void delete(long id) throws SQLException {
        String sql = "{ CALL DeleteProduct(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, id);
            stmt.execute();
        }
    }

    // Method to get all products
    public List<UnitType> getMeasurementTypes() throws SQLException {
        List<UnitType> types = new ArrayList<>();
        String sql = "{ CALL GetUnitTypes() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UnitType product = new UnitType(rs.getInt("id"), rs.getString("name"));
                types.add(product);
            }
        }
        return types;
    }
}
