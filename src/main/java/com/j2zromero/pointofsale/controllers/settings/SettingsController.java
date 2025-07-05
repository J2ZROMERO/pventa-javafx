package com.j2zromero.pointofsale.controllers.settings;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.Objects;

public class SettingsController {
    public AnchorPane anchorSettings;

    private void initialize() throws SQLException {
        Platform.runLater(() -> {
            if (anchorSettings.getScene() != null) {
                anchorSettings.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
    }
}
