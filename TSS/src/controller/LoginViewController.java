package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller für LoginView.fxml.
 *
 * Verknüpft die Login- und Registrierungs-Formulare mit dem Backend.
 * Alle Geschäftslogik liegt in AuthService und NavigationService.
 */
public class LoginViewController {

    // ── Login-Bereich ──────────────────────────────────────────────────────────

    /** fx:id="loginEmailField" */
    @FXML private TextField loginEmailField;

    /** fx:id="loginPasswordField" */
    @FXML private PasswordField loginPasswordField;

    /** fx:id="loginErrorLabel" – zeigt Fehlermeldungen */
    @FXML private Label loginErrorLabel;

    // ── Registrierungs-Bereich ────────────────────────────────────────────────

    /** fx:id="registerEmailField" */
    @FXML private TextField registerEmailField;

    /** fx:id="registerPasswordField" */
    @FXML private PasswordField registerPasswordField;

    /** fx:id="registerMessageLabel" – zeigt Erfolgs- oder Fehlermeldung */
    @FXML private Label registerMessageLabel;

    // ── Controller ──────────────────────────────────────────────────────────────

    private final AuthController       authController      = AuthController.getInstance();
    private final NavigationController navigationController = NavigationController.getInstance();

    // ── FXML-Aktionen (Button onAction) ───────────────────────────────────────

    /**
     * Wird aufgerufen wenn der "Sign In"-Button geklickt wird.
     */
    @FXML
    public void handleLogin() {
        clearMessages();

        String email    = loginEmailField.getText().trim();
        String password = loginPasswordField.getText();

        // Validierung
        if (email.isEmpty() || password.isEmpty()) {
            showLoginError("Bitte E-Mail und Passwort eingeben.");
            return;
        }
        if (!email.contains("@")) {
            showLoginError("Bitte eine gültige E-Mail-Adresse eingeben.");
            return;
        }

        // Authentifizierung
        Optional<User> result = authController.login(email, password);

        if (result.isPresent()) {
            User user  = result.get();
            Stage stage = (Stage) loginEmailField.getScene().getWindow();

            System.out.println("[Login] Erfolgreich: " + user.getEmail()
                + " | Rolle: " + user.getRole());

            navigationController.navigateToRoleView(stage, user.getRole());

        } else {
            showLoginError("Ungültige E-Mail-Adresse oder falsches Passwort.");
            loginPasswordField.clear();
        }
    }

    /**
     * Wird aufgerufen wenn der "Create Account"-Button geklickt wird.
     * FXML: onAction="#handleRegister"
     */
    @FXML
    public void handleRegister() {
       
    	clearMessages();

        String email    = registerEmailField.getText().trim();
        String password = registerPasswordField.getText();

        // Validierung
        if (email.isEmpty() || password.isEmpty()) {
            showRegisterMessage("Bitte E-Mail und Passwort eingeben.", false);
            return;
        }
        if (!email.contains("@")) {
            showRegisterMessage("Bitte eine gültige E-Mail-Adresse eingeben.", false);
            return;
        }
        if (password.length() < 6) {
            showRegisterMessage("Das Passwort muss mindestens 6 Zeichen lang sein.", false);
            return;
        }
        if (authController.emailExists(email)) {
            showRegisterMessage("Diese E-Mail-Adresse ist bereits registriert.", false);
            return;
        }

        // Registrierung
        boolean success = authController.register(email, password);

        if (success) {
            // Erkannte Rolle anzeigen (hilfreich für Entwicklung)
            String detectedRole = authController.detectRole(email).name();
            showRegisterMessage(
                "Account erstellt! Erkannte Rolle: " + detectedRole, true
            );
            registerEmailField.clear();
            registerPasswordField.clear();

        } else {
            showRegisterMessage("Registrierung fehlgeschlagen. Bitte erneut versuchen.", false);
        }
    }

    // ── Hilfsmethoden ─────────────────────────────────────────────────────────

    private void clearMessages() {
       
    	if (loginErrorLabel    != null) { 
        	loginErrorLabel.setText("");
        }
        
        if (registerMessageLabel != null){ 
        	registerMessageLabel.setText("");
        }
    }

    private void showLoginError(String message) {
        
    	if (loginErrorLabel != null) {
            loginErrorLabel.setText(message);
            loginErrorLabel.setStyle("-fx-text-fill: #FF6B6B;");
        }
    }

    private void showRegisterMessage(String message, boolean success) {
        
    	if (registerMessageLabel != null) {
            registerMessageLabel.setText(message);
            registerMessageLabel.setStyle(
                success ? "-fx-text-fill: #90EE90;" : "-fx-text-fill: #FF6B6B;"
            );
        }
    }
    @FXML
    private void openHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeView.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) loginErrorLabel.getScene().getWindow();
            stage.setTitle("TSSupport System");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
