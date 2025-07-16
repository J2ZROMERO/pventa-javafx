package com.j2zromero.pointofsale.repositories.suppliers;

import com.j2zromero.pointofsale.models.suppliers.Supplier;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepository {

    // Método para agregar un nuevo proveedor
    public boolean add(Supplier supplier) throws SQLException {
        String sql = "{ CALL AddSupplier(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setInt(1, supplier.getId());
            stmt.setString(2, supplier.getName());
            stmt.setString(3, supplier.getCode());
            stmt.setString(4, supplier.getContact());
            stmt.setString(5, supplier.getContactName());
            stmt.setString(6, supplier.getContactPhone());
            stmt.setString(7, supplier.getDirection());
            stmt.setString(8, supplier.getCity());
            stmt.setString(9, supplier.getExtraInformation());
            stmt.setBoolean(10, supplier.isStatus());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("alreadyExists");
                }
            }
        }
        return false;
    }

    // Método para obtener todos los proveedores
    public List<Supplier> getAll() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "{ CALL GetAllSuppliers() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setId(rs.getInt("id"));
                supplier.setName(rs.getString("name"));
                supplier.setCode(rs.getString("code"));
                supplier.setContact(rs.getString("contact"));
                supplier.setContactName(rs.getString("contact_name"));
                supplier.setContactPhone(rs.getString("contact_phone"));
                supplier.setDirection(rs.getString("direction"));
                supplier.setCity(rs.getString("city"));
                supplier.setExtraInformation(rs.getString("extra_information"));
                supplier.setStatus(rs.getBoolean("status"));
                // Manejo de timestamps a java.sql.Date
                Timestamp createdTs = rs.getTimestamp("created_at");
                Timestamp updatedTs = rs.getTimestamp("updated_at");
                supplier.setCreated_at(createdTs != null ? new java.sql.Date(createdTs.getTime()) : null);
                supplier.setUpdated_at(updatedTs != null ? new java.sql.Date(updatedTs.getTime()) : null);
                suppliers.add(supplier);
            }
        }
        return suppliers;
    }

    // Método para actualizar un proveedor
    public void update(Supplier supplier) throws SQLException {
        String sql = "{ CALL UpdateSupplier(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setInt(1, supplier.getId());
            stmt.setString(2, supplier.getName());
            stmt.setString(3, supplier.getCode());
            stmt.setString(4, supplier.getContact());
            stmt.setString(5, supplier.getContactName());
            stmt.setString(6, supplier.getContactPhone());
            stmt.setString(7, supplier.getDirection());
            stmt.setString(8, supplier.getCity());
            stmt.setString(9, supplier.getExtraInformation());
            stmt.setBoolean(10, supplier.isStatus());
            stmt.execute();
        }
    }

    // Método para eliminar un proveedor por ID
    public void delete(long id) throws SQLException {
        String sql = "{ CALL DeleteSupplier(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, id);
            stmt.execute();
        }
    }
}
