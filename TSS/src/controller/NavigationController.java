package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.UserRole;
import tiket.TicketSeite;

/**
 * Singleton – verwaltet die Navigation zwischen den Views.
 *
 * ── Neue Views ergänzen ────────────────────────────────────────────────────────
 *
 *   Schritt 1: FXML-Datei erstellen (z. B. AdminView.fxml) in src/application/
 *   Schritt 2: In der Methode getFxmlForRole() den TODO-Kommentar durch den
 *              echten Dateinamen ersetzen:
 *
 *              case ADMIN:
 *                  return "AdminView.fxml"; // ← einfach hier eintragen
 *
 *   Das war's. NavigationService übernimmt den Rest automatisch.
 *
 * ── Fenstertitel ──────────────────────────────────────────────────────────────
 *   getTitleForRole() analog anpassen.
 */
public class NavigationController {

    // ── Singleton ─────────────────────────────────────────────────────────────

    private static NavigationController instance;

    public static NavigationController getInstance() {
        if (instance == null) {
            instance = new NavigationController();
        }
        return instance;
    }

    // ── Navigation ─────────────────────────────────────────────────────────────

    /**
     * Navigiert nach dem Login zur rollenspezifischen View.
     * Wird automatisch nach erfolgreichem Login aufgerufen.
     *
     * @param stage Das aktuelle JavaFX-Fenster
     * @param role  Die erkannte Rolle des eingeloggten Benutzers
     */
    public void navigateToRoleView(Stage stage, UserRole role) {
       
    	String fxml  = getFxmlForRole(role);
        String title = getTitleForRole(role);

        
        if (fxmlExists(fxml)) {
            loadView(stage, fxml, title);
        }else {
            stage.setTitle(title);
            new TicketSeite().zeige(stage);
        }
    }

    /** Navigiert zurück zum Login (z. B. nach Logout). */
    public void navigateToLogin(Stage stage) {
        loadView(stage, "LoginView.fxml", "Login");
    }

    // ── Rollenkonfiguration ────────────────────────────────────────────────────

    /**
     * Gibt den FXML-Dateinamen für die jeweilige Rolle zurück.
     */
    private String getFxmlForRole(UserRole role) {
        switch (role) {
            case ADMIN:
                return "AdminView.fxml";       
            case SUPPORT:
                return "SupportView.fxml";     
            case USER:
            default:
                return "UserView.fxml";        
        }
    }

    /**
     * Gibt den Fenstertitel für die jeweilige Rolle zurück.
     *
     * ⬇ HIER Titel anpassen ⬇
     */
    private String getTitleForRole(UserRole role) {
        switch (role) {
            case ADMIN:   return "Admin Panel – Ticket-Support-System";
            case SUPPORT: return "Support Dashboard – Ticket-Support-System";
            case USER:    return "Mein Bereich – Ticket-Support-System";
            default:      return "Ticket-Support-System";
        }
    }

    // ── Interne Helfer ─────────────────────────────────────────────────────────

    private void loadView(Stage stage, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/" + fxmlFile)
            );
            Parent root  = loader.load();
            Scene  scene = new Scene(root);

            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.err.println("[NavigationService] Fehler beim Laden von " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }


    /** Prüft, ob die FXML-Datei im Classpath existiert. */
    private boolean fxmlExists(String fxmlFile) {
        return getClass().getResource("/view/" + fxmlFile) != null;
    }
}
