package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.User;
import model.UserRole;

public class UserDAO {

	private Connection con() {
		return DatabaseConnection.getInstance().getConnection();
	}
	
	/**
     * Sucht einen Benutzer anhand seiner E-Mail-Adresse.
     */
    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT u.email, u.password_hash, r.name AS role_name
                FROM users u
                LEFT JOIN roles r ON u.role_id = r.id
                WHERE u.email = ?
                """;

        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase().trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserRole role = parseRole(rs.getString("role_name"));
                User user = new User(
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    role
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] findByEmail Fehler: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    /**
     * Gibt alle registrierten Benutzer zurück (für Admin-Ansicht).
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<User>();
        String sql = """
                SELECT u.email, u.password_hash, r.name AS role_name
                FROM users u
                LEFT JOIN roles r ON u.role_id = r.id
                ORDER BY u.created_at DESC
                """;

        try (PreparedStatement ps = con().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserRole role = parseRole(rs.getString("role_name"));
                users.add(new User(
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    role
                ));
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] findAll Fehler: " + e.getMessage());
        }
        return users;
    }
    
    /** Prüft ob eine E-Mail bereits registriert ist. */
    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase().trim());
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("[UserDAO] emailExists Fehler: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Speichert einen neuen Benutzer in der Datenbank.
     *
     * @return true bei Erfolg
     */
    public boolean insert(String email, String passwordHash, UserRole role) {
        String sql = """
                INSERT INTO users (username, email, password_hash, role_id)
                VALUES (?, ?, ?, (SELECT id FROM roles WHERE name = ?))
                """;

        try (PreparedStatement ps = con().prepareStatement(sql)) {
            String lower = email.toLowerCase().trim();
            ps.setString(1, lower.split("@")[0]); // username = Teil vor @
            ps.setString(2, lower);
            ps.setString(3, passwordHash);
            ps.setString(4, roleToDbName(role));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[UserDAO] insert Fehler: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Ändert die Rolle eines Benutzers.
     *
     * @return true bei Erfolg
     */
    public boolean updateRole(String email, UserRole newRole) {
        String sql = """
                UPDATE users
                SET role_id = (SELECT id FROM roles WHERE name = ?)
                WHERE email = ?
                """;

        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, roleToDbName(newRole));
            ps.setString(2, email.toLowerCase().trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDAO] updateRole Fehler: " + e.getMessage());
            return false;
        }
    }
    
    // ── Hilfsmethoden ─────────────────────────────────────────────────────────

    private UserRole parseRole(String roleName) {
        if (roleName == null) {
        	return UserRole.USER;
        }
        return switch (roleName) {
            case "Admin"   -> UserRole.ADMIN;
            case "Support" -> UserRole.SUPPORT;
            default        -> UserRole.USER;
        };
    }

    private String roleToDbName(UserRole role) {
        return switch (role) {
            case ADMIN   -> "Admin";
            case SUPPORT -> "Support";
            case USER    -> "User";
        };
    }

}
