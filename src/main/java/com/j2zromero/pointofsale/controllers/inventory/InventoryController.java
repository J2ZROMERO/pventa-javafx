package com.j2zromero.pointofsale.controllers.inventory;

import com.j2zromero.pointofsale.models.inventories.Inventory;
import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.services.inventory.InventoryService;
import com.j2zromero.pointofsale.services.product.ProductService;

import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import com.j2zromero.pointofsale.utils.InputUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Objects;

public class InventoryController {

    @FXML public TextField    txtAmountEntered;
    @FXML public DatePicker   dateExpirationDate;
    @FXML public TextField    txtBatchNumber;
    @FXML public TextField    txtTotalAmount;
    @FXML public TextArea     txtLocation;
    @FXML public Label        lbl_unit_measurement;
    @FXML public Button       btnUpdate;
    @FXML public Button       btnDelete;
    @FXML public Button       btnAdd;
    @FXML public ComboBox     cbxStatus;
    @FXML public AnchorPane   anchorInventory;
    @FXML public Label        lblProduct;
    @FXML public VBox substractStockPane;
    public TextField txtRemoveStock;

    @FXML private Pane inventory_fields;

    private final ProductService   productService   = new ProductService();
    private final InventoryService inventoryService = new InventoryService();
    private       Inventory        currentInventory;
    private       Product          product;

    @FXML
    private void initialize() {
        // just styling and static setup
        Platform.runLater(() -> {
            if (anchorInventory.getScene() != null) {
                anchorInventory.getScene().getStylesheets().add(
                        Objects.requireNonNull(
                                getClass().getResource("/styles/global.css")
                        ).toExternalForm()
                );
            }
        });
        substractStockPane.setVisible(UserService.has("VER.INVENTARIOS.RESTAR_STOCK"));



        DialogUtils.TooltipHelper.install(btnUpdate,
                "Haz clic para Actualizar el inventario",
                DialogUtils.TooltipColor.DARK);

        cbxStatus.getItems().setAll("activo", "inactivo");
        cbxStatus.setStyle("-fx-font-size:16px;");

    }

    /**
     * Must be called right after loading the FXML:
     *
     *   FXMLLoader loader = new FXMLLoader(...);
     *   Parent root = loader.load();
     *   InventoryController ctrl = loader.getController();
     *   ctrl.setProduct(myProduct);
     *   // then show stage...
     */
    public void setProduct(Product p) {
        this.product = p;
        lblProduct.setText(p.getName());
        lbl_unit_measurement.setText(p.getUnitMeasurement());
        try {
            currentInventory = inventoryService.getInventoryByProductCode(p.getCode());
            if(currentInventory.getFkProductCode() == null){
                currentInventory.setFkProductCode(p.getCode());
            }
            if (currentInventory != null) {
                // populate fields
                txtBatchNumber.setText(currentInventory.getBatchNumber());
                txtLocation.setText(currentInventory.getLocation());
                dateExpirationDate.setValue(currentInventory.getExpirationDate() == null? null:currentInventory.getExpirationDate().toLocalDate());
                cbxStatus.setValue(currentInventory.getStatus() == null? "inactivo": currentInventory.getStatus()?  "activo" : "inactivo");
                txtTotalAmount.setText(String.valueOf(currentInventory.getStock()));
            } else {
                cleanFields();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            DialogUtils.showWarningAlert(
                    "Error",
                    "No se pudo cargar inventario existente",
                    null
            );
        }
    }

    @FXML
    private void add(ActionEvent event) {
        if (product == null) {
            DialogUtils.showWarningAlert(
                    "Producto",
                    "Debes seleccionar algún producto.",
                    null
            );
            return;
        }
        if (txtAmountEntered.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert(
                    "Inventario",
                    "Debes agregar cantidad al producto.",
                    null
            );
            return;
        }

        try {
            Inventory inv = new Inventory();
            System.out.println(product.getCode());
            inv.setFkProductCode(product.getCode());
            inv.setStock(InputUtils.parseDouble(txtAmountEntered.getText()));
            inv.setBatchNumber(txtBatchNumber.getText());
            inv.setExpirationDate(
                    dateExpirationDate.getValue() != null
                            ? Date.valueOf(dateExpirationDate.getValue())
                            : null
            );
            inv.setLocation(txtLocation.getText());
            inv.setStatus("activo".equalsIgnoreCase(cbxStatus.getValue().toString()));
            inventoryService.add(inv);

            DialogUtils.showWarningAlert("Éxito", "Inventario agregado correctamente.", null);
            setProduct(product);
        } catch (SQLException ex) {
            ex.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo agregar inventario.", null);
        }
    }

    @FXML
    private void update() {
        if (currentInventory == null) {
            DialogUtils.showWarningAlert(
                    "Inventario",
                    "No hay inventario para actualizar.",
                    null
            );
            return;
        }
        try {
            currentInventory.setStock(InputUtils.parseDouble(txtAmountEntered.getText()));
            currentInventory.setBatchNumber(txtBatchNumber.getText());
            currentInventory.setExpirationDate(
                    dateExpirationDate.getValue() != null
                            ? Date.valueOf(dateExpirationDate.getValue())
                            : null
            );
            currentInventory.setLocation(txtLocation.getText());
            currentInventory.setStatus("activo".equalsIgnoreCase(cbxStatus.getValue().toString()));
            currentInventory.setRemoveStock(InputUtils.parseDouble(txtRemoveStock.getText()));
            inventoryService.update(currentInventory);
            DialogUtils.showToast("Inventario actualizado.",2, "green");
            cleanFields();
            setProduct(product);
        } catch (SQLException ex) {
            ex.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo actualizar inventario.", null);
        }
    }

    @FXML
    private void delete() {
        if (currentInventory == null) {
            DialogUtils.showWarningAlert(
                    "Inventario",
                    "No hay inventario para eliminar.",
                    null
            );
            return;
        }
        DialogUtils.showConfirmationDialog(
                "Confirmar eliminación",
                "¿Eliminar este registro de inventario?",
                "Esta acción no se puede deshacer.", "WARNING"
        ).ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    inventoryService.delete(currentInventory.getId());
                    DialogUtils.showWarningAlert("Eliminado", "Inventario eliminado.", null);
                    setProduct(product);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    DialogUtils.showWarningAlert("Error", "No se pudo eliminar inventario.", null);
                }
            }
        });
    }

    public void cleanFields() {
    txtAmountEntered.setText(null);
    txtRemoveStock.setText(null);
    }
}
