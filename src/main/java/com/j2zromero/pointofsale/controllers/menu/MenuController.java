package com.j2zromero.pointofsale.controllers.menu;

import com.j2zromero.pointofsale.Main;
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

        // Reusable method to open any modal view with the specified path and title
        private void openModalView(MouseEvent event, String fxmlPath, String title) {
            try {
                // Get the current stage and hide it
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.hide();

                // Load the FXML file for the new view
                FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
                Parent root = loader.load();

                // Create a new Stage (window) for the modal view
                Stage modalStage = new Stage();
                modalStage.setTitle(title);
                modalStage.setScene(new Scene(root));
                modalStage.centerOnScreen();
                modalStage.initModality(Modality.WINDOW_MODAL);
                modalStage.initOwner(mainStage);

                // Show the modal window and wait until it is closed
                modalStage.showAndWait();

                // When the modal closes, show the main window again
                mainStage.show();
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



}

