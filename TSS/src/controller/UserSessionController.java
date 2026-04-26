package controller;

import model.User;
import model.UserRole;

/**
 * Singleton – hält den aktuell eingeloggten Benutzer für die gesamte
 * Laufzeit der Anwendung.
 *
 * Verwendung in jedem Controller:
 *   User me = UserSession.getInstance().getCurrentUser();
 */
public class UserSessionController {

    private static UserSessionController instance;
    private User currentUser;


    public static UserSessionController getInstance() {
        if (instance == null) {
            instance = new UserSessionController();
        }
        return instance;
    }

    // ── Session-Verwaltung ────────────────────────────────────────────────────

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /** Gibt den eingeloggten Benutzer zurück, oder null wenn niemand angemeldet. */
    public User getCurrentUser() {
        return currentUser;
    }

    /** Gibt die Rolle des eingeloggten Benutzers zurück. */
    public UserRole getCurrentRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    /** Gibt true zurück, wenn gerade jemand angemeldet ist. */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /** Sitzung beenden (Logout). */
    public void clearSession() {
        currentUser = null;
    }
}
