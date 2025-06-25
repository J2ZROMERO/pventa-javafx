package com.j2zromero.pointofsale.services.user;
import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.repositories.user.UserRepository;


import java.sql.SQLException;
import java.util.List;

public class UserService {

    private UserRepository userRepository = new UserRepository();

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
