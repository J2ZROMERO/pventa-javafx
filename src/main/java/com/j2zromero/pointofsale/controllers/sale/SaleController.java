package com.j2zromero.pointofsale.controllers.sale;

import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.services.product.ProductService;
import com.j2zromero.pointofsale.services.sale.SaleService;
import com.j2zromero.pointofsale.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

import java.sql.SQLException;

public class SaleController {

    private Product currentProduct;
    private ProductService productService = new ProductService(); // Initialize your service
    private ObservableList<Sale> salesList = FXCollections.observableArrayList();
    private Sale sale;
    private SaleService salesService = new SaleService();
    @FXML
    private TextField txt_code;

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
    }


    @FXML
    public void search_product() {
        String code = txt_code.getText().trim();
        if (code.isEmpty()) {
            AlertUtils.showWarningAlert("Producto", "Inserte código del producto.", txt_code);
            return;
        }

        try {
            currentProduct = productService.getByCode(code);
            if (currentProduct != null) {
                System.out.println(currentProduct.getStock());

                System.out.println(currentProduct.getName());
                addProductToTable(currentProduct);
            } else {
                AlertUtils.showWarningAlert("Producto", "No se encontró el producto con el código: " + code, txt_code);
            }
        } catch (SQLException e) {
            e.printStackTrace();
           // AlertUtils.showErrorAlert("Error", "Error al buscar el producto.");
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
                txt_code.clear(); // Clear the input field
                return; // Exit the method
            }
        }
        System.out.println(  product.getStock());
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
            AlertUtils.showWarningAlert("Eliminar", "No hay ninguna fila seleccionada.", null);
        }
    }

    @FXML
    public void saleProduct() {
        if (salesList.isEmpty()) {
            AlertUtils.showWarningAlert("Guardar Ventas", "No hay registros para guardar.", null);
            return;
        }

        try {
            salesService.saveAllSales(salesList); // Save all sales using the service
            //AlertUtils.showWarningAlert("Guardar Ventas", "Registros guardados exitosamente.");
            salesList.clear(); // Optionally clear the table after saving
            sales_table.refresh(); // Refresh the table to reflect changes
        } catch (IllegalArgumentException e) {
            AlertUtils.showWarningAlert("Guardar Ventas", e.getMessage(), null);
        } catch (SQLException e) {
            e.printStackTrace();
            //AlertUtils.showErrorAlert("Error", "Ocurrió un error al guardar los registros.");
        }
    }
}
