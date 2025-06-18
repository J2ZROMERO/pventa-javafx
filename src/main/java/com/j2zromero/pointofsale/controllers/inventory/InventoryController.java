package com.j2zromero.pointofsale.controllers.inventory;

import com.j2zromero.pointofsale.models.inventories.Inventory;
import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.services.inventory.InventoryService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import com.j2zromero.pointofsale.utils.InputUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class InventoryController {


    public TextField txtAmountEntered;
    public TextField txtAmountAvailable;
    public DatePicker dateExpirationDate;
    public ComboBox cbxSelectedProduct;
    public TextField txtBatchNumber;
    public TextField txtStatus;
    public TextField txtTotalAmount;
    public TextArea txtLocation;
    @FXML
    private Pane inventory_fields;

    @FXML
    private Product currentProduct;
    private Inventory selectedInventory;
    private InventoryService inventoryService = new InventoryService();
    List<Inventory> inventoryList;

    public void initialize() {
        try {
            // load product data
            inventoryList = inventoryService.getAll();

            FormUtils.applyComboBoxFilter(cbxSelectedProduct,inventoryList, inventory ->  inventory.getProductName(), selected -> {

                if (selected == null) {
                    return;
                }

                selectedInventory = selected;

                // Populate form fields with selectedInventory data
                txtAmountEntered.setText(String.valueOf(selectedInventory.getAmountEntered()));
                txtAmountAvailable.setText(String.valueOf(selectedInventory.getAmountAvailable()));
                txtBatchNumber.setText(selectedInventory.getBatchNumber());
                txtStatus.setText(selectedInventory.getStatus());
                txtLocation.setText(selectedInventory.getLocation());

                if (selectedInventory.getExpirationDate() != null) {
                    dateExpirationDate.setValue(
                            selectedInventory.getExpirationDate()
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                    );
                } else {
                    dateExpirationDate.setValue(null);
                }

            });


            FormUtils.applyNumericDoubleFilter(txtAmountEntered);
            FormUtils.applyNumericDoubleFilter(txtAmountAvailable);



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
           // DialogUtils.showWarningAlert("Input Error", "Seleccione un producto.",txt_product);
            return;
        }

        try {
            Inventory inventory = new Inventory();
            inventory.setFkProductCode(currentProduct.getCode());
            inventory.setAmountEntered(InputUtils.parseDouble(txtAmountEntered.getText()));
            inventory.setAmountAvailable(InputUtils.parseDouble(txtAmountAvailable.getText()));
            inventory.setExpirationDate(InputUtils.convertToDate(dateExpirationDate));
            //inventory.setLocation(txt_location.getText());
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
            inventory.setAmountEntered(InputUtils.parseDouble(txtAmountEntered.getText()));
            inventory.setAmountAvailable(InputUtils.parseDouble(txtAmountAvailable.getText()));
            inventory.setExpirationDate(InputUtils.convertToDate(dateExpirationDate));
           // inventory.setLocation(txt_location.getText());

            inventoryService.update(inventory);
            cleanFields();
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void delete() {
        DialogUtils.showConfirmationDialog(
                "Confirmar eliminación",
                "¿Estás seguro de que deseas eliminar este producto?",
                "Esta acción no se puede deshacer."
        ).ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    inventoryService.delete(selectedInventory.getId());
                    loadData();
                    cleanFields();
                } catch (SQLException e) {
                    e.printStackTrace();
                    DialogUtils.showWarningAlert("Error", "No se pudo eliminar el producto.", null);
                }
            }
        });

//        NodeActions.enableDisable(true,btn_delete, btn_update);
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
