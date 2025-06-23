package com.j2zromero.pointofsale.controllers.sale;

import com.j2zromero.pointofsale.models.payments.Payment;
import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.models.sale.SaleDetail;
import com.j2zromero.pointofsale.services.product.ProductService;
import com.j2zromero.pointofsale.services.sale.SaleService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.InputUtils;
import com.j2zromero.pointofsale.utils.UnitType;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DoubleStringConverter;

import java.sql.SQLException;

public class SaleController {

    public Label lblCashier;
    public Label lblTerminal;
    public ComboBox cbxPaymentMethod;
    public TextField txtSubtotal;
    public TextField txtDiscount;
    public TextField txtChange;
    public TextField txtPay;
    public TextField txtProductCode;
    public Button btnAddproduct;
    public TableView<SaleDetail> salesTable;

    @FXML private TableColumn<SaleDetail, String> productNameColumn;
    @FXML private TableColumn<SaleDetail, Double> availableColumn;
    @FXML private TableColumn<SaleDetail, String> unitMeasurementColumn;
    @FXML private TableColumn<SaleDetail, Double> unitPriceColumn;
    @FXML private TableColumn<SaleDetail, Double> quantityColumn;
    @FXML private TableColumn<SaleDetail, Double> discountColumn;
    @FXML private TableColumn<SaleDetail, Double> totalSoldColumn;

    public Button btnRemoveRow;
    public Button btnSaleProduct;
    public Button btnClearTable;
    public TextField txtReceived;
    public TextField txtTotal;
    public Button btnGenerateSell;


    private Product currentProduct;
    private ProductService productService = new ProductService(); // Initialize your service
    private ObservableList<Sale> salesList = FXCollections.observableArrayList();
    private Sale sale;
    private SaleService salesService = new SaleService();
    private SaleDetail saleDetail = new SaleDetail();

    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize() {
        Payment defaultPayment = new Payment(1L, "cash", "Efectivo");

        cbxPaymentMethod.getItems().add(defaultPayment);
        cbxPaymentMethod.setValue(defaultPayment);


       /* // Set up the TableView columns
        product_name_column.setCellValueFactory(new PropertyValueFactory<>("productName"));
        sold_amount_column.setCellValueFactory(new PropertyValueFactory<>("soldAmount"));
        unit_price_column.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        total_sold_column.setCellValueFactory(new PropertyValueFactory<>("totalSale"));
        product_available_column.setCellValueFactory(new PropertyValueFactory<>("amountAvailable"));

        // Bind the ObservableList to the TableView
        sales_table.setItems(salesList);

        // Allow editing of specific columns
        sales_table.setEditable(true);
        sold_amount_column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        unit_price_column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        // Update totalSale when soldAmount is edited
        sold_amount_column.setOnEditCommit(event -> {
            Sale sale = event.getRowValue(); // Get the edited Sale
            double newSoldAmount = event.getNewValue(); // Get the new soldAmount value

            // Check if the new sold amount exceeds the available stock
            if (newSoldAmount > sale.getAmountAvailable()) {
                DialogUtils.showWarningAlert(
                        "Stock Limit",
                        "No puedes vender más unidades de las disponibles. Disponible: " + sale.getAmountAvailable(),
                        null
                );

                // Revert to the original value by refreshing the table
                sales_table.refresh();
                return;
            }

            // Update the soldAmount and recalculate the totalSale
            sale.setSoldAmount(newSoldAmount);
            sale.setTotalSale(newSoldAmount * sale.getUnitPrice());
            sales_table.refresh(); // Refresh the table to reflect changes
            calculateTotal(); // Recalculate the total for the updated quantity
        });

        // Add key event listener for Enter key in txt_code
        txt_code.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    search_product(); // Call the method for the "Agregar" button
                    break;
                default:
                    break;
            }
        });
        // Add global key listener for F9
                rootPane.setOnKeyPressed(event -> {
                    if (event.getCode() == javafx.scene.input.KeyCode.F9) {
                        saleProduct(); // Call saleProduct when F9 is pressed
                    }
                });

        // Add global key listener for F9 to trigger saleProduct
        sales_table.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.DELETE) {
                removeSelectedRow(); // Call removeSelectedRow when Delete or Suprimir is pressed
            }
        });

        // Calculate total when the sales list changes
        salesList.addListener((ListChangeListener<Sale>) change -> calculateTotal());

        // Calculate change when the pay amount changes
        txt_pay.textProperty().addListener((observable, oldValue, newValue) -> calculateChange());
*/
    }

    public void AddSale(ActionEvent actionEvent) {

      if (salesTable.getItems().isEmpty()) {
            DialogUtils.showWarningAlert("Venta", "No hay productos en la venta.", null);
            return;
        }

        Sale sale = new Sale();
        sale.setTerminalId(lblTerminal.getText());
        sale.setCashierId(lblCashier.getText());
        //sale.setClientId(null);
        sale.setSubtotal(InputUtils.parseDouble(txtSubtotal.getText()));
        sale.setDiscount(InputUtils.parseDouble(txtDiscount.getText()));
        sale.setTotal(InputUtils.parseDouble(txtTotal.getText()));
        UnitType uniType =  (UnitType) cbxPaymentMethod.getSelectionModel().getSelectedItem();
        ///sale.setPaymentMethod(payment.getCode());

    }

    public void addProductToTable(ActionEvent actionEvent) {
        String productCode = txtProductCode.getText().trim();
        Payment payment =  (Payment)cbxPaymentMethod.getSelectionModel().getSelectedItem();
        if(productCode.isEmpty()){
            DialogUtils.showWarningAlert("Producto", "Debes seleccionar algun producto.", txtProductCode);
            return;
        }


        try {
            saleDetail =  salesService.getProductFromInventory(productCode);

            if (saleDetail == null || saleDetail.getProductCode() == null || saleDetail.getProductCode().trim().isEmpty()) {
                DialogUtils.showWarningAlert("Producto", "No hay un producto con el codigo escrito, intenta nuevamente.", txtProductCode);
                return;
            }

            if (saleDetail == null || saleDetail.getAmountEntered() == null || saleDetail.getAmountEntered() <= 0) {
                DialogUtils.showWarningAlert("Producto", "No hay productos disponibles.", txtProductCode);
                return;
                    }

                salesTable.setEditable(true);
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
            availableColumn.setCellValueFactory(new PropertyValueFactory<>("amountEntered"));
            unitMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("unitMeasurement"));
            unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            discountColumn.setCellValueFactory(new PropertyValueFactory<>("discountLine"));
            totalSoldColumn.setCellValueFactory(new PropertyValueFactory<>("totalLine"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

            discountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            totalSoldColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

            // Make quantity editable and recalculate total
            quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            // Editable columns
            quantityColumn.setOnEditCommit((TableColumn.CellEditEvent<SaleDetail, Double> ev) -> {
                SaleDetail det = ev.getRowValue();
                double qty = ev.getNewValue() != null ? ev.getNewValue() : 0.0;
                if (qty > det.getAmountEntered()) {
                    DialogUtils.showWarningAlert(
                            "Stock Excedido",
                            "No puedes vender más de lo disponible (" + det.getAmountEntered() + ").",
                            null
                    );
                    salesTable.refresh();
                    return;
                }
                det.setQuantity(qty);
                det.setTotalLine(qty * (det.getUnitPrice() != null ? det.getUnitPrice() : 0.0));
                salesTable.refresh();
            });

            discountColumn.setOnEditCommit((TableColumn.CellEditEvent<SaleDetail, Double> ev) -> {
                SaleDetail det = ev.getRowValue();
                double discount = ev.getNewValue() != null ? ev.getNewValue() : 0.0;

                double qty = det.getQuantity() != null ? det.getQuantity() : 0.0;
                if (qty <= 0) {
                    DialogUtils.showWarningAlert(
                            "Descuento inválido",
                            "Primero necesitas ingresar una cantidad válida para aplicar descuento.",
                            null
                    );
                    salesTable.refresh();
                    return;
                }

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
                det.setTotalLine(lineTotal - discount);
                salesTable.refresh();
            });

            salesTable.getItems().add(saleDetail);

            // Limpiar campo
            txtProductCode.clear();


        /*sold_amount_column.setCellValueFactory(new PropertyValueFactory<>("soldAmount"));
        unit_price_column.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        total_sold_column.setCellValueFactory(new PropertyValueFactory<>("totalSale"));
        product_available_column.setCellValueFactory(new PropertyValueFactory<>("amountAvailable"));

        // Bind the ObservableList to the TableView
        sales_table.setItems(salesList);

        // Allow editing of specific columns
        */
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

/*
    @FXML
    public void search_product() {
        String code = txt_code.getText().trim();
        if (code.isEmpty()) {
            DialogUtils.showWarningAlert("Producto", "Inserte código del producto.", txt_code);
            return;
        }

        try {
            currentProduct = productService.getByCode(code);
            if (currentProduct != null) {
                addProductToTable(currentProduct);
            } else {
                DialogUtils.showWarningAlert("Producto", "No se encontró el producto con el código: " + code, txt_code);
            }
        } catch (SQLException e) {
            e.printStackTrace();
           // AlertUtils.showErrorAlert("Error", "Error al buscar el producto.");
        }
    }*/
/*
    private void calculateTotal() {
        double total = 0.0;

        // Sum up the totalSale column for all rows
        for (Sale sale : salesList) {
            total += sale.getTotalSale();
        }

        // Update the total field
        txt_total.setText(String.format("%.2f", total));

        // Recalculate the change in case the pay amount is already entered
        calculateChange();
    }

    private void calculateChange() {
        try {
            // Parse total and pay amounts
            double total = Double.parseDouble(txt_total.getText());
            double pay = Double.parseDouble(txt_pay.getText());

            // Calculate the change
            double change = pay - total;

            // Update the change field
            txt_change.setText(String.format("%.2f", change));
        } catch (NumberFormatException e) {
            // If parsing fails, reset change to 0
            txt_change.setText("0.00");
        }
    }

    private void addProductToTable(Product product) {
        // Check if the product already exists in the sales list
        for (Sale existingSale : salesList) {
            if (existingSale.getIdProduct().equals(product.getId())) {
                // Product exists, increment the soldAmount and check availability
                double newSoldAmount = existingSale.getSoldAmount() + 1.0;

                if (newSoldAmount > existingSale.getAmountAvailable()) {
                    DialogUtils.showWarningAlert("Stock Limit", "No hay más unidades disponibles para este producto.", txt_code);
                    return; // Exit if trying to exceed availability
                }

                existingSale.setSoldAmount(newSoldAmount);
                existingSale.setTotalSale(newSoldAmount * existingSale.getUnitPrice());
                sales_table.refresh(); // Refresh the table to update the UI
                calculateTotal(); // Ensure total is recalculated
                txt_code.clear(); // Clear the input field
                return; // Exit the method
            }
        }

        // Product does not exist, create a new Sale entry
        boolean isUnitPrice = true;
        if (product.getUnitPrice() == null || product.getUnitPrice() == 0) {
            isUnitPrice = false;
        }
        Sale sale = new Sale(
                product.getId(),
                1.0, // Default sold amount
                isUnitPrice ? product.getUnitPrice() : product.getVolumePrice(),
                isUnitPrice ? product.getUnitPrice() : product.getVolumePrice(), // Total sale = unit price * quantity
                "Normal", // Default sale type
                product.getName(),
                product.getStock()
        );

        // Check availability for the new product
        if (sale.getSoldAmount() > sale.getAmountAvailable()) {
            DialogUtils.showWarningAlert("Stock Limit", "No hay más unidades disponibles para este producto.", txt_code);
            return; // Exit if trying to exceed availability
        }

        // Add the sale to the ObservableList
        salesList.add(sale);

        // Calculate total after adding a product
        calculateTotal();

        // Clear the input field for new searches
        txt_code.clear();
    }

    @FXML
    public void removeSelectedRow() {
        // Get the selected row
        Sale selectedSale = sales_table.getSelectionModel().getSelectedItem();
        if (selectedSale != null) {
            salesList.remove(selectedSale);
        } else {
            DialogUtils.showWarningAlert("Eliminar", "No hay ninguna fila seleccionada.", null);
        }
    }

    @FXML
    public void saleProduct() {
        if (salesList.isEmpty()) {
            DialogUtils.showWarningAlert("Guardar Ventas", "No hay registros para guardar.", null);
            return;
        }

        try {
            salesService.saveAllSales(salesList); // Save all sales using the service
            //AlertUtils.showWarningAlert("Guardar Ventas", "Registros guardados exitosamente.");
            salesList.clear(); // Optionally clear the table after saving
            sales_table.refresh(); // Refresh the table to reflect changes
        } catch (IllegalArgumentException e) {
            DialogUtils.showWarningAlert("Guardar Ventas", e.getMessage(), null);
        } catch (SQLException e) {
            e.printStackTrace();
            //AlertUtils.showErrorAlert("Error", "Ocurrió un error al guardar los registros.");
        }
    }


    @FXML
    public void clearTable() {
        if (!salesList.isEmpty()) {
            // Show confirmation dialog before clearing the table
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmar limpieza");
            confirmationAlert.setHeaderText("¿Estás seguro de limpiar la tabla?");
            confirmationAlert.setContentText("Esta acción eliminará todos los registros de la tabla y no se puede deshacer.");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Clear the table
                    salesList.clear();
                    sales_table.refresh();
                }
            });
        } else {
            DialogUtils.showWarningAlert("Limpieza de tabla", "La tabla ya está vacía.", null);
        }
    }
*/
}
