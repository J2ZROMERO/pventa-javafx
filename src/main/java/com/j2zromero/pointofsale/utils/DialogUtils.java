package com.j2zromero.pointofsale.utils;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Optional;
// 1. Enum de colores permitidos


// 2. Helper con solo 3 parámetros
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
public class DialogUtils {

    public static void showWarningAlert(String title, String message, Node nodeToHighlight) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane pane = alert.getDialogPane();
        pane.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 15px;"
        );
        alert.getDialogPane().lookupButton(ButtonType.OK)
                .setStyle("-fx-font-size: 13px;");

        alert.showAndWait();

        if (nodeToHighlight != null) {
            nodeToHighlight.setStyle("-fx-border-color: red;");
            nodeToHighlight.requestFocus();
        }
    }


    /**
     * Muestra un diálogo con botón OK y CANCEL, usando el tipo indicado por nombre.
     *
     * @param title     Título de la ventana.
     * @param header    Encabezado del diálogo.
     * @param content   Texto de contenido.
     * @param typeName  Nombre del tipo de alerta: "confirmation", "info", "warning", "error" o null/otro para sin icono.
     * @return          Optional con el ButtonType pulsado.
     */
    public static Optional<ButtonType> showConfirmationDialog(
            String title,
            String header,
            String content,
            String typeName
    ) {
        // Convertimos el nombre en AlertType; si falla, NONE (sin icono).
        Alert.AlertType alertType;
        if (typeName == null) {
            alertType = Alert.AlertType.NONE;
        } else {
            switch (typeName.trim().toLowerCase()) {
                case "confirmation":
                case "confirm":
                    alertType = Alert.AlertType.CONFIRMATION;
                    break;
                case "information":
                case "info":
                    alertType = Alert.AlertType.INFORMATION;
                    break;
                case "warning":
                    alertType = Alert.AlertType.WARNING;
                    break;
                case "error":
                case "alert":
                    alertType = Alert.AlertType.ERROR;
                    break;
                default:
                    alertType = Alert.AlertType.NONE;
            }
        }

        // Creamos la alerta con el tipo resultante
        Alert dialog = new Alert(alertType);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        // Estilos de fuente
        dialog.getDialogPane().setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;"
        );

        // Fuerzo siempre OK y CANCEL
        dialog.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Ajuste de estilos de los botones
        Button okBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        okBtn.setStyle("-fx-font-size: 13px; -fx-padding: 8px 16px;");
        cancelBtn.setStyle("-fx-font-size: 13px; -fx-padding: 8px 16px;");

        return dialog.showAndWait();
    }
    /**
     * Show a toast at top-center with a custom background color.
     *
     * @param message        The text to display.
     * @param secondsToShow  How long (in seconds) before auto-closing.
     * @param backgroundColor  A CSS color (e.g. "rgba(40,167,69,0.9)" or "#ff5722").
     */
    public static void showToast(String message, int secondsToShow, String backgroundColor) {
        Platform.runLater(() -> {
            Stage toastStage = new Stage(StageStyle.TRANSPARENT);
            toastStage.setAlwaysOnTop(true);

            Label text = new Label(message);
            text.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

            Button closeButton = new Button("✖");
            closeButton.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;"
            );
            closeButton.setOnAction(e -> toastStage.close());

            HBox root = new HBox(10, text, closeButton);
            root.setAlignment(Pos.CENTER);
            root.setStyle(
                    "-fx-background-radius: 5;" +
                            "-fx-background-color: " + backgroundColor + ";"
            );
            root.setPadding(new Insets(10));

            Scene scene = new Scene(root);
            scene.setFill(null);
            toastStage.setScene(scene);

            // Show first to compute width, then center at top
            toastStage.show();

            Rectangle2D vb = Screen.getPrimary().getVisualBounds();
            double x = vb.getMinX() + (vb.getWidth() - toastStage.getWidth()) / 2;
            double y = vb.getMinY() + 50;
            toastStage.setX(x);
            toastStage.setY(y);

            PauseTransition delay = new PauseTransition(Duration.seconds(secondsToShow));
            delay.setOnFinished(evt -> toastStage.close());
            delay.play();
        });
    }

    /**
     * Convenience default toast in “success” green.
     */
    public static void showToast(String message, int secondsToShow) {
        showToast(message, secondsToShow, "rgba(40,167,69,0.9)");
    }

    public class TooltipHelper {
        /**
         * Instala un Tooltip en target con texto y color predefinido.
         * @param target  Nodo al que se le aplica (ej. Button)
         * @param text    Texto del tooltip
         * @param color   Color predefinido (BLUE o RED)
         * @return        El Tooltip creado
         */
        public static Tooltip install(Node target,
                                      String text,
                                      TooltipColor color) {
            Tooltip tip = new Tooltip(text);
            // delays y duración por defecto
            tip.setShowDelay(Duration.millis(100));
            tip.setHideDelay(Duration.millis(100));
            tip.setShowDuration(Duration.seconds(5));

            String baseStyle = color.getStyle();
            String fontStyle = "-fx-font-size: 14px;";
            tip.setStyle(baseStyle + " " + fontStyle);

            // aplicar solo el CSS del enum
            tip.setStyle(color.getStyle());

            // instala en el nodo
            Tooltip.install(target, tip);
            return tip;
        }
    }
    public enum TooltipColor {
        BLUE("-fx-background-color: blue;         -fx-text-fill: white;"),
        RED( "-fx-background-color: red;          -fx-text-fill: white;"),
        DARK("-fx-background-color: #333333;      -fx-text-fill: white;");  // gris oscuro

        private final String style;
        TooltipColor(String style) { this.style = style; }
        public String getStyle() { return style; }
    }


}
