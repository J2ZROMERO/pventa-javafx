package com.j2zromero.pointofsale.services.user;
import com.j2zromero.pointofsale.models.caja.Caja;
import com.j2zromero.pointofsale.models.permission.Permission;
import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.repositories.permission.PermissionRepository;
import com.j2zromero.pointofsale.repositories.user.UserRepository;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserService {

    private UserRepository userRepository = new UserRepository();
    private static Set<String> currentPermissions = new HashSet<>();
    private static User user;
    private static PermissionRepository permissionRepository = new PermissionRepository();
    private static Long cajaId;

    public static User getUser() {
        return user;
    }

    public static Long getCajaId() {
        return cajaId;
    }

    public static void setCajaId(Long cajaId) {
        UserService.cajaId = cajaId;
    }

    private static void setUser(User user) {
        UserService.user = user;
    }


    /**
     * Llamar una sola vez después de hacer login.
     * roleNames viene de tu User.getFkRoleCode()
     */
    public static void loadPermissionsByRole(User userLoaded) {
        currentPermissions.clear();
        user = userLoaded;
        currentPermissions.addAll(fetchPermissionsFromDb(user.getFkRoleCode()));
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



    // Agregar un nuevo usuario
    public void add(User user) throws SQLException {
        userRepository.add(user);
    }

    // Obtener todos los usuarios
    public List<User> getAll() throws SQLException {
        return userRepository.getAll();
    }

    // Actualizar usuario existente
    public void update(User user) throws SQLException {
        userRepository.update(user);
    }

    // Eliminar un usuario por ID
    public void delete(long id) throws SQLException {
        userRepository.delete(id);
    }
}
