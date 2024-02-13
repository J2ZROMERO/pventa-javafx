package com.j2zromero.pointofsale.models;

import com.j2zromero.pointofsale.utils.CrudDB;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class User {

    public static boolean CreateDB(String id, String rol, String name, String lastName, String pass, String contact) throws SQLException {
            boolean success = false;

            if(!(id.equalsIgnoreCase("") && rol.equalsIgnoreCase("") && name.equalsIgnoreCase("") && pass.equalsIgnoreCase(""))){

                try(Connection con = DriverManager.getConnection(MariaDB.URL,MariaDB.user,MariaDB.password);
                    CallableStatement cstm = con.prepareCall("{ CALL PuntoDeVenta.add_user( ?,?,?,?,?,?) }"))   // dentro statement connection and resulset
                {
                    cstm.setString( 1,id);
                    cstm.setString( 2,rol);
                    cstm.setString( 3,name);
                    cstm.setString( 4,lastName);
                    cstm.setString( 5,pass);
                    cstm.setString( 6,contact);

                    cstm.executeUpdate();
                    success = true;
                    System.out.println("datos insertados");
                } catch (SQLException e){
                    //e.printStackTrace();
                    System.out.println("DB erros");

                }
            }

        return success;
        }





    public static void createDB(String id, String rol, String name, String secondName, String pass, String contact) throws SQLException {

    }


    public static void ReadDB() {

    }


    public  static void UpdateDB() {

    }


    public static void DeleteDB() {

    }
}
