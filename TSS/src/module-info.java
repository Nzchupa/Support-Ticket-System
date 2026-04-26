module TSS {
    requires javafx.controls;
    requires javafx.fxml;

    opens application to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.base;

    exports application;
    exports controller;
    exports model;
    exports tiket;
}