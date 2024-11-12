module com.j2zromero.pointofsale {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.sun.jna.platform;


    opens com.j2zromero.pointofsale to javafx.fxml;
    exports com.j2zromero.pointofsale;
    exports com.j2zromero.pointofsale.utils;

    /* controllers */
    opens com.j2zromero.pointofsale.controllers.login to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.menu to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.brand to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.category to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.supplier to javafx.fxml;
    /* models */
    opens com.j2zromero.pointofsale.models.brands to javafx.base;
    opens com.j2zromero.pointofsale.models.categories to javafx.base;
    opens com.j2zromero.pointofsale.models.suppliers to javafx.base;

}