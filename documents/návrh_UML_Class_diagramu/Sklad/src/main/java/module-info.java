module com.example.sklad {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens Strecno to javafx.fxml;
    exports Strecno;
}