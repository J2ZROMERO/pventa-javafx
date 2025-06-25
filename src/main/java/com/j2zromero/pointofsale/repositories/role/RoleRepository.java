package com.j2zromero.pointofsale.repositories.role;
import com.j2zromero.pointofsale.models.role.Role;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleRepository {

    /**
     * Obtiene todos los roles de la base de datos.
     */
    public List<Role> getAll() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT id, name, description, created_at, updated_at FROM roles";

        try (Connection con = DriverManager.getConnection(
                MariaDB.URL, MariaDB.user, MariaDB.password);
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setName(rs.getString("name"));
                role.setDescription(rs.getString("description"));
                role.setCreatedAt(rs.getTimestamp("created_at"));
                role.setUpdatedAt(rs.getTimestamp("updated_at"));
                roles.add(role);
            }
        }
        return roles;
    }
}
