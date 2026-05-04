package tiket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TicketManeger – Verwaltet alle Tickets, die History und die Anhänge.
 *
 * SINGLETON-MUSTER:
 *   Diese Klasse existiert nur EIN MAL im gesamten Programm.
 *   Egal ob TicketSeite oder AdminViewController – alle greifen auf
 *   dieselbe Instanz zu und sehen dieselben Daten.
 *
 *   Zugriff immer über: TicketManeger.getInstance()
 *   NIEMALS: new TicketManeger() → das würde eine neue, leere Instanz erstellen!
 */
public class TicketManeger {

    // ── Singleton ─────────────────────────────────────────────────────────────

    /**
     * Die einzige Instanz dieser Klasse.
     * "static" bedeutet: gehört zur Klasse, nicht zu einem Objekt.
     * Wird beim ersten Aufruf von getInstance() erstellt.
     */
    private static TicketManeger instance;

    /**
     * Privater Konstruktor – verhindert, dass jemand "new TicketManeger()" schreibt.
     * Die einzige Möglichkeit diese Klasse zu nutzen ist über getInstance().
     */
    private TicketManeger() {}

    /**
     * Gibt die einzige Instanz des TicketManeger zurück.
     * Falls noch keine existiert, wird sie jetzt erstellt.
     *
     * @return Die globale TicketManeger-Instanz
     */
    public static TicketManeger getInstance() {
        if (instance == null) {
            // Erste Aufruf: Instanz erstellen
            instance = new TicketManeger();
        }
        // Alle weiteren Aufrufe: dieselbe Instanz zurückgeben
        return instance;
    }

    // ── Listen ────────────────────────────────────────────────────────────────

    /**
     * Alle aktuell vorhandenen Tickets als Ticket-Objekte.
     * ObservableList → ListView aktualisiert sich automatisch bei Änderungen.
     */
    private ObservableList<Ticket> ticketListe = FXCollections.observableArrayList();

    /**
     * Alle bisherigen Aktionen (z.B. "Ticket erstellt", "Ticket gelöscht").
     * Neueste Einträge stehen ganz oben (Index 0).
     */
    private ObservableList<String> historyListe = FXCollections.observableArrayList();

    /**
     * Alle angehängten Dateien (z.B. "Datei.pdf", "Screenshot.png").
     */
    private ObservableList<String> anhangListe = FXCollections.observableArrayList();

    // ── Ticket-Aktionen ───────────────────────────────────────────────────────

    /**
     * Erstellt ein neues Ticket und fügt es zur Liste hinzu.
     *
     * @param titel     Titel des neuen Tickets
     * @param kategorie Kategorie des neuen Tickets
     */
    public void ticketHinzufuegen(String titel, String kategorie) {
        Ticket ticket = new Ticket(titel, kategorie);
        ticketListe.add(ticket);
        historyEintrag("✅ Ticket erstellt: " + titel);
    }

    /**
     * Löscht ein einzelnes Ticket.
     *
     * @param ticket Das Ticket-Objekt, das gelöscht werden soll
     */
    public void ticketLoeschen(Ticket ticket) {
        ticketListe.remove(ticket);
        historyEintrag("🗑️ Ticket gelöscht: " + ticket.getTitel());
    }

    /** Löscht alle Tickets auf einmal. */
    public void alleLoeschen() {
        ticketListe.clear();
        historyEintrag("🧹 Alle Tickets gelöscht");
    }

    /**
     * Fügt einen Datei-Anhang zur Liste hinzu.
     *
     * @param dateiName Name der angehängten Datei
     */
    public void anhangHinzufuegen(String dateiName) {
        anhangListe.add("📎 " + dateiName);
        historyEintrag("📎 Anhang hinzugefügt: " + dateiName);
    }

    /**
     * Erstellt einen History-Eintrag mit aktuellem Datum/Uhrzeit.
     * Neueste Einträge kommen immer ganz oben (Index 0).
     *
     * @param text Beschreibung der Aktion
     */
    public void historyEintrag(String text) {
        String zeit = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        );
        historyListe.add(0, text + " — " + zeit);
    }

    // ── Getter ────────────────────────────────────────────────────────────────

    public ObservableList<Ticket> getTicketListe()  { return ticketListe; }
    public ObservableList<String> getHistoryListe() { return historyListe; }
    public ObservableList<String> getAnhangListe()  { return anhangListe; }
}