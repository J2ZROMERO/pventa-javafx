package com.j2zromero.pointofsale.services.role;


import com.j2zromero.pointofsale.models.role.Role;
import com.j2zromero.pointofsale.repositories.role.RoleRepository;

import java.sql.SQLException;
import java.util.List;

public class RoleService {
    private RoleRepository repository = new RoleRepository();

    /**
     * Obtiene todos los roles desde la base de datos.
     */
    public List<Role> getAll() throws SQLException {
        return repository.getAll();
    }
}