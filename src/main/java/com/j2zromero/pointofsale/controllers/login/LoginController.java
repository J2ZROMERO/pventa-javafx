package com.j2zromero.pointofsale.controllers.login;

import com.j2zromero.pointofsale.Main;
import com.j2zromero.pointofsale.models.login.Login;
import com.j2zromero.pointofsale.services.login.LoginService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class LoginController {

    private LoginService loginService = new LoginService();

    @FXML
    private TextField txt_identifier;  // Assuming you have a username field in your FXML

    @FXML
    private Label messageLabel;         // Assuming you have a label to show error messages

    // Method triggered on a button click event for authentication
    public void authenticate(ActionEvent event) {
        // Authenticate user
        Login test = loginService.authenticate(txt_identifier.getText());

        if (test.getCode() != null) {
            openMenu(event);  // Pass the event to openMenu
        } else {
            messageLabel.setText("Contraseña o Correo Erroneo");
        }
    }

    public void openMenu(ActionEvent event) {
        try {
            // Load the FXML file for the next view
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/menu/menu.fxml"));

            Parent menuRoot = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(menuRoot);

            // Retrieve the current stage from the event
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setTitle("Bienvenido");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
