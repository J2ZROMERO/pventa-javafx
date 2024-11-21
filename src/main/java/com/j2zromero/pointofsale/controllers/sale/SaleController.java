package com.j2zromero.pointofsale.controllers.sale;

import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.services.product.ProductService;
import com.j2zromero.pointofsale.services.sale.SaleService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DoubleStringConverter;

import java.sql.SQLException;

public class SaleController {

    private Product currentProduct;
    private ProductService productService = new ProductService(); // Initialize your service
    private ObservableList<Sale> salesList = FXCollections.observableArrayList();
    private Sale sale;
    private SaleService salesService = new SaleService();

    @FXML
    private TextField txt_total;

    @FXML
    private TextField txt_pay;

    @FXML
    private TextField txt_change;

    @FXML
    private TextField txt_code;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Sale> sales_table;

    @FXML
    private TableColumn<Sale, String> product_name_column;

    @FXML
    private TableColumn<Sale, Double> sold_amount_column;

    @FXML
    private TableColumn<Sale, Double> unit_price_column;

    @FXML
    private TableColumn<Sale, Double> total_sold_column;

    @FXML
    private TableColumn<Sale, Double> product_available_column;

    @FXML
    private Button btn_add_product;

    @FXML
    private Button btn_remove_row;

    @FXML
    public void initialize() {
        // Set up the TableView columns
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
            sale.setSoldAmount(newSoldAmount); // Update the soldAmount
            sale.setTotalSale(newSoldAmount * sale.getUnitPrice()); // Recalculate the totalSale
            sales_table.refresh(); // Refresh the table to reflect changes
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

    }


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
                System.out.println(currentProduct.getStock());

                System.out.println(currentProduct.getName());
                addProductToTable(currentProduct);
            } else {
                DialogUtils.showWarningAlert("Producto", "No se encontró el producto con el código: " + code, txt_code);
            }
        } catch (SQLException e) {
            e.printStackTrace();
           // AlertUtils.showErrorAlert("Error", "Error al buscar el producto.");
        }
    }

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
                // Product exists, increment the soldAmount and update totalSale
                double newSoldAmount = existingSale.getSoldAmount() + 1.0;
                existingSale.setSoldAmount(newSoldAmount);
                existingSale.setTotalSale(newSoldAmount * existingSale.getUnitPrice());
                sales_table.refresh(); // Refresh the table to update the UI
                calculateTotal(); // Ensure total is recalculated
                txt_code.clear(); // Clear the input field
                return; // Exit the method
            }
        }

        // Product does not exist, create a new Sale entry
        Sale sale = new Sale(
                product.getId(),
                1.0, // Default sold amount
                product.getUnitPrice(),
                product.getUnitPrice(), // Total sale = unit price * quantity
                "Normal", // Default sale type
                product.getName(),
                product.getStock()
        );

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

}
