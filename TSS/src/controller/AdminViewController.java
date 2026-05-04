package controller;

import javafx.fxml.FXML; 
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tiket.StatistikView;
import tiket.TicketManeger;

/**
 * AdminViewController – Controller für die AdminView.fxml.
 *
 * JavaFX verbindet diese Klasse automatisch mit der AdminView.fxml,
 * weil in der FXML-Datei fx:controller="controller.AdminViewController" steht.
 *
 * Jede Methode mit @FXML kann direkt in der FXML-Datei als onAction aufgerufen werden.
 * Jedes Feld mit @FXML wird automatisch mit dem passenden Element aus der FXML verbunden
 * (das Element muss denselben fx:id-Namen haben).
 */
public class AdminViewController {

    /**
     * Der Statistik-Button aus der AdminView.fxml.
     * "@FXML" sagt JavaFX: "Such in der FXML nach einem Element mit fx:id='statistikBtn'
     * und verbinde es automatisch mit dieser Variable."
     */
    @FXML
    private Button statistikBtn;

    /**
     * initialize() wird von JavaFX automatisch aufgerufen,
     * sobald die FXML vollständig geladen wurde.
     * Hier kann man Startkonfigurationen vornehmen.
     */
    @FXML
    public void initialize() {
        // Hier könnte man z.B. den Button deaktivieren oder Daten laden.
        // Aktuell brauchen wir hier nichts zu tun.
    }

    /**
     * Wird aufgerufen, wenn der Admin auf den Statistik-Button klickt.
     * "@FXML" erlaubt es, diese Methode direkt in der FXML als onAction anzugeben.
     *
     * Öffnet die StatistikView mit den aktuellen Ticket-Daten.
     */
    @FXML
    public void onStatistikClick() {
        // Stage (Fenster) aus dem Button holen
        Stage stage = (Stage) statistikBtn.getScene().getWindow();

        // StatistikView öffnen und den globalen TicketManeger übergeben,
        // damit die Statistik dieselben Daten sieht wie die TicketSeite.
        new StatistikView(TicketManeger.getInstance()).zeige(stage);
    }
}