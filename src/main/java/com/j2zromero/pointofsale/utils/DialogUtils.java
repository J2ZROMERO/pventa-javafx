package com.j2zromero.pointofsale.utils;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.Optional;

public class DialogUtils {

    public static void showWarningAlert(String title, String message, Node nodeToHighlight) {
        // Crear una alerta de tipo advertencia
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Mostrar la alerta y esperar hasta que el usuario la cierre
        alert.showAndWait();

        // Resaltar el campo de texto si se proporciona
        if (nodeToHighlight != null) {
            nodeToHighlight.setStyle("-fx-border-color: red;");
            nodeToHighlight.requestFocus();
        }
    }


    /**
     * Show a reusable confirmation dialog.
     *
     * @param title   The title of the dialog.
     * @param header  The header text of the dialog.
     * @param content The content message of the dialog.
     * @return Optional<ButtonType> The user's response.
     */
    public static Optional<ButtonType> showConfirmationDialog(String title, String header, String content) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(header);
        confirmationAlert.setContentText(content);

        // Show the dialog and return the user's response
        return confirmationAlert.showAndWait();
    }
}
