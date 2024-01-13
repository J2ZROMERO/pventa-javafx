module com.j2zromero.pointofsale {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.j2zromero.pointofsale to javafx.fxml;
    exports com.j2zromero.pointofsale;
    exports com.j2zromero.pointofsale.utils;
    exports com.j2zromero.pointofsale.controllers;
    exports com.j2zromero.pointofsale.utils;
}