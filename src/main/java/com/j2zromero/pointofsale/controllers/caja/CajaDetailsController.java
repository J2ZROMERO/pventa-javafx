package com.j2zromero.pointofsale.controllers.caja;

import com.j2zromero.pointofsale.models.caja.SummaryCaja;
import com.j2zromero.pointofsale.models.caja.SummaryDetailsCaja;
import com.j2zromero.pointofsale.services.caja.CajaService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class CajaDetailsController {
    public TextField txtTotal;
    public AnchorPane anchorCajaDetails;
    @FXML private TextField txtSearch;
    @FXML private TableView<SummaryDetailsCaja> detailsTable;

    @FXML private TableColumn<SummaryDetailsCaja, Long>   idColumn;
    @FXML private TableColumn<SummaryDetailsCaja, String> cashierColumn;
    @FXML private TableColumn<SummaryDetailsCaja, String> productCodeColumn;
    @FXML private TableColumn<SummaryDetailsCaja, String> productNameColumn;
    @FXML private TableColumn<SummaryDetailsCaja, Integer> soldUnitsColumn;
    @FXML private TableColumn<SummaryDetailsCaja, Double>  unitPriceColumn;
    @FXML private TableColumn<SummaryDetailsCaja, Double>  lineColumn;

    private final CajaService service = new CajaService();
    private final ObservableList<SummaryDetailsCaja> detailList = FXCollections.observableArrayList();
    private SummaryCaja caja;

    public void setSummaryCaja(SummaryCaja caja) {
        this.caja = caja;
        loadDetailsForCaja();
    }

    private void loadDetailsForCaja() {
        try {
            List<SummaryDetailsCaja> rows =
                    service.getSummaryDetailsCajas();
            detailList.setAll(rows);
            detailsTable.setItems(detailList);
            txtTotal.setText(String.valueOf(service.getWithdrawalsByCajaId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (anchorCajaDetails.getScene() != null) {
                anchorCajaDetails.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
        // 1) Configure each column with the correct property name:
        idColumn        .setCellValueFactory(new PropertyValueFactory<>("cajaId"));
        cashierColumn   .setCellValueFactory(new PropertyValueFactory<>("cajeroName"));
        productCodeColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        soldUnitsColumn .setCellValueFactory(new PropertyValueFactory<>("soldUnits"));
        unitPriceColumn .setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        lineColumn      .setCellValueFactory(new PropertyValueFactory<>("importeTotal"));

        try {
            double  total = service.getTotalCaja();
            txtTotal.setText(String.valueOf(total));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // 2) Load the data
        loadDetails();

        // 3) Hook up the search/filter logic
        setupSearchFilter();
    }

    private void loadDetails() {
        // Replace getSummaryCaja() with the actual method to fetch detail rows
        List<SummaryDetailsCaja> rows = service.getSummaryDetailsCajas();
        detailList.setAll(rows);
        detailsTable.setItems(detailList);
    }

    private void setupSearchFilter() {
        FilteredList<SummaryDetailsCaja> filtered = new FilteredList<>(detailList, p -> true);

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal == null ? "" : newVal.toLowerCase().trim();
            filtered.setPredicate(item -> {
                if (filter.isEmpty()) return true;
                // you can adjust which fields are searchable
                return  item.getCajeroName().toLowerCase().contains(filter)
                        || item.getProductCode().toLowerCase().contains(filter)
                        || item.getProductName().toLowerCase().contains(filter)
                        || item.getSoldUnits().toString().contains(filter)
                        || item.getUnitPrice().toString().contains(filter)
                        || item.getImporteTotal().toString().contains(filter);
            });
        });

        SortedList<SummaryDetailsCaja> sorted = new SortedList<SummaryDetailsCaja>(filtered);
        sorted.comparatorProperty().bind(detailsTable.comparatorProperty());
        detailsTable.setItems(sorted);
    }
}
