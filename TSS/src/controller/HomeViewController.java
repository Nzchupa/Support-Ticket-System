package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeViewController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private void showHome() {
        titleLabel.setText("TSSupport System");
        descriptionLabel.setText(
            "Ein modernes Ticket- und Supportsystem für Benutzer, Admins und Projektverwaltung."
        );
    }

    @FXML
    private void showProjectInfo() {
        titleLabel.setText("Über das Projekt");
        descriptionLabel.setText(
            "Dieses Projekt wurde mit JavaFX entwickelt. "
            + "Es enthält Login, Registrierung, Benutzerrollen und eine Ticketverwaltung."
        );
    }

    @FXML
    private void openLogin() {
        openView("/view/LoginView.fxml", "Anmelden");
    }

    @FXML
    private void openRegister() {
        openView("/view/LoginView.fxml", "Registrieren");
    }

    private void openView(String fxmlPath, String title) {
        try {
            Stage stage = (Stage) titleLabel.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}