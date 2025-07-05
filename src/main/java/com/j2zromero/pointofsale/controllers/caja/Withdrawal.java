package com.j2zromero.pointofsale.controllers.caja;

import com.j2zromero.pointofsale.services.caja.CajaService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class Withdrawal {
    public TextField txtAmount;
    public TextArea txtMotive;
    private CajaService cajaService  = new CajaService();

    @FXML
    private void initialize() throws SQLException {
        FormUtils.applyNumericOnlyFilter(txtAmount);
    }

    public void add(){
        if (txtAmount.getText().trim().isEmpty()){
            DialogUtils.showWarningAlert("Error", "Debes de agregar monto de retiro", txtAmount);
            return;
        }
        if (txtAmount.getText().trim().isEmpty()){
            DialogUtils.showWarningAlert("Error", "Debes de agregar motivo de retiro", txtMotive);
            return;
        }

        try {
            cajaService.addWithdrawal(Double.parseDouble(txtAmount.getText()), txtMotive.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }





    public void add(ActionEvent actionEvent) {
    }
}
