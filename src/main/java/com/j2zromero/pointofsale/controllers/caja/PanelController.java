package com.j2zromero.pointofsale.controllers.caja;

import com.j2zromero.pointofsale.services.user.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class PanelController {
    public TabPane tabPane;
    public Tab overviewTab;
    public Tab closeTab;
    public Tab withdrawalTab;

    @FXML
    public void initialize() {
        overviewTab.setDisable(!UserService.has("VER.CONF.DETALLES_CAJA"));
        withdrawalTab.setDisable(!UserService.has("VER.CONF.RETIROS"));
    }


}
