package com.j2zromero.pointofsale.repositories.inventory;

import com.j2zromero.pointofsale.models.inventories.Inventory;
import com.j2zromero.pointofsale.utils.MariaDB;
import com.j2zromero.pointofsale.utils.SQLUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryRepository {

    // Method to add a new inventory record
    public boolean add(Inventory inventory) throws SQLException {
        String sql = "{ CALL AddInventory(?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1,inventory.getProductCode());
            stmt.setString(2,inventory.getBatchNumber());
            SQLUtils.setNullable(stmt,3, inventory.getAmountEntered(),Types.DOUBLE);
            SQLUtils.setNullable(stmt,4,inventory.getExpirationDate(),Types.DATE);
            stmt.setString(5,inventory.getLocation());
            SQLUtils.setNullable(stmt,6,inventory.getStatus(),Types.DOUBLE);


            return stmt.execute();

}
    }

    // Method to get all inventory records
    public List<Inventory> getAll() throws SQLException {
        List<Inventory> inventories = new ArrayList<>();
        String sql = "{ CALL GetInventory() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Inventory inventory = new Inventory();
                inventory.setId(rs.getLong("id"));
                inventory.setFkProductCode(rs.getString("fk_product_code"));
                inventory.setProductName(rs.getString("product_name"));
                inventory.setAmountEntered(rs.getDouble("amount_entered"));
                inventory.setExpirationDate(rs.getDate("expiration_date"));
                inventory.setLocation(rs.getString("location"));
                inventory.setBatchNumber(rs.getString("batch_number"));
                inventory.setCreatedAt(rs.getDate("created_at"));
                inventory.setUpdatedAt(rs.getDate("updated_at"));
                inventory.setStatus(rs.getBoolean("status"));
                inventory.setUnitType(rs.getString("unit_type"));
                inventories.add(inventory);
            }
        }
        return inventories;
    }

    // Method to update an existing inventory record
    public void update(Inventory inventory) throws SQLException {
        String sql = "{ CALL UpdateInventory(?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
            CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1,inventory.getId());
            stmt.setString(2,inventory.getFkProductCode());

            stmt.setBigDecimal(3, BigDecimal.valueOf(inventory.getAmountEntered()).setScale(3, RoundingMode.HALF_UP));
            SQLUtils.setNullable(stmt,4,inventory.getExpirationDate(),Types.DATE);
            stmt.setString(5,inventory.getLocation());
            stmt.setString(6,inventory.getBatchNumber());
            stmt.setBoolean(7, inventory.getStatus());
            stmt.execute();

        }
    }

    // Method to delete an inventory record by ID
    public void delete(long id) throws SQLException {
        String sql = "{ CALL DeleteInventory(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, id);
            stmt.execute();
        }
    }

    public Inventory getInventorByProductCode(String productCode) throws SQLException {
        String sql = "{ CALL GetInventoryByProductCode(?) }";
        Inventory inventory = new Inventory();

        try (
                Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
                CallableStatement stmt = con.prepareCall(sql)
        ) {
            stmt.setString(1, productCode);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    inventory.setId(rs.getLong("id"));
                    inventory.setFkProductCode(rs.getString("fk_product_code"));
                    inventory.setAmountEntered(rs.getDouble("amount_entered"));
                    inventory.setExpirationDate(rs.getDate("expiration_date"));
                    inventory.setLocation(rs.getString("location"));
                    inventory.setBatchNumber(rs.getString("batch_number"));
                    inventory.setCreatedAt(rs.getDate("created_at"));
                    inventory.setUpdatedAt(rs.getDate("updated_at"));
                    inventory.setStatus(rs.getBoolean("status"));
                    inventory.setUnitType(rs.getString("fk_unit_types"));
                }
            }
        }

        return inventory;
    }

}
