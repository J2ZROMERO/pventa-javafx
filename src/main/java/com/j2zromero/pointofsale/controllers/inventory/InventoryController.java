package com.j2zromero.pointofsale.controllers.inventory;

import com.j2zromero.pointofsale.models.inventories.Inventory;
import com.j2zromero.pointofsale.models.payments.Payment;
import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.services.inventory.InventoryService;
import com.j2zromero.pointofsale.services.product.ProductService;
import com.j2zromero.pointofsale.utils.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class InventoryController {


    public TextField txtAmountEntered;
    public DatePicker dateExpirationDate;
    public ComboBox cbxSelectedProduct;
    public TextField txtBatchNumber;
    public TextField txtStatus;
    public TextField txtTotalAmount;
    public TextArea txtLocation;
    public Label lbl_unit_measurement;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnAdd;
    public ComboBox cbxStatus;
    public AnchorPane anchorInventory;
    public TextField txtPiecesPerPackage;
    @FXML
    private Pane inventory_fields;


    @FXML
    private ProductService productService = new ProductService();
    List<Product> productList;
    private InventoryService inventoryService = new InventoryService();
    private Inventory currentInventory = new Inventory();
    public void initialize() {
        Platform.runLater(() -> {
            if (anchorInventory.getScene() != null) {
                anchorInventory.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
        cbxStatus.getItems().setAll("activo", "inactivo");
        cbxSelectedProduct.setStyle("-fx-font-size: 16px;");
        cbxStatus.setStyle("-fx-font-size: 16px;");

        loadData();

    }

    @FXML
    private void add(ActionEvent actionEvent) {
        Product selectedProduct = (Product) cbxSelectedProduct.getSelectionModel().getSelectedItem();

        if (selectedProduct == null || selectedProduct.getCode() == null) {
            DialogUtils.showWarningAlert("Producto", "Debes seleccionar algún producto.", null);
            return;
        }

        if (txtAmountEntered.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Producto", "Debes agregar cantidad al producto.", null);
            return;
        }
        try {
            Inventory inventory = new Inventory();
            inventory.setProductCode(selectedProduct.getCode());
            inventory.setAmountEntered(InputUtils.parseDouble(txtAmountEntered.getText()));
            inventory.setBatchNumber(txtBatchNumber.getText());
            inventory.setExpirationDate(
                    dateExpirationDate.getValue() != null
                            ? Date.valueOf(dateExpirationDate.getValue())
                            : null
            );
            inventory.setLocation(txtLocation.getText());
            if(cbxStatus.getValue() != null){
            boolean active = "activo".equalsIgnoreCase(cbxStatus.getValue().toString());
            inventory.setStatus(active);
            }

            boolean alreadyExists = inventoryService.add(inventory);
            if(alreadyExists){
                DialogUtils.showWarningAlert("Producto", "El producto ya existe, asi que solo puedes actualizarlo.", cbxSelectedProduct);
                return;
            }

            cleanFields();
            loadData();
       } catch (SQLException e) {
          e.printStackTrace();
        }
    }

    @FXML
    private void update() {
        Product selectedProduct = (Product) cbxSelectedProduct.getSelectionModel().getSelectedItem();
        if (selectedProduct == null || selectedProduct.getCode() == null) {
            DialogUtils.showWarningAlert("Producto", "Debes seleccionar algún producto.", null);
            return;
        }

        try {
            Inventory inventory = new Inventory();
            // Assume currentInventory was loaded during selection and contains the existing ID
            inventory.setId(currentInventory.getId());
            inventory.setFkProductCode(selectedProduct.getCode());
            inventory.setAmountEntered(InputUtils.parseDouble(txtAmountEntered.getText()));
            inventory.setBatchNumber(txtBatchNumber.getText());
            inventory.setExpirationDate(
                    dateExpirationDate.getValue() != null
                            ? Date.valueOf(dateExpirationDate.getValue())
                            : null
            );
            inventory.setLocation(txtLocation.getText());
            boolean active = "activo".equalsIgnoreCase(cbxStatus.getValue().toString());

            inventory.setStatus(active);
            inventoryService.update(inventory);
            cleanFields();
            loadData();
            FormUtils.enableDisable(false,btnAdd);
        } catch (SQLException e) {
            e.printStackTrace();
                DialogUtils.showWarningAlert("Error", "No se pudo actualizar el inventario", null);
        }
    }

    @FXML
    private void delete() {
        DialogUtils.showConfirmationDialog(
                "Confirmar eliminación",
                "El inventario quedara vacio pero el producto seguira visible, si quieres eliminar el producto, ve al apartado de productos.",
                "Esta acción no se puede deshacer."
        ).ifPresent(response -> {
            if (response == ButtonType.OK) {

                try {
                    inventoryService.delete(currentInventory.getId());
                    loadData();
                    cleanFields();
                } catch (SQLException e) {
                    DialogUtils.showWarningAlert("Error", "No se pudo eliminar el producto.", null);
                    throw new RuntimeException(e);
                }

            }
        });

      FormUtils.enableDisable(true,btnDelete, btnUpdate);
    }

    public void cleanFields() {

        FormUtils.clearFields(inventory_fields);
        FormUtils.enableDisable(true,btnUpdate,btnDelete);

    }

    public Inventory getInventoryById(String productCode) throws SQLException {
            String sql = "{ CALL GetInventoryByid(?) }";
            try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
                 CallableStatement stmt = con.prepareCall(sql)) {

                stmt.setString(1, productCode);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Inventory inventory = new Inventory();
                        inventory.setId(rs.getLong("id"));
                        inventory.setFkProductCode(rs.getString("fk_product_code"));
                        inventory.setAmountEntered(rs.getDouble("amount_entered"));
                        inventory.setLocation(rs.getString("location"));
                        inventory.setBatchNumber(rs.getString("batch_number"));
                        inventory.setStatus(rs.getBoolean("status"));
                        inventory.setUnitType(rs.getString("unit_type"));
                        inventory.setExpirationDate(SQLUtils.getNullable(rs, "expiration_date", java.sql.Date.class)); // Or LocalDate
                        return inventory;
                    }
                }
            }
            return null; // Return null if no product is found
    }

    private void loadData(){
        try {
            // load product data
            productList = productService.getAll();
            FormUtils.applyComboBoxFilter(cbxSelectedProduct,productList, product -> product.getName() +" - medido en " + product.getUnitMeasurement()  , selected -> {
                FormUtils.applyNumericDoubleFilter(txtAmountEntered);
                FormUtils.applyNumericDoubleFilter(txtTotalAmount);
                try {
                    currentInventory = inventoryService.getInventoryByProductCode(selected.getCode());
                    // Populate form fields with selectedInventory data
                    if(currentInventory.getId() != null){
                        FormUtils.enableDisable(true,btnAdd);
                        FormUtils.enableDisable(false,btnUpdate, btnDelete);
                    }else{
                        FormUtils.enableDisable(false,btnAdd);
                        FormUtils.enableDisable(true,btnUpdate, btnDelete);

                    }

                    txtTotalAmount.setText(String.valueOf(currentInventory.getAmountEntered()));
                    txtBatchNumber.setText(currentInventory.getBatchNumber());
                   if(currentInventory.getStatus() != null){
                       cbxStatus.setValue(currentInventory.getStatus() ? "activo":"inactivo");
                   }
                    txtLocation.setText(currentInventory.getLocation());
                    lbl_unit_measurement.setText(currentInventory.getUnitType());
                    System.out.println(currentInventory.getUnitType());
                    if (currentInventory.getExpirationDate() != null) {
                        dateExpirationDate.setValue(
                                currentInventory.getExpirationDate().toLocalDate()
                        );
                    } else {
                        dateExpirationDate.setValue(null);
                    }
                    txtPiecesPerPackage.setText(currentInventory.getTotalInPackage().toString());

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            });



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
