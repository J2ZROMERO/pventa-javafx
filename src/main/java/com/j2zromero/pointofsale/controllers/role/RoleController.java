package com.j2zromero.pointofsale.controllers.role;

import com.j2zromero.pointofsale.models.permission.Permission;
import com.j2zromero.pointofsale.models.role.Role;
import com.j2zromero.pointofsale.services.PermissionService;
import com.j2zromero.pointofsale.services.role.RoleService;
import com.j2zromero.pointofsale.utils.DialogUtils;
import com.j2zromero.pointofsale.utils.FormUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class RoleController {
    public TableView tableListPermission;
    public TableColumn rolPermissionColumn;
    public AnchorPane anchorRole;
    @FXML private ComboBox<Role> cbxRole;

    // Left: all permissions
    @FXML private TableColumn<Permission, String> permissionListColumn;
    @FXML private TableColumn<Permission, String> descriptionListColumn;
    @FXML private TableColumn<Permission, Void> actionListColumn;

    // Right: permissions assigned to role
    @FXML private TableView<Permission> tableRolePermissions;
    @FXML private TableColumn<Permission, String> roleDescriptionColumn;
    @FXML private TableColumn<Permission, Void> roleActionColumn;

    private final RoleService roleService = new RoleService();
    private final PermissionService permissionService = new PermissionService();

    private final ObservableList<Permission> allPermissions      = FXCollections.observableArrayList();
    private final ObservableList<Permission> assignedPermissions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (anchorRole.getScene() != null) {
                anchorRole.getScene().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/global.css")).toExternalForm()
                );
            }
        });
        try {
            // Load roles and configure comboBox
            List<Role> roles = roleService.getAll();
            FormUtils.applyComboBoxFilter(cbxRole, roles,
                    Role::getName,
                    this::reloadTablesForSelectedRole);
            cbxRole.setStyle("-fx-font-size: 16px;");
            // Configure ALL permissions table
            permissionListColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            descriptionListColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            addAssignButtonToAllPermissions();

            // Configure ROLE permissions table
            rolPermissionColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            roleDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            addRevokeButtonToRolePermissions();

            // Select first role by default (if any)
            if (!roles.isEmpty()) {
                cbxRole.getSelectionModel().selectFirst();
                reloadTablesForSelectedRole(cbxRole.getValue());
            }
        } catch (SQLException e) {
            DialogUtils.showWarningAlert("Error", "No se pudieron cargar los roles.", null);
        }
    }

    private void reloadTablesForSelectedRole(Role role) {
        if (role == null) return;
        try {
            List<Permission> all = permissionService.getAll();
            List<Permission> assigned = permissionService.getPermissionsByRole(role.getName());

            allPermissions.setAll(all);
            assignedPermissions.setAll(assigned);
            allPermissions.removeAll(assignedPermissions);

            tableListPermission.setItems(allPermissions);
            tableRolePermissions.setItems(assignedPermissions);
        } catch (SQLException e) {
            DialogUtils.showWarningAlert("Error", "No se pudieron cargar permisos.", null);
        }
    }

    private void addAssignButtonToAllPermissions() {
        actionListColumn.setCellFactory(col -> new TableCell<Permission, Void>() {
            private final Button btn = new Button("+");
            private final HBox container = new HBox(btn);
            {
                btn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;"); // green
                container.setAlignment(Pos.CENTER_RIGHT);
                HBox.setHgrow(btn, Priority.NEVER);
                btn.setOnAction(e -> {
                    Permission perm = getTableView().getItems().get(getIndex());
                    Role role = cbxRole.getValue();
                    try {
                        permissionService.addRolePermission(role.getId(), perm.getId());
                        reloadTablesForSelectedRole(role);
                    } catch (SQLException ex) {
                        DialogUtils.showWarningAlert("Error", "No se pudo asignar el permiso.", null);
                    }
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void addRevokeButtonToRolePermissions() {
        roleActionColumn.setCellFactory(col -> new TableCell<Permission, Void>() {
            private final Button btn = new Button("X");
            private final HBox container = new HBox(btn);
            {
                btn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;"); // red
                container.setAlignment(Pos.CENTER_RIGHT);
                HBox.setHgrow(btn, Priority.NEVER);
                btn.setOnAction(e -> {
                    Permission perm = getTableView().getItems().get(getIndex());
                    Role role = cbxRole.getValue();
                    try {
                        permissionService.removeRolePermission(role.getId(), perm.getId());
                        reloadTablesForSelectedRole(role);
                    } catch (SQLException ex) {
                        DialogUtils.showWarningAlert("Error", "No se pudo revocar el permiso.", null);
                    }
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }
}
