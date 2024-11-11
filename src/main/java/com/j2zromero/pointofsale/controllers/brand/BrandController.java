package com.j2zromero.pointofsale.controllers.brand;

import com.j2zromero.pointofsale.models.brands.Brand;
import com.j2zromero.pointofsale.services.brand.BrandService;
import com.j2zromero.pointofsale.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;

public class BrandController {
    @FXML
    private TableView<Brand> table_brand;
    @FXML
    private TextField txt_brand;
    @FXML
    private   TableColumn<Brand,Integer> id_column;
    @FXML
    private TableColumn<Brand,Integer> name_column;

    private BrandService brandService = new BrandService();
    private Brand brand = new Brand();
    private ObservableList<Brand> brandList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));

        try {
            loadBrandData();
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showWarningAlert("Error", "No se encontraron datos (db).",null);
        }
        // Configurar el evento de clic para obtener datos de la fila seleccionada
        table_brand.setOnMouseClicked(this::handleRowClick);
    }

    private void loadBrandData() throws SQLException {
        List<Brand> brands = brandService.getAll();
        brandList.setAll(brands);
        table_brand.setItems(brandList);
    }


    private void handleRowClick(MouseEvent event) {
        // Verificar si se hizo clic en una fila
        if (event.getClickCount() == 1 && !table_brand.getSelectionModel().isEmpty()) {
            // Obtener el objeto Brand seleccionado
            Brand selectedBrand = table_brand.getSelectionModel().getSelectedItem();

            if (selectedBrand != null) {
                // Obtener el ID y el Nombre de la fila seleccionada
                int id = selectedBrand.getId();
                String name = selectedBrand.getName();

                // Mostrar los datos o realizar alguna acción
                System.out.println("ID seleccionado: " + id);
                System.out.println("Nombre seleccionado: " + name);

                // Opcional: establecer el nombre en el TextField
                txt_brand.setText(name);
                brand.setName(name);
                brand.setId(id);
                txt_brand.requestFocus();
            }
        }
    }

    @FXML
     public void add(ActionEvent actionEvent) {

        if (txt_brand.getText().trim().isEmpty()) {
            AlertUtils.showWarningAlert("Marca","Necesitas agregar el nombre de la marca.", txt_brand);
            return;
        }

        this.brand.setName(txt_brand.getText());

        try {

            brandService.add(this.brand);

            loadBrandData();
            txt_brand.setStyle("-fx-border-color: transparent;");
            txt_brand.setText("");
            txt_brand.requestFocus();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void update(){
        if (txt_brand.getText().trim().isEmpty()) {
            AlertUtils.showWarningAlert("Marca","Necesitas Seleccionar una marca.", txt_brand);
            return;
        }

        try {

            brandService.update(new Brand(txt_brand.getText(),this.brand.getId()));
            loadBrandData();
            txt_brand.setStyle("-fx-border-color: transparent;");
            txt_brand.setText("");
            txt_brand.requestFocus();

        } catch (SQLException e) {
            e.printStackTrace();
        }
}

    @FXML
    public  void delete(){
        if (txt_brand.getText().trim().isEmpty()) {
            AlertUtils.showWarningAlert("Marca","Necesitas Seleccionar una marca.", txt_brand);
            return;
        }

        try {

            brandService.delete(new Brand(txt_brand.getText(),this.brand.getId()).getId());
            loadBrandData();
            txt_brand.setStyle("-fx-border-color: transparent;");
            txt_brand.setText("");
            txt_brand.requestFocus();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
