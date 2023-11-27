module app.finalapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    opens Entity;

    opens app to javafx.fxml;
    exports app;
}