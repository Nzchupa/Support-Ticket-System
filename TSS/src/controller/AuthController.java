package controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import model.User;
import model.UserRole;

/**
 * Zentraler Authentifizierungs-Controller.
 */
public class AuthController {

    // ── Singleton ─────────────────────────────────────────────────────────────

    private static AuthController instance;

    private AuthController() {
        seedDemoUsers();
    }

    public static AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }


    /** Key = E-Mail in Kleinschreibung */
    private final Map<String, User> userStore = new HashMap<>();

    /**
     * Demo-Benutzer für Entwicklung/Tests.
     */
    private void seedDemoUsers() {
        registerInternal("admin@admin.com",    "admin123");
        registerInternal("support@support.com", "support123");
        registerInternal("user@example.com",    "user123");
    }

    /**
     * Meldet einen Benutzer an.
     *
     * @param email    E-Mail-Adresse (Groß-/Kleinschreibung egal)
     * @param password Klartext-Passwort
     * @return Optional mit dem User-Objekt bei Erfolg, sonst empty()
     */
    public Optional<User> login(String email, String password) {
        
    	if (isBlank(email) || isBlank(password)) {
    		return Optional.empty();
    	}

        User user = userStore.get(email.toLowerCase().trim());

        if (user == null) {
        	return Optional.empty();
        }

        if (!user.getPasswordHash().equals(hashPassword(password))) {
            return Optional.empty();
        }

        UserSessionController.getInstance().setCurrentUser(user);
        return Optional.of(user);
    }

    /**
     * Registriert einen neuen Benutzer.
     * Die Rolle wird automatisch anhand der E-Mail-Domain vergeben.
     *
     * @param email    E-Mail-Adresse
     * @param password Klartext-Passwort (wird gehasht gespeichert)
     * @return true bei Erfolg, false wenn E-Mail bereits existiert oder Eingabe ungültig
     */
    public boolean register(String email, String password) {
        
    	if (isBlank(email) || isBlank(password)) {
        	return false;
        }
        
        return registerInternal(email, password);
    }

    /**
     * Erkennt die Benutzerrolle anhand der E-Mail-Domain.
     */
    public UserRole detectRole(String email) {
        if (isBlank(email)) return UserRole.USER;

        String lower = email.toLowerCase().trim();

        // Domain-Teil extrahieren (nach dem @)
        int atIndex = lower.indexOf('@');
        if (atIndex < 0) return UserRole.USER;
        String domain = lower.substring(atIndex + 1); // z. B. "admin.com"

        if (domain.startsWith("admin")) return UserRole.ADMIN;
        if (domain.startsWith("support")) return UserRole.SUPPORT;

        return UserRole.USER;
    }

    /** Gibt an, ob eine E-Mail bereits registriert ist. */
    public boolean emailExists(String email) {
       
    	if (isBlank(email)) {
        	return false;
        }
        return userStore.containsKey(email.toLowerCase().trim());
    }

    // ── Interne Helfer ─────────────────────────────────────────────────────────

    private boolean registerInternal(String email, String password) {
        
    	String key = email.toLowerCase().trim();
        if (userStore.containsKey(key)) {
        	return false; // bereits vorhanden
        }

        UserRole role = detectRole(email);
        String hash   = hashPassword(password);
        userStore.put(key, new User(email, hash, role));
        return true;
    }

    /**
     * SHA-256-Hashing
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 nicht verfügbar", e);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
