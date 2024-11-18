package com.j2zromero.pointofsale.repositories.inventory;

import com.j2zromero.pointofsale.models.inventories.Inventory;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryRepository {

    // Method to add a new inventory record
    public void add(Inventory inventory) throws SQLException {
        String sql = "{ CALL AddInventory(?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, inventory.getFkProduct());
            stmt.setDate(2, new java.sql.Date(inventory.getEntryDate().getTime()));
            stmt.setDouble(3, inventory.getAmountEntered());
            stmt.setDouble(4, inventory.getAmountAvailable());
            stmt.setDate(5, inventory.getExpirationDate() != null ? new java.sql.Date(inventory.getExpirationDate().getTime()) : null);
            stmt.setString(6, inventory.getLocation());
            stmt.execute();
        }
    }

    // Method to get all inventory records
    public List<Inventory> getAll() throws SQLException {
        List<Inventory> inventories = new ArrayList<>();
        String sql = "{ CALL GetAllInventories() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Inventory inventory = new Inventory();
                inventory.setId(rs.getLong("id"));
                inventory.setFkProduct(rs.getLong("fk_product"));
                inventory.setEntryDate(rs.getDate("entry_date"));
                inventory.setAmountEntered(rs.getDouble("amount_entered"));
                inventory.setAmountAvailable(rs.getDouble("amount_available"));
                inventory.setExpirationDate(rs.getDate("expiration_date"));
                inventory.setLocation(rs.getString("location"));
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
            stmt.setLong(1, inventory.getId());
            stmt.setLong(2, inventory.getFkProduct());
            stmt.setDate(3, new java.sql.Date(inventory.getEntryDate().getTime()));
            stmt.setDouble(4, inventory.getAmountEntered());
            stmt.setDouble(5, inventory.getAmountAvailable());
            stmt.setDate(6, inventory.getExpirationDate() != null ? new java.sql.Date(inventory.getExpirationDate().getTime()) : null);
            stmt.setString(7, inventory.getLocation());
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
