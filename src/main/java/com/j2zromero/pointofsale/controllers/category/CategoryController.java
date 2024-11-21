package com.j2zromero.pointofsale.controllers.category;

import com.j2zromero.pointofsale.models.brands.Brand;
import com.j2zromero.pointofsale.models.categories.Category;
import com.j2zromero.pointofsale.services.category.CategoryService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;

public class CategoryController {
    public TextField txt_category;
    @FXML
    private TableView<Category> table_category;
    @FXML
    private   TableColumn<Brand,Integer> id_column;
    @FXML
    private TableColumn<Brand,Integer> name_column;

    private CategoryService categoryService = new CategoryService();
    private Category category = new Category();
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));

        try {
            loadBrandData();
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se encontraron datos (db).",null);
        }
        // Configurar el evento de clic para obtener datos de la fila seleccionada
        table_category.setOnMouseClicked(this::handleRowClick);
    }

    private void loadBrandData() throws SQLException {
        List<Category> categories = categoryService.getAll();
        categoryList.setAll(categories);
        table_category.setItems(categoryList);
    }


    private void handleRowClick(MouseEvent event) {
        // Verificar si se hizo clic en una fila
        if (event.getClickCount() == 1 && !table_category.getSelectionModel().isEmpty()) {
            // Obtener el objeto Brand seleccionado
            Category selectedBrand = table_category.getSelectionModel().getSelectedItem();

            if (selectedBrand != null) {
                // Obtener el ID y el Nombre de la fila seleccionada
                int id = selectedBrand.getId();
                String name = selectedBrand.getName();

                // Opcional: establecer el nombre en el TextField
                txt_category.setText(name);
                category.setName(name);
                category.setId(id);
                txt_category.requestFocus();
            }
        }
    }

    @FXML
     public void add(ActionEvent actionEvent) {

        if (txt_category.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Marca","Necesitas agregar el nombre de la marca.", txt_category);
            return;
        }

        this.category.setName(txt_category.getText());

        try {

            categoryService.add(this.category);

            loadBrandData();
            txt_category.setStyle("-fx-border-color: transparent;");
            txt_category.setText("");
            txt_category.requestFocus();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void update(){
        if (txt_category.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Marca","Necesitas Seleccionar una marca.", txt_category);
            return;
        }

        try {

            categoryService.update(new Category(txt_category.getText(),this.category.getId()));
            loadBrandData();
            txt_category.setStyle("-fx-border-color: transparent;");
            txt_category.setText("");
            txt_category.requestFocus();

        } catch (SQLException e) {
            e.printStackTrace();
        }
}

    @FXML
    public  void delete(){
        if (txt_category.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Marca","Necesitas Seleccionar una marca.", txt_category);
            return;
        }

        try {

            categoryService.delete(new Brand(txt_category.getText(),this.category.getId()).getId());
            loadBrandData();
            txt_category.setStyle("-fx-border-color: transparent;");
            txt_category.setText("");
            txt_category.requestFocus();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
