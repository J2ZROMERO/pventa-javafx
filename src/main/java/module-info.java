module com.j2zromero.pointofsale {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.sun.jna.platform;


    opens com.j2zromero.pointofsale to javafx.fxml;
    exports com.j2zromero.pointofsale;
    exports com.j2zromero.pointofsale.utils;
    opens com.j2zromero.pointofsale.controllers.login to javafx.fxml;

}