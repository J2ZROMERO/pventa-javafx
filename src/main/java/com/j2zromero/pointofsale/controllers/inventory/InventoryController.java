package com.j2zromero.pointofsale.controllers.inventory;

import com.j2zromero.pointofsale.models.inventories.Inventory;
import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.repositories.inventory.InventoryRepository;
import com.j2zromero.pointofsale.services.inventory.InventoryService;
import com.j2zromero.pointofsale.services.product.ProductService;
import com.j2zromero.pointofsale.utils.AlertUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class InventoryController {

    @FXML
    private TableView<Product> table_product;

    @FXML
    private TableView<Inventory> table_inventory;

    @FXML
    private TableColumn<Product, String> name_product_column;

    @FXML
    private TableColumn<Product, String> name_inventory_column;

    @FXML
    private TextField search_product_field;


    @FXML
    private TextField search_inventory_field;


    @FXML
    private Pane inventory_fields;

    @FXML
    private TextField txt_product;

    @FXML
    private DatePicker date_register;

    @FXML
    private TextField txt_amount_entered;

    @FXML
    private TextField txt_amount_available;

    @FXML
    private DatePicker date_expiration;

    @FXML
    private TextField txt_location;

    private final ProductService productService = new ProductService();
    private final InventoryRepository inventoryRepository = new InventoryRepository();
    private final InventoryService inventoryService = new InventoryService();
    private ObservableList<Product> productList;
    private ObservableList<Inventory> inventoryList;
    private Product selectedProduct;
    private Inventory selectedInventory;
    public void initialize() {
        try {
            // Load product data
            productList = FXCollections.observableArrayList(productService.getAll());

            // load inventory data
            inventoryList = FXCollections.observableArrayList(inventoryService.getAll());

            // Configure table columns
            name_product_column.setCellValueFactory(new PropertyValueFactory<>("name"));
            name_inventory_column.setCellValueFactory(new PropertyValueFactory<>("product_name"));

            // Setup filtering product
            FilteredList<Product> filteredListProduct = new FilteredList<>(productList, p -> true);
            search_product_field.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredListProduct.setPredicate(product -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return product.getName().toLowerCase().contains(lowerCaseFilter);

                });
            });

            // Setup filtering product
            FilteredList<Inventory> filteredListInventory = new FilteredList<>(inventoryList, p -> true);
            search_inventory_field.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredListInventory.setPredicate(inventory -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return inventory.getProduct_name().toLowerCase().contains(lowerCaseFilter);

                });
            });

            // Setup sorting
            SortedList<Product> sortedListProduct = new SortedList<>(filteredListProduct);
            sortedListProduct.comparatorProperty().bind(table_product.comparatorProperty());

            // Setup sorting
            SortedList<Inventory> sortedListInventory = new SortedList<>(filteredListInventory);
            sortedListInventory.comparatorProperty().bind(table_inventory.comparatorProperty());

            // Set items to the table
            table_product.setItems(sortedListProduct);
            table_inventory.setItems(sortedListInventory);

            // Row click event for editing
            table_product.setOnMouseClicked(this::handleRowClickProduct);
            table_inventory.setOnMouseClicked(this::handleRowClickInventory);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleRowClickProduct(MouseEvent event) {
        if (!table_product.getSelectionModel().isEmpty()) {
            selectedProduct = table_product.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                txt_product.setText(selectedProduct.getName());
            }
        }
    }
    private java.time.LocalDate convertToLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            // Convert java.sql.Date to java.time.LocalDate directly
            return ((java.sql.Date) date).toLocalDate();
        } else if (date != null) {
            // Convert java.util.Date to java.time.LocalDate via Instant
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }

    private void handleRowClickInventory(MouseEvent event) {
        if (!table_inventory.getSelectionModel().isEmpty()) {
            selectedInventory = table_inventory.getSelectionModel().getSelectedItem();
            if (selectedInventory != null) {
                try {
                    // Populate the product name
                    Product product = productService.getByCode(selectedInventory.getFkProduct());
                    selectedProduct = product;
                    txt_product.setText(product != null ? product.getName() : "");

                    // Populate the date fields
                    date_register.setValue(convertToLocalDate(selectedInventory.getEntryDate()));
                    date_expiration.setValue(convertToLocalDate(selectedInventory.getExpirationDate()));

                    // Populate the numeric fields
                    txt_amount_entered.setText(selectedInventory.getAmountEntered() != null
                            ? String.valueOf(selectedInventory.getAmountEntered())
                            : "");
                    txt_amount_available.setText(selectedInventory.getAmountAvailable() != null
                            ? String.valueOf(selectedInventory.getAmountAvailable())
                            : "");

                    // Populate the location field
                    txt_location.setText(selectedInventory.getLocation() != null
                            ? selectedInventory.getLocation()
                            : "");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void add() {
        if (selectedProduct == null ||selectedProduct.getName() == null ) {
            AlertUtils.showWarningAlert("Input Error", "Seleccione un producto.",txt_product);
            return;
        }

        try {
            Inventory inventory = new Inventory();
            inventory.setFkProduct(selectedProduct.getCode());
            inventory.setEntryDate(convertToDate(date_register));
            inventory.setAmountEntered(parseDouble(txt_amount_entered.getText()));
            inventory.setAmountAvailable(parseDouble(txt_amount_available.getText()));
            inventory.setExpirationDate(convertToDate(date_expiration));
            inventory.setLocation(txt_location.getText());

            inventoryService.add(inventory);

            loadProductData();
            cleanFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void update() {
        if (selectedInventory == null || selectedInventory.getId() == null) {
            AlertUtils.showWarningAlert("Selection Error", "Selecciona un Producto de inventario.", null);
            return;
        }
        try {
            Inventory inventory = new Inventory();
            inventory.setId(selectedInventory.getId());
            inventory.setFkProduct(selectedProduct.getCode());
            inventory.setEntryDate(convertToDate(date_register));
            inventory.setAmountEntered(parseDouble(txt_amount_entered.getText()));
            inventory.setAmountAvailable(parseDouble(txt_amount_available.getText()));
            inventory.setExpirationDate(convertToDate(date_expiration));
            inventory.setLocation(txt_location.getText());

            inventoryService.update(inventory);
            cleanFields();
            loadProductData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void delete() {
        if (selectedProduct == null) {
            AlertUtils.showWarningAlert("Selection Error", "Please select a product to delete.",txt_product);
            return;
        }


        try {
            inventoryService.delete(selectedInventory.getId());
            cleanFields();
            loadProductData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private Date convertToDate(DatePicker datePicker) {
        if (datePicker.getValue() != null) {
            return Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }


    private Double parseDouble(String value) {
        try {
            return value != null && !value.trim().isEmpty() ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public void cleanFields() {

        FormUtils.clearFields(inventory_fields);
        selectedProduct = new Product();
        selectedInventory = new Inventory();

    }

    private void loadProductData() throws SQLException {
        List<Product> products = productService.getAll();
        productList.setAll(products);
        table_product.setItems(productList);

        List<Inventory> inventories = inventoryService.getAll();
        inventoryList.setAll(inventories);
        table_inventory.setItems(inventoryList);
    }

}
