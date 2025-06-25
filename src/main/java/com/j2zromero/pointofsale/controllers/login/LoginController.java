package com.j2zromero.pointofsale.controllers.login;

import com.j2zromero.pointofsale.Main;
import com.j2zromero.pointofsale.models.login.Login;
import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.repositories.auth.AuthRepository;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField txtUser;
    @FXML private TextField txtPassword;
    @FXML private Button login;
    @FXML private Label messageLabel;

    private AuthRepository authRepo = new AuthRepository();

    @FXML
    public void authenticate(ActionEvent event) {
        try {
            User currentUser = authRepo.login(txtUser.getText(), txtPassword.getText());
            if (currentUser == null) {
                DialogUtils.showWarningAlert("Error", "Usuario no válido", txtUser);
                txtUser.requestFocus();
                return;
            }
            openMenuModal(event);
        } catch (SQLException e) {
            DialogUtils.showWarningAlert("Error", "No se pudo ingresar", null);
        }
    }

    private void openMenuModal(ActionEvent event) {
        try {
            // Carga del FXML de menú
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/menu/menu.fxml"));
            Parent menuRoot = loader.load();
            Scene menuScene = new Scene(menuRoot);

            // Stage del login
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Nuevo Stage para menú
            Stage menuStage = new Stage();
            menuStage.setTitle("Bienvenido");
            menuStage.initOwner(loginStage);
            menuStage.initModality(Modality.WINDOW_MODAL);
            menuStage.setScene(menuScene);

            // Al cerrar menú, vuelves al login
            menuStage.setOnCloseRequest(e -> {
                e.consume();
                menuStage.close();
                loginStage.show();
            });

            // Ocultas login y abres menú
            loginStage.hide();
            menuStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Si tienes un botón de logout dentro del menú también puedes usarlo */
    @FXML
    public void onLogout(ActionEvent event) {
        Stage menuStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        menuStage.close();
        // recupera el loginStage asociado si lo guardas o reármalo igual que en el closeRequest
    }
}
