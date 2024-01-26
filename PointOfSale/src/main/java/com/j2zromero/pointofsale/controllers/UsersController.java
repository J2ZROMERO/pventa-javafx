package com.j2zromero.pointofsale.controllers;

import com.j2zromero.pointofsale.utils.GenericValue;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class UsersController extends Application {
    @FXML // this loader will inyect all of the values in this controller
    public TextField txt_id;
    public TextField txt_rol;
    public TextField txt_nombre;
    public TextField txt_apellido;
    public TextField txt_password;
    public ChoiceBox roleChoiceBox;


    public void getData(ActionEvent e){


    }

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
}
