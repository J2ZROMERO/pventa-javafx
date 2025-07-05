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
        String sql = "{ CALL GetPermissionsByRole(?) }";

        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, roleName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Permission p = new Permission();
                    p.setId(rs.getLong("permission_id"));
                    p.setName(rs.getString("permission_name"));
                    p.setDescription(rs.getString("permission_description"));
                    p.setRoleId(rs.getLong("role_id"));
                    perms.add(p);
                }
            }
        }
        return perms;
    }


    public List<Permission> getAll() throws SQLException {
        List<Permission> perms = new ArrayList<>();
        String sql = "{ CALL GetPermission() }";

        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Permission p = new Permission();
                    p.setId(rs.getLong("id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    perms.add(p);
                }
            }
        }
        return perms;
    }


    /**
     * Asigna un permiso a un rol usando el procedimiento almacenado AddRolePermission.
     *
     * @param roleId       ID del rol.
     * @param permissionId ID del permiso.
     * @throws SQLException en caso de error en la base de datos.
     */
    public void addRolePermission(long roleId, long permissionId) throws SQLException {
        String sql = "{ CALL AddRolePermission(?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, roleId);
            stmt.setLong(2, permissionId);
            stmt.executeUpdate();
        }
    }

    /**
     * Revoca un permiso de un rol usando el procedimiento almacenado RemoveRolePermission.
     *
     * @param roleId       ID del rol.
     * @param permissionId ID del permiso.
     * @throws SQLException en caso de error en la base de datos.
     */
    public void removeRolePermission(long roleId, long permissionId) throws SQLException {
        String sql = "{ CALL RemoveRolePermission(?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, roleId);
            stmt.setLong(2, permissionId);
            stmt.executeUpdate();
        }
    }



}
