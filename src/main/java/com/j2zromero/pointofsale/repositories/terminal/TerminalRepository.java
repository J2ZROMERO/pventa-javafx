package com.j2zromero.pointofsale.repositories.terminal;

import com.j2zromero.pointofsale.models.terminal.Terminal;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TerminalRepository {
    private static final String SCHEMA = "p_venta";

    /**
     * Obtiene todos los terminales de la base de datos.
     */
    public List<Terminal> getAll() throws SQLException {
        List<Terminal> list = new ArrayList<>();
        String sql = "SELECT id, code, location, description, created_at, updated_at FROM " + SCHEMA + ".terminals";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Terminal t = new Terminal();
                t.setId(rs.getLong("id"));
                t.setCode(rs.getString("code"));
                t.setLocation(rs.getString("location"));
                t.setDescription(rs.getString("description"));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(t);
            }
        }
        return list;
    }

    /**
     * Inserta un nuevo terminal y retorna el ID generado.
     */
    public Long add(Terminal terminal) throws SQLException {
        String sql = "INSERT INTO " + SCHEMA + ".terminals(code, location, description) VALUES(?, ?, ?)";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, terminal.getCode());
            stmt.setString(2, terminal.getLocation());
            stmt.setString(3, terminal.getDescription());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    Long id = keys.getLong(1);
                    terminal.setId(id);
                    return id;
                }
            }
        }
        return null;
    }

    /**
     * Actualiza un terminal existente.
     */
    public void update(Terminal terminal) throws SQLException {
        String sql = "UPDATE " + SCHEMA + ".terminals SET code=?, location=?, description=? WHERE id=?";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, terminal.getCode());
            stmt.setString(2, terminal.getLocation());
            stmt.setString(3, terminal.getDescription());
            stmt.setLong(4, terminal.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina un terminal por ID.
     */
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM " + SCHEMA + ".terminals WHERE id = ?";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
