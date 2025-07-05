package com.j2zromero.pointofsale.controllers.caja;

import com.j2zromero.pointofsale.services.user.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class PanelController {
    public TabPane tabPane;
    public Tab overviewTab;
    public Tab detailsTab;
    public Tab closeTab;

    @FXML
    public void initialize() {
        overviewTab.setDisable(!UserService.has("VER.CONF.DETALLES_CAJA"));
        detailsTab.setDisable(!UserService.has("'VER.CONF.CAJAS'"));
    }


}
