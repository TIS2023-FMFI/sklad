module com.example.sklad {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.warehouse to javafx.fxml;
    exports com.example.warehouse;
}