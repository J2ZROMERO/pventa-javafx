package com.j2zromero.pointofsale.controllers.catalog.category;

import com.j2zromero.pointofsale.models.categories.Category;
import com.j2zromero.pointofsale.services.category.CategoryService;
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

public class CategoryController {
    @FXML private ComboBox<Category> cbxCategory;
    @FXML private TextField txtName;
    @FXML private TextField txtSlug;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClean;
    @FXML private AnchorPane anchorCategory;
    @FXML private AnchorPane categoriesPane;

    private final CategoryService service = new CategoryService();
    private Category currentCategory = new Category();
    private List<Category> categoryList;

    @FXML
    private void initialize() {
        DialogUtils.TooltipHelper.install(btnAdd,    "Agregar",   TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnUpdate, "Actualizar",TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnDelete, "Eliminar",  TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnClean,  "Limpiar",   TooltipColor.DARK);

        Platform.runLater(() -> {
            if (anchorCategory.getScene() != null) {
                anchorCategory.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });

        loadData();
    }

    /* ─── Validación reutilizable ────────────────────────── */
    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Categoría", "El nombre es obligatorio.", txtName);
            return false;
        }
        return true;
    }

    /* ─── ADD ────────────────────────────────────────────── */
    @FXML
    public void add(ActionEvent e) {
        if (!validateForm()) return;

        Category c = new Category();
        c.setName(txtName.getText());
        c.setSlug(txtSlug.getText());

        try {
            boolean exists = service.add(c);
            if (exists) {
                DialogUtils.showWarningAlert("Categoría", "Esta categoría ya existe.", txtName);
                return;
            }
            DialogUtils.showToast("Categoría agregada con éxito!",2,"green");
            cleanFields();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo guardar la categoría.", null);
        }
    }

    /* ─── UPDATE ─────────────────────────────────────────── */
    @FXML
    public void update(ActionEvent e) {
        if (!validateForm()) return;

        Category c = new Category();
        c.setId(currentCategory.getId());
        c.setName(txtName.getText());
        c.setSlug(txtSlug.getText());

        try {
            service.update(c);
            DialogUtils.showToast("Categoría actualizada.",2,"blue");
            cleanFields();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo actualizar.", null);
        }
    }

    /* ─── DELETE ─────────────────────────────────────────── */
    @FXML
    public void delete(ActionEvent e) {
        DialogUtils.showConfirmationDialog(
                "Confirmar eliminación",
                "¿Eliminar esta categoría?", null
        ).ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    service.delete(currentCategory.getId());
                    DialogUtils.showToast("Categoría eliminada.",2,"blue");
                    cleanFields();
                    loadData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    DialogUtils.showWarningAlert("Error", "No se pudo eliminar.", null);
                }
            }
        });
    }

    /* ─── CLEAN ──────────────────────────────────────────── */
    public void cleanFields() {
        txtName.clear();
        txtSlug.clear();
        cbxCategory.setValue(null);
        FormUtils.clearAndResetStyles(categoriesPane);
        FormUtils.clearFields(categoriesPane);
        FormUtils.enableDisable(true, btnDelete, btnUpdate);
        FormUtils.enableDisable(false, btnAdd);
        txtName.requestFocus();
    }

    /* ─── LOAD ───────────────────────────────────────────── */
    private void loadData() {
        try {
            categoryList = service.getAll();
            ObservableList<Category> opts = FXCollections.observableArrayList(categoryList);
            cbxCategory.setItems(opts);
            FormUtils.applyComboBoxFilter(
                    cbxCategory, categoryList,
                    Category::getName,
                    selected -> {
                        currentCategory = selected;
                        txtName.setText(selected.getName());
                        txtSlug.setText(selected.getSlug());
                        FormUtils.enableDisable(false, btnUpdate, btnDelete);
                        FormUtils.enableDisable(true, btnAdd);
                    }
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo cargar categorías.", null);
        }
    }
}