package com.j2zromero.pointofsale.controllers;

import com.j2zromero.pointofsale.utils.CrudDB;
import com.j2zromero.pointofsale.utils.MariaDB;
import com.j2zromero.pointofsale.utils.CrudEvent;
import com.j2zromero.pointofsale.utils.verifyField;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UsersController extends Application implements CrudEvent {
    @FXML // this loader will inyect all of the values in this controller
    public TextField txt_id;
    public TextField txt_nombre;
    public TextField txt_secondName;
    public TextField txt_password;
    public ChoiceBox roleChoiceBox;

    public TextField txt_contact;


    public Object getDataInputs(Node inputElement) {
        if (inputElement instanceof TextField) {
            return (String)((TextField) inputElement).getText();
        } else if (inputElement instanceof ChoiceBox) {
            return (String)((ChoiceBox<?>) inputElement).getValue();
        }
        // Handle other cases or return a default value
        return null;
    }



    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UsersController.class.getResource("/com/j2zromero/pointofsale/user/user.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Handle the exception (e.g., log it or show an error dialog)
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }


    @Override
    public void CreateEvent() throws SQLException {

        if( getDataInputs(txt_id).toString().equalsIgnoreCase("") &&
            getDataInputs(txt_nombre).toString().equalsIgnoreCase("") &&
            getDataInputs(txt_password).toString().equalsIgnoreCase("")) {

            verifyField.checkTextField(txt_id);
            verifyField.checkTextField(txt_nombre);
            verifyField.checkTextField(txt_password);
            verifyField.checkChoiceBox(roleChoiceBox);

            System.out.println();
        }else{
            String choiceB = (String)roleChoiceBox.getValue();
            CreateDB(txt_id.getText(),choiceB,txt_nombre.getText(),txt_secondName.getText(),txt_password.getText(),txt_contact.getText());
        }


    }
    @Override
    public void ReadEvent() {
        System.out.println("leer");
    }

    @Override
    public void UpdateEvent() {
        System.out.println("actualizar");
    }

    @Override
    public void DeleteEvent() {
        System.out.println("eliminado");
    }

    public void Keyboard(KeyEvent e){

    }



    public void CreateDB(String id,String rol, String name, String lastName,String pass,String contact) throws SQLException {

        try(Connection con = DriverManager.getConnection(MariaDB.URL,MariaDB.user,MariaDB.password);
            CallableStatement cstm = con.prepareCall("{ CALL PuntoDeVenta.add_user( ?,?,?,?,?,?) }"))   // dentro statement connection and resulset
        {
            cstm.setString( 1,id);
            cstm.setString( 2, rol);
            cstm.setString( 3,name);
            cstm.setString( 4, lastName);
            cstm.setString( 5,pass);
            cstm.setString( 6,contact);

            cstm.executeUpdate();
            System.out.println("datos insertados");
        } catch (SQLException e){
            e.printStackTrace();
        }

    }


    public void ReadDB() {

    }


    public void UpdateDB() {

    }


    public void DeleteDB() {

    }
}
