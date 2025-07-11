// ------- LoginController.java -------
package com.j2zromero.pointofsale.controllers.login;
import com.j2zromero.pointofsale.Main;
import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.services.auth.AuthService;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {

    public AnchorPane anchorLogin;
    public AnchorPane login_container;
    @FXML private TextField txtUser;
    @FXML private TextField txtPassword;
    @FXML private Button login;
    @FXML private Label messageLabel;

    private AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (login_container.getScene() != null) {
                login_container.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
        // 1) Once the scene is ready, give focus to the email field
        Platform.runLater(() -> txtUser.requestFocus());

        // 2) Pressing Enter in either field will run authenticate(...)
        txtUser.setOnAction(this::authenticate);
        txtPassword.setOnAction(this::authenticate);

        // 3) Mark your login button as the default button,
        //    so that Enter anywhere also “clicks” it
        login.setDefaultButton(true);

    }

    @FXML
    public void authenticate(ActionEvent event) {
        try {
            User currentUser = authService.login(txtUser.getText(), txtPassword.getText());
            if (currentUser == null) {
                txtPassword.clear();
                DialogUtils.showWarningAlert("Error", "Usuario o contraseña no válidos", txtPassword);
                txtUser.requestFocus();
                return;
            }
            UserService.loadPermissionsByRole(currentUser);
            openCajaThenMenu(event);
        } catch (SQLException e) {
            DialogUtils.showWarningAlert("Error", "No se pudo ingresar", null);
        }
    }

    private void openCajaThenMenu(ActionEvent event) {
        try {
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 1) Modal apertura de caja
            FXMLLoader cajaLoader = new FXMLLoader(Main.class.getResource("/views/caja/caja.fxml"));
            Parent cajaRoot = cajaLoader.load();
            Stage cajaStage = new Stage();
            cajaStage.setTitle("Apertura de Caja");
            cajaStage.initOwner(loginStage);
            cajaStage.initModality(Modality.WINDOW_MODAL);
            cajaStage.setScene(new Scene(cajaRoot));
            cajaStage.setResizable(false);
            cajaStage.showAndWait();
            loginStage.hide();



        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void onLogout(ActionEvent event) {
        Stage menuStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        menuStage.close();
        showLogin(menuStage);
    }

    public void showLogin(Stage stage) {
        try {
            Parent loginRoot = new FXMLLoader(Main.class.getResource("/views/login/login.fxml")).load();
            stage.setTitle("Login");
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
