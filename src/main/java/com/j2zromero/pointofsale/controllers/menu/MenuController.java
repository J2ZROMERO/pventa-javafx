package com.j2zromero.pointofsale.controllers.menu;

import com.j2zromero.pointofsale.Main;
import com.j2zromero.pointofsale.controllers.caja.CloseCajaController;
import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.services.user.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.io.IOException;
import java.util.Objects;

public class MenuController {
    public AnchorPane anchorSales;
    public AnchorPane anchorInventory;
    public AnchorPane anchorProduct;
    public AnchorPane anchorSupplier;
    public AnchorPane anchorCaja;
    public AnchorPane anchorSettings;
    public AnchorPane anchorMenu;
    @FXML
    private AnchorPane hoverBrand;  // Reference to the AnchorPane with fx:id "rootPane"
    @FXML
    private AnchorPane hoverCategory;  // Reference to the AnchorPane with fx:id "rootPane"

    // Método para aplicar el estilo de hover solo al AnchorPane que activó el evento
    @FXML
    public void hover_button(MouseEvent event) {
        AnchorPane source = (AnchorPane) event.getSource(); // Obtiene el AnchorPane que disparó el evento
        source.setStyle("-fx-background-color: #e0e0e0;"); // Aplica el estilo al AnchorPane actual
    }

    @FXML
    public void exit_button(MouseEvent event) {
        AnchorPane source = (AnchorPane) event.getSource(); // Obtiene el AnchorPane que disparó el evento
        source.setStyle("-fx-background-color: transparent;"); // Elimina el estilo del AnchorPane actual
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
        anchorProduct.setDisable(!UserService.has("VER.PRODUCTOS"));
        anchorSettings.setDisable(!UserService.has("VER.CONFIGURACION"));
        anchorSales.setDisable(!UserService.has("VER.VENTAS"));
        anchorInventory.setDisable(!UserService.has("VER.INVENTARIOS"));
        anchorSupplier.setDisable(!UserService.has("VER.PROVEDORES"));
        anchorCaja.setDisable(!UserService.has("VER.CAJAS"));

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
    public void openBrandView(MouseEvent event){
      openModalView(event,"/views/brand/brand.fxml","Marcas");
    }

    @FXML
    public void openCategoryView(MouseEvent event){
        openModalView(event,"/views/category/category.fxml","Categoria");
    }

    @FXML
    public void openSupplierView(MouseEvent event){
        openModalView(event,"/views/supplier/supplier.fxml","Provedores");
    }

    @FXML
    public void openProductView(MouseEvent event){
        openModalView(event,"/views/product/product.fxml","Producto");
    }


    @FXML
    public void openInventoryView(MouseEvent event){
        openModalView(event,"/views/inventory/inventory.fxml","Inventario");
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

