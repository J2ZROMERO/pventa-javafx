package com.j2zromero.pointofsale.controllers.caja;

import com.j2zromero.pointofsale.models.caja.Caja;
import com.j2zromero.pointofsale.models.terminal.Terminal;
import com.j2zromero.pointofsale.services.caja.CajaService;
import com.j2zromero.pointofsale.services.permission.PermissionService;
import com.j2zromero.pointofsale.services.terminal.TerminalService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CajaController {

    public TextField txtNotes;
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
        FormUtils.applyNumericDoubleFilter(txtOpeningAmount);
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
        // Aquí podrías parsear el monto y guardarlo en BD con tu CajaService.
        PermissionService.getUser();

        try {
         Terminal terminal = terminalService.getAll().get(0);
            Caja caja = new Caja();
            caja.setCashierId(PermissionService.getUser().getId());
            caja.setTerminalId(terminal.getId());
            caja.setNotes(txtNotes.getText());
            caja.setOpeningAmount(Double.parseDouble(txtOpeningAmount.getText()));
            cajaService.openCaja(caja);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Luego cierras la ventana de caja:
        stage.close();
    }
}
