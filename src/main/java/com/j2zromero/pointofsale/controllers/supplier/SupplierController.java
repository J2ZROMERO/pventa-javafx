package com.j2zromero.pointofsale.controllers.supplier;
import com.j2zromero.pointofsale.services.supplier.SupplierService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;
import   com.j2zromero.pointofsale.models.suppliers.Supplier;
import javafx.scene.layout.Pane;

public class SupplierController {
    public ComboBox cbxSupplier;
    public Button btnClean;
    public Button btnDelete;
    public Button btnUpdate;
    public Button btnAdd;
    public TextField txtExtraData;
    public TextField txtAddress;
    public TextField txtContact;
    public TextField txtName;
    @FXML
    private Pane suppliers_pane;

    private SupplierService supplierService = new SupplierService();
    private Supplier currentSupplier = new Supplier();
    private List<Supplier> supplierList;

    @FXML
    private void initialize() {
        loadData();
    }

    @FXML
    public void add(ActionEvent actionEvent) {

        if (txtName.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Proveedor", "Necesitas agregar el nombre del proveedor.", txtName);
            return;
        }

        Supplier supplier = new Supplier();
        supplier.setId(currentSupplier.getId());
        supplier.setName(txtName.getText());
        supplier.setContact(txtContact.getText());
        supplier.setDirection(txtAddress.getText());
        supplier.setExtraInformation(txtExtraData.getText());

        try {
            boolean alreadyExists = supplierService.add(supplier);
            if(alreadyExists){
                DialogUtils.showWarningAlert("Proveedor", "No puedes insertar el mismo usuario.", txtName);
                txtName.requestFocus();
                return;
            }
            cleanFields();
            loadData();
            txtName.requestFocus();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void update() {
        Supplier supplier = new Supplier();
        supplier.setId(currentSupplier.getId());
        supplier.setName(txtName.getText());
        supplier.setContact(txtContact.getText());
        supplier.setDirection(txtAddress.getText());
        supplier.setExtraInformation(txtExtraData.getText());
        try {
            supplierService.update(supplier);
            cleanFields();
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FormUtils.enableDisable(true,btnDelete, btnUpdate);
    }

    @FXML
    public void delete() {
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
                } catch (SQLException e) {
                    e.printStackTrace();
                    DialogUtils.showWarningAlert("Error", "Ocurrió un error al eliminar el proveedor.", null);
                }
            } else {
                System.out.println("Eliminación cancelada por el usuario.");
            }
        });
        FormUtils.enableDisable(true,btnDelete, btnUpdate);
    }

    public void cleanFields() {
        txtName.requestFocus();
        FormUtils.clearAndResetStyles(suppliers_pane);
        FormUtils.clearFields(suppliers_pane);
        FormUtils.enableDisable(true,btnDelete, btnUpdate);
        FormUtils.enableDisable(false,btnAdd);

    }

    private void loadData(){
        try {
            // load product data
            supplierList = supplierService.getAll();
            FormUtils.applyComboBoxFilter(cbxSupplier,supplierList, supplier -> supplier.getName()  , selected -> {


                    currentSupplier = selected;

                  // Populate form fields with selectedInventory data
                    if(selected.getName() != null){
                        FormUtils.enableDisable(false,btnUpdate, btnDelete);
                        FormUtils.enableDisable(true,btnAdd);

                    }else{
                        FormUtils.enableDisable(false,btnAdd);
                        FormUtils.enableDisable(true,btnUpdate, btnDelete);

                    }
                    txtName.setText(currentSupplier.getName());
                    txtContact.setText(currentSupplier.getContact());
                    txtExtraData.setText(currentSupplier.getExtraInformation());
                    txtAddress.setText(currentSupplier.getDirection());



            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
