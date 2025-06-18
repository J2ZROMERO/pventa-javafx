package com.j2zromero.pointofsale.utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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

    // version WITH callback
    public static <T> void applyComboBoxFilter(
            ComboBox<T> comboBox,
            List<T> masterList,
            Function<T, String> toString,
            Consumer<T> onSelect
    ) {
        applyComboBoxFilterInternal(comboBox, masterList, toString, onSelect);
    }

    // version WITHOUT callback
    public static <T> void applyComboBoxFilter(
            ComboBox<T> comboBox,
            List<T> masterList,
            Function<T, String> toString
    ) {
        applyComboBoxFilterInternal(comboBox, masterList, toString, t -> {}); // no-op callback
    }


    /**
     * Turns any editable ComboBox into a “type‐to‐filter” combo.
     * @param comboBox    the ComboBox to wire up
     * @param masterList  the full list of items to search
     * @param toString    a function that returns the searchable text for each item
     */
    public static <T> void applyComboBoxFilterInternal(
            ComboBox<T> comboBox,
            List<T> masterList,
            Function<T,String> toString,
            Consumer<T> onSelect
    ) {
        // 1) build your filtered list
        ObservableList<T> all = FXCollections.observableArrayList(masterList);
        FilteredList<T> filtered = new FilteredList<>(all, t -> true);
        comboBox.setItems(filtered);
        comboBox.setEditable(true);
        // 2) always show 5 rows tall
        comboBox.setVisibleRowCount(5);

        // 2) install a converter so getValue()↔String works safely
        comboBox.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T item) {
                return item == null ? "" : toString.apply(item);
            }
            @Override
            public T fromString(String text) {
                // find the first item whose toString matches exactly
                return filtered.stream()
                        .filter(it -> toString.apply(it).equals(text))
                        .findFirst()
                        .orElse(null);
            }
        });

        // 3) intercept ENTER to commit a matching item (and never add raw Strings)
        comboBox.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            if (evt.getCode() == KeyCode.ENTER) {
                String typed = comboBox.getEditor().getText();
                T match = comboBox.getConverter().fromString(typed);
                if (match != null) {
                    comboBox.getSelectionModel().select(match);
                    onSelect.accept(match); // ✅ callback on ENTER selection
                }
                evt.consume();
            }
        });

        comboBox.setOnHidden(e -> {
            T selected = comboBox.getSelectionModel().getSelectedItem();
            if (selected != null) {
                onSelect.accept(selected);
            }
        });

        comboBox.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused) {
                // focus was just lost → commit editor text
                String txt = comboBox.getEditor().getText();
                T match = comboBox.getConverter().fromString(txt);
                comboBox.getSelectionModel().select(match);
                comboBox.setValue(match);
            }
        });

        // 4) on all other key‐releases (except arrows/ENTER), update the filter
        comboBox.getEditor().addEventHandler(KeyEvent.KEY_RELEASED, evt -> {
            KeyCode code = evt.getCode();
            if (code.isArrowKey() || code == KeyCode.ENTER) return;

            String raw = comboBox.getEditor().getText().trim().toLowerCase();
            if (raw.isEmpty()) {
                filtered.setPredicate(t -> true);
            } else {
                filtered.setPredicate(t ->
                        toString.apply(t)
                                .toLowerCase()
                                .contains(raw)
                );
            }
            if (!comboBox.isShowing()) {
                comboBox.show();
            }
        });
    }}

