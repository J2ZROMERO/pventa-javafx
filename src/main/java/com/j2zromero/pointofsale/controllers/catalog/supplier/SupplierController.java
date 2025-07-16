package com.j2zromero.pointofsale.controllers.catalog.supplier;

import com.j2zromero.pointofsale.models.suppliers.Supplier;
import com.j2zromero.pointofsale.services.supplier.SupplierService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class SupplierController {
    @FXML private ComboBox<Supplier> cbxSupplier;
    @FXML private Button btnClean;
    @FXML private Button btnDelete;
    @FXML private Button btnUpdate;
    @FXML private Button btnAdd;
    @FXML private TextField txtCode;
    @FXML private TextField txtName;
    @FXML private TextField txtContact;
    @FXML private TextField txtContactName;
    @FXML private TextField txtContactPhone;
    @FXML private TextField txtAddress;
    @FXML private TextField txtCity;
    @FXML private TextField txtExtraData;
    @FXML private CheckBox chkStatus;
    @FXML private AnchorPane anchorSupplier;
    @FXML private Pane suppliers_pane;

    private final SupplierService supplierService = new SupplierService();
    private Supplier currentSupplier = new Supplier();
    private List<Supplier> supplierList;

    @FXML
    private void initialize() {
        // Tooltips
        DialogUtils.TooltipHelper.install(btnAdd, "Guardar", DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnUpdate, "Actualizar", DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnDelete, "Eliminar", DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnClean, "Limpiar", DialogUtils.TooltipColor.DARK);

        Platform.runLater(() -> {
            if (anchorSupplier.getScene() != null) {
                anchorSupplier.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });

        loadData();
    }

    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Proveedor", "El nombre es obligatorio.", txtName);
            return false;
        }
        return true;
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        if (!validateForm()) return;

        Supplier supplier = new Supplier();
        supplier.setId(currentSupplier.getId());
        supplier.setCode(txtCode.getText());
        supplier.setName(txtName.getText());
        supplier.setContact(txtContact.getText());
        supplier.setContactName(txtContactName.getText());
        supplier.setContactPhone(txtContactPhone.getText());
        supplier.setDirection(txtAddress.getText());
        supplier.setCity(txtCity.getText());
        supplier.setExtraInformation(txtExtraData.getText());
        supplier.setStatus(chkStatus.isSelected());

        try {
            boolean alreadyExists = supplierService.add(supplier);
            if (alreadyExists) {
                DialogUtils.showWarningAlert("Proveedor", "No puedes insertar el mismo proveedor.", txtName);
                txtName.requestFocus();
                return;
            }
            cleanFields();
            DialogUtils.showToast("Se agregó al proveedor con éxito!", 2);
            loadData();
            txtName.requestFocus();
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "Ocurrió un error al guardar el proveedor.", null);
        }
    }

    @FXML
    public void update(ActionEvent actionEvent) {
        if (!validateForm()) return;

        Supplier supplier = new Supplier();
        supplier.setId(currentSupplier.getId());
        supplier.setCode(txtCode.getText());
        supplier.setName(txtName.getText());
        supplier.setContact(txtContact.getText());
        supplier.setContactName(txtContactName.getText());
        supplier.setContactPhone(txtContactPhone.getText());
        supplier.setDirection(txtAddress.getText());
        supplier.setCity(txtCity.getText());
        supplier.setExtraInformation(txtExtraData.getText());
        supplier.setStatus(chkStatus.isSelected());

        try {
            supplierService.update(supplier);
            cleanFields();
            DialogUtils.showToast("Proveedor actualizado.", 2, "blue");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "Ocurrió un error al actualizar el proveedor.", null);
        }
    }

    @FXML
    public void delete(ActionEvent actionEvent) {
        DialogUtils.showConfirmationDialog(
                "Confirmar eliminación",
                "¿Estás seguro de eliminar este proveedor?",
                "Esta acción no se puede deshacer."
        ).ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    supplierService.delete(currentSupplier.getId());
                    cleanFields();
                    loadData();
                    DialogUtils.showToast("Proveedor eliminado.", 2, "blue");
                } catch (SQLException e) {
                    e.printStackTrace();
                    DialogUtils.showWarningAlert("Error", "Ocurrió un error al eliminar el proveedor.", null);
                }
            }
        });
    }

    public void cleanFields() {
        txtCode.clear();
        txtName.clear();
        txtContact.clear();
        txtContactName.clear();
        txtContactPhone.clear();
        txtAddress.clear();
        txtCity.clear();
        txtExtraData.clear();
        chkStatus.setSelected(true);

        FormUtils.clearAndResetStyles(suppliers_pane);
        FormUtils.clearFields(suppliers_pane);
        FormUtils.enableDisable(true, btnDelete, btnUpdate);
        FormUtils.enableDisable(false, btnAdd);
        txtName.requestFocus();
    }

    private void loadData() {
        try {
            supplierList = supplierService.getAll();
            ObservableList<Supplier> options = FXCollections.observableArrayList(supplierList);
            cbxSupplier.setItems(options);
            FormUtils.applyComboBoxFilter(cbxSupplier, supplierList,
                    Supplier::getName,
                    selected -> {
                        currentSupplier = selected;
                        txtCode.setText(selected.getCode());
                        txtName.setText(selected.getName());
                        txtContact.setText(selected.getContact());
                        txtContactName.setText(selected.getContactName());
                        txtContactPhone.setText(selected.getContactPhone());
                        txtAddress.setText(selected.getDirection());
                        txtCity.setText(selected.getCity());
                        txtExtraData.setText(selected.getExtraInformation());
                        chkStatus.setSelected(selected.isStatus());

                        FormUtils.enableDisable(false, btnUpdate, btnDelete);
                        FormUtils.enableDisable(true, btnAdd);
                    }
            );
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo cargar los proveedores.", null);
        }
    }
}