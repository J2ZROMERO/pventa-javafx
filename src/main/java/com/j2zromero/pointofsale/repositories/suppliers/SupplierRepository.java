package com.j2zromero.pointofsale.repositories.suppliers;

import com.j2zromero.pointofsale.models.suppliers.Supplier;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepository {

    // Método para agregar un nuevo proveedor
    public void add(Supplier supplier) throws SQLException {
        String sql = "{ CALL AddSupplier(?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setInt(1, supplier.getId());
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContact());
            stmt.setString(3, supplier.getDirection());
            stmt.setString(4, supplier.getExtraInformation());
            stmt.execute();
        }
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
                supplier.setContact(rs.getString("contact"));
                supplier.setDirection(rs.getString("direction"));
                supplier.setExtraInformation(rs.getString("extra_information"));
                suppliers.add(supplier);
            }
        }
        return suppliers;
    }

    // Método para actualizar un proveedor
    public void update(Supplier supplier) throws SQLException {
        String sql = "{ CALL UpdateSupplier(?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, supplier.getId());
            stmt.setString(2, supplier.getName());
            stmt.setString(3, supplier.getContact());
            stmt.setString(4, supplier.getDirection());
            stmt.setString(5, supplier.getExtraInformation());
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
