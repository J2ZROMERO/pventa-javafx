package com.j2zromero.pointofsale.controllers.sale;

import com.j2zromero.pointofsale.models.payments.Payment;
import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.sale.SaleDetail;
import com.j2zromero.pointofsale.models.terminal.Terminal;
import com.j2zromero.pointofsale.services.printer.PrinterService;
import com.j2zromero.pointofsale.services.sale.SaleService;
import com.j2zromero.pointofsale.services.product.ProductService;
import com.j2zromero.pointofsale.services.terminal.TerminalService;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import com.j2zromero.pointofsale.utils.InputUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Controller for handling sales operations in the POS system.
 * Manages adding products, calculating totals, and finalizing sales.
 */
public class SaleController {

    public CheckBox ckPrint;
    public TableColumn codeProductColumn;
    public TableColumn totalInPackageColumn;
    public TableColumn codePriceColumn;
    public TableColumn code_price;
    public TableColumn descriptionColumn;
    @FXML private TableColumn<SaleDetail, Double> extraColumn;

    @FXML private TableColumn<SaleDetail, Double> pieceColumn;
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
    private PrinterService printerService = new PrinterService();
    /**
     * Initializes UI state, default values, and event listeners.
     */
    @FXML
    public void initialize() throws SQLException {
        DialogUtils.TooltipHelper.install(btnAddproduct,
                "Agregar producto",
                DialogUtils.TooltipColor.DARK);

        DialogUtils.TooltipHelper.install(btnClearTable,
                "Limpiar tabla",
                DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnRemoveRow,
                "Quitar de la venta",
                DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnGenerateSell,
                "Generar venta",
                DialogUtils.TooltipColor.DARK);



        Platform.runLater(() -> {
            if (rootPane.getScene() != null) {
                rootPane.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
        Platform.runLater(() -> {
            txtProductCode.requestFocus();
        });
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

        Platform.runLater(() -> {
            Scene scene = rootPane.getScene();
            scene.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
                if (evt.getCode() == KeyCode.F9) {
                    // call your sale finalization
                    AddSale(new ActionEvent());
                    evt.consume();
                }
            });
        });
        // Recalculate totals when discount changes
        txtDiscount.textProperty().addListener((obs, oldText, newText) -> updateTotalWithDiscount(newText));

        // Recalculate change when amount received changes
        txtReceived.textProperty().addListener((obs, oldText, newText) -> updateChange(newText));

        // Add user name
        lblCashier.setText(UserService.getUser().getName());
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

        boolean anyMissingPrice = salesTable.getItems().stream().anyMatch(item -> item.getTotalLine() == null || item.getTotalLine() <= 0);
        if (anyMissingPrice) {
            DialogUtils.showWarningAlert("Venta inválida", "Hay productos sin precio total válido. Por favor verifica cada línea antes de continuar.", null);
            return;
        }


        Sale sale = new Sale();
        sale.setTerminalId(terminal.getId());
        sale.setCashierId(UserService.getUser().getId());
        sale.setSubtotal(InputUtils.parseDouble(txtSubtotal.getText()));
        sale.setDiscount(InputUtils.parseDouble(txtDiscount.getText()));
        sale.setTotal(InputUtils.parseDouble(txtTotal.getText()));
        Payment uniType = cbxPaymentMethod.getSelectionModel().getSelectedItem();
        sale.setPaymentMethod(uniType.getCode());
        sale.setCajaId(UserService.getCajaId());
        // 2) Grab all details from the TableView
        List<SaleDetail> details = new ArrayList<>( salesTable.getItems() );
        System.out.println(details);
        try {
            Long saleId = salesService.add(sale, details);
            sale.setId(saleId);


            DialogUtils.showToast("Venta generada con exito!",2);

            try {

                if(ckPrint.isSelected()){
                    printerService.openCashDrawer();
                    printerService.printReceipt(sale,details);
                }
                else{
                    printerService.openCashDrawer();
                }
                salesTable.getItems().clear();
                productCodeFocus();


            } catch (SQLException ex) {
                DialogUtils.showWarningAlert("Error","La venta a sido generada pero la impresora no fue encontrada", null);
            }

        } catch (SQLException e) {
            DialogUtils.showWarningAlert("Error","La venta no pudo ser completada", null);
            throw new RuntimeException(e);


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
                DialogUtils.showWarningAlert("Producto", "No hay un producto con el codigo escrito o no tiene stock disponible, intenta nuevamente.", txtProductCode);
                txtProductCode.setText("");
                return;
            }
            if (saleDetail.getStock() == null || saleDetail.getStock() <= 0) {
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
        codeProductColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        unitMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("unitMeasurement"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discountLine"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalSoldColumn.setCellValueFactory(new PropertyValueFactory<>("totalLine"));
        totalInPackageColumn.setCellValueFactory(new PropertyValueFactory<>("totalInPackage"));
        code_price.setCellValueFactory(new PropertyValueFactory<>("codePrice"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        unitPriceColumn.setCellValueFactory(
                new PropertyValueFactory<>("unitPrice")
        );
        extraColumn.setCellValueFactory(new PropertyValueFactory<>("extraLine"));

        setupEditableColumn(extraColumn, this::onExtraEditCommit);

        unitPriceColumn.setCellFactory(col -> new TableCell<SaleDetail, Double>() {
            private final ComboBox<PriceOption> combo = new ComboBox<>();

            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                SaleDetail det = getTableView().getItems().get(getIndex());

                double unitPrice    = det.getUnitPrice();
                double lastPrice    = det.getLastPrice() != null ? det.getLastPrice() : unitPrice;
                Double packagePrice = det.getPackagePrice();

                // Crear opciones con etiqueta
                PriceOption option1 = new PriceOption(lastPrice, det.getUnitMeasurement());
                PriceOption option2 = new PriceOption(packagePrice != null ? packagePrice : 0, "pack");

                if (det.isHasPackageLogic()) {
                    combo.setItems(FXCollections.observableArrayList(option1, option2));
                    // Seleccionar la opción activa por valor
                    PriceOption currentSelection = combo.getItems().stream()
                            .filter(opt -> Double.compare(opt.getValue(), unitPrice) == 0)
                            .findFirst()
                            .orElse(option1);
                    det.setCodePrice(currentSelection.getCode());
                    combo.getSelectionModel().select(currentSelection);
                    combo.setMaxWidth(Double.MAX_VALUE);

                    combo.valueProperty().addListener((obs, oldV, newV) -> {
                        if (newV == null) return;

                        // 1) Calcular cuántas piezas resultan con la selección nueva
                        double qtyPieces;
                        if ("pack".equalsIgnoreCase(newV.getCode())) {         // nueva selección = paquete
                            qtyPieces = (det.getQuantity() == null ? 0d : det.getQuantity())
                                    * det.getTotalInPackage();             // piezas = cant * piezas/pack
                        } else {                                               // selección = unidad
                            qtyPieces = (det.getQuantity() == null ? 0d : det.getQuantity());
                        }

                        // 2) Validar contra stock
                        if (qtyPieces > det.getStock()) {
                            DialogUtils.showWarningAlert(
                                    "Stock Excedido",
                                    "No puedes vender más de lo disponible (" + det.getStock() + ").",
                                    null
                            );
                            // Revertir selección
                            combo.getSelectionModel().select(oldV);
                            return;
                        }

                        // 3) Selección válida → actualizar modelo
                        det.setCodePrice(newV.getCode());              // guarda "pack" o "unit"
                        det.setUnitPrice(newV.getValue());

                        if (packagePrice != null && Double.compare(packagePrice, oldV.getValue()) != 0) {
                            det.setLastPrice(oldV.getValue());
                        }

                        det.setTotalLine(
                                (det.getQuantity() == null ? 0d : det.getQuantity()) * newV.getValue()
                                        - (det.getDiscountLine() == null ? 0d : det.getDiscountLine())
                        );

                        commitEdit(newV.getValue());
                        recalcTotals();
                        getTableView().refresh();
                    });

                    setText(null);
                    setGraphic(combo);
                } else {
                    setGraphic(null);
                    setText(String.format(Locale.US, "$%.2f", unitPrice));
                }

            }
        });


        String centerStyle = "-fx-alignment: CENTER;";
        productNameColumn.setStyle(centerStyle);
        availableColumn.setStyle(centerStyle);
        unitMeasurementColumn.setStyle(centerStyle);
        unitPriceColumn.setStyle(centerStyle);
        quantityColumn.setStyle(centerStyle);
        codeProductColumn.setStyle(centerStyle);
        discountColumn.setStyle(centerStyle);
        totalSoldColumn.setStyle(centerStyle);
        extraColumn.setStyle(centerStyle);
        actionsColumn.setCellFactory(createRemoveButtonCellFactory());
        setupEditableColumn(quantityColumn, this::onQuantityEditCommit);
        setupEditableColumn(discountColumn, this::onDiscountEditCommit);

    }

    /** Recalcula subtotal, total y cambio SIN tocar el descuento ya escrito. */
    private void recalcTotals() {
        double subtotal = salesTable.getItems().stream()
                .mapToDouble(d -> d.getTotalLine() == null ? 0d : d.getTotalLine())
                .sum();
        txtSubtotal.setText(String.format("%.2f", subtotal));

        double discount = parseDoubleSafely(txtDiscount.getText());
        txtTotal.setText(String.format("%.2f", subtotal - discount));

        // si el usuario ya escribió efectivo recibido, vuelve a calcular el cambio
        updateChange(txtReceived.getText());
    }

    /**
     * Maneja la edición de extraColumn: suma extra al totalLine.
     */
    private void onExtraEditCommit(TableColumn.CellEditEvent<SaleDetail, Double> ev) {
        SaleDetail det = ev.getRowValue();
        double extra = ev.getNewValue() != null ? ev.getNewValue() : 0.0;
        det.setExtraLine(extra);  // Asegúrate de que SaleDetail tenga este campo con getter/setter

        double qty      = det.getQuantity()     != null ? det.getQuantity()     : 0.0;
        double price    = det.getUnitPrice()    != null ? det.getUnitPrice()    : 0.0;
        double discount = det.getDiscountLine() != null ? det.getDiscountLine() : 0.0;

        // Recalcula totalLine incluyendo extra
        det.setTotalLine(qty * price - discount + extra);

        // Vuelve a calcular subtotales y refresca la tabla
        updateSubtotal();
        salesTable.refresh();
    }


    /**
     * Creates a cell factory that adds a delete button to each row.
     */
    private Callback<TableColumn<SaleDetail, Void>, TableCell<SaleDetail, Void>> createRemoveButtonCellFactory() {
        return col -> new TableCell<>() {
            private final Button btnDelete = new Button("X");
            private final Button btnInfo = new Button("👁");
            private final Region spacer = new Region();
            private final HBox container = new HBox(spacer, btnInfo, btnDelete);
            {
                btnDelete.setOnAction(e -> getTableView().getItems().remove(getIndex()));
                DialogUtils.TooltipHelper.install(btnDelete, "Quitar producto de la venta", DialogUtils.TooltipColor.DARK);

                btnInfo.getStyleClass().add("icon-button");
                btnDelete.getStyleClass().add("icon-button");
                btnDelete.setStyle("-fx-background-color: #e74c3c;");




                DialogUtils.TooltipHelper.install(btnInfo, "Ver descripcion del producto", DialogUtils.TooltipColor.DARK);
                btnInfo.setOnAction(e -> {
                    SaleDetail det = getTableView().getItems().get(getIndex());
                    DialogUtils.showConfirmationDialog("Descripción", det.getDescription() , null, "INFORMATION");
                });

                HBox.setHgrow(spacer, Priority.ALWAYS);
                container.setAlignment(Pos.CENTER_RIGHT);
                container.setPadding(new Insets(0, 5, 0, 0));
                container.setSpacing(3);
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
        if (newQty > det.getStock()) {
            DialogUtils.showWarningAlert(
                    "Stock Excedido",
                    "No puedes vender más de lo disponible (" + det.getStock() + ").",
                    null
            );
            salesTable.refresh();
            return;
        }

        if ("pack".equalsIgnoreCase(det.getCodePrice())) {
            double total = newQty * det.getTotalInPackage();

            if(total > det.getStock()){
                DialogUtils.showWarningAlert(
                        "Stock Excedido",
                        "No puedes vender más paquetes de lo disponibles (" + det.getStock() + ").",
                        null
                );
                salesTable.refresh();
                return;
            }

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
        det.setTotalLine(lineTotal- discount);
        updateSubtotal();
        salesTable.refresh();
    }

    /**
     * Increments quantity for an existing product in the table.
     */
    private void incrementExistingProduct(SaleDetail existingDetail) {
        double currentQty = existingDetail.getQuantity() != null ? existingDetail.getQuantity() : 0.0;
        double newQty = currentQty + 1;
        if (newQty > existingDetail.getStock()) {
            DialogUtils.showWarningAlert(
                    "Stock Excedido",
                    "No puedes vender más de lo disponible (" + existingDetail.getStock() + ").",
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
d     * Adds a new product entry to the sales table.
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

class PriceOption {
    private final double value;
    private final String label;
    private final String code;

    public PriceOption(double value, String code) {
        this.value = value;
        this.code = code;
        switch (code.toLowerCase()) {
            case "kg":
            case "kl":
                this.label = "Kilogramo"; // or "Kilogram" if you want it in English
                break;
            case "lt":
                this.label = "Litro";     // or "Liter"
                break;
            case "pz":
                this.label = "Pieza";
                break;
            case "box":
                this.label = "Caja";
                break;
            case "paquete":
            case "pack":
                this.label = "Paquete";
                break;
            default:
                this.label = code.toUpperCase(); // fallback
        }
    }

    public double getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public String getCode(){
        return this.code;
    }
    @Override
    public String toString() {
        // Muestra en el ComboBox: "$20.00 - Paquete"
        return String.format("$%.2f - %s", value, label);
    }
}
