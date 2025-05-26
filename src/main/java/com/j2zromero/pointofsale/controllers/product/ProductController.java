package com.j2zromero.pointofsale.controllers.product;

import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.models.suppliers.Supplier;
import com.j2zromero.pointofsale.services.product.ProductService;
import com.j2zromero.pointofsale.services.supplier.SupplierService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import com.j2zromero.pointofsale.utils.UnitType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class ProductController {
    @FXML
    private Pane product_fields;
    @FXML
    private TableView<Product> table_product;
    @FXML
    private TextField txt_name;
    @FXML
    private TextField txt_description;
    @FXML
    private TextField txt_code;
    @FXML
    private ChoiceBox<UnitType> cbx_unitMeasurement;
    @FXML
    private ChoiceBox<Supplier> cbx_supplier;
    @FXML
    private TextField txt_unitPrice;
    @FXML
    private TextField txt_volumePrice;
    @FXML
    private TextField txt_stock;
    @FXML
    private TextField txt_category;
    @FXML
    private TextField txt_brand;
    @FXML
    private TextField txt_search;
    @FXML
    public TableColumn<Product, Integer> id_column;
    @FXML
    private TableColumn<Product, String> name_column;
    @FXML
    private TableColumn<Product, String> description_column;
    @FXML
    private TableColumn<Product, String> code_column;
    @FXML
    private TableColumn<Product, String> unitMeasurement_column;
    @FXML
    private TableColumn<Product, Double> unitPrice_column;
    @FXML
    private TableColumn<Product, Double> volumePrice_column;
    @FXML
    private TableColumn<Product, Double> stock_column;
    @FXML
    private TableColumn<Product, String> category_column;
    @FXML
    private TableColumn<Product, String> brand_column;
    @FXML
    private TableColumn<Product, Integer> fkSupplier_column;

    private ProductService productService = new ProductService();
    private Product product = new Product();
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private SupplierService supplierService = new SupplierService();
    private FilteredList<Product> filteredData;
    private SortedList<Product> sortedData;

    List<Supplier> supplier;
    List<UnitType> measureUnits;

    @FXML
    private void initialize() throws SQLException {
        FormUtils.applyNumericOnlyFilter(txt_unitPrice);
        FormUtils.applyNumericOnlyFilter(txt_volumePrice);

        measureUnits = productService.getMeasurementTypes();
        supplier = supplierService.getAll();

        cbx_unitMeasurement.setItems(FXCollections.observableArrayList(measureUnits));
        cbx_supplier.setItems(FXCollections.observableArrayList(supplier));

        if (!measureUnits.isEmpty()) {
            cbx_unitMeasurement.setValue(measureUnits.get(0));
        }

        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        code_column.setCellValueFactory(new PropertyValueFactory<>("code"));
        unitMeasurement_column.setCellValueFactory(new PropertyValueFactory<>("unitMeasurement"));
        unitPrice_column.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        volumePrice_column.setCellValueFactory(new PropertyValueFactory<>("volumePrice"));
        stock_column.setCellValueFactory(new PropertyValueFactory<>("stock"));
        category_column.setCellValueFactory(new PropertyValueFactory<>("category"));
        brand_column.setCellValueFactory(new PropertyValueFactory<>("brand"));
        fkSupplier_column.setCellValueFactory(new PropertyValueFactory<>("fkSupplier"));

        loadProductData();
        implementSearchFilter();

        table_product.setOnMouseClicked(this::handleRowClick);
        table_product.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DELETE:
                case BACK_SPACE:
                    delete();
                    break;
                default:
                    break;
            }
        });

        product_fields.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                add(new ActionEvent());
            }
        });

        cbx_unitMeasurement.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedValue = newValue.getId();
                switch (selectedValue) {
                    case 0:
                        txt_unitPrice.setDisable(false);
                        txt_volumePrice.setDisable(true);
                        break;
                    case 1:
                    case 2:
                        txt_unitPrice.setDisable(true);
                        txt_volumePrice.setDisable(false);
                        break;
                    default:
                        txt_unitPrice.setDisable(false);
                        txt_volumePrice.setDisable(false);
                        break;
                }
            }
        });
    }

    private void loadProductData() throws SQLException {
        List<Product> products = productService.getAll();
        productList.setAll(products);
    }

    private void handleRowClick(MouseEvent event) {
        if (event.getClickCount() == 1 && !table_product.getSelectionModel().isEmpty()) {
            Product selectedProduct = table_product.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                txt_name.setText(selectedProduct.getName() != null ? selectedProduct.getName() : "");
                txt_description.setText(selectedProduct.getDescription() != null ? selectedProduct.getDescription() : "");
                txt_code.setText(selectedProduct.getCode() != null ? selectedProduct.getCode() : "");
                txt_unitPrice.setText(selectedProduct.getUnitPrice() != null ? String.valueOf(selectedProduct.getUnitPrice()) : "");
                txt_volumePrice.setText(selectedProduct.getVolumePrice() != null ? String.valueOf(selectedProduct.getVolumePrice()) : "");
                txt_category.setText(selectedProduct.getCategory() != null ? selectedProduct.getCategory() : "");
                txt_brand.setText(selectedProduct.getBrand() != null ? selectedProduct.getBrand() : "");

                cbx_supplier.setValue(
                        supplier.stream()
                                .filter(s -> selectedProduct.getFkSupplier() != null && s.getId() == selectedProduct.getFkSupplier().intValue())
                                .findFirst()
                                .orElse(null)
                );

                cbx_unitMeasurement.setValue(
                        measureUnits.stream()
                                .filter(s -> s.getId() == selectedProduct.getUnitMeasurement())
                                .findFirst()
                                .orElse(null)
                );

                product = selectedProduct;
            }
        }
    }

    private void implementSearchFilter() {
        filteredData = new FilteredList<>(productList, p -> true);

        txt_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (product.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (product.getDescription().toLowerCase().contains(lowerCaseFilter)) {
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
        sortedData.comparatorProperty().bind(table_product.comparatorProperty());
        table_product.setItems(sortedData);
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        if (txt_name.getText().trim().isEmpty() || txt_code.getText().trim().isEmpty() || txt_unitPrice.getText().trim().isEmpty() || cbx_unitMeasurement.getValue() == null) {
            DialogUtils.showWarningAlert("Producto", "Llena los campos necesarios", txt_name);
            return;
        }
        setProductFieldsFromInput();
        try {
            productService.add(product);
            loadProductData();
            cleanFields();
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo agregar el producto.", null);
        }
    }

    @FXML
    public void update() {
        if (txt_name.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Producto", "Debes seleccionar un producto.", txt_name);
            return;
        }
        setProductFieldsFromInput();
        try {
            productService.update(product);
            loadProductData();
            cleanFields();
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo actualizar el producto.", null);
        }
    }

    @FXML
    public void delete() {
        if (txt_name.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Producto", "Debes seleccionar un producto.", txt_name);
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmar eliminación");
        confirmationAlert.setHeaderText("¿Estás seguro de que deseas eliminar este producto?");
        confirmationAlert.setContentText("Esta acción no se puede deshacer.");
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    productService.delete(product.getId());
                    loadProductData();
                    cleanFields();
                } catch (SQLException e) {
                    e.printStackTrace();
                    DialogUtils.showWarningAlert("Error", "No se pudo eliminar el producto.", null);
                }
            }
        });
    }

    private void setProductFieldsFromInput() {
        product.setName(txt_name.getText());
        product.setDescription(txt_description.getText());
        product.setCode(txt_code.getText());
        product.setUnitMeasurement(cbx_unitMeasurement.getValue().getId());
        product.setUnitPrice(txt_unitPrice.getText().trim().isEmpty() ? null : Double.parseDouble(txt_unitPrice.getText().trim()));
        product.setVolumePrice(txt_volumePrice.getText().isEmpty() ? null : Double.parseDouble(txt_volumePrice.getText()));
        product.setCategory(txt_category.getText());
        product.setBrand(txt_brand.getText());
        product.setFkSupplier(cbx_supplier.getValue() != null ? Long.valueOf(cbx_supplier.getValue().getId()) : null);
    }

    public void cleanFields() {
        FormUtils.clearAndResetStyles(product_fields);
        cbx_supplier.setValue(null);
        cbx_unitMeasurement.setValue(null);
        product = new Product();
    }
}
