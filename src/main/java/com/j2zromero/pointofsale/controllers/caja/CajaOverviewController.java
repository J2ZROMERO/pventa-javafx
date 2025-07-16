package com.j2zromero.pointofsale.controllers.caja;

import com.j2zromero.pointofsale.models.caja.SummaryCaja;
import com.j2zromero.pointofsale.services.caja.CajaService;
import com.j2zromero.pointofsale.utils.DialogUtils;
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

public class CajaOverviewController {
    @FXML public Button btnUpdate;                  // “⟳ Refrescar”
    @FXML public AnchorPane anchorCajaOverview;
    @FXML public TextField txtSearch;
    @FXML public TableView<SummaryCaja> tableDetails;
    @FXML public DatePicker inputDate;

    @FXML private TableColumn<SummaryCaja, Long>   cajaIdColumn;
    @FXML private TableColumn<SummaryCaja, String> userNameColumn;
    @FXML private TableColumn<SummaryCaja, Double> openingAmountColumn;
    @FXML private TableColumn<SummaryCaja, Double> closingAmountColumn;
    @FXML private TableColumn<SummaryCaja, Double> salesCountColumn;
    @FXML private TableColumn<SummaryCaja, Double> subtotalColumn;
    @FXML private TableColumn<SummaryCaja, Double> totalDiscountsColumn;
    @FXML private TableColumn<SummaryCaja, Double> totalSoldColumn;
    @FXML private TableColumn<SummaryCaja, Double> totalWithdrawalsColumn;
    @FXML private TableColumn<SummaryCaja, String> openedAtColumn;
    @FXML private TableColumn<SummaryCaja, String> closedAtColumn;
    @FXML private TableColumn<SummaryCaja, Void>   actionsColumn;

    private final CajaService service = new CajaService();
    private final ObservableList<SummaryCaja> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        DialogUtils.TooltipHelper.install(btnUpdate,
                "Actualizar datos de la tabla",
                DialogUtils.TooltipColor.DARK);
        // Aplico CSS global
        Platform.runLater(() -> {
            if (anchorCajaOverview.getScene() != null) {
                anchorCajaOverview.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css"))
                                .toExternalForm()
                );
            }
        });

        // Configuro columnas con propiedades camelCase
        cajaIdColumn        .setCellValueFactory(new PropertyValueFactory<>("cajaId"));
        userNameColumn     .setCellValueFactory(new PropertyValueFactory<>("userName"));
        openingAmountColumn.setCellValueFactory(new PropertyValueFactory<>("openingAmount"));
        closingAmountColumn.setCellValueFactory(new PropertyValueFactory<>("closingAmount"));
        salesCountColumn   .setCellValueFactory(new PropertyValueFactory<>("salesCount"));
        subtotalColumn     .setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        totalDiscountsColumn.setCellValueFactory(new PropertyValueFactory<>("totalDiscounts"));
        totalSoldColumn    .setCellValueFactory(new PropertyValueFactory<>("totalSold"));
        totalWithdrawalsColumn.setCellValueFactory(new PropertyValueFactory<>("totalWithdrawals"));
        openedAtColumn     .setCellValueFactory(new PropertyValueFactory<>("openedAtFormatted"));
        closedAtColumn     .setCellValueFactory(new PropertyValueFactory<>("closedAtFormatted"));

        // Añade columna de acciones
    //     addViewDetailsButtonToActions();

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

    private void addViewDetailsButtonToActions() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("🔍");
            {
                btn.getStyleClass().add("icon-button");
                btn.setOnAction(e -> {
                    SummaryCaja selected = getTableView().getItems().get(getIndex());
                    openDetailsView(selected);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void openDetailsView(SummaryCaja caja) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/caja/cajaDetails.fxml")
            );
            Parent root = loader.load();
            CajaDetailsController ctrl = loader.getController();
            ctrl.setSummaryCaja(caja);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Detalle de Caja #" + caja.getCajaId());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "No se pudo abrir el detalle", ButtonType.OK).showAndWait();
        }
    }

    private void onRefreshClicked(ActionEvent event) {
        refreshTable();
        DialogUtils.showToast("Datos actualizados.",2, "blue");



    }

    private void refreshTable() {
        loadDataForDate(inputDate.getValue());
        applyFilterAndSort();
    }

    private void loadDataForDate(LocalDate date) {
        try {
            List<SummaryCaja> data = service.getSummaryCaja(date);
            masterData.setAll(data);
        } catch (SQLException e) {
            e.printStackTrace();
            masterData.clear();
        }
    }

    private void applyFilterAndSort() {
        FilteredList<SummaryCaja> filtered = new FilteredList<>(masterData, c -> true);
        String f = txtSearch.getText() != null ? txtSearch.getText().toLowerCase().trim() : "";

        filtered.setPredicate(caja -> {
            if (f.isEmpty()) return true;
            return caja.getCajaId().toString().contains(f)
                    || caja.getUserName().toLowerCase().contains(f)
                    || caja.getSalesCount().toString().contains(f)
                    || caja.getSubTotal().toString().contains(f)
                    || caja.getTotalDiscounts().toString().contains(f)
                    || caja.getTotalSold().toString().contains(f)
                    || caja.getTotalWithdrawals().toString().contains(f);
        });

        SortedList<SummaryCaja> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableDetails.comparatorProperty());
        tableDetails.setItems(sorted);
    }
}
