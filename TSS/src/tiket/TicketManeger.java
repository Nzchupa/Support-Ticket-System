package tiket;


	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import java.time.LocalDateTime;
	import java.time.format.DateTimeFormatter;

	public class TicketManeger {

	    private ObservableList<String> ticketListe = FXCollections.observableArrayList();
	    private ObservableList<String> historyListe = FXCollections.observableArrayList();
	    private ObservableList<String> anhangListe  = FXCollections.observableArrayList();

	    public void ticketHinzufuegen(String titel, String kategorie) {
	        String ticket = "🎫 " + titel + " | " + kategorie;
	        ticketListe.add(ticket);
	        historyEintrag("✅ Ticket erstellt: " + titel);
	    }

	    public void ticketLoeschen(String ticket) {
	        ticketListe.remove(ticket);
	        historyEintrag("🗑️ Ticket gelöscht: " + ticket);
	    }

	    public void alleLoeschen() {
	        ticketListe.clear();
	        historyEintrag("🧹 Alle Tickets gelöscht");
	    }

	    public void anhangHinzufuegen(String dateiName) {
	        anhangListe.add("📎 " + dateiName);
	        historyEintrag("📎 Anhang hinzugefügt: " + dateiName);
	    }

	    public void historyEintrag(String text) {
	        String zeit = LocalDateTime.now().format(
	            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
	        );
	        historyListe.add(0, text + " — " + zeit);
	    }

	    public ObservableList<String> getTicketListe()  { return ticketListe; }
	    public ObservableList<String> getHistoryListe() { return historyListe; }
	    public ObservableList<String> getAnhangListe()  { return anhangListe; }
	}

