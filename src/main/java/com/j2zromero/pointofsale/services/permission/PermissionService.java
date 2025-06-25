package com.j2zromero.pointofsale.services.permission;

import com.j2zromero.pointofsale.models.permission.Permission;
import com.j2zromero.pointofsale.models.role.Role;
import com.j2zromero.pointofsale.repositories.permission.PermissionRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionService {
    private static PermissionRepository permissionRepository = new PermissionRepository();
        private static Set<String> currentPermissions = new HashSet<>();

        /**
         * Llamar una sola vez después de hacer login.
         * roleNames viene de tu User.getFkRoleCode()
         */
        public static void loadPermissionsForRoles(List<Role> roles) {
            currentPermissions.clear();
            for (Role r : roles) {
                currentPermissions.addAll(fetchPermissionsFromDb(r.getName()));
            }
        }

        public static boolean has(String permission) {
            return currentPermissions.contains(permission);
        }

    private static List<String> fetchPermissionsFromDb(String roleName) {
        try {
            List<Permission> perms = permissionRepository.getPermissionsByRole(roleName);
            List<String> names = new ArrayList<>();
            for (Permission p : perms) {
                names.add(p.getName());
            }
            return names;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
