package com.j2zromero.pointofsale.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AlertUtils {

    public static void showWarningAlert(String title, String message, TextField fieldToHighlight) {
        // Crear una alerta de tipo advertencia
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Mostrar la alerta y esperar hasta que el usuario la cierre
        alert.showAndWait();

        // Resaltar el campo de texto si se proporciona
        if (fieldToHighlight != null) {
            fieldToHighlight.setStyle("-fx-border-color: red;");
            fieldToHighlight.requestFocus();
        }
    }
}
