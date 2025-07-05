package com.j2zromero.pointofsale.services;

import com.j2zromero.pointofsale.models.permission.Permission;
import com.j2zromero.pointofsale.repositories.permission.PermissionRepository;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class PermissionService {
    private PermissionRepository repo =  new PermissionRepository();

    public List<Permission> getAll() throws SQLException {
        return repo.getAll();
    }

    public List<Permission> getPermissionsByRole(String roleName) throws SQLException {
    return repo.getPermissionsByRole(roleName);
    }

    public void addRolePermission(long roleId, long permissionId) throws SQLException {
        repo.addRolePermission(roleId,permissionId);
    }


    public void removeRolePermission(long roleId, long permissionId) throws SQLException {
        repo.removeRolePermission(roleId,permissionId);
    }

}
