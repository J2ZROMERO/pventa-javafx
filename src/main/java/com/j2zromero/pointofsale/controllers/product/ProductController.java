package com.j2zromero.pointofsale.controllers.product;

import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.models.suppliers.Supplier;
import com.j2zromero.pointofsale.services.product.ProductService;
import com.j2zromero.pointofsale.services.supplier.SupplierService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import com.j2zromero.pointofsale.utils.UnitType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ProductController {
    public AnchorPane anchorProduct;
    @FXML
    private ComboBox<Supplier> cbxSuppliers;

    @FXML

    private Pane productFields;

    @FXML
    private TableView<Product> tableProduct;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtCode;

    @FXML
    private ChoiceBox<UnitType> cbxUnitMeasurement;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private TextField txtVolumePrice;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;

    @FXML
    private TableColumn<Product, String> codeColumn;

    @FXML
    private TableColumn<Product, String> unitMeasurementColumn;

    @FXML
    private TableColumn<Product, Double> unitPriceColumn;

    @FXML
    private TableColumn<Product, Double> volumePriceColumn;

    @FXML
    private TableColumn<Product, Double> stockColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, String> brandColumn;

    @FXML
    private TableColumn<Product, Integer> fkSupplierColumn;

    @FXML
    private TableColumn<Product, String> supplierName;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClean;

    private ProductService productService = new ProductService();
    private Product currentProduct = new Product();
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private SupplierService supplierService = new SupplierService();
    private FilteredList<Product> filteredData;
    private SortedList<Product> sortedData;

    List<Supplier> supplierList;
    List<UnitType> measureUnits;

    @FXML
    private void initialize() throws SQLException {
        Platform.runLater(() -> {
            if (anchorProduct.getScene() != null) {
                anchorProduct.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
        FormUtils.applyNumericOnlyFilter(txtUnitPrice);
        FormUtils.applyNumericOnlyFilter(txtVolumePrice);
        cbxSuppliers.setStyle("-fx-font-size: 16px;");
        cbxUnitMeasurement.setStyle("-fx-font-size: 16px;");

        measureUnits = productService.getMeasurementTypes();
        supplierList = supplierService.getAll();

        FormUtils.applyComboBoxFilter(cbxSuppliers, supplierList,
                supplier -> supplier.getName() + " " + supplier.getDirection() + " " + supplier.getDirection());
        cbxSuppliers.setStyle("-fx-font-size: 18px;");
        cbxUnitMeasurement.setStyle("-fx-font-size: 18px;");
        cbxSuppliers.setStyle("-fx-font-size: 18px;");
        if (!measureUnits.isEmpty()) {
            cbxUnitMeasurement.setItems(FXCollections.observableArrayList(measureUnits));
            cbxUnitMeasurement.setValue(measureUnits.get(0));
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        unitMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("unitMeasurement"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        volumePriceColumn.setCellValueFactory(new PropertyValueFactory<>("volumePrice"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        supplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        fkSupplierColumn.setCellValueFactory(new PropertyValueFactory<>("fkSupplier"));
        fkSupplierColumn.setVisible(false);

        loadData();
        implementSearchFilter();

        tableProduct.setOnMouseClicked(this::handleRowClick);
        tableProduct.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DELETE:
                case BACK_SPACE:
                    delete();
                    break;
                default:
                    break;
            }
        });

        productFields.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                add(new ActionEvent());
            }
        });

        cbxUnitMeasurement.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedValue = newValue.getId();
                switch (selectedValue) {
                    case 0:
                        txtUnitPrice.setDisable(false);
                        txtVolumePrice.setDisable(true);
                        break;
                    case 1:
                    case 2:
                        txtUnitPrice.setDisable(true);
                        txtVolumePrice.setDisable(false);
                        break;
                    default:
                        txtUnitPrice.setDisable(false);
                        txtVolumePrice.setDisable(false);
                        break;
                }
            }
        });
    }

    private void loadData() throws SQLException {
        List<Product> products = productService.getAll();
        productList.setAll(products);
    }

    private void handleRowClick(MouseEvent event) {
        if (event.getClickCount() == 1 && !tableProduct.getSelectionModel().isEmpty()) {
            Product selectedProduct = tableProduct.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                FormUtils.enableDisable(false, btnDelete, btnUpdate);
                FormUtils.enableDisable(true, btnAdd);

                txtName.setText(selectedProduct.getName() != null ? selectedProduct.getName() : "");
                txtDescription.setText(selectedProduct.getDescription() != null ? selectedProduct.getDescription() : "");
                txtCode.setText(selectedProduct.getCode() != null ? selectedProduct.getCode() : "");
                txtUnitPrice.setText(selectedProduct.getUnitPrice() != null ? String.valueOf(selectedProduct.getUnitPrice()) : "");
                txtVolumePrice.setText(selectedProduct.getVolumePrice() != null ? String.valueOf(selectedProduct.getVolumePrice()) : "");
                txtCategory.setText(selectedProduct.getCategory() != null ? selectedProduct.getCategory() : "");
                txtBrand.setText(selectedProduct.getBrand() != null ? selectedProduct.getBrand() : "");
                Supplier sup = supplierList.stream()
                        .filter(s ->
                                selectedProduct.getFkSupplier() != null
                                        && s.getId() == selectedProduct.getFkSupplier())
                        .findFirst()
                        .orElse(null);

                cbxSuppliers.setValue(sup);
                System.out.println(selectedProduct);
                cbxUnitMeasurement.setValue(
                        measureUnits.stream()
                                .filter(s -> selectedProduct.getUnitMeasurement() != null &&
                                        selectedProduct.getUnitMeasurement().equals(s.getCode()))
                                .findFirst()
                                .orElse(null)
                );


                currentProduct = selectedProduct;
            }
        }
    }

    private void implementSearchFilter() {
        filteredData = new FilteredList<>(productList, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (product.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (product.getUnitMeasurement().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (String.valueOf(product.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (product.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (product.getCategory().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (product.getBrand().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableProduct.comparatorProperty());
        tableProduct.setItems(sortedData);
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        if (txtName.getText().trim().isEmpty() || txtCode.getText().trim().isEmpty() || cbxUnitMeasurement.getValue() == null) {
            DialogUtils.showWarningAlert("Producto", "Llena los campos necesarios", txtName);
            return;
        }


        UnitType um = cbxUnitMeasurement.getValue();
        if (um == null) {
            DialogUtils.showWarningAlert(
                    "Unidad de medida",
                    "Selecciona primero una unidad de medida",
                    cbxUnitMeasurement
            );
            return;
        }

        // 1) Recupera el código limpio
        String code = um.getCode().trim().toLowerCase();

        // 2) Si es “pz”, valida txtUnitPrice; si no, valida txtVolumePrice
        if ("pz".equals(code)) {
            if (txtUnitPrice.getText().trim().isEmpty()) {
                DialogUtils.showWarningAlert(
                        "Precio unitario",
                        "Ingresa el precio unitario",
                        txtUnitPrice
                );
                return;
            }
        } else {
            if (txtVolumePrice.getText().trim().isEmpty()) {
                DialogUtils.showWarningAlert(
                        "Precio por volumen",
                        "Ingresa el precio por volumen",
                        txtVolumePrice
                );
                return;
            }
        }


        setProductFieldsFromInput();
        try {
            boolean alreadyExists = productService.add(currentProduct);
            if(alreadyExists){
                DialogUtils.showWarningAlert("Producto", "El producto ya existe por lo tanto solo puedes actualizarlo.", txtName);
                txtName.requestFocus();
                return;
            }
            loadData();
            cleanFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void update() {
        setProductFieldsFromInput();
        try {
            productService.update(currentProduct);
            loadData();
            cleanFields();
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo actualizar el producto.", null);
        }
        FormUtils.enableDisable(true,btnDelete, btnUpdate);
    }

    @FXML
    public void delete() {
        DialogUtils.showConfirmationDialog(
                "Confirmar eliminación",
                "¿Estás seguro de que deseas eliminar este producto?",
                "Esta acción no se puede deshacer."
        ).ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    productService.delete(currentProduct.getId());
                    loadData();
                    cleanFields();
                } catch (SQLException e) {
                    e.printStackTrace();
                    DialogUtils.showWarningAlert("Error", "No se pudo eliminar el producto.", null);
                }
            }
        });

        FormUtils.enableDisable(true,btnDelete, btnUpdate);
    }

    private void setProductFieldsFromInput() {
        currentProduct.setName(txtName.getText());
        currentProduct.setDescription(txtDescription.getText());
        currentProduct.setCode(txtCode.getText());
        currentProduct.setUnitMeasurement(cbxUnitMeasurement.getValue().getCode());
        currentProduct.setUnitPrice(txtUnitPrice.getText().trim().isEmpty() ? null : Double.parseDouble(txtUnitPrice.getText().trim()));
        currentProduct.setVolumePrice(txtVolumePrice.getText().isEmpty() ? null : Double.parseDouble(txtVolumePrice.getText()));
        currentProduct.setCategory(txtCategory.getText());
        currentProduct.setBrand(txtBrand.getText());
        currentProduct.setFkSupplier(cbxSuppliers.getValue() != null ? Long.valueOf(((Supplier) cbxSuppliers.getValue()).getId() ): null);
    }

    public void cleanFields() {
        FormUtils.clearAndResetStyles(productFields);
        cbxSuppliers.setValue(null);
        cbxUnitMeasurement.setValue(null);
        currentProduct = new Product();
        FormUtils.enableDisable(true,btnDelete, btnUpdate);
        FormUtils.enableDisable(false,btnAdd);
        if (!measureUnits.isEmpty()) {
            cbxUnitMeasurement.setItems(FXCollections.observableArrayList(measureUnits));
            cbxUnitMeasurement.setValue(measureUnits.get(0));
        }
    }
}
