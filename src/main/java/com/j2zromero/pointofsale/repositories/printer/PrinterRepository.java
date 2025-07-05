package com.j2zromero.pointofsale.repositories.printer;

import com.j2zromero.pointofsale.models.printer.LocalPrinter;
import com.j2zromero.pointofsale.utils.MariaDB;
import com.j2zromero.pointofsale.utils.SQLUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrinterRepository {


    /** Agrega una nueva impresora llamando al procedimiento almacenado */
    public void add(LocalPrinter printer) throws SQLException {
        String sql = "{ CALL p_venta.AddPrinter(?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, printer.getName());
            cs.setString(2, printer.getDescription());
            SQLUtils.setNullable(cs, 3, printer.getEnterpriseName(), Types.VARCHAR);
            SQLUtils.setNullable(cs, 4, printer.getAddress(), Types.VARCHAR);
            cs.execute();  // o executeUpdate()
        }
    }

    /** Leer todas las impresoras */
    public List<LocalPrinter> getAll() throws SQLException {
        List<LocalPrinter> list = new ArrayList<>();
        String sql = "SELECT id, name, description, created_at, updated_at FROM printers";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LocalPrinter p = new LocalPrinter();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(p);
            }
        }
        return list;
    }

    /** Encontrar por id */
    public LocalPrinter findById(String id) throws SQLException {
        String sql = "call AddPrinter( ?, ?)";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id );
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalPrinter p = new LocalPrinter();
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    return p;
                }
            }
        }
        return null;
    }


    public LocalPrinter getLocalPrinter() throws SQLException {
        String sql = "{ CALL GetPrinter() }";
        try (Connection con = DriverManager.getConnection(
                MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement cs = con.prepareCall(sql)) {

            // Ejecutamos la consulta
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    LocalPrinter lp = new LocalPrinter();
                    // Ajusta los nombres de columna según tu tabla
                    lp.setId(rs.getLong("id"));
                    lp.setName(rs.getString("name"));
                    lp.setAddress(rs.getString("address"));
                    lp.setEnterpriseName(rs.getString("enterprise_name"));
                    lp.setDescription(rs.getString("description"));
                    lp.setCreatedAt(rs.getTimestamp("created_at"));
                    lp.setUpdatedAt(rs.getTimestamp("updated_at"));
                    System.out.println(lp.getEnterpriseName());
                    return lp;
                }
            }
        }
        return null;
    }



}
