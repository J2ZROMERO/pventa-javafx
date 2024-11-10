package com.j2zromero.pointofsale.repositories;

import com.j2zromero.pointofsale.models.login.Login;
import com.j2zromero.pointofsale.utils.MariaDB;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class LoginRepository {


    public Login authenticate(String code) {
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement cstm = con.prepareCall("{ CALL punto_de_venta.get_user(?) }")) {

            cstm.setString(1, code);

            try (ResultSet rs = cstm.executeQuery()) {
                if (rs.next()) {
                    String value = rs.getNString("code");
                    return new Login(value);  // Return Login object if found
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to authenticate user. Please check your connection or database query.");
        }

        return new Login();
    }
}
