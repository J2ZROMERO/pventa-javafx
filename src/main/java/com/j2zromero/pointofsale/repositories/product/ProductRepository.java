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
    public boolean add(Product product) throws SQLException {
        String sql = "{ CALL AddProduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);

             CallableStatement stmt = con.prepareCall(sql)) {
            // All fields, using SQLUtils.setNullable for consistency
            SQLUtils.setNullable(stmt, 1, product.getId(), Types.BIGINT);           // p_id
            SQLUtils.setNullable(stmt, 2, product.getName(), Types.VARCHAR);           // p_name: VARCHAR(100)  // mandatory
            SQLUtils.setNullable(stmt, 3, product.getDescription(), Types.VARCHAR);    // p_description: TEXT  // mandatory
            SQLUtils.setNullable(stmt, 4, product.getCode(), Types.VARCHAR);           // p_code: VARCHAR(100)  // mandatory
            SQLUtils.setNullable(stmt, 5, product.getUnitMeasurement(), Types.INTEGER); // p_unit_measurement: INT  // mandatory
            SQLUtils.setNullable(stmt, 6, product.getUnitPrice(), Types.DOUBLE);       // p_unit_price: DECIMAL(10, 2)  // mandatory
            SQLUtils.setNullable(stmt, 7, product.getFkCategory(), Types.BIGINT);       // p_category: VARCHAR(50)
            SQLUtils.setNullable(stmt, 8, product.getFkBrand(), Types.BIGINT);          // p_brand: VARCHAR(50)
            SQLUtils.setNullable(stmt, 9, product.getFkSupplier(), Types.BIGINT);     // p_fk_supplier: BIGINT
            SQLUtils.setNullable(stmt, 10, product.getPackagePrice(), Types.BIGINT);     // p_fk_supplier: BIGINT
            SQLUtils.setNullable(stmt, 11, product.isHasPackageLogic(), Types.BOOLEAN);
            SQLUtils.setNullable(stmt, 12, product.getTotalInPackage(), Types.BIGINT);



            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean alreadyExists = rs.getBoolean("alreadyExists");
                    return alreadyExists;
                }
            }

            return false;
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
                product.setUnitMeasurement(rs.getString("unit_measurement"));
                product.setUnitPrice(rs.getDouble("unit_price"));
                product.setStock(rs.getDouble("stock"));
                product.setFkCategory(rs.getLong("fk_category"));
                product.setFkBrand(rs.getLong("fk_brand"));
                product.setFkSupplier(rs.wasNull() ? null : rs.getLong("fk_supplier"));
                product.setCreatedAt(rs.getDate("created_at"));
                product.setUpdatedAt(rs.getDate("updated_at"));
                product.setSupplierName(rs.getString("supplier_name"));
                product.setPackagePrice(rs.getDouble("package_price"));
                product.setHasPackageLogic(rs.getBoolean("has_package_logic"));
                product.setTotalInPackage(rs.getDouble("total_in_package"));
                product.setCategoryName(rs.getString("category_name"));
                product.setBrandName(rs.getString("brand_name"));

                products.add(product);
            }
        }
        return products;
    }

    // Method to update an existing product
    public void update(Product product) throws SQLException {
        String sql = "{ CALL UpdateProduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        System.out.println(product);
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setString(4, product.getCode());
            stmt.setString(5, product.getUnitMeasurement());
            stmt.setObject(6, product.getUnitPrice(), Types.DOUBLE);
            SQLUtils.setNullable(stmt, 7, product.getFkCategory(), Types.BIGINT);
            SQLUtils.setNullable(stmt, 8, product.getFkBrand(), Types.BIGINT);
            SQLUtils.setNullable(stmt, 9, product.getFkSupplier(), Types.BIGINT);
            stmt.setObject(10, product.getPackagePrice());
            stmt.setObject(11, product.isHasPackageLogic());
            stmt.setObject(12, product.getTotalInPackage());
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
                UnitType product = new UnitType(rs.getInt("id"), rs.getString("name"),rs.getString("code"));
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
                    product.setUnitMeasurement(rs.getString("unit_measurement"));
                    product.setStock(rs.getDouble("stock"));
                    product.setUnitPrice(rs.getDouble("unit_price"));
                    product.setFkCategory(rs.getLong("fk_category"));
                    product.setFkBrand(rs.getLong("fk_brand"));
                    product.setFkSupplier(rs.wasNull() ? null : rs.getLong("fk_supplier"));
                    product.setHasPackageLogic(rs.wasNull() ? null : rs.getBoolean("has_package_logic"));
                    product.setTotalInPackage(rs.wasNull() ? null : rs.getDouble("total_in_package"));
                    return product;
                }
            }
        }
        return null; // Return null if no product is found
    }

}
