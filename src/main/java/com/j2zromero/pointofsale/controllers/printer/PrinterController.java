package com.j2zromero.pointofsale.controllers.printer;

import com.j2zromero.pointofsale.models.printer.LocalPrinter;
import com.j2zromero.pointofsale.services.printer.PrinterService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.Printer;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PrinterController {

    public TextField txtAddress;
    public TextField txtName;
    public AnchorPane anchorPrinter;
    @FXML private ComboBox<String> cbxPrinter;
    @FXML private TextArea txtNotes;
    private PrinterService printerService = new PrinterService();

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            if (anchorPrinter.getScene() != null) {
                anchorPrinter.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
        // 1) Load all system printers into the combo
        List<String> names = Printer.getAllPrinters()
                .stream()
                .map(Printer::getName)
                .collect(Collectors.toList());
        ObservableList<String> printerNames = FXCollections.observableArrayList(names);
        cbxPrinter.setItems(printerNames);

        // 2) If there’s one in the DB, select it as default
        try {
            LocalPrinter local = printerService.getLocalPrinter();  // your SP-backed call
            System.out.println(local);
            if (local != null) {
                String savedName = local.getName();
                System.out.println(local);
                if (printerNames.contains(savedName)) {
                    cbxPrinter.getSelectionModel().select(savedName);
                    txtNotes.setText(local.getDescription());
                    txtAddress.setText(local.getAddress());
                    txtName.setText(local.getEnterpriseName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert(
                    "Impresora",
                    "No se pudo cargar la impresora guardada",
                    null
            );
        }
    }

    @FXML
    private void onAddPrinter() {
        // Ahora obtenemos directamente el nombre seleccionado
        String selectedName = cbxPrinter.getSelectionModel().getSelectedItem();
        String notes = txtNotes.getText();
        LocalPrinter printer = new LocalPrinter();
        printer.setName(selectedName);
        printer.setDescription(notes);
        printer.setEnterpriseName(txtName.getText());
        printer.setAddress(txtAddress.getText());
        try {
            printerService.add(printer);
            DialogUtils.showWarningAlert("Impresora", "Impresora agregada", null);
        } catch (SQLException e) {
            DialogUtils.showWarningAlert("Impresora", "No se pudo agregar impresora", null);

            throw new RuntimeException(e);
        }
    }
}
