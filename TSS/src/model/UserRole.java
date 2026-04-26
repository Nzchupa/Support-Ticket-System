package model;

/**
 * Die drei Rollen im Ticket-Support-System.
 * Die Rollenerkennung erfolgt anhand der E-Mail-Domain in AuthService.
 *
 * Rollenregeln (konfigurierbar in AuthService):
 *   @admin.*   → ADMIN
 *   @support.* → SUPPORT
 *   alles andere → USER
 */
public enum UserRole {
    ADMIN,
    SUPPORT,
    USER
}
