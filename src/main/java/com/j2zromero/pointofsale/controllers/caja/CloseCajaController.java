package com.j2zromero.pointofsale.controllers.caja;

import com.j2zromero.pointofsale.Main;
import com.j2zromero.pointofsale.controllers.login.LoginController;
import com.j2zromero.pointofsale.models.caja.Caja;
import com.j2zromero.pointofsale.models.caja.CloseCaja;
import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.services.caja.CajaService;
import com.j2zromero.pointofsale.services.sale.SaleService;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class CloseCajaController {
    @FXML public TextField txtTotalSales;
    @FXML public TextField txtClosingAmount;
    @FXML public Button btnCloseCaja;
    @FXML public AnchorPane anchorCloseCaja;
    @FXML public TextField txtOpeningAmount;
    @FXML public TextField txtWithdraw;
    public Button btnUpdate;
    @FXML private boolean closedFromButton = false; // default false
    @FXML public TextField txtNotes;
    @FXML public TextField txtDiscount;
    private Double totalSales;
    private CajaService cajaService = new CajaService();
    private Sale sale;


    @FXML
    public void initialize() {
        loadData();
    }
    @FXML
    public void closeCaja() throws IOException {
        if (txtClosingAmount.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Error", "Debes de agregar monto de cierre", txtClosingAmount);
            return;
        }
        Caja caja = new Caja();
        caja.setId(UserService.getCajaId());
        caja.setClosingAmount(Double.parseDouble(txtClosingAmount.getText()));
        caja.setTotalSales(Double.parseDouble(txtTotalSales.getText()));
        caja.setTotalDiscounts(Double.parseDouble(txtDiscount.getText()));
        caja.setTotalCash(Double.parseDouble(txtClosingAmount.getText()));

        try {
            cajaService.closeCaja(caja);
            FormUtils.clearAndResetStyles(anchorCloseCaja);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        closedFromButton = true;
        Stage modalStage = (Stage) btnCloseCaja.getScene().getWindow();
        modalStage.close();

        Window owner = modalStage.getOwner();
        if (owner instanceof Stage) {
            ((Stage) owner).close();
        }

        FXMLLoader loginLoader = new FXMLLoader(Main.class.getResource("/views/login/login.fxml"));
        Parent loginRoot = loginLoader.load();
        Stage loginStage = new Stage();
        loginStage.setTitle("Login");
        loginStage.setScene(new Scene(loginRoot));
        loginStage.show();
    }
    public boolean wasClosedFromButton() {
        return closedFromButton;
    }

    public void loadData(){
        try {

            CloseCaja saleInit = cajaService.getCajaSummary();
            txtTotalSales.setText(String.valueOf( saleInit.getTotalSales()));
            txtDiscount.setText(String.valueOf(saleInit.getTotalDiscount()));
            txtOpeningAmount.setText(String.valueOf(saleInit.getOpeningAmount()));
            txtWithdraw.setText(String.valueOf(saleInit.getTotalWithdrawl()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(ActionEvent actionEvent) {
        loadData();
    }
}
