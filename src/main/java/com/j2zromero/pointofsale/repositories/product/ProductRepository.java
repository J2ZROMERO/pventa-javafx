package com.j2zromero.pointofsale.repositories.product;

import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.utils.MariaDB;
import com.j2zromero.pointofsale.utils.SQLUtils;
import com.j2zromero.pointofsale.utils.UnitType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    // Method to add a new product
    public void add(Product product) throws SQLException {
        System.out.println(product.getUnitMeasurement());
        String sql = "{ CALL AddProduct(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);

             CallableStatement stmt = con.prepareCall(sql)) {
            // All fields, using SQLUtils.setNullable for consistency
            SQLUtils.setNullable(stmt, 1, product.getName(), Types.VARCHAR);           // p_name: VARCHAR(100)  // mandatory
            SQLUtils.setNullable(stmt, 2, product.getDescription(), Types.VARCHAR);    // p_description: TEXT  // mandatory
            SQLUtils.setNullable(stmt, 3, product.getCode(), Types.VARCHAR);           // p_code: VARCHAR(100)  // mandatory
            SQLUtils.setNullable(stmt, 4, product.getUnitMeasurement(), Types.INTEGER); // p_unit_measurement: INT  // mandatory
            SQLUtils.setNullable(stmt, 5, product.getUnitPrice(), Types.DOUBLE);       // p_unit_price: DECIMAL(10, 2)  // mandatory
            SQLUtils.setNullable(stmt, 6, product.getVolumePrice(), Types.DOUBLE);     // p_volume_price: DECIMAL(10, 2)
            SQLUtils.setNullable(stmt, 7, product.getCategory(), Types.VARCHAR);       // p_category: VARCHAR(50)
            SQLUtils.setNullable(stmt, 8, product.getBrand(), Types.VARCHAR);          // p_brand: VARCHAR(50)
            SQLUtils.setNullable(stmt, 9, product.getFkSupplier(), Types.BIGINT);     // p_fk_supplier: BIGINT

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
                product.setVolumePrice(SQLUtils.getNullable(rs, "volume_price", Double.class)); // Nullable field
                product.setStock(rs.getDouble("stock"));
                product.setCategory(rs.getString("category"));
                product.setBrand(rs.getString("brand"));
                product.setFkSupplier(rs.wasNull() ? null : rs.getLong("fk_supplier"));
                products.add(product);
            }
        }
        return products;
    }

    // Method to update an existing product
    public void update(Product product) throws SQLException {
        String sql = "{ CALL UpdateProduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setString(4, product.getCode());
            stmt.setInt(5, product.getUnitMeasurement());
            stmt.setObject(6, product.getUnitPrice(), Types.DOUBLE);
            stmt.setObject(7, product.getVolumePrice(), Types.DOUBLE); // Nullable field
            stmt.setString(8, product.getCategory());
            stmt.setString(9, product.getBrand());
            stmt.setObject(10, product.getFkSupplier());
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
    // Method to get a product by ID
    public Product getById(String code) throws SQLException {
        String sql = "{ CALL GetProductById(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getLong("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setCode(rs.getString("code"));
                    product.setUnitMeasurement(rs.getInt("unit_measurement"));
                    product.setStock(rs.getDouble("stock"));
                    product.setUnitPrice(rs.getDouble("unit_price"));
                    product.setVolumePrice(SQLUtils.getNullable(rs, "volume_price", Double.class)); // Nullable field
                    product.setCategory(rs.getString("category"));
                    product.setBrand(rs.getString("brand"));
                    product.setFkSupplier(rs.wasNull() ? null : rs.getLong("fk_supplier"));
                    return product;
                }
            }
        }
        return null; // Return null if no product is found
    }

}
