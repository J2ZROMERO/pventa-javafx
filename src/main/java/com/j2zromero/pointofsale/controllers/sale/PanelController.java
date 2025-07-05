package com.j2zromero.pointofsale.controllers.sale;

import com.j2zromero.pointofsale.services.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class PanelController {

    public Tab salesPane;
    public Tab salesOverviewPane;
    @FXML private TabPane tabPane;
    @FXML private Tab    salesTab;

    @FXML
    public void initialize() {
        salesOverviewPane.setDisable(!UserService.has("VER.VENTAS_DETALLES"));
    }


}
