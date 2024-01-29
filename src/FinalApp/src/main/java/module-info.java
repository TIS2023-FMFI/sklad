module app.finalapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires itextpdf;
    requires com.jfoenix;

    opens Entity;
    opens GUI;
    opens app;


    exports app;
    exports Exceptions;
    exports Entity;
    opens Exceptions to javafx.fxml;
    opens GUI.OrderProduct;
    opens GUI.WarehouseLayout;
    opens GUI.StoreInProduct;
    opens GUI.Statistics;
    opens GUI.RelocateProduct;
    opens GUI.Reservations;
    opens GUI.CustomerManagement;
    opens GUI.UserManagement;
}