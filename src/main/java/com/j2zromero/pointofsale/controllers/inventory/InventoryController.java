package com.j2zromero.pointofsale.controllers.inventory;

import com.j2zromero.pointofsale.models.inventories.Inventory;
import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.services.inventory.InventoryService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class InventoryController {

    public ComboBox cbx_fk_product;
    @FXML
    private TableColumn<Product, String> name_product_column;

    @FXML
    private TableColumn<Product, String> name_date_column;

    @FXML
    private TableColumn<Product, String> name_inventory_column;

    @FXML
    private TextField search_product_field;


    @FXML
    private TextField search_inventory_field;


    @FXML
    private Pane inventory_fields;

    @FXML
    private TextField txt_product;

    @FXML
    private DatePicker date_register;

    @FXML
    private TextField txt_amount_entered;

    @FXML
    private TextField txt_amount_available;

    @FXML
    private DatePicker date_expiration;

    @FXML
    private TextField txt_location;
    private Product currentProduct;
    private Inventory selectedInventory;
    private InventoryService inventoryService = new InventoryService();
    List<Inventory> inventoryList;

    public void initialize() {
        try {
            // load product data
            inventoryList = inventoryService.getAll();
            System.out.println(inventoryList);
            FormUtils.applyComboBoxFilter(cbx_fk_product,inventoryList, inventory ->  inventory.getProduct_name());
            FormUtils.applyNumericDoubleFilter(txt_amount_available);
            FormUtils.applyNumericDoubleFilter(txt_amount_entered);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addTableKeyListeners() {
        // Handle keys for the product table
        inventory_fields.setOnKeyPressed(event -> {
            if (event.getCode().equals(javafx.scene.input.KeyCode.ENTER)) {
                add(new ActionEvent()); // Trigger the add method
            }
        });


    }


    private java.time.LocalDate convertToLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            // Convert java.sql.Date to java.time.LocalDate directly
            return ((java.sql.Date) date).toLocalDate();
        } else if (date != null) {
            // Convert java.util.Date to java.time.LocalDate via Instant
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }


    @FXML
    private void add(ActionEvent actionEvent) {
        if (currentProduct == null || currentProduct.getName() == null ) {
            DialogUtils.showWarningAlert("Input Error", "Seleccione un producto.",txt_product);
            return;
        }

        try {
            Inventory inventory = new Inventory();
            inventory.setFkProductCode(currentProduct.getCode());
            inventory.setAmountEntered(parseDouble(txt_amount_entered.getText()));
            inventory.setAmountAvailable(parseDouble(txt_amount_available.getText()));
            inventory.setExpirationDate(convertToDate(date_expiration));
            inventory.setLocation(txt_location.getText());
            System.out.println(currentProduct.getCode());
            inventory.setProductCode(currentProduct.getCode());
            inventoryService.add(inventory);

            loadData();
            cleanFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void update() {

        try {
            Inventory inventory = new Inventory();
            inventory.setId(selectedInventory.getId());
            inventory.setFkProductCode(currentProduct.getCode());
            inventory.setAmountEntered(parseDouble(txt_amount_entered.getText()));
            inventory.setAmountAvailable(parseDouble(txt_amount_available.getText()));
            inventory.setExpirationDate(convertToDate(date_expiration));
            inventory.setLocation(txt_location.getText());

            inventoryService.update(inventory);
            cleanFields();
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void delete() {

        // Create a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmar eliminación");
        confirmationAlert.setHeaderText("¿Estás seguro de eliminar este producto del inventario?");
        confirmationAlert.setContentText("Esta acción no se puede deshacer.");

        // Show the alert and wait for the user's response
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed, proceed with deletion
                try {
                    inventoryService.delete(selectedInventory.getId());
                    cleanFields();
                    loadData();
                } catch (SQLException e) {
                    e.printStackTrace();
                    DialogUtils.showWarningAlert("Error", "Ocurrió un error al eliminar el producto del inventario.", null);
                }
            } else {
                // User cancelled, no action required
                System.out.println("Eliminación cancelada por el usuario.");
            }
        });
    }



    private Date convertToDate(DatePicker datePicker) {
        if (datePicker.getValue() != null) {
            return Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }


    private Double parseDouble(String value) {
        try {
            return value != null && !value.trim().isEmpty() ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public void cleanFields() {

        FormUtils.clearFields(inventory_fields);
      //  selectedProduct = new Product();
        selectedInventory = new Inventory();

    }

    private void loadData() throws SQLException {
        List<Inventory> inventory = inventoryService.getAll();
        //inventoryList.setAll();
    }

}
