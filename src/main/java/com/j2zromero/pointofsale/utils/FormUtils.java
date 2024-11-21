package com.j2zromero.pointofsale.utils;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

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


    public static void clearAndResetStyles(Pane parent) {
        parent.getChildren().filtered(node -> node instanceof TextField).forEach(node -> {
            TextField field = (TextField) node;
            field.setText("");
            field.setStyle(null); // Reset styles
        });
    }

    // Method to apply numeric-only restriction
    public static void applyNumericOnlyFilter(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            // Allow only digits and optional decimal point
            if (newText.matches("\\d*\\.?\\d*")) {
                return change; // Accept the change
            }
            return null; // Reject the change
        }));
    }

    // Method to apply integer-only restriction
    public static void applyIntegerOnlyFilter(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            // Allow only digits (no decimal points)
            if (newText.matches("\\d*")) {
                return change; // Accept the change
            }
            return null; // Reject the change
        }));
    }

    // Method to apply max length restriction
    public static void applyMaxLengthFilter(TextField textField, int maxLength) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            // Check length and numeric-only condition
            if (newText.length() <= maxLength && newText.matches("\\d*\\.?\\d*")) {
                return change; // Accept the change
            }
            return null; // Reject the change
        }));
    }


        // Method to apply numeric (double) only filter and formatting
        public static void applyNumericDoubleFilter(TextField textField) {
            // Set a TextFormatter to allow only valid double input
            textField.setTextFormatter(new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                // Allow only digits and optional decimal point
                if (newText.matches("\\d*\\.?\\d*")) {
                    return change; // Accept the change
                }
                return null; // Reject the change
            }));

            // Add a listener to format the value on focus loss
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) { // Focus lost
                    try {
                        // Parse and format the value
                        String text = textField.getText().trim();
                        double value = text.isEmpty() ? 0.0 : Double.parseDouble(text);
                        textField.setText(String.format("%.2f", value)); // Format to two decimal places
                    } catch (NumberFormatException e) {
                        textField.setText("0.00"); // Reset to default if invalid
                    }
                }
            });

    }

}
