package com.j2zromero.pointofsale.controllers.user;

import com.j2zromero.pointofsale.models.role.Role;
import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.services.role.RoleService;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class UserController {

    public TextField cbxAddress;
    public AnchorPane anchorUser;
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
        DialogUtils.TooltipHelper.install(btnAdd,
                "Agregar usuario.",
                DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnUpdate,
                "Actualizar usuario.",
                DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnClean,
                "Limpiar campos",
                DialogUtils.TooltipColor.DARK);
        DialogUtils.TooltipHelper.install(btnDelete,
                "Eliminar usuario.",
                DialogUtils.TooltipColor.RED);



        Platform.runLater(() -> {
            if (anchorUser.getScene() != null) {
                anchorUser.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });

        cbxStatus.getItems().setAll("activo", "inactivo");
        try {
            roles = roleService.getAll();
            cbxRol.getItems().setAll(roles);
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudieron cargar los roles.", null);
        }

        loadData();
    }

    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Usuario", "Ingrese el nombre.", txtName);
            return false;
        }
        if (txtPassword.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Contraseña", "Ingrese la contraseña.", txtPassword);
            return false;
        }
        if (cbxRol.getSelectionModel().isEmpty()) {
            DialogUtils.showWarningAlert("Rol", "Debes agregar un rol.", cbxRol);
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            DialogUtils.showWarningAlert("Usuario", "Ingrese correo.", txtEmail);
            return false;
        }
        return true;
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
                    user -> user.getName() + " " + user.getEmail(),
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
                        System.out.println(currentUser);
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
        validateForm();

        User user = new User();
        user.setName(txtName.getText());
        user.setFkRoleCode(cbxRol.getSelectionModel().getSelectedItem().getName());
        user.setEmail(txtEmail.getText());
        user.setPhone(txtPhone.getText());
        user.setStatus(cbxStatus.getValue() == "activo");
        user.setPassword(txtPassword.getText());
        try {
            userService.add(user);
            cleanFields(null);
            DialogUtils.showToast("Usuario agregado con exito.",2, "green");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showWarningAlert("Error", "No se pudo agregar el usuario.", null);
        }
    }

    @FXML
    public void update(ActionEvent actionEvent) {
        validateForm();
        currentUser.setName(txtName.getText());
        //currentUser.setFkRoleCode(cbxRol.getValue());
        currentUser.setEmail(txtEmail.getText());
        currentUser.setPhone(txtPhone.getText());
        currentUser.setStatus("activo".equalsIgnoreCase(cbxStatus.getValue()));
        currentUser.setPassword(txtPassword.getText());

        try {
            userService.update(currentUser);
            cleanFields(null);
            DialogUtils.showToast("Usuario actualizado.",2, "blue");
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
                    DialogUtils.showToast("Usuario eliminado.",2, "blue");
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
        cbxStatus.setValue("");
    }
}
