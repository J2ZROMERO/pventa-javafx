package com.j2zromero.pointofsale.utils;

import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.Optional;

public class DialogUtils {

    public static void showWarningAlert(String title, String message, Node nodeToHighlight) {
        // Crear una alerta de tipo advertencia
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Increase font size across the dialog:
        // 2) Inline styling for the dialog pane
        DialogPane pane = alert.getDialogPane();
        pane.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 15px;"
        );

        // If you want even larger buttons, you can also style them:
        alert.getDialogPane().lookupButton(ButtonType.OK)
                .setStyle("-fx-font-size: 13px;");


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
// 1) Inline styling: bump up the overall font family & size
        confirmationAlert.getDialogPane().setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;"
        );

        // 2) Style each button (OK / Cancel) to be larger and more touch-friendly
        Button okButton = (Button) confirmationAlert.getDialogPane()
                .lookupButton(ButtonType.OK);
        Button cancelButton = (Button) confirmationAlert.getDialogPane()
                .lookupButton(ButtonType.CANCEL);
        okButton.setStyle("-fx-font-size: 13px; -fx-padding: 8px 16px;");
        cancelButton.setStyle("-fx-font-size: 13px; -fx-padding: 8px 16px;");


        // Show the dialog and return the user's response
        return confirmationAlert.showAndWait();
    }
}
