module app.finalapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    opens Entity;
    opens GUI;


    opens app to javafx.fxml;
    exports app;
    exports Exceptions;
    opens Exceptions to javafx.fxml;
}