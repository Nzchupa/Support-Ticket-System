package tiket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class StatistikView {

    private static final String DARK_BG  = "#1e1e1e";
    private static final String CARD_BG  = "#2b2b2b";

    private final TicketManeger manager;

    public StatistikView(TicketManeger manager) {
        this.manager = manager;
    }

    public void zeige(Stage ownerStage) {

        Stage stage = new Stage();
        stage.initOwner(ownerStage);
        stage.initModality(Modality.NONE);
        stage.setTitle("📊 Admin – Ticket Statistik");

        ObservableList<Ticket> tickets = manager.getTicketListe();

        // ===== TITEL =====
        Label titel = new Label("📊 Ticket Statistik");
        titel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titel.setTextFill(Color.WHITE);

        // ===== DASHBOARD CARDS =====
        long gesamt = tickets.size();
        long heute = tickets.stream()
            .filter(t -> t.getErstelltAm().toLocalDate().equals(LocalDate.now()))
            .count();
        long dieseWoche = tickets.stream()
            .filter(t -> t.getErstelltAm().toLocalDate()
                .isAfter(LocalDate.now().minusDays(7)))
            .count();

        HBox cards = new HBox(15);
        cards.getChildren().addAll(
            erstelleKarte("🎫 Gesamt",       String.valueOf(gesamt),      "#4CAF50"),
            erstelleKarte("📅 Heute",         String.valueOf(heute),       "#2196F3"),
            erstelleKarte("📆 Diese Woche",   String.valueOf(dieseWoche),  "#9C27B0")
        );

        // ===== PIE CHARTS nebeneinander =====
        HBox pieCharts = new HBox(20);
        pieCharts.getChildren().addAll(
            erstelleKategorieChart(tickets),
            erstelleStatusChart(tickets)
        );

        // ===== BALKEN CHART =====
        BarChart<String, Number> barChart = erstelleBarChart(tickets);

        // ===== UNTERE SEKTION: Top-Kategorien + History nebeneinander =====
        HBox unterSektion = new HBox(20);
        unterSektion.getChildren().addAll(
            erstelleTopKategorien(tickets),
            erstelleHistory()
        );

        // ===== CLOSE BUTTON =====
        Button closeBtn = new Button("✕  Schließen");
        styleButton(closeBtn, "#f44336");
        closeBtn.setOnAction(e -> stage.close());

        // ===== HAUPT-LAYOUT =====
        VBox layout = new VBox(16);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: " + DARK_BG + ";");
        layout.getChildren().addAll(
            titel,
            new Separator(),
            cards,
            new Separator(),
            pieCharts,
            new Separator(),
            barChart,
            new Separator(),
            unterSektion,
            closeBtn
        );

        ScrollPane scroll = new ScrollPane(layout);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + DARK_BG + "; -fx-background-color: " + DARK_BG + ";");

        Scene scene = new Scene(scroll, 760, 720);
        stage.setScene(scene);
        stage.show();
    }

    // ── Dashboard-Karte ───────────────────────────────────────────────────────

    private VBox erstelleKarte(String bezeichnung, String wert, String farbe) {
        Label wertLabel = new Label(wert);
        wertLabel.setFont(Font.font("Arial", FontWeight.BOLD, 34));
        wertLabel.setTextFill(Color.web(farbe));

        Label nameLabel = new Label(bezeichnung);
        nameLabel.setFont(Font.font("Arial", 13));
        nameLabel.setTextFill(Color.LIGHTGRAY);

        VBox karte = new VBox(6, wertLabel, nameLabel);
        karte.setAlignment(Pos.CENTER);
        karte.setPadding(new Insets(22));
        karte.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + farbe + ";" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 1.5;"
        );
        HBox.setHgrow(karte, Priority.ALWAYS);
        return karte;
    }

    // ── Kategorie Pie-Chart ───────────────────────────────────────────────────

    private PieChart erstelleKategorieChart(ObservableList<Ticket> tickets) {
        Map<String, Long> katMap = new HashMap<>();
        for (Ticket t : tickets) {
            String kat = t.getKategorie() != null ? t.getKategorie() : "Unbekannt";
            katMap.merge(kat, 1L, Long::sum);
        }

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        if (katMap.isEmpty()) {
            data.add(new PieChart.Data("Keine Daten", 1));
        } else {
            katMap.forEach((k, v) -> data.add(new PieChart.Data(k + " (" + v + ")", v)));
        }

        PieChart chart = new PieChart(data);
        chart.setTitle("Kategorienverteilung");
        chart.setPrefSize(340, 290);
        chart.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 12;");
        HBox.setHgrow(chart, Priority.ALWAYS);
        return chart;
    }

    // ── Status Pie-Chart ──────────────────────────────────────────────────────

    private PieChart erstelleStatusChart(ObservableList<Ticket> tickets) {
        long offen       = tickets.stream().filter(t -> t.getStatus() == TicketStatus.OFFEN).count();
        long bearbeitung = tickets.stream().filter(t -> t.getStatus() == TicketStatus.IN_BEARBEITUNG).count();
        long geloest     = tickets.stream().filter(t -> t.getStatus() == TicketStatus.GELOEST).count();

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        if (tickets.isEmpty()) {
            data.add(new PieChart.Data("Keine Daten", 1));
        } else {
            if (offen       > 0) data.add(new PieChart.Data("🔴 Offen ("       + offen       + ")", offen));
            if (bearbeitung > 0) data.add(new PieChart.Data("🟡 In Bearb. ("   + bearbeitung + ")", bearbeitung));
            if (geloest     > 0) data.add(new PieChart.Data("🟢 Gelöst ("      + geloest     + ")", geloest));
            if (data.isEmpty())  data.add(new PieChart.Data("Keine Daten", 1));
        }

        PieChart chart = new PieChart(data);
        chart.setTitle("Statusverteilung");
        chart.setPrefSize(340, 290);
        chart.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 12;");
        HBox.setHgrow(chart, Priority.ALWAYS);
        return chart;
    }

    // ── Balken-Chart: Tickets letzte 7 Tage ──────────────────────────────────

    private BarChart<String, Number> erstelleBarChart(ObservableList<Ticket> tickets) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis   yAxis = new NumberAxis();
        xAxis.setLabel("Datum");
        yAxis.setLabel("Anzahl Tickets");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Tickets der letzten 7 Tage");
        chart.setPrefHeight(240);
        chart.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 12;");
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tickets");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM");
        for (int i = 6; i >= 0; i--) {
            LocalDate tag    = LocalDate.now().minusDays(i);
            long      anzahl = tickets.stream()
                .filter(t -> t.getErstelltAm().toLocalDate().equals(tag))
                .count();
            series.getData().add(new XYChart.Data<>(tag.format(fmt), anzahl));
        }

        chart.getData().add(series);
        return chart;
    }

    // ── Top Kategorien ────────────────────────────────────────────────────────

    private VBox erstelleTopKategorien(ObservableList<Ticket> tickets) {
        Label header = new Label("🏆 Top Kategorien");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        header.setTextFill(Color.LIGHTGRAY);

        Map<String, Long> katMap = new HashMap<>();
        for (Ticket t : tickets) {
            String kat = t.getKategorie() != null ? t.getKategorie() : "Unbekannt";
            katMap.merge(kat, 1L, Long::sum);
        }

        VBox box = new VBox(10, header);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 12;");
        HBox.setHgrow(box, Priority.ALWAYS);

        if (katMap.isEmpty()) {
            Label leer = new Label("Noch keine Tickets vorhanden");
            leer.setTextFill(Color.GRAY);
            box.getChildren().add(leer);
        } else {
            katMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .forEach(entry -> {
                    // Fortschrittsbalken
                    long max = katMap.values().stream().mapToLong(Long::longValue).max().orElse(1);
                    double prozent = (double) entry.getValue() / max;

                    Label nameLabel = new Label(entry.getKey());
                    nameLabel.setTextFill(Color.WHITE);
                    nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                    Label anzahlLabel = new Label(entry.getValue() + " Ticket(s)");
                    anzahlLabel.setTextFill(Color.LIGHTGRAY);
                    anzahlLabel.setFont(Font.font("Arial", 11));

                    HBox kopf = new HBox(nameLabel);
                    HBox.setHgrow(kopf, Priority.ALWAYS);
                    kopf.getChildren().add(new Region());
                    ((Region) kopf.getChildren().get(1)).setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(kopf.getChildren().get(1), Priority.ALWAYS);
                    kopf.getChildren().add(anzahlLabel);

                    ProgressBar bar = new ProgressBar(prozent);
                    bar.setMaxWidth(Double.MAX_VALUE);
                    bar.setPrefHeight(8);
                    bar.setStyle("-fx-accent: #4CAF50;");

                    VBox eintrag = new VBox(4, kopf, bar);
                    box.getChildren().add(eintrag);
                });
        }

        return box;
    }

    // ── Letzte 5 History-Einträge ─────────────────────────────────────────────

    private VBox erstelleHistory() {
        Label header = new Label("📜 Letzte 5 Aktivitäten");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        header.setTextFill(Color.LIGHTGRAY);

        VBox box = new VBox(10, header);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 12;");
        HBox.setHgrow(box, Priority.ALWAYS);

        ObservableList<String> history = manager.getHistoryListe();

        if (history.isEmpty()) {
            Label leer = new Label("Keine Aktivitäten vorhanden");
            leer.setTextFill(Color.GRAY);
            box.getChildren().add(leer);
        } else {
            int anzahl = Math.min(5, history.size());
            for (int i = 0; i < anzahl; i++) {
                Label eintrag = new Label("• " + history.get(i));
                eintrag.setTextFill(Color.LIGHTGRAY);
                eintrag.setFont(Font.font("Arial", 12));
                eintrag.setWrapText(true);
                box.getChildren().add(eintrag);
            }
        }

        return box;
    }

    // ── Button-Style ──────────────────────────────────────────────────────────

    private void styleButton(Button btn, String farbe) {
        btn.setStyle(
            "-fx-background-color: " + farbe + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-padding: 8 20;"
        );
    }
}