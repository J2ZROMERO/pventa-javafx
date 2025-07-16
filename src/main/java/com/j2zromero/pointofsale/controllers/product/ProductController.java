package com.j2zromero.pointofsale.controllers.product;

import com.j2zromero.pointofsale.controllers.inventory.InventoryController;
import com.j2zromero.pointofsale.models.brands.Brand;
import com.j2zromero.pointofsale.models.categories.Category;
import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.models.suppliers.Supplier;
import com.j2zromero.pointofsale.services.brand.BrandService;
import com.j2zromero.pointofsale.services.category.CategoryService;
import com.j2zromero.pointofsale.services.product.ProductService;
import com.j2zromero.pointofsale.services.supplier.SupplierService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import com.j2zromero.pointofsale.utils.InputUtils;
import com.j2zromero.pointofsale.utils.UnitType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ProductController {


    /* ===  FXML fields  === */
    @FXML public TableColumn<Product, String> fkCategoryColumn;
    @FXML public TableColumn<Product, String> fkBrandColumn;
    @FXML private AnchorPane anchorProduct;
    @FXML private Pane productFields;
    @FXML private TableView<Product> tableProduct;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;
    @FXML private TableColumn<Product, String> codeColumn;
    @FXML private TableColumn<Product, String> unitMeasurementColumn;
    @FXML private TableColumn<Product, Double> unitPriceColumn;
    @FXML private TableColumn<Product, Double> stockColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, String> brandColumn;
    @FXML private TableColumn<Product, Integer> fkSupplierColumn;
    @FXML private TableColumn<Product, String> supplierName;
    @FXML private TableColumn<Product, Double> packagePriceColumn;
    @FXML private TableColumn<Product, Integer> amountPerPackageColumn;
    @FXML private TableColumn actionsColumn;

    @FXML private TextField txtName;
    @FXML private TextField txtDescription;
    @FXML private TextField txtCode;
    @FXML private ChoiceBox<UnitType> cbxUnitMeasurement;
    @FXML private TextField txtPrice;
    @FXML private TextField txtPackagePrice;
    @FXML private TextField txtAmountPerPackage;
    @FXML private CheckBox checkPackage;
    @FXML private TextField txtSearch;

    // NEW ComboBoxes
    @FXML private ComboBox<Category> cbxCategory;
    @FXML private ComboBox<Brand> cbxBrand;

    @FXML private ComboBox<Supplier> cbxSuppliers;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClean;
    @FXML private Button btnUpdateTable;

    /* ===  Services & data  === */
    private final ProductService productService   = new ProductService();
    private final SupplierService supplierService = new SupplierService();
    private final CategoryService categoryService = new CategoryService();
    private final BrandService    brandService    = new BrandService();

    private Product currentProduct = new Product();
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private List<Supplier> supplierList;
    private List<Category> categoryList;
    private List<Brand>    brandList;
    private List<UnitType> measureUnits;

    private FilteredList<Product> filteredData;
    private SortedList<Product>   sortedData;

    /* ===  INITIALIZE  === */
    @FXML
    private void initialize() throws SQLException {

        // -- Tooltips --------------------------------------------------------
        DialogUtils.TooltipHelper.install(btnAdd,    "Guardar",          DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnUpdate, "Actualizar",       DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnDelete, "Eliminar",         DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnClean,  "Limpiar",          DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnUpdateTable, "Actualizar tabla", DialogUtils.TooltipColor.DARK);

        // -- Global stylesheet ----------------------------------------------
        Platform.runLater(() -> {
            if (anchorProduct.getScene() != null) {
                anchorProduct.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });

        // -- Numeric filter for price ---------------------------------------
        FormUtils.applyNumericOnlyFilter(txtPrice);

        // -- ComboBox styles -------------------------------------------------
        cbxSuppliers.setStyle("-fx-font-size: 18px;");
        cbxUnitMeasurement.setStyle("-fx-font-size: 18px;");
        cbxCategory.setStyle("-fx-font-size: 18px;");
        cbxBrand.setStyle("-fx-font-size: 18px;");

        // -- Load reference data --------------------------------------------
        measureUnits  = productService.getMeasurementTypes();
        supplierList  = supplierService.getAll();
        categoryList  = categoryService.getAll();
        brandList     = brandService.getAll();

        // -- Apply filters to ComboBoxes ------------------------------------
        FormUtils.applyComboBoxFilter(cbxSuppliers, supplierList, s -> s.getName() + " " + s.getDirection());
        FormUtils.applyComboBoxFilter(cbxCategory, categoryList, Category::getName);
        FormUtils.applyComboBoxFilter(cbxBrand,    brandList,    Brand::getName);

        // -- Unit measurement choices ---------------------------------------
        if (!measureUnits.isEmpty()) {
            cbxUnitMeasurement.setItems(FXCollections.observableArrayList(measureUnits));
            cbxUnitMeasurement.setValue(measureUnits.get(0));
        }

        // -- Package checkbox logic -----------------------------------------
        txtAmountPerPackage.setDisable(true);
        txtPackagePrice.setDisable(true);
        checkPackage.selectedProperty().addListener((obs, was, is) -> {
            txtAmountPerPackage.setDisable(!is);
            txtPackagePrice.setDisable(!is);
            if (is) txtAmountPerPackage.requestFocus();
            else {
                txtAmountPerPackage.clear();
                txtPackagePrice.clear();
            }
        });

        // -- Table columns ---------------------------------------------------
        idColumn   .setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn .setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        codeColumn .setCellValueFactory(new PropertyValueFactory<>("code"));
        unitMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("unitMeasurement"));
        unitPriceColumn .setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        stockColumn     .setCellValueFactory(new PropertyValueFactory<>("stock"));
        categoryColumn  .setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        brandColumn     .setCellValueFactory(new PropertyValueFactory<>("brandName"));
        supplierName    .setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        fkSupplierColumn.setCellValueFactory(new PropertyValueFactory<>("fkSupplier"));
        fkBrandColumn.setCellValueFactory(new PropertyValueFactory<>("fkBrand"));
        fkCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("fkCategory"));
        packagePriceColumn .setCellValueFactory(new PropertyValueFactory<>("packagePrice"));
        amountPerPackageColumn.setCellValueFactory(new PropertyValueFactory<>("totalInPackage"));
        fkSupplierColumn.setVisible(false);
        unitMeasurementColumn.setVisible(false);

        addViewDetailsButtonToActions();

        loadData();
        implementSearchFilter();

        // -- Row click & key delete -----------------------------------------
        tableProduct.setOnMouseClicked(this::handleRowClick);
        tableProduct.setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.DELETE || evt.getCode() == KeyCode.BACK_SPACE) delete();
        });

        // Enter = save
        productFields.setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.ENTER) add(new ActionEvent());
        });
    }
    private boolean validateForm() {
        // Campos mínimos
        if (txtName.getText().trim().isEmpty()
                || txtCode.getText().trim().isEmpty()
                || cbxUnitMeasurement.getValue() == null) {
            DialogUtils.showWarningAlert("Producto", "Llena los campos necesarios", txtName);
            return false;
        }
        if (txtPrice.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Precio unitario", "Ingresa el precio unitario", txtPrice);
            return false;
        }
        // Paquete (si está activado)
        if (checkPackage.isSelected() &&
                (txtAmountPerPackage.getText().trim().isEmpty()
                        || txtPackagePrice.getText().trim().isEmpty())) {
            DialogUtils.showWarningAlert(
                    "Paquete",
                    "Precio y cantidad por paquete deben de ser completados.",
                    null);
            return false;
        }
        return true;
    }

    /* ===  CRUD  === */
    @FXML
    public void add(ActionEvent e) {
        if (!validateForm()) return;          // ← usa la validación


        setProductFieldsFromInput();

        try {
            if (productService.add(currentProduct)) {
                DialogUtils.showWarningAlert("Producto",
                        "El producto ya existe; sólo puedes actualizarlo.", txtName);
                return;
            }
            DialogUtils.showToast("Producto agregado con éxito!", 2, "green");
            loadData();
            cleanFields();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void update() {
        if (!validateForm()) return;          // ← usa la validación

        setProductFieldsFromInput();
        try {
            productService.update(currentProduct);
            DialogUtils.showToast("Producto actualizado", 2, "blue");
            loadData();
            cleanFields();
        } catch (SQLException ex) {
            ex.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo actualizar.", null);
        }
    }

    @FXML
    public void delete() {
        DialogUtils.showConfirmationDialog(
                "Confirmar eliminación",
                "¿Eliminar este producto?",
                "Esta acción no se puede deshacer."
        ).ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try {
                    productService.delete(currentProduct.getId());
                    loadData();
                    cleanFields();
                    DialogUtils.showToast("Producto eliminado", 2, "blue");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    DialogUtils.showWarningAlert("Error", "No se pudo eliminar.", null);
                }
            }
        });
    }

    /* ===  Helpers  === */
    private void loadData() throws SQLException {
        productList.setAll(productService.getAll());

    }

    private void handleRowClick(MouseEvent evt) {
        if (evt.getClickCount() == 1 && !tableProduct.getSelectionModel().isEmpty()) {
            Product sel = tableProduct.getSelectionModel().getSelectedItem();

            txtName.setText(sel.getName());
            txtDescription.setText(sel.getDescription());
            txtCode.setText(sel.getCode());
            txtPrice.setText(String.valueOf(sel.getUnitPrice()));

            // Category Combo
            Category cat = categoryList.stream()
                    .filter(c -> sel.getFkCategory()!= null & c.getId() == sel.getFkCategory())
                    .findFirst().orElse(null);
            cbxCategory.setValue(cat);

            // Brand Combo
            Brand br = brandList.stream()
                    .filter(b -> sel.getFkBrand()!= null & b.getId() == sel.getFkBrand() )
                    .findFirst().orElse(null);
            cbxBrand.setValue(br);

            // Supplier Combo
            Supplier sup = supplierList.stream()
                    .filter(s -> sel.getFkSupplier() != null && s.getId() == sel.getFkSupplier())
                    .findFirst().orElse(null);
            cbxSuppliers.setValue(sup);

            // Unit measurement
            cbxUnitMeasurement.setValue(
                    measureUnits.stream()
                            .filter(u -> sel.getUnitMeasurement().equals(u.getCode()))
                            .findFirst().orElse(null)
            );

            // Package
            checkPackage.setSelected(sel.isHasPackageLogic());
            txtAmountPerPackage.setText(
                    sel.getTotalInPackage() != null ? sel.getTotalInPackage().toString() : "");
            txtPackagePrice.setText(
                    sel.getPackagePrice() != null ? sel.getPackagePrice().toString() : "");

            currentProduct = sel;

            FormUtils.enableDisable(false, btnDelete, btnUpdate);
            FormUtils.enableDisable(true, btnAdd);
        }
    }

    private void implementSearchFilter() {
        filteredData = new FilteredList<>(productList, p -> true);
        txtSearch.textProperty().addListener((obs, oldV, newV) -> {
            String f = newV == null ? "" : newV.toLowerCase();
            filteredData.setPredicate(p -> {
                if (f.isEmpty()) return true;
                return p.getName().toLowerCase().contains(f) ||
                        p.getCode().toLowerCase().contains(f) ||
                        p.getDescription().toLowerCase().contains(f) ||
                        p.getUnitMeasurement().toLowerCase().contains(f) ||
                        String.valueOf(p.getId()).contains(f);
            });
        });
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableProduct.comparatorProperty());
        tableProduct.setItems(sortedData);
    }

    private void setProductFieldsFromInput() {
        currentProduct.setName(txtName.getText());
        currentProduct.setDescription(txtDescription.getText());
        currentProduct.setCode(txtCode.getText());
        currentProduct.setUnitMeasurement(cbxUnitMeasurement.getValue().getCode());
        currentProduct.setUnitPrice(InputUtils.parseDouble(txtPrice.getText()));

        currentProduct.setFkCategory(
                cbxCategory.getValue() != null ? cbxCategory.getValue().getId() : null);
        currentProduct.setFkBrand(
                cbxBrand.getValue() != null ? cbxBrand.getValue().getId() : null);

        currentProduct.setFkSupplier(
                cbxSuppliers.getValue() != null ? (long) cbxSuppliers.getValue().getId() : null);

        currentProduct.setHasPackageLogic(checkPackage.isSelected());
        currentProduct.setPackagePrice(
                txtPackagePrice.getText().isEmpty() ? null : InputUtils.parseDouble(txtPackagePrice.getText()));
        currentProduct.setTotalInPackage(
                txtAmountPerPackage.getText().isEmpty() ? null : InputUtils.parseDouble(txtAmountPerPackage.getText()));
    }

    public void cleanFields() {
        FormUtils.clearAndResetStyles(productFields);

        txtName.clear(); txtDescription.clear(); txtCode.clear(); txtPrice.clear();
        txtAmountPerPackage.clear(); txtPackagePrice.clear();
        cbxSuppliers.setValue(null);
        cbxCategory.setValue(null);
        cbxBrand.setValue(null);
        cbxUnitMeasurement.setValue(measureUnits.isEmpty() ? null : measureUnits.get(0));
        checkPackage.setSelected(false);

        currentProduct = new Product();
        FormUtils.enableDisable(true, btnDelete, btnUpdate);
        FormUtils.enableDisable(false, btnAdd);
        txtName.requestFocus();
    }

    /* ===  Actions column  === */
    private void addViewDetailsButtonToActions() {
        actionsColumn.setCellFactory(col -> new TableCell<Product, Void>() {
            private final Button btn = new Button("👁");
            {
                btn.getStyleClass().add("icon-button");
                DialogUtils.TooltipHelper.install(btn, "Agregar/Ver inventario", DialogUtils.TooltipColor.DARK);
                btn.setOnAction(e -> openDetailView(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void openDetailView(Product p) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/inventory/inventory.fxml"));
            Parent root = loader.load();
            loader.<InventoryController>getController().setProduct(p);
            Stage stage = new Stage(); stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Detalle de Producto #" + p.getName());
            stage.setScene(new Scene(root)); stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo abrir el detalle.", null);
        }
    }

    /* ===  Refresh table  === */
    public void updateDataTable(ActionEvent e) throws SQLException {
        loadData();
        DialogUtils.showToast("Tabla actualizada", 2, "blue");
    }
}