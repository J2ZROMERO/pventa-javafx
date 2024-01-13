package com.j2zromero.pointofsale.utils;

import java.sql.*;

public class test {

    public static void main(String args[]) throws SQLException {
        String sqlQeury = "select count(*) from information_schema.tables where table_schema='PuntoDeVenta'";

        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQeury)) {
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }

        }
    }

}

// considerar que hay variables sql en java
// usamos try-with para asegurarnos de cerrar los recursos automaticamente
// es mejor usar el llamador prepared statement ya que no compila  y luego a cache en el SGBD sino pasa parametros directos y ejecuta