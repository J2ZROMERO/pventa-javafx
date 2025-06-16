package com.j2zromero.pointofsale.controllers.product;

import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.models.suppliers.Supplier;
import com.j2zromero.pointofsale.services.product.ProductService;
import com.j2zromero.pointofsale.services.supplier.SupplierService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import com.j2zromero.pointofsale.utils.NodeActions;
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
import java.sql.SQLException;
import java.util.List;

public class ProductController {
    public ComboBox cbx_suppliers;
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
    @FXML
    private TableColumn<Product, String> supplier_name;
    @FXML
    private Button btn_delete;
    @FXML
    private Button btn_update;


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
        FormUtils.applyNumericOnlyFilter(txt_unitPrice);
        FormUtils.applyNumericOnlyFilter(txt_volumePrice);

        measureUnits = productService.getMeasurementTypes();
        supplierList = supplierService.getAll();

        FormUtils.applyComboBoxFilter(cbx_suppliers,supplierList, supplier -> supplier.getName() +" " +supplier.getDirection() +" " + supplier.getDirection());

        if (!measureUnits.isEmpty()) {
            cbx_unitMeasurement.setItems(FXCollections.observableArrayList(measureUnits));
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
        supplier_name.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        fkSupplier_column.setCellValueFactory(new PropertyValueFactory<>("fkSupplier"));
        fkSupplier_column.setVisible(false);



        loadData();
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

    private void loadData() throws SQLException {
        List<Product> products = productService.getAll();
        System.out.println(products);
        productList.setAll(products);
    }

    private void handleRowClick(MouseEvent event) {
        if (event.getClickCount() == 1 && !table_product.getSelectionModel().isEmpty()) {
            NodeActions.enableDisable(false,btn_delete, btn_update);
            Product selectedProduct = table_product.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                txt_name.setText(selectedProduct.getName() != null ? selectedProduct.getName() : "");
                txt_description.setText(selectedProduct.getDescription() != null ? selectedProduct.getDescription() : "");
                txt_code.setText(selectedProduct.getCode() != null ? selectedProduct.getCode() : "");
                txt_unitPrice.setText(selectedProduct.getUnitPrice() != null ? String.valueOf(selectedProduct.getUnitPrice()) : "");
                txt_volumePrice.setText(selectedProduct.getVolumePrice() != null ? String.valueOf(selectedProduct.getVolumePrice()) : "");
                txt_category.setText(selectedProduct.getCategory() != null ? selectedProduct.getCategory() : "");
                txt_brand.setText(selectedProduct.getBrand() != null ? selectedProduct.getBrand() : "");
                Supplier sup = supplierList.stream()
                        .filter(s ->
                                selectedProduct.getFkSupplier() != null
                                        && s.getId() == selectedProduct.getFkSupplier())
                        .findFirst()
                        .orElse(null);

                cbx_suppliers.setValue(sup);

                cbx_unitMeasurement.setValue(
                        measureUnits.stream()
                                .filter(s -> s.getId() == selectedProduct.getUnitMeasurement())
                                .findFirst()
                                .orElse(null)
                );

                currentProduct = selectedProduct;
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
          boolean alreadyExists = productService.add(currentProduct);
            if(alreadyExists){
                DialogUtils.showWarningAlert("Producto", "No puedes ingresar el mismo producto 2 veces", txt_name);
                cleanFields();
                txt_name.requestFocus();
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
        NodeActions.enableDisable(true,btn_delete, btn_update);
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

        NodeActions.enableDisable(true,btn_delete, btn_update);
    }

    private void setProductFieldsFromInput() {
        currentProduct.setName(txt_name.getText());
        currentProduct.setDescription(txt_description.getText());
        currentProduct.setCode(txt_code.getText());
        currentProduct.setUnitMeasurement(cbx_unitMeasurement.getValue().getId());
        currentProduct.setUnitPrice(txt_unitPrice.getText().trim().isEmpty() ? null : Double.parseDouble(txt_unitPrice.getText().trim()));
        currentProduct.setVolumePrice(txt_volumePrice.getText().isEmpty() ? null : Double.parseDouble(txt_volumePrice.getText()));
        currentProduct.setCategory(txt_category.getText());
        currentProduct.setBrand(txt_brand.getText());
        currentProduct.setFkSupplier(cbx_suppliers.getValue() != null ? Long.valueOf(((Supplier) cbx_suppliers.getValue()).getId() ): null);
    }

    public void cleanFields() {
        FormUtils.clearAndResetStyles(product_fields);
        cbx_suppliers.setValue(null);
        cbx_unitMeasurement.setValue(null);
        currentProduct = new Product();
        NodeActions.enableDisable(true,btn_delete, btn_update);
    }
}
