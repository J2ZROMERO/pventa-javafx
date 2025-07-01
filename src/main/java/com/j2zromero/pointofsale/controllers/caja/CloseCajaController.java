package com.j2zromero.pointofsale.controllers.caja;

import com.j2zromero.pointofsale.Main;
import com.j2zromero.pointofsale.controllers.login.LoginController;
import com.j2zromero.pointofsale.models.caja.Caja;
import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.services.caja.CajaService;
import com.j2zromero.pointofsale.services.sale.SaleService;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;

public class CloseCajaController {
    public TextField txtTotalSales;
    public TextField txtClosingAmount;
    public Button btnCloseCaja;
    private boolean closedFromButton = false; // default false
    public TextField txtNotes;
    public TextField txtDiscount;
    private Double totalSales;
    private SaleService saleService = new SaleService();
    private Sale sale;
    private CajaService cajaService = new CajaService();
    /**
     * Inicializa el controlador: habilita el botón sólo si hay monto.
     */
    @FXML
    public void initialize() {
        try {
           Sale saleInit = saleService.getSalesSummary();
           txtTotalSales.setText(saleInit.getTotal().toString());
            txtDiscount.setText(saleInit.getDiscount().toString());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void closeCaja() throws IOException {
        if(txtClosingAmount.getText().trim().isEmpty()){
            DialogUtils.showWarningAlert("Error", "Debes de agregar monto de cierre", txtClosingAmount);
            return;

        }
        closedFromButton = true; // Mark that the close came from button
        Stage currentStage = (Stage) btnCloseCaja.getScene().getWindow();
        Sale sale;
        try {
            sale = saleService.getSalesSummary();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Caja caja =  new Caja();
       caja.setClosingAmount(Double.parseDouble(txtClosingAmount.getText()));
       caja.setTotalSales(Double.parseDouble(txtTotalSales.getText()));
       caja.setTotalDiscounts(sale.getDiscount());
       caja.setTotalCash(Double.parseDouble(txtClosingAmount.getText()));

        try {
            cajaService.closeCaja(caja);
            currentStage.close(); // Close the current modal

            // ✅ Reopen Login
            FXMLLoader loginLoader = new FXMLLoader(Main.class.getResource("/views/login/login.fxml"));
            Parent loginRoot = loginLoader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.show();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




    }
    public boolean wasClosedFromButton() {
        return closedFromButton;
    }

}
