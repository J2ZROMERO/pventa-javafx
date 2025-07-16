package com.j2zromero.pointofsale.controllers.menu;

import com.j2zromero.pointofsale.Main;
import com.j2zromero.pointofsale.controllers.caja.CloseCajaController;
import com.j2zromero.pointofsale.services.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.io.IOException;
import java.util.Objects;

public class MenuController {
    public AnchorPane anchorMenu;
    public VBox vBoxSettings;
    public VBox vBoxProducts;
    public VBox vBoxCatalogs;
    public VBox vBoxCaja;
    public VBox vBoxSales;

    // Método para aplicar el estilo de hover solo al AnchorPane que activó el evento
    @FXML
    public void hover_enter_transparent(MouseEvent event) {
        VBox source = (VBox) event.getSource();
        source.setStyle("-fx-alignment: center; -fx-background-color: #e0e0e0;");
    }

    @FXML
    public void hover_exit_transparent(MouseEvent event) {
        VBox source = (VBox) event.getSource();
        source.setStyle("-fx-alignment: center;");
    }
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (anchorMenu.getScene() != null) {
                anchorMenu.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
        
        // Disable navigation panes based on permissions
        vBoxProducts.setDisable(!UserService.has("VER.PRODUCTOS"));
        vBoxSettings.setDisable(!UserService.has("VER.CONFIGURACION"));
        vBoxSales.setDisable(!UserService.has("VER.VENTAS"));
        vBoxCatalogs.setDisable(!UserService.has("VER.CATALOGOS"));
        vBoxCaja.setDisable(!UserService.has("VER.CAJAS"));

    }

    private void openModalView(MouseEvent event, String fxmlPath, String title) {
        try {
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.hide();
            mainStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage modalStage = new Stage();
            modalStage.setTitle(title);
            modalStage.setScene(new Scene(root));
            modalStage.centerOnScreen();
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(mainStage);
            if (fxmlPath.equals("/views/caja/closeCaja.fxml")) {
                // Access controller instance
                CloseCajaController controller = loader.getController();

                modalStage.setOnHidden(e -> {
                    if (controller.wasClosedFromButton()) {
                        mainStage.close(); // Exit the menu
                    } else {
                        mainStage.show();  // Return to menu if closed via X
                    }
                });
            } else {
                // Optional: reopen menu for other views
                modalStage.setOnHidden(e -> {
                    mainStage.show();
                });
            }

            modalStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openProductView(MouseEvent event){
        openModalView(event,"/views/product/product.fxml","Producto");
    }


    @FXML
    public void openCatalogView(MouseEvent event){
        openModalView(event,"/views/catalog/panel.fxml","Catalogos");
    }

    @FXML
    public void openSalesView(MouseEvent event){
        openModalView(event,"/views/sale/panel.fxml","Ventas");
    }

    @FXML
    public void closeSalesView(MouseEvent event){
        openModalView(event,"/views/caja/panel.fxml","Caja");
    }

    public void openSettingsView(MouseEvent event) {  openModalView(event,"/views/settings/settings.fxml","Configuracion"); }

}

