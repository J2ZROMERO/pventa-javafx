package com.j2zromero.pointofsale.repositories.auth;

import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.*;

public class AuthRepository {
    /**
     * Llama al stored procedure p_venta.LoginApp para autenticar.
     * @param email    correo del usuario
     * @param password contraseña
     * @return Usuario completo si coincide, o null si falla autenticación
     * @throws SQLException error en BD
     */
    public User login(String email, String password) throws SQLException {
        String sql = "{ CALL LoginApp(?, ?) }";
        try (Connection con = DriverManager.getConnection(
                MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getLong("id"));
                    u.setName(rs.getString("name"));
                    u.setFkRoleCode(rs.getString("fk_role_code"));
                    u.setEmail(rs.getString("email"));
                    u.setPhone(rs.getString("phone"));
                    u.setStatus(rs.getBoolean("status"));
                    u.setCreatedAt(rs.getDate("created_at"));
                    u.setUpdatedAt(rs.getDate("updated_at"));
                    return u;
                }
            }
        }
        return null;
    }
}