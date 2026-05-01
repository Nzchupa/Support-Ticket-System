package controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import database.UserDAO;
import model.User;
import model.UserRole;

/**
 * Zentraler Authentifizierungs-Controller.
 */
public class AuthController {

    // ── Singleton ─────────────────────────────────────────────────────────────

    private static AuthController instance;

    public AuthController() {
       
    }

    public static AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }


    /** Key = E-Mail in Kleinschreibung */
    private final UserDAO userDAO = new UserDAO();



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

        Optional<User> result = userDAO.findByEmail(email);

        if (result.isEmpty()) {
        	return Optional.empty();
        }
        
        User user = result.get();
        
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
    	if(userDAO.emailExists(email)) {
    		return false;
    	}
    	
    	UserRole role = detectRole(email);
    	String hash = hashPassword(password);
        
        return userDAO.insert(email, hash, role);
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
        return userDAO.emailExists(email);
    }

    /**
     * SHA-256-Hashing
     */
    public String hashPassword(String password) {
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
    
    public List<User> getAllUsers(){
    	return userDAO.findAll();
    }
    
    public boolean assignRole(String email, UserRole newRole) {
    	if(isBlank(email) || newRole == null) {
    		return false;
    	}
    	
    	return userDAO.updateRole(email, newRole);
    }
}
