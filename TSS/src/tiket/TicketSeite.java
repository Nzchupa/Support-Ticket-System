package tiket;


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
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;

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

	    private TicketManeger manager = new TicketManeger();

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
	        ListView<String> ticketAnzeige = new ListView<>(manager.getTicketListe());
	        ticketAnzeige.setPrefHeight(150);
	        ticketAnzeige.setStyle(
	            "-fx-background-color: #2b2b2b;" +
	            "-fx-border-color: #555555;" +
	            "-fx-border-radius: 8;"
	        );
	        ticketAnzeige.setCellFactory(lv -> new ListCell<>() {
	            @Override
	            protected void updateItem(String item, boolean empty) {
	                super.updateItem(item, empty);
	                if (empty || item == null) {
	                    setText(null);
	                    setStyle("-fx-background-color: transparent;");
	                } else {
	                    setText(item);
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
	        Button zurueckBtn = new Button("⬅ Zurück zur Home Page");
	        
	        styleButton(speichernBtn,    "#4CAF50");
	        styleButton(bearbeitenBtn,   "#2196F3");
	        styleButton(loeschenBtn,     "#f44336");
	        styleButton(alleLoeschenBtn, "#FF5722");
	        styleButton(suchenBtn,       "#9C27B0");
	        styleButton(zurueckBtn, "#2563eb");

	        // ===== SPEICHERN =====
	        speichernBtn.setOnAction(e -> {
	            if (titelFeld.getText().isEmpty()) {
	                FehlerFenster.zeige("❌ Titel darf nicht leer sein!");
	            } else if (beschreibungFeld.getText().isEmpty()) {
	                FehlerFenster.zeige("❌ Beschreibung darf nicht leer sein!");
	            } else if (kategorie.getValue() == null) {
	                FehlerFenster.zeige("❌ Bitte Kategorie wählen!");
	            } else {
	                manager.ticketHinzufuegen(titelFeld.getText(), kategorie.getValue());
	                titelFeld.clear();
	                beschreibungFeld.clear();
	                kategorie.setValue(null);
	                anhangLabel.setText("Keine Datei ausgewählt");
	                anhangLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11;");
	                meldung.setText("✅ Ticket gespeichert!");
	                meldung.setTextFill(Color.LIGHTGREEN);
	                zaehler.setText("📊 Tickets gesamt: " + manager.getTicketListe().size());
	            }
	        });

	        // ===== BEARBEITEN =====
	        bearbeitenBtn.setOnAction(e -> {
	            String ausgewaehlt = ticketAnzeige.getSelectionModel().getSelectedItem();
	            if (ausgewaehlt == null) {
	                FehlerFenster.zeige("❌ Bitte erst ein Ticket auswählen!");
	            } else {
	                manager.ticketLoeschen(ausgewaehlt);
	                String[] teile = ausgewaehlt.replace("🎫 ", "").split(" \\| ");
	                titelFeld.setText(teile[0]);
	                if (teile.length > 1) kategorie.setValue(teile[1]);
	                manager.historyEintrag("✏️ Ticket bearbeitet: " + teile[0]);
	                meldung.setText("✏️ Bearbeiten — danach Speichern!");
	                meldung.setTextFill(Color.LIGHTBLUE);
	                zaehler.setText("📊 Tickets gesamt: " + manager.getTicketListe().size());
	            }
	        });
	        zurueckBtn.setOnAction(e -> openHome(stage));

	        // ===== LÖSCHEN =====
	        loeschenBtn.setOnAction(e -> {
	            String ausgewaehlt = ticketAnzeige.getSelectionModel().getSelectedItem();
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
	                ObservableList<String> gefunden = FXCollections.observableArrayList();
	                for (String t : manager.getTicketListe()) {
	                    if (t.toLowerCase().contains(suche)) gefunden.add(t);
	                }
	                ticketAnzeige.setItems(gefunden);
	                meldung.setText("🔍 " + gefunden.size() + " Ticket(s) gefunden!");
	                meldung.setTextFill(Color.LIGHTBLUE);
	            }
	        });

	        // ===== LAYOUT =====
	        Label appTitel = new Label("🎫 Ticket System");
	        appTitel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
	        appTitel.setTextFill(Color.WHITE);

	        HBox buttons1 = new HBox(8);
	        buttons1.getChildren().addAll(speichernBtn, bearbeitenBtn, loeschenBtn, alleLoeschenBtn);

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
	            zurueckBtn,
	            new Separator(),
	            styledLabel("Titel:"),        titelFeld,
	            styledLabel("Beschreibung:"), beschreibungFeld,
	            styledLabel("Kategorie:"),    kategorie,
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
	    private void openHome(Stage stage) {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeView.fxml"));
	            Scene scene = new Scene(loader.load());

	            stage.setTitle("TSSupport System");
	            stage.setScene(scene);
	            stage.show();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
