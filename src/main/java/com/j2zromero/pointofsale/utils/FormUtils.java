package com.j2zromero.pointofsale.utils;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class FormUtils {
    public static void clearFields(Pane parent) {
        for (Node node : parent.getChildren()) {
            if (node instanceof TextField) {
                // Clear TextField
                ((TextField) node).clear();
            } else if (node instanceof ComboBox) {
                // Reset ComboBox
                ((ComboBox<?>) node).setValue(null);
            } else if (node instanceof DatePicker) {
                // Reset DatePicker
                ((DatePicker) node).setValue(null);
            } else if (node instanceof Pane) {
                // Recursive call for nested containers (e.g., VBox, HBox, GridPane)
                clearFields((Pane) node);
            }
        }
    }
}
