package tiket;

import controller.UserSessionController;
import model.UserRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class TicketSeite {

    private static final String DARK_BG = "#1e1e1e";

    private static final String FIELD_STYLE =
        "-fx-background-color: #2b2b2b;" +
        "-fx-text-fill: white;" +
        "-fx-prompt-text-fill: #888888;" +
        "-fx-highlight-text-fill: white;" +
        "-fx-highlight-fill: #4a4a4a;" +
        "-fx-border-color: #555555;" +
        "-fx-border-radius: 8;" +
        "-fx-background-radius: 8;" +
        "-fx-padding: 8;";

    private static final String TEXTAREA_STYLE =
        "-fx-control-inner-background: #2b2b2b;" +
        "-fx-text-fill: white;" +
        "-fx-prompt-text-fill: #888888;" +
        "-fx-highlight-text-fill: white;" +
        "-fx-highlight-fill: #4a4a4a;" +
        "-fx-border-color: #555555;" +
        "-fx-border-radius: 8;" +
        "-fx-background-radius: 8;" +
        "-fx-padding: 8;";

    // Singleton: immer dieselbe Instanz holen, damit Admin und User dieselben Daten sehen
    private TicketManeger manager = TicketManeger.getInstance();

    public void zeige(Stage stage) {

        // ===== FELDER =====
        TextField titelFeld = new TextField();
        titelFeld.setPromptText("Titel eingeben...");
        titelFeld.setStyle(FIELD_STYLE);

        TextArea beschreibungFeld = new TextArea();
        beschreibungFeld.setPromptText("Beschreibung eingeben...");
        beschreibungFeld.setPrefHeight(90);
        beschreibungFeld.setWrapText(true);
        beschreibungFeld.setStyle(TEXTAREA_STYLE);

        ComboBox<String> kategorie = new ComboBox<>();
        kategorie.getItems().addAll("🐛 Bug", "📋 Anfrage", "❓ Sonstiges");
        kategorie.setPromptText("Kategorie wählen");
        kategorie.setMaxWidth(Double.MAX_VALUE);
        kategorie.setStyle(FIELD_STYLE);
        fixComboBox(kategorie);

        // Status ComboBox (für Bearbeiten)
        ComboBox<TicketStatus> statusBox = new ComboBox<>();
        statusBox.getItems().addAll(TicketStatus.values());
        statusBox.setValue(TicketStatus.OFFEN);
        statusBox.setMaxWidth(Double.MAX_VALUE);
        statusBox.setStyle(FIELD_STYLE);
        statusBox.setVisible(false); // nur beim Bearbeiten sichtbar

        TextField suchFeld = new TextField();
        suchFeld.setPromptText("Ticket suchen...");
        suchFeld.setStyle(FIELD_STYLE);

        Label meldung = new Label();
        meldung.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        Label zaehler = new Label("📊 Tickets gesamt: 0");
        zaehler.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12;");

        // ===== ANHANG =====
        Label anhangLabel = new Label("Keine Datei ausgewählt");
        anhangLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11;");

        ListView<String> anhangAnzeige = new ListView<>(manager.getAnhangListe());
        anhangAnzeige.setPrefHeight(70);
        anhangAnzeige.setStyle(
            "-fx-background-color: #2b2b2b;" +
            "-fx-border-color: #555555;" +
            "-fx-border-radius: 8;"
        );
        anhangAnzeige.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item);
                    setStyle(
                        "-fx-background-color: #2b2b2b;" +
                        "-fx-text-fill: #90EE90;" +
                        "-fx-padding: 5;" +
                        "-fx-font-size: 11;"
                    );
                }
            }
        });

        Button anhangBtn = new Button("📎 Datei anhängen");
        styleButton(anhangBtn, "#607D8B");
        anhangBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Datei auswählen");
            fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Alle Dateien", "*.*"),
                new FileChooser.ExtensionFilter("Bilder", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
            );
            File file = fc.showOpenDialog(stage);
            if (file != null) {
                manager.anhangHinzufuegen(file.getName());
                anhangLabel.setText("✅ " + file.getName());
                anhangLabel.setStyle("-fx-text-fill: lightgreen; -fx-font-size: 11;");
            }
        });

        // ===== TICKET LISTE =====
        ListView<Ticket> ticketAnzeige = new ListView<>(manager.getTicketListe());
        ticketAnzeige.setPrefHeight(150);
        ticketAnzeige.setStyle(
            "-fx-background-color: #2b2b2b;" +
            "-fx-border-color: #555555;" +
            "-fx-border-radius: 8;"
        );
        ticketAnzeige.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Ticket item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item.toDisplayString());
                    setStyle(
                        "-fx-background-color: #3a3a3a;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 8;" +
                        "-fx-border-color: #555555;" +
                        "-fx-border-width: 0 0 1 0;"
                    );
                }
            }
        });

        // ===== HISTORY LISTE =====
        ListView<String> historyAnzeige = new ListView<>(manager.getHistoryListe());
        historyAnzeige.setPrefHeight(100);
        historyAnzeige.setStyle(
            "-fx-background-color: #252525;" +
            "-fx-border-color: #555555;" +
            "-fx-border-radius: 8;"
        );
        historyAnzeige.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item);
                    setStyle(
                        "-fx-background-color: #252525;" +
                        "-fx-text-fill: #aaaaaa;" +
                        "-fx-padding: 6;" +
                        "-fx-font-size: 11;"
                    );
                }
            }
        });

        // ===== BUTTONS =====
        Button speichernBtn    = new Button("💾 Speichern");
        Button bearbeitenBtn   = new Button("✏️ Bearbeiten");
        Button loeschenBtn     = new Button("🗑️ Löschen");
        Button alleLoeschenBtn = new Button("🧹 Alle löschen");
        Button suchenBtn       = new Button("🔍 Suchen");
        Button statistikBtn    = new Button("📊 Statistik");

        styleButton(speichernBtn,    "#4CAF50");
        styleButton(bearbeitenBtn,   "#2196F3");
        styleButton(loeschenBtn,     "#f44336");
        styleButton(alleLoeschenBtn, "#FF5722");
        styleButton(suchenBtn,       "#9C27B0");
        styleButton(statistikBtn,    "#FF9800");

        // Statistik-Button nur für Admins sichtbar
        boolean istAdmin = UserSessionController.getInstance().getCurrentRole() == UserRole.ADMIN;
        statistikBtn.setVisible(istAdmin);
        statistikBtn.setManaged(istAdmin);

        // ===== SPEICHERN =====
        // Hilfsvariable für Bearbeiten-Modus
        final Ticket[] zuBearbeitendes = {null};

        speichernBtn.setOnAction(e -> {
            if (titelFeld.getText().isEmpty()) {
                FehlerFenster.zeige("❌ Titel darf nicht leer sein!");
            } else if (beschreibungFeld.getText().isEmpty()) {
                FehlerFenster.zeige("❌ Beschreibung darf nicht leer sein!");
            } else if (kategorie.getValue() == null) {
                FehlerFenster.zeige("❌ Bitte Kategorie wählen!");
            } else {
                if (zuBearbeitendes[0] != null) {
                    // Bearbeiten-Modus: Felder aktualisieren
                    zuBearbeitendes[0].setTitel(titelFeld.getText());
                    zuBearbeitendes[0].setKategorie(kategorie.getValue());
                    zuBearbeitendes[0].setStatus(statusBox.getValue());
                    manager.historyEintrag("✏️ Ticket bearbeitet: " + zuBearbeitendes[0].getTitel());
                    ticketAnzeige.refresh();
                    zuBearbeitendes[0] = null;
                    statusBox.setVisible(false);
                } else {
                    // Neu erstellen
                    manager.ticketHinzufuegen(titelFeld.getText(), kategorie.getValue());
                }
                titelFeld.clear();
                beschreibungFeld.clear();
                kategorie.setValue(null);
                statusBox.setValue(TicketStatus.OFFEN);
                anhangLabel.setText("Keine Datei ausgewählt");
                anhangLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11;");
                meldung.setText("✅ Ticket gespeichert!");
                meldung.setTextFill(Color.LIGHTGREEN);
                zaehler.setText("📊 Tickets gesamt: " + manager.getTicketListe().size());
            }
        });

        // ===== BEARBEITEN =====
        bearbeitenBtn.setOnAction(e -> {
            Ticket ausgewaehlt = ticketAnzeige.getSelectionModel().getSelectedItem();
            if (ausgewaehlt == null) {
                FehlerFenster.zeige("❌ Bitte erst ein Ticket auswählen!");
            } else {
                zuBearbeitendes[0] = ausgewaehlt;
                titelFeld.setText(ausgewaehlt.getTitel());
                kategorie.setValue(ausgewaehlt.getKategorie());
                statusBox.setValue(ausgewaehlt.getStatus());
                statusBox.setVisible(true);
                meldung.setText("✏️ Bearbeiten — danach Speichern!");
                meldung.setTextFill(Color.LIGHTBLUE);
                zaehler.setText("📊 Tickets gesamt: " + manager.getTicketListe().size());
            }
        });

        // ===== LÖSCHEN =====
        loeschenBtn.setOnAction(e -> {
            Ticket ausgewaehlt = ticketAnzeige.getSelectionModel().getSelectedItem();
            if (ausgewaehlt == null) {
                FehlerFenster.zeige("❌ Bitte erst ein Ticket auswählen!");
            } else {
                manager.ticketLoeschen(ausgewaehlt);
                meldung.setText("🗑️ Ticket gelöscht!");
                meldung.setTextFill(Color.SALMON);
                zaehler.setText("📊 Tickets gesamt: " + manager.getTicketListe().size());
            }
        });

        // ===== ALLE LÖSCHEN =====
        alleLoeschenBtn.setOnAction(e -> {
            manager.alleLoeschen();
            meldung.setText("🧹 Alle Tickets gelöscht!");
            meldung.setTextFill(Color.SALMON);
            zaehler.setText("📊 Tickets gesamt: 0");
        });

        // ===== SUCHEN =====
        suchenBtn.setOnAction(e -> {
            String suche = suchFeld.getText().toLowerCase();
            if (suche.isEmpty()) {
                ticketAnzeige.setItems(manager.getTicketListe());
                meldung.setText("🔍 Alle Tickets angezeigt!");
                meldung.setTextFill(Color.WHITE);
            } else {
                ObservableList<Ticket> gefunden = FXCollections.observableArrayList();
                for (Ticket t : manager.getTicketListe()) {
                    if (t.getTitel().toLowerCase().contains(suche) ||
                        t.getKategorie().toLowerCase().contains(suche)) {
                        gefunden.add(t);
                    }
                }
                ticketAnzeige.setItems(gefunden);
                meldung.setText("🔍 " + gefunden.size() + " Ticket(s) gefunden!");
                meldung.setTextFill(Color.LIGHTBLUE);
            }
        });

        // ===== STATISTIK =====
        statistikBtn.setOnAction(e -> new StatistikView(manager).zeige(stage));

        // ===== LAYOUT =====
        Label appTitel = new Label("🎫 Ticket System");
        appTitel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        appTitel.setTextFill(Color.WHITE);

        HBox buttons1 = new HBox(8);
        buttons1.getChildren().addAll(speichernBtn, bearbeitenBtn, loeschenBtn, alleLoeschenBtn, statistikBtn);

        HBox suchZeile = new HBox(10);
        suchZeile.getChildren().addAll(suchFeld, suchenBtn);
        HBox.setHgrow(suchFeld, Priority.ALWAYS);

        HBox anhangZeile = new HBox(10);
        anhangZeile.setAlignment(Pos.CENTER_LEFT);
        anhangZeile.getChildren().addAll(anhangBtn, anhangLabel);

        VBox layout = new VBox(12);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: " + DARK_BG + ";");
        layout.getChildren().addAll(
            appTitel,
            new Separator(),
            styledLabel("Titel:"),        titelFeld,
            styledLabel("Beschreibung:"), beschreibungFeld,
            styledLabel("Kategorie:"),    kategorie,
            styledLabel("Status:"),       statusBox,
            styledLabel("📎 Anhänge:"),   anhangZeile, anhangAnzeige,
            buttons1,
            new Separator(),
            styledLabel("🔍 Suche:"),     suchZeile,
            styledLabel("📋 Alle Tickets:"), ticketAnzeige,
            zaehler,
            new Separator(),
            styledLabel("📜 History:"),   historyAnzeige,
            meldung
        );

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #1e1e1e; -fx-background: #1e1e1e;");

        Scene scene = new Scene(scrollPane, 500, 750);
        stage.setTitle("🎫 Ticket System");
        stage.setScene(scene);
        stage.show();
    }

    // ===== HILFSMETHODEN =====
    private void fixComboBox(ComboBox<String> box) {
        box.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-background-color: #2b2b2b; -fx-padding: 6;");
                }
            }
        });
        box.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(box.getPromptText());
                    setStyle("-fx-text-fill: #888888; -fx-background-color: #2b2b2b;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-background-color: #2b2b2b;");
                }
            }
        });
    }

    private void styleButton(Button btn, String farbe) {
        btn.setStyle(
            "-fx-background-color: " + farbe + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 14;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: derive(" + farbe + ", -20%);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 14;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + farbe + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 14;"
        ));
    }

    private Label styledLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.LIGHTGRAY);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        return label;
    }
}