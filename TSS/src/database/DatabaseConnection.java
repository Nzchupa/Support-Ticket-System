package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	// ── Verbindungsdaten – hier anpassen ──────────────────────────────────────
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/ticket_system"
                                            + "?useSSL=false&serverTimezone=Europe/Berlin"
                                            + "&allowPublicKeyRetrieval=true";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "dwP+cD_p68+0"; // ← euer MySQL-Passwort eintragen

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        connect();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
 // ── Verbindung ────────────────────────────────────────────────────────────

    /**
     * Gibt die aktive Verbindung zurück.
     * Stellt die Verbindung automatisch wieder her falls sie unterbrochen wurde.
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.err.println("[DB] Verbindungsprüfung fehlgeschlagen: " + e.getMessage());
            connect();
        }
        return connection;
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("[DB] Verbindung hergestellt.");
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] JDBC-Treiber nicht gefunden – mysql-connector-j.jar im Build Path?");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[DB] Verbindung fehlgeschlagen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Verbindung schließen (bei App-Ende aufrufen). */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Verbindung geschlossen.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Fehler beim Schließen: " + e.getMessage());
        }
    }
}
