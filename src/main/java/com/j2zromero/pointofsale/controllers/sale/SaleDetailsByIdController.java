package com.j2zromero.pointofsale.controllers.sale;

import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.sale.SaleDetail;
import com.j2zromero.pointofsale.services.printer.PrinterService;
import com.j2zromero.pointofsale.services.sale.SaleService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SaleDetailsByIdController {

    public AnchorPane anchorSalesDetails;
    // El TableView y sus columnas (añádelos también en el FXML con los fx:id correspondientes)
    @FXML private TableView<SaleDetail> tableDetails;
    @FXML private TableColumn<SaleDetail, Long>   idColumn;
    @FXML private TableColumn<SaleDetail, String> productCodeColumn;
    @FXML private TableColumn<SaleDetail, Double> amountColumn;
    @FXML private TableColumn<SaleDetail, Double> priceColumn;
    @FXML private TableColumn<SaleDetail, Double> totalColumn;
    @FXML private TableColumn<SaleDetail, Double> discountColumn;
    @FXML private TableColumn<SaleDetail, Date> dateColumn;  // formateada
    @FXML private Button btnPrint;
    @FXML private Label  lblTicket;
    private PrinterService printerService = new PrinterService();
    private final SaleService saleService = new SaleService();
    private final ObservableList<SaleDetail> detailsList = FXCollections.observableArrayList();
    private Sale sale;
    List<SaleDetail> datos;
    /** Este método será llamado desde el controlador padre */
    public void setSaleId(Sale sale) {
        this.sale = sale;
        lblTicket.setText("#" + sale.getId());
        loadColumns();
        loadDetails();
    }
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (anchorSalesDetails.getScene() != null) {
                anchorSalesDetails.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
    }
    /** Configura los cellValueFactory y cellFormatter de columnas */
    private void loadColumns() {
        idColumn           .setCellValueFactory(new PropertyValueFactory<>("id"));
        productCodeColumn  .setCellValueFactory(new PropertyValueFactory<>("productCode"));
        amountColumn       .setCellValueFactory(new PropertyValueFactory<>("quantity"));      // o amountEntered según tu modelo
        priceColumn        .setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        discountColumn     .setCellValueFactory(new PropertyValueFactory<>("discountLine"));
        totalColumn        .setCellValueFactory(new PropertyValueFactory<>("totalLine"));

        // Para la fecha, vamos a formatearla con SimpleDateFormat
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));


        tableDetails.setItems(detailsList);
    }

    /** Carga los detalles desde la BBDD a través del servicio */
    private void loadDetails() {
        try {
            datos = saleService.getDetailsBySaleId(sale.getId());
            detailsList.setAll(datos);
        } catch (SQLException e) {
            e.printStackTrace();
            detailsList.clear();
            new Alert(Alert.AlertType.ERROR, "No se pudieron cargar los detalles", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void onPrintTicket() {
        try {
            printerService.printReceipt(sale,datos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
