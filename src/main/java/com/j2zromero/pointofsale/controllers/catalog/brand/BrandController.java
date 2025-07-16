package com.j2zromero.pointofsale.controllers.catalog.brand;
import com.j2zromero.pointofsale.models.brands.Brand;
import com.j2zromero.pointofsale.services.brand.BrandService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static com.j2zromero.pointofsale.utils.DialogUtils.TooltipColor;

public class BrandController {
    @FXML private ComboBox<Brand> cbxBrand;
    @FXML private TextField txtName;
    @FXML private TextField txtSlug;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClean;
    @FXML private AnchorPane anchorBrand;
    @FXML private AnchorPane brandsPane;

    private BrandService service = new BrandService();
    private Brand currentBrand = new Brand();
    private List<Brand> brandList;

    @FXML
    private void initialize() {
        DialogUtils.TooltipHelper.install(btnAdd,    "Agregar",   TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnUpdate, "Actualizar",TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnDelete, "Eliminar",  TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnClean,  "Limpiar",   TooltipColor.DARK);

        Platform.runLater(() -> {
            if (anchorBrand.getScene() != null) {
                anchorBrand.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });

        loadData();
    }

    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Marca", "El nombre es obligatorio.", txtName);
            return false;
        }
        return true;
    }

    @FXML
    public void add(ActionEvent e) {
        if (!validateForm()) return;

        Brand b = new Brand();
        b.setName(txtName.getText());
        b.setSlug(txtSlug.getText());
        try {
            boolean exists = service.add(b);
            if (exists) {
                DialogUtils.showWarningAlert("Marca", "Esta marca ya existe.", txtName);
                return;
            }
            DialogUtils.showToast("Marca agregada con exito!.", 2, "green");
            cleanFields();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo guardar la marca.", null);
        }
    }

    @FXML
    public void update(ActionEvent e) {
        if (!validateForm()) return;

        Brand b = new Brand();
        b.setId(currentBrand.getId());
        b.setName(txtName.getText());
        b.setSlug(txtSlug.getText());
        try {
            service.update(b);
            DialogUtils.showToast("Marca actualizada.", 2, "blue");
            cleanFields();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo actualizar.", null);
        }
    }

    @FXML
    public void delete(ActionEvent e) {
        DialogUtils.showConfirmationDialog(
                "Confirmar eliminación",
                "¿Eliminar esta marca?", null
        ).ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    service.delete(currentBrand.getId());
                    DialogUtils.showToast("Marca eliminada.", 2, "blue");
                    cleanFields();
                    loadData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    DialogUtils.showWarningAlert("Error", "No se pudo eliminar.", null);
                }
            }
        });
    }

    public void cleanFields() {
        txtName.clear();
        txtSlug.clear();
        FormUtils.clearAndResetStyles(brandsPane);
        FormUtils.clearFields(brandsPane);
        FormUtils.enableDisable(true, btnDelete, btnUpdate);
        FormUtils.enableDisable(false, btnAdd);
        txtName.requestFocus();
    }

    private void loadData() {
        try {
            brandList = service.getAll();
            ObservableList<Brand> opts = FXCollections.observableArrayList(brandList);
            cbxBrand.setItems(opts);
            FormUtils.applyComboBoxFilter(
                    cbxBrand, brandList,
                    Brand::getName,
                    selected -> {
                        currentBrand = selected;
                        txtName.setText(selected.getName());
                        txtSlug.setText(selected.getSlug());
                        FormUtils.enableDisable(false, btnUpdate, btnDelete);
                        FormUtils.enableDisable(true, btnAdd);
                    }
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo cargar marcas.", null);
        }
    }
}