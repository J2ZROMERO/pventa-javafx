package com.j2zromero.pointofsale.controllers.caja;

import com.j2zromero.pointofsale.models.caja.Withdrawal;
import com.j2zromero.pointofsale.services.caja.CajaService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.time.LocalDate;

public class WithdrawalController {
    @FXML public TextField txtAmount;
    @FXML public TextArea  txtMotive;
    @FXML public DatePicker inputDate;              // ← Nuevo
    @FXML public TableView<Withdrawal> withDrawalTable;
    @FXML public TableColumn<Withdrawal, Double> amountColumn;
    @FXML public TableColumn<Withdrawal, String> dateColumn;      // Ahora String
    @FXML public TableColumn<Withdrawal, String> reasonColumn;
    @FXML public AnchorPane anchorWithdrawal;

    private final CajaService cajaService = new CajaService();
    private final ObservableList<Withdrawal> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {
        FormUtils.applyNumericOnlyFilter(txtAmount);

        // columna monto y razón igual
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

        // columna fecha formateada (usamos el getter getOpenedAtFormatted())
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        withDrawalTable.setItems(data);


        // primera carga
        refreshTable();
    }

    /** Botón “+ Añadir retiro” */
    @FXML
    public void addWithdrawal() {
        if (txtAmount.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Error", "Debes agregar el monto del retiro", txtAmount);
            return;
        }
        if (txtMotive.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Error", "Debes agregar el motivo del retiro", txtMotive);
            return;
        }

        try {
            double amount = Double.parseDouble(txtAmount.getText());
            String reason = txtMotive.getText();
            cajaService.addWithdrawal(amount, reason);
            txtMotive.clear();
            txtAmount.clear();
            refreshTable();
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo guardar el retiro", null);
        }
    }

    /** Recarga la tabla usando la fecha seleccionada */
    private void refreshTable() {
        loadWithdrawalsForDate();
    }

    /** Obtiene los retiros de la fecha dada */
    private void loadWithdrawalsForDate() {
        try {
            // Asegúrate de tener este método en tu servicio
            var list = cajaService.getWithdrawalsByCajaId();
            data.setAll(list);
        } catch (SQLException e) {
            e.printStackTrace();
            data.clear();
        }
    }
}

