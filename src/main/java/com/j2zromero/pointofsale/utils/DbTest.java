package com.j2zromero.pointofsale.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbTest {

    public static void Test() throws SQLException {
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password)) {
            if (con == null || con.isClosed()) {
                throw new SQLException("Failed to establish connection.");
            }
            System.out.println("Connection to the database was successful!");
        }
    }
}
