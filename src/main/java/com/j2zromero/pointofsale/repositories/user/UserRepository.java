package com.j2zromero.pointofsale.repositories.user;

import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    // Agregar un nuevo usuario
    public void add(User user) throws SQLException {
        String sql = "{ CALL AddUser(?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getFkRoleCode());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setBoolean(5, user.getStatus());
            stmt.setString(6, user.getPassword());

            stmt.execute();
        }
    }

    // Obtener todos los usuarios
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "{ CALL GetAllUsers() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setFkRoleCode(rs.getString("fk_role_code"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setStatus(rs.getBoolean("status"));
                user.setCreatedAt(rs.getDate("created_at"));
                user.setUpdatedAt(rs.getDate("updated_at"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        }
        return users;
    }

    // Actualizar un usuario
    public void update(User user) throws SQLException {
        String sql = "{ CALL UpdateUser(?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setLong(1, user.getId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getFkRoleCode());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhone());
            stmt.setBoolean(6, user.getStatus());
            stmt.setString(7, user.getPassword());

            stmt.execute();
        }
    }

    // Eliminar un usuario por ID
    public void delete(long id) throws SQLException {
        String sql = "{ CALL DeleteUser(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setLong(1, id);
            stmt.execute();
        }
    }
}
