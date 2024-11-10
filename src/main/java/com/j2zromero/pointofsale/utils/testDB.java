package com.j2zromero.pointofsale.utils;

import java.sql.*;

public class testDB {

    public static void main(String args[]) throws SQLException {
        // Try to establish the connection
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password)) {
            if (con != null) {
                System.out.println("Connection to the database was successful!");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace(); // This will print the exact error if the connection fails
        }
    }



}

// considerar que hay variables sql en java
// usamos try-with para asegurarnos de cerrar los recursos automaticamente
// es mejor usar el llamador prepared statement ya que no compila  y luego a cache en el SGBD sino pasa parametros directos y ejecuta