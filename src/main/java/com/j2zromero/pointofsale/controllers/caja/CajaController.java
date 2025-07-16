package com.j2zromero.pointofsale.controllers.caja;

import com.j2zromero.pointofsale.Main;
import com.j2zromero.pointofsale.models.caja.Caja;
import com.j2zromero.pointofsale.models.terminal.Terminal;
import com.j2zromero.pointofsale.services.caja.CajaService;
import com.j2zromero.pointofsale.services.terminal.TerminalService;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class CajaController {

    public TextArea txtNotes;
    @FXML private Button btnConfirm;
    @FXML private TextField txtOpeningAmount;
    @FXML private AnchorPane cajaContainer;

    private CajaService cajaService = new CajaService();
    private TerminalService terminalService = new TerminalService();
    /**
     * Inicializa el controlador: habilita el botón sólo si hay monto.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (cajaContainer.getScene() != null) {
                cajaContainer.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
        // numeric‐only filter
        FormUtils.applyNumericDoubleFilter(txtOpeningAmount);

        // 1) Request focus once the scene is up
        Platform.runLater(() -> txtOpeningAmount.requestFocus());

        // 2) Pressing Enter in the amount field fires your confirm(...)
        txtOpeningAmount.setOnAction(this::confirm);

        // 3) Mark your “Confirm” button as the default so Enter anywhere also clicks it
        btnConfirm.setDefaultButton(true);
    }

    /**
     * Al confirmar apertura, procesa el monto y cierra la ventana de caja.
     */
    @FXML
    public void confirm(ActionEvent event) {
        if(txtOpeningAmount.getText().trim().isEmpty()){
            DialogUtils.showWarningAlert("Error", "Debes de agregar un monto inicial", txtOpeningAmount);
            return;

        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        UserService.getUser();

        try {
         Terminal terminal = terminalService.getAll().get(0);
            Caja caja = new Caja();
            caja.setCashierId(UserService.getUser().getId());
            caja.setTerminalId(terminal.getId());
            caja.setNotes(txtNotes.getText());
            caja.setOpeningAmount(Double.parseDouble(txtOpeningAmount.getText()));
            Long ie = cajaService.openCaja(caja);
            UserService.setCajaId(ie);
            openCajaThenMenu(event);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stage.close();
    }
    private void openCajaThenMenu(ActionEvent event) {
        try {
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.hide();

    FXMLLoader menuLoader = new FXMLLoader(Main.class.getResource("/views/menu/menu.fxml"));
            Parent menuRoot = menuLoader.load();
            Stage menuStage = new Stage();
            menuStage.setTitle("Bienvenido");
            menuStage.initModality(Modality.WINDOW_MODAL);
            menuStage.setScene(new Scene(menuRoot));
            menuStage.show();
            menuStage.setOnCloseRequest(e -> {
                e.consume();
                DialogUtils.showWarningAlert(
                        "Caja Abierta",
                        "Debe cerrar la caja antes de salir del sistema.",
                        null
                );

            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
