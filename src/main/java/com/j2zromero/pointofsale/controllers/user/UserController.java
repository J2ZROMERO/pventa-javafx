package com.j2zromero.pointofsale.controllers.user;

import com.j2zromero.pointofsale.models.role.Role;
import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.services.role.RoleService;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.List;

public class UserController {

    public TextField cbxAddress;
    @FXML private ComboBox<User> cbxUser;        // Para listar/seleccionar usuario existente
    @FXML private ComboBox<String> cbxStatus;     // "Activo" / "Inactivo"
    @FXML private ComboBox<Role> cbxRol;        // 'admin', 'gerente', 'cajero', etc.

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClean;

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtPassword;

    @FXML private AnchorPane users_pane;

    private UserService userService = new UserService();
    private User currentUser = new User();
    private List<User> userList;
    private RoleService roleService = new RoleService();
    List<Role> roles;
    @FXML
    private void initialize() {
        // Inicializamos estado estático
        cbxStatus.getItems().setAll("activo", "inactivo");
        cbxStatus.setValue("activo");
        // Cargamos roles dinámicos
        try {
            roles = roleService.getAll();
            cbxRol.getItems().setAll(roles);
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudieron cargar los roles.", null);
        }

        loadData();
    }


    private void loadData() {
        try {
            userList = userService.getAll();
            // Si no hay usuarios, limpiamos el ComboBox y salimos
            if (userList == null || userList.isEmpty()) {
                return;
            }

            FormUtils.applyComboBoxFilter(
                    cbxUser,
                    userList,
                    user -> user.getName(),
                    selected -> {
                        currentUser = selected;

                        txtName.setText(currentUser.getName());
                        txtEmail.setText(currentUser.getEmail());
                        txtPhone.setText(currentUser.getPhone());
                        txtPassword.setText(currentUser.getPassword());
                        cbxRol.setValue(
                                roles.stream()
                                        .filter(s -> selected.getFkRoleCode() != null &&
                                                selected.getFkRoleCode().equals(s.getName()))
                                        .findFirst()
                                        .orElse(null)
                        );
                        cbxStatus.setValue(currentUser.getStatus()? "activo" : "inactivo");

                        FormUtils.enableDisable(false, btnUpdate, btnDelete);
                        FormUtils.enableDisable(true, btnAdd);
                    }
            );
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo cargar la lista de usuarios.", null);
        }
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        if (txtName.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Usuario", "Ingrese el nombre.", txtName);
            return;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Usuario", "Ingrese correo.", txtEmail);
            return;
        }

        User user = new User();
        user.setName(txtName.getText());
        user.setFkRoleCode(cbxRol.getSelectionModel().getSelectedItem().getName());
        user.setEmail(txtEmail.getText());
        user.setPhone(txtPhone.getText());
        user.setStatus(cbxStatus.getValue() == "activo");
        user.setPassword(txtPassword.getText());
        System.out.println(user);
        try {
            userService.add(user);
            cleanFields(null);
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo agregar el usuario.", null);
        }
    }

    @FXML
    public void update(ActionEvent actionEvent) {
        currentUser.setName(txtName.getText());
        //currentUser.setFkRoleCode(cbxRol.getValue());
        currentUser.setEmail(txtEmail.getText());
        currentUser.setPhone(txtPhone.getText());
        currentUser.setStatus("Activo".equals(cbxStatus.getValue()));
        currentUser.setPassword(txtPassword.getText());

        try {
            userService.update(currentUser);
            cleanFields(null);
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo actualizar el usuario.", null);
        }
        FormUtils.enableDisable(true, btnUpdate, btnDelete);
    }

    @FXML
    public void delete(ActionEvent actionEvent) {
        DialogUtils.showConfirmationDialog(
                "Eliminar usuario",
                "¿Seguro que desea eliminar este usuario?",
                "Esta acción no se puede deshacer"
        ).ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    userService.delete(currentUser.getId());
                    cleanFields(null);
                    loadData();
                } catch (SQLException e) {
                    e.printStackTrace();
                    DialogUtils.showWarningAlert("Error", "No se pudo eliminar el usuario.", null);
                }
            }
        });
        FormUtils.enableDisable(true, btnUpdate, btnDelete);
    }

    @FXML
    public void cleanFields(ActionEvent actionEvent) {
        FormUtils.clearAndResetStyles(users_pane);
        FormUtils.clearFields(users_pane);
        FormUtils.enableDisable(true, btnUpdate, btnDelete);
        FormUtils.enableDisable(false, btnAdd);
        txtName.requestFocus();
        cbxStatus.setValue("activo");
    }
}
