package com.j2zromero.pointofsale.controllers.menu;

import com.j2zromero.pointofsale.Main;
import com.j2zromero.pointofsale.controllers.caja.CloseCajaController;
import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.services.user.UserService;
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

public class MenuController {
    public AnchorPane anchorUser;
    public AnchorPane anchorSales;
    public AnchorPane anchorInventory;
    public AnchorPane anchorProduct;
    public AnchorPane anchorSupplier;
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
        // Disable navigation panes based on permissions
        //anchorSupplier.setDisable(!PermissionService.has("VIEW.SUPPLIERS"));
        anchorProduct.setDisable(UserService.has("VIEW.PRODUCTS"));
        /*anchorInventory.setDisable(!PermissionService.has("VIEW.INVENTORY"));
        anchorSales.setDisable(!PermissionService.has("VIEW.SALES"));
        anchorUser.setDisable(!PermissionService.has("MANAGE.USERS"));
        hoverBrand.setDisable(!PermissionService.has("MANAGE.BRANDS"));
        hoverCategory.setDisable(!PermissionService.has("MANAGE.CATEGORIES"));*/
    }

    private void openModalView(MouseEvent event, String fxmlPath, String title) {
        try {
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.hide();

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
        openModalView(event,"/views/sale/sale.fxml","Ventas");
    }

    @FXML
    public void closeSalesView(MouseEvent event){
        openModalView(event,"/views/caja/closeCaja.fxml","Cierre de Caja");
    }



    public void openSettingsView(MouseEvent event) {  openModalView(event,"/views/settings/settings.fxml","Configuracion"); }
}

