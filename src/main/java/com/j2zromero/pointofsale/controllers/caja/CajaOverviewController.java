package com.j2zromero.pointofsale.controllers.caja;

import com.j2zromero.pointofsale.models.caja.SummaryCaja;
import com.j2zromero.pointofsale.services.caja.CajaService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CajaOverviewController {
    @FXML public Button btnUpdate;                  // “⟳ Refrescar”
    public AnchorPane anchorCajaOverview;
    @FXML public TextField txtSearch;
    @FXML public TableView<SummaryCaja> tableDetails;
    public DatePicker inputDate;

    @FXML private TableColumn<SummaryCaja, Long>   idColumn;
    @FXML private TableColumn<SummaryCaja, String> cashierColumn;
    @FXML private TableColumn<SummaryCaja, Double> sellsPerCajaColumn;
    @FXML private TableColumn<SummaryCaja, Double> subtotalSellsColumn;
    @FXML private TableColumn<SummaryCaja, Double> discountColumn;
    @FXML private TableColumn<SummaryCaja, Double> totalCajaColumn;
    @FXML private TableColumn<SummaryCaja, LocalDate> openedAtColumn;
    @FXML private TableColumn<SummaryCaja, LocalDate> closedAtColumn;

    private final CajaService service = new CajaService();
    private final ObservableList<SummaryCaja> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Aplico CSS global
        Platform.runLater(() -> {
            if (anchorCajaOverview.getScene() != null) {
                anchorCajaOverview.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css"))
                                .toExternalForm()
                );
            }
        });

        // Configuro columnas
        idColumn            .setCellValueFactory(new PropertyValueFactory<>("cajaId"));
        cashierColumn       .setCellValueFactory(new PropertyValueFactory<>("cashier"));
        sellsPerCajaColumn  .setCellValueFactory(new PropertyValueFactory<>("salesPerCaja"));
        subtotalSellsColumn .setCellValueFactory(new PropertyValueFactory<>("subtotalCaja"));
        discountColumn      .setCellValueFactory(new PropertyValueFactory<>("totalCajaDiscount"));
        totalCajaColumn     .setCellValueFactory(new PropertyValueFactory<>("totalCaja"));
        openedAtColumn      .setCellValueFactory(new PropertyValueFactory<>("openedAt"));
        closedAtColumn      .setCellValueFactory(new PropertyValueFactory<>("closedAt"));

        // DatePicker por defecto hoy
        inputDate.setValue(LocalDate.now());

        // Listeners
        inputDate.valueProperty().addListener((obs, o, n) -> refreshTable());
        txtSearch.textProperty().addListener((obs, o, n) -> applyFilterAndSort());

        // Botón refrescar
        btnUpdate.setOnAction(this::onRefreshClicked);

        // Primera carga
        refreshTable();
    }

    /** Handler para el botón “Refrescar” */
    private void onRefreshClicked(ActionEvent event) {
        refreshTable();
    }

    /** Carga datos y luego filtra y ordena */
    private void refreshTable() {
        loadDataForDate(inputDate.getValue());
        applyFilterAndSort();
    }

    /** Obtiene los datos del servicio según la fecha */
    private void loadDataForDate(LocalDate date) {
        try {
            List<SummaryCaja> data = service.getSummaryCaja(date);

            masterData.setAll(data);
        } catch (SQLException e) {
            e.printStackTrace();
            masterData.clear();
        }
    }

    /** Aplica el filtro de texto y enlaza al TableView */
    private void applyFilterAndSort() {
        FilteredList<SummaryCaja> filtered = new FilteredList<>(masterData, c -> true);
        String f = txtSearch.getText() != null ? txtSearch.getText().toLowerCase().trim() : "";

        filtered.setPredicate(caja -> {
            if (f.isEmpty()) return true;
            return caja.getCajaId().toString().contains(f)
                    || caja.getCashier().toLowerCase().contains(f)
                    || caja.getTotalCaja().toString().contains(f)
                    || caja.getSubtotalCaja().toString().contains(f)
                    || caja.getTotalCajaDiscount().toString().contains(f)
                    || caja.getTotalCaja().toString().contains(f);
        });

        SortedList<SummaryCaja> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableDetails.comparatorProperty());
        tableDetails.setItems(sorted);
    }
}
