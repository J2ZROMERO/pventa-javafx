package com.j2zromero.pointofsale.controllers.inventory;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class InventoryController {

    // Define the ComboBox
    @FXML
    private ComboBox<String> productComboBox;

    // Initialize method to set up the ComboBox
    public void initialize() {
        // Example product list (replace with actual product data or database results)
        ObservableList<String> productList = FXCollections.observableArrayList(
                "Apple", "Banana", "Orange", "Grapes"
        );

        // Set the items for the ComboBox
        productComboBox.setItems(productList);

        // Make ComboBox editable so the user can type and search
        productComboBox.setEditable(true);
    }
}
