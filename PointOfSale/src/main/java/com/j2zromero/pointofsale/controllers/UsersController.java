package com.j2zromero.pointofsale.controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class UsersController extends Application {

    @FXML
    private Button add;

    @FXML
    private void eventKey(KeyEvent e){
        System.out.println("algo algo");
    }

    @FXML
    private void eventAction(ActionEvent e){

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
