package com.j2zromero.pointofsale.controllers.sale;
import com.j2zromero.pointofsale.controllers.sale.SaleDetailsByIdController;
import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.services.sale.SaleService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class SaleOverviewController {

    @FXML public Button btnRefresh;
    public AnchorPane anchorSalesOverview;
    @FXML private DatePicker inputDate;
    @FXML private TextField txtSearch;
    @FXML private TableView<Sale> tableSales;

    @FXML private TableColumn<Sale, Long>   idColumn;
    @FXML private TableColumn<Sale, String> userColumn;
    @FXML private TableColumn<Sale, String> terminalColumn;
    @FXML private TableColumn<Sale, String> paymentTypeColumn;
    @FXML private TableColumn<Sale, Double> subtotalColumn;
    @FXML private TableColumn<Sale, Double> discountColumn;
    @FXML private TableColumn<Sale, Double> totalColumn;
    @FXML private TableColumn<Sale, String> createdAtColumn;
    @FXML private TableColumn<Sale, Void>   actionsColumn;

    private final SaleService saleService = new SaleService();
    private final ObservableList<Sale> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (anchorSalesOverview.getScene() != null) {
                anchorSalesOverview.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
        // columnas
        idColumn         .setCellValueFactory(new PropertyValueFactory<>("id"));
        userColumn       .setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        terminalColumn   .setCellValueFactory(new PropertyValueFactory<>("terminalName"));
        paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        subtotalColumn   .setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        discountColumn   .setCellValueFactory(new PropertyValueFactory<>("discount"));
        totalColumn      .setCellValueFactory(new PropertyValueFactory<>("total"));
        createdAtColumn  .setCellValueFactory(new PropertyValueFactory<>("createdAtFormatted"));
        addViewDetailsButtonToActions();

        // datepicker por defecto hoy
        inputDate.setValue(LocalDate.now());
        // listeners
        inputDate.valueProperty().addListener((obs, o, n) -> refreshTable());
        txtSearch.textProperty().addListener((obs, o, n) -> applyFilterAndSort());

        // primera carga
        refreshTable();
    }

    /** Botón “⟳ Refrescar” */
    @FXML
    public void loadSales(ActionEvent event) {
        refreshTable();
    }

    /** Carga las ventas y luego aplica filtro y orden */
    private void refreshTable() {
        loadSalesForDate(inputDate.getValue());
        applyFilterAndSort();
    }

    /** Obtiene los datos desde el servicio */
    private void loadSalesForDate(LocalDate date) {
        try {
            List<Sale> ventas = saleService.getSalesByDate(date);
            masterData.setAll(ventas);
        } catch (SQLException e) {
            e.printStackTrace();
            masterData.clear();
        }
    }

    /** Filtra y ordena según el contenido de txtSearch y la columna clickeada */
    private void applyFilterAndSort() {
        FilteredList<Sale> filtered = new FilteredList<>(masterData, s -> true);
        String f = txtSearch.getText() != null ? txtSearch.getText().toLowerCase().trim() : "";

        filtered.setPredicate(sale -> {
            if (f.isEmpty()) return true;
            if (sale.getId().toString().contains(f)) return true;
            if (sale.getCashierName().toLowerCase().contains(f)) return true;
            if (sale.getTerminalName().toLowerCase().contains(f)) return true;
            if (sale.getPaymentMethod().toLowerCase().contains(f)) return true;
            if (String.valueOf(sale.getSubtotal()).contains(f)) return true;
            if (String.valueOf(sale.getDiscount()).contains(f)) return true;
            if (String.valueOf(sale.getTotal()).contains(f)) return true;
            return sale.getCreatedAtFormatted().toLowerCase().contains(f);
        });

        SortedList<Sale> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableSales.comparatorProperty());
        tableSales.setItems(sorted);
    }

    private void addViewDetailsButtonToActions() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("👁");
            {
                btn.getStyleClass().add("icon-button");
                btn.setOnAction(e -> {
                    Sale selected = getTableView().getItems().get(getIndex());
                    openDetailView(selected);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void openDetailView(Sale sale) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/sale/saleDetailsById.fxml"));
            Parent root = loader.load();
            SaleDetailsByIdController ctrl = loader.getController();
            ctrl.setSaleId(sale);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Detalle de Venta #" + sale.getId());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir el detalle", ButtonType.OK).showAndWait();
        }
    }
}
