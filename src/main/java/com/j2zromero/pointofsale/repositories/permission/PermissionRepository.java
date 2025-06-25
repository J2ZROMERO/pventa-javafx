package com.j2zromero.pointofsale.repositories.permission;

import com.j2zromero.pointofsale.models.permission.Permission;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionRepository {

    /**
     * Obtiene los permisos asociados a un rol dado, usando el procedimiento almacenado.
     *
     * @param roleName Nombre del rol (e.g. "cajero", "admin").
     * @return Lista de objetos Permission.
     */
    public List<Permission> getPermissionsByRole(String roleName) throws SQLException {
        List<Permission> perms = new ArrayList<>();
        String sql = "{ CALL p_venta.GetPermissionsByRole(?) }";

        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, roleName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Permission p = new Permission();
                    p.setId(rs.getLong("permission_id"));
                    p.setName(rs.getString("permission_name"));
                    p.setDescription(rs.getString("permission_description"));
                    perms.add(p);
                }
            }
        }
        return perms;
    }
}
