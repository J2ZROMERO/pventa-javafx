package com.j2zromero.pointofsale.repositories.inventory;

import com.j2zromero.pointofsale.models.inventories.Inventory;
import com.j2zromero.pointofsale.utils.MariaDB;
import com.j2zromero.pointofsale.utils.SQLUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryRepository {

    // Method to add a new inventory record
    public void add(Inventory inventory) throws SQLException {
        String sql = "{ CALL AddInventory(?, ?, ?, ?, ?, ?, ?) }";
        System.out.println(inventory.getProductCode());
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            //stmt.setLong(1, inventory.getFkProductCode());
            SQLUtils.setNullable(stmt,3, inventory.getAmountEntered(),Types.DOUBLE);
            SQLUtils.setNullable(stmt,4, inventory.getAmountAvailable(),Types.DOUBLE);
            stmt.setDate(5, inventory.getExpirationDate() != null ? new java.sql.Date(inventory.getExpirationDate().getTime()) : null);
            stmt.setString(6, inventory.getLocation());
            stmt.setString(7, inventory.getProductCode()); // Added the missing parameter
            stmt.execute();

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
                inventory.setProduct_name(rs.getString("product_name"));
                inventory.setAmountEntered(rs.getDouble("amount_entered"));
                inventory.setAmountAvailable(rs.getDouble("amount_available"));
                inventory.setExpirationDate(rs.getDate("expiration_date"));
                inventory.setLocation(rs.getString("location"));
                inventory.setLocation(rs.getString("batch_number"));
                inventory.setCreated_at(rs.getDate("created_at"));
                inventory.setCreated_at(rs.getDate("updated_at"));
                inventory.setProductCode(rs.getString("status"));
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
            //stmt.setLong(1, inventory.getFkProductCode());
            stmt.setString(2, inventory.getBatch_number());
            SQLUtils.setNullable(stmt,3, inventory.getAmountEntered(),Types.DOUBLE);
            SQLUtils.setNullable(stmt,4, inventory.getAmountAvailable(),Types.DOUBLE);
            SQLUtils.setNullable(stmt, 5,
                    inventory.getExpirationDate() != null ? new java.sql.Date(inventory.getExpirationDate().getTime()) : null,
                    java.sql.Types.DATE);
            stmt.setString(6, inventory.getLocation());
            stmt.setString(7,inventory.getStatus());

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
}
