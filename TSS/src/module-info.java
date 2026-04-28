module SupportTicketSystem {

    requires javafx.controls;
    requires javafx.fxml;

    opens application to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.base;
    opens view to javafx.fxml;

    exports application;
    exports controller;
    exports model;
    exports tiket;
}