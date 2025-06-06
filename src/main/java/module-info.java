module com.j2zromero.pointofsale {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.sun.jna.platform;
    requires io.github.cdimascio.dotenv.java;


    opens com.j2zromero.pointofsale to javafx.fxml;
    exports com.j2zromero.pointofsale;
    exports com.j2zromero.pointofsale.utils;

    /* controllers */
    opens com.j2zromero.pointofsale.controllers.login to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.menu to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.brand to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.category to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.supplier to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.product to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.inventory to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.sale to javafx.fxml;
    opens com.j2zromero.pointofsale.controllers.db to javafx.fxml;

    /* models */
    opens com.j2zromero.pointofsale.models.brands to javafx.base;
    opens com.j2zromero.pointofsale.models.categories to javafx.base;
    opens com.j2zromero.pointofsale.models.suppliers to javafx.base;
    opens com.j2zromero.pointofsale.models.products to javafx.base, javafx.fxml;
    opens com.j2zromero.pointofsale.models.inventories to javafx.base;
    opens com.j2zromero.pointofsale.models.sale to javafx.base;

}