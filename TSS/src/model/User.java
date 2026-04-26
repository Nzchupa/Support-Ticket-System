package model;

/**
 * Repräsentiert einen registrierten Benutzer im System.
 * Das Passwort wird ausschließlich als SHA-256-Hash gespeichert.
 */
public class User {

    private final String email;
    private final String passwordHash; // SHA-256 Hash, niemals Klartext
    private final UserRole role;

    public User(String email, String passwordHash, UserRole role) {
        this.email        = email.toLowerCase().trim();
        this.passwordHash = passwordHash;
        this.role         = role;
    }

    // ── Getter ────────────────────────────────────────────────────────────────

    public String getEmail(){
    	return email; 
    }
   
    public String getPasswordHash() { 
    	return passwordHash; 
    }
    
    public UserRole getRole(){ 
    	return role; 
    }

    /** Anzeigename: der lokale Teil vor dem @-Zeichen. */
    public String getDisplayName() {
       
    	int at = email.indexOf('@');
    	return email.substring(0, at);	
    }

    @Override
    public String toString() {
        return "User{email='" + email + "', role=" + role + '}';
    }
}
