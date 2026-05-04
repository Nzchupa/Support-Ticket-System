package tiket;

import java.time.LocalDateTime;

public class Ticket {

    private static int zaehler = 0;

    private final String    id;
    private       String    titel;
    private       String    kategorie;
    private       TicketStatus status;
    private final LocalDateTime erstelltAm;

    public Ticket(String titel, String kategorie) {
        this.id         = "T-" + String.format("%03d", ++zaehler);
        this.titel      = titel;
        this.kategorie  = kategorie;
        this.status     = TicketStatus.OFFEN;
        this.erstelltAm = LocalDateTime.now();
    }

    // ── Getter / Setter ───────────────────────────────────────────────────────

    public String       getId()        { return id; }
    public String       getTitel()     { return titel; }
    public String       getKategorie() { return kategorie; }
    public TicketStatus getStatus()    { return status; }
    public LocalDateTime getErstelltAm() { return erstelltAm; }

    public void setTitel(String titel)          { this.titel = titel; }
    public void setKategorie(String kategorie)  { this.kategorie = kategorie; }
    public void setStatus(TicketStatus status)  { this.status = status; }

    /** Anzeige-Text in der ListView */
    public String toDisplayString() {
        return "🎫 " + titel + "  |  " + kategorie + "  |  " + status.getAnzeige();
    }

    @Override
    public String toString() { return toDisplayString(); }
}