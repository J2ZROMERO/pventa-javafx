package com.j2zromero.pointofsale.controllers.sale;

import com.j2zromero.pointofsale.models.payments.Payment;
import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.sale.SaleDetail;
import com.j2zromero.pointofsale.models.terminal.Terminal;
import com.j2zromero.pointofsale.services.permission.PermissionService;
import com.j2zromero.pointofsale.services.sale.SaleService;
import com.j2zromero.pointofsale.services.product.ProductService;
import com.j2zromero.pointofsale.services.terminal.TerminalService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import com.j2zromero.pointofsale.utils.InputUtils;
import com.j2zromero.pointofsale.utils.UnitType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling sales operations in the POS system.
 * Manages adding products, calculating totals, and finalizing sales.
 */
public class SaleController {

    // UI Components
    @FXML private Label lblCashier;
    @FXML private Label lblTerminal;
    @FXML private ComboBox<Payment> cbxPaymentMethod;
    @FXML private TextField txtProductCode;
    @FXML private Button btnAddproduct;
    @FXML private TableView<SaleDetail> salesTable;
    @FXML private TableColumn<SaleDetail, Void> actionsColumn;

    @FXML private TableColumn<SaleDetail, String> productNameColumn;
    @FXML private TableColumn<SaleDetail, Double> availableColumn;
    @FXML private TableColumn<SaleDetail, String> unitMeasurementColumn;
    @FXML private TableColumn<SaleDetail, Double> unitPriceColumn;
    @FXML private TableColumn<SaleDetail, Double> quantityColumn;
    @FXML private TableColumn<SaleDetail, Double> discountColumn;
    @FXML private TableColumn<SaleDetail, Double> totalSoldColumn;

    @FXML private TextField txtSubtotal;
    @FXML private TextField txtDiscount;
    @FXML private TextField txtTotal;
    @FXML private TextField txtReceived;
    @FXML private TextField txtPay;

    @FXML private Button btnRemoveRow;
    @FXML private Button btnSaleProduct;
    @FXML private Button btnClearTable;
    @FXML private Button btnGenerateSell;

    @FXML private AnchorPane rootPane;

    // Services and data models
    private final ProductService productService = new ProductService();
    private final SaleService salesService = new SaleService();
    private final ObservableList<Sale> salesList = FXCollections.observableArrayList();
    private Sale sale;
    private SaleDetail saleDetail;
    private TerminalService terminalService = new TerminalService();
    private Terminal terminal;
    /**
     * Initializes UI state, default values, and event listeners.
     */
    @FXML
    public void initialize() throws SQLException {
        // Add default payment option
        Payment defaultPayment = new Payment(1L, "cash", "Efectivo");
        cbxPaymentMethod.getItems().add(defaultPayment);
        cbxPaymentMethod.setValue(defaultPayment);
        // Handle Enter key in product code field
        txtProductCode.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addProductToTable();
            }
        });

        // Recalculate totals when discount changes
        txtDiscount.textProperty().addListener((obs, oldText, newText) -> updateTotalWithDiscount(newText));

        // Recalculate change when amount received changes
        txtReceived.textProperty().addListener((obs, oldText, newText) -> updateChange(newText));

        // Add user name
        lblCashier.setText(PermissionService.getUser().getName());
        // get first terminal
        terminal = terminalService.getAll().get(0);
        lblTerminal.setText(terminal.getCode());

    }

    /**
     * Updates subtotal and resets payment fields.
     */
    private void updateSubtotal() {
        double sum = salesTable.getItems().stream()
                .mapToDouble(d -> d.getTotalLine() != null ? d.getTotalLine() : 0.0)
                .sum();
        txtSubtotal.setText(String.format("%.2f", sum));
        txtTotal.setText(String.format("%.2f", sum));
        txtReceived.clear();
        txtPay.clear();
        txtDiscount.clear();
    }

    /**
     * Applies discount to subtotal and updates total.
     */
    private void updateTotalWithDiscount(String discountText) {
        double subtotal = parseDoubleSafely(txtSubtotal.getText());
        double discount = parseDoubleSafely(discountText);
        txtTotal.setText(String.format("%.2f", subtotal - discount));
    }

    /**
     * Calculates the change to return based on received amount.
     */
    private void updateChange(String receivedText) {
        double total = parseDoubleSafely(txtTotal.getText());
        double received = parseDoubleSafely(receivedText);
        double change = received - total;
        if (change < 0) {
            txtPay.clear();
        } else {
            txtPay.setText(String.format("%.2f", change));
        }
    }

    /**
     * Safely parses a double from text, returning 0.0 on error.
     */
    private double parseDoubleSafely(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Handles the final sale action.
     */
    public void AddSale(ActionEvent actionEvent)  {
        if (salesTable.getItems().isEmpty()) {
            DialogUtils.showWarningAlert("Venta", "No hay productos en la venta.", null);
            return;
        }
        Sale sale = new Sale();
        sale.setTerminalId(terminal.getId());
        sale.setCashierId(PermissionService.getUser().getId());
        sale.setSubtotal(InputUtils.parseDouble(txtSubtotal.getText()));
        sale.setDiscount(InputUtils.parseDouble(txtDiscount.getText()));
        sale.setTotal(InputUtils.parseDouble(txtTotal.getText()));
        Payment uniType = cbxPaymentMethod.getSelectionModel().getSelectedItem();
        sale.setPaymentMethod(uniType.getCode());


        // 2) Grab all details from the TableView
        List<SaleDetail> details = new ArrayList<>( salesTable.getItems() );
        try {
            System.out.println(sale);
            System.out.println(details);
            boolean ok = salesService.add(sale, details);
            if (ok) {
                salesTable.getItems().clear();
                productCodeFocus();
            }
        } catch (SQLException ex) {
            DialogUtils.showWarningAlert("Error","Error al guardar venta", null);
        }

    }

    public void productCodeFocus(){
        txtProductCode.setText("");
        txtProductCode.requestFocus();
    }

    /**
     * Adds a product to the sales table or increments quantity if it already exists.
     */
    public void addProductToTable() {
        String productCode = txtProductCode.getText().trim();
        if (productCode.isEmpty()) {
            DialogUtils.showWarningAlert("Producto", "Debes seleccionar algun producto.", txtProductCode);
            productCodeFocus();
            return;
        }
        try {
            saleDetail = salesService.getProductFromInventory(productCode);
            if (saleDetail == null || saleDetail.getProductCode() == null || saleDetail.getProductCode().isBlank()) {
                DialogUtils.showWarningAlert("Producto", "No hay un producto con el codigo escrito, intenta nuevamente.", txtProductCode);
                return;
            }
            if (saleDetail.getAmountEntered() == null || saleDetail.getAmountEntered() <= 0) {
                DialogUtils.showWarningAlert("Producto", "No hay productos disponibles.", txtProductCode);
                return;
            }
            configureSalesTableColumns();
            boolean productExists = false;
            for (SaleDetail existingDetail : salesTable.getItems()) {
                if (existingDetail.getProductCode().equals(saleDetail.getProductCode())) {
                    incrementExistingProduct(existingDetail);
                    productExists = true;
                    break;
                }
            }
            if (!productExists) {
                addNewProductToTable();
            }
            updateSubtotal();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets up cell factories and value factories for the sales table.
     */
    private void configureSalesTableColumns() {
        salesTable.setEditable(true);
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("amountEntered"));
        unitMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("unitMeasurement"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discountLine"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalSoldColumn.setCellValueFactory(new PropertyValueFactory<>("totalLine"));
        String centerStyle = "-fx-alignment: CENTER;";
        productNameColumn.setStyle(centerStyle);
        availableColumn.setStyle(centerStyle);
        unitMeasurementColumn.setStyle(centerStyle);
        unitPriceColumn.setStyle(centerStyle);
        quantityColumn.setStyle(centerStyle);
        discountColumn.setStyle(centerStyle);
        totalSoldColumn.setStyle(centerStyle);
        actionsColumn.setCellFactory(createRemoveButtonCellFactory());
        setupEditableColumn(quantityColumn, this::onQuantityEditCommit);
        setupEditableColumn(discountColumn, this::onDiscountEditCommit);
    }

    /**
     * Creates a cell factory that adds a delete button to each row.
     */
    private Callback<TableColumn<SaleDetail, Void>, TableCell<SaleDetail, Void>> createRemoveButtonCellFactory() {
        return col -> new TableCell<>() {
            private final Button btn = new Button("X");
            private final Region spacer = new Region();
            private final HBox container = new HBox(spacer, btn);
            {
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                btn.setOnAction(e -> getTableView().getItems().remove(getIndex()));
                HBox.setHgrow(spacer, Priority.ALWAYS);
                container.setAlignment(Pos.CENTER);
                container.setPadding(new Insets(0, 5, 0, 0));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
                updateSubtotal();
                if (!empty) setStyle("-fx-alignment: CENTER-RIGHT;");
            }
        };
    }

    /**
     * Configures a column for in-line editing with DoubleStringConverter.
     */
    private void setupEditableColumn(TableColumn<SaleDetail, Double> column,
                                     javafx.event.EventHandler<TableColumn.CellEditEvent<SaleDetail, Double>> handler) {
        column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        column.setOnEditCommit(handler);
    }

    /**
     * Handles quantity edits, validating against stock and updating totals.
     */
    private void onQuantityEditCommit(TableColumn.CellEditEvent<SaleDetail, Double> ev) {
        SaleDetail det = ev.getRowValue();
        double newQty = ev.getNewValue() != null ? ev.getNewValue() : 0.0;
        if (newQty > det.getAmountEntered()) {
            DialogUtils.showWarningAlert(
                    "Stock Excedido",
                    "No puedes vender más de lo disponible (" + det.getAmountEntered() + ").",
                    null
            );
            salesTable.refresh();
            return;
        }
        det.setQuantity(newQty);
        double price = det.getUnitPrice() != null ? det.getUnitPrice() : 0.0;
        double discount = det.getDiscountLine() != null ? det.getDiscountLine() : 0.0;
        det.setTotalLine((newQty * price) - discount);
        updateSubtotal();
        salesTable.refresh();
    }

    /**
     * Handles discount edits, ensuring discount does not exceed line total.
     */
    private void onDiscountEditCommit(TableColumn.CellEditEvent<SaleDetail, Double> ev) {
        SaleDetail det = ev.getRowValue();
        double discount = ev.getNewValue() != null ? ev.getNewValue() : 0.0;
        double qty = det.getQuantity() != null ? det.getQuantity() : 0.0;
        double price = det.getUnitPrice() != null ? det.getUnitPrice() : 0.0;
        double lineTotal = qty * price;
        if (qty <= 0) {
            DialogUtils.showWarningAlert(
                    "Descuento inválido",
                    "Primero necesitas ingresar una cantidad válida para aplicar descuento.",
                    null
            );
            salesTable.refresh();
            return;
        }
        if (discount > lineTotal) {
            DialogUtils.showWarningAlert(
                    "Descuento inválido",
                    "El descuento no puede ser mayor al total (" + lineTotal + ").",
                    null
            );
            salesTable.refresh();
            return;
        }
        det.setDiscountLine(discount);
        det.setTotalLine(lineTotal - discount);
        updateSubtotal();
        salesTable.refresh();
    }

    /**
     * Increments quantity for an existing product in the table.
     */
    private void incrementExistingProduct(SaleDetail existingDetail) {
        double currentQty = existingDetail.getQuantity() != null ? existingDetail.getQuantity() : 0.0;
        double newQty = currentQty + 1;
        if (newQty > existingDetail.getAmountEntered()) {
            DialogUtils.showWarningAlert(
                    "Stock Excedido",
                    "No puedes vender más de lo disponible (" + existingDetail.getAmountEntered() + ").",
                    null
            );
            return;
        }
        existingDetail.setQuantity(newQty);
        double price = existingDetail.getUnitPrice() != null ? existingDetail.getUnitPrice() : 0.0;
        double discount = existingDetail.getDiscountLine() != null ? existingDetail.getDiscountLine() : 0.0;
        existingDetail.setTotalLine((newQty * price) - discount);
        salesTable.refresh();
        txtProductCode.clear();
    }

    /**
     * Adds a new product entry to the sales table.
     */
    private void addNewProductToTable() {
        saleDetail.setQuantity(1.0);
        double price = saleDetail.getUnitPrice() != null ? saleDetail.getUnitPrice() : 0.0;
        saleDetail.setTotalLine(price);
        salesTable.getItems().add(saleDetail);
        txtProductCode.clear();
    }

    public void clearFields(ActionEvent actionEvent) {
        FormUtils.clearFields(rootPane);
    }
}
