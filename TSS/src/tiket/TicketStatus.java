package tiket;

public enum TicketStatus {
    OFFEN("🔴 Offen"),
    IN_BEARBEITUNG("🟡 In Bearbeitung"),
    GELOEST("🟢 Gelöst");

    private final String anzeige;

    TicketStatus(String anzeige) {
        this.anzeige = anzeige;
    }

    public String getAnzeige() { return anzeige; }

    @Override
    public String toString() { return anzeige; }
}