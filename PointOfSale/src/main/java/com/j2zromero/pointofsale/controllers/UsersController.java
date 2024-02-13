package com.j2zromero.pointofsale.controllers;

import com.j2zromero.pointofsale.models.User;
import com.j2zromero.pointofsale.utils.MariaDB;
import com.j2zromero.pointofsale.utils.CrudEvent;
import com.j2zromero.pointofsale.utils.VerifyField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

    @FXML


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


        Object inputs[] = {txt_id, txt_nombre, txt_password, roleChoiceBox};
        VerifyField.checkTextFieldChoiceBoxEmpty(inputs);

        if( !getDataInputs(txt_id).toString().equalsIgnoreCase("") &&
            !getDataInputs(txt_nombre).toString().equalsIgnoreCase("") &&
            !getDataInputs(txt_password).toString().equalsIgnoreCase("") &&
            roleChoiceBox.getValue() != null) {

            String choiceB = (String)roleChoiceBox.getValue();
            User.CreateDB(txt_id.getText(),choiceB,txt_nombre.getText(),txt_secondName.getText(),txt_password.getText(),txt_contact.getText());
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







public void KeyPressed_id(){
    VerifyField.checkTextFieldChoiceBoxEmpty(txt_id);
}


    public void KeyPressed_Name(KeyEvent keyEvent) {
        VerifyField.checkTextFieldChoiceBoxEmpty(txt_nombre);
    }

    public void KeyPressed_pass(KeyEvent keyEvent) {
        VerifyField.checkTextFieldChoiceBoxEmpty(txt_password);
    }

    public void handleChoiceBoxAction(ActionEvent actionEvent) {

    VerifyField.checkTextFieldChoiceBoxEmpty(roleChoiceBox);
    }
}
