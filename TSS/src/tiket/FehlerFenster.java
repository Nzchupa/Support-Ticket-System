package tiket;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FehlerFenster {

    public static void zeige(String text) {

        Stage errorStage = new Stage();
        errorStage.initStyle(StageStyle.TRANSPARENT);
        errorStage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox();
        root.setPrefSize(520, 300);
        root.setMaxSize(520, 300);
        root.setMinSize(520, 300);
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #263142 0%, #1a1d22 22%, #191b1f 100%);" +
            "-fx-background-radius: 26;" +
            "-fx-border-color: #3f4856;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 26;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setRadius(22);
        shadow.setSpread(0.08);
        shadow.setColor(Color.rgb(0, 0, 0, 0.45));
        root.setEffect(shadow);

        // ===== HEADER =====
        Label topTitle = new Label("Fehler");
        topTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));
        topTitle.setTextFill(Color.web("#EAEAEA"));

        Button closeBtn = new Button("✕");
        closeBtn.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));
        closeBtn.setTextFill(Color.web("#CFCFCF"));
        closeBtn.setCursor(Cursor.HAND);
        closeBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 4 10 4 10;"
        );
        closeBtn.setOnAction(e -> errorStage.close());
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-border-color: transparent;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 4 10 4 10;" +
            "-fx-text-fill: white;"
        ));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 4 10 4 10;" +
            "-fx-text-fill: #CFCFCF;"
        ));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(new HBox(topTitle), spacer, closeBtn);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18, 18, 18, 24));
        header.setStyle(
            "-fx-background-color: linear-gradient(to right, #313846 0%, #263142 45%, #2b313a 100%);" +
            "-fx-background-radius: 26 26 0 0;"
        );

        // ===== BODY =====
        Label bodyTitle = new Label("Fehler");
        bodyTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 34));
        bodyTitle.setTextFill(Color.web("#F1F1F1"));

        Label nachricht = new Label(text);
        nachricht.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 22));
        nachricht.setTextFill(Color.web("#D6D6D6"));
        nachricht.setWrapText(true);
        nachricht.setAlignment(Pos.CENTER);
        nachricht.setMaxWidth(390);

        Button okBtn = new Button("OK");
        okBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        okBtn.setTextFill(Color.WHITE);
        okBtn.setCursor(Cursor.HAND);
        okBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ff4c42 0%, #ef3b32 100%);" +
            "-fx-background-radius: 18;" +
            "-fx-border-radius: 18;" +
            "-fx-padding: 14 42 14 42;"
        );
        okBtn.setOnAction(e -> errorStage.close());
        okBtn.setOnMouseEntered(e -> okBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ff5a50 0%, #f5483f 100%);" +
            "-fx-background-radius: 18;" +
            "-fx-border-radius: 18;" +
            "-fx-padding: 14 42 14 42;" +
            "-fx-text-fill: white;"
        ));
        okBtn.setOnMouseExited(e -> okBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ff4c42 0%, #ef3b32 100%);" +
            "-fx-background-radius: 18;" +
            "-fx-border-radius: 18;" +
            "-fx-padding: 14 42 14 42;" +
            "-fx-text-fill: white;"
        ));

        VBox body = new VBox(24, new HBox(16, bodyTitle), nachricht, okBtn);
        body.setAlignment(Pos.CENTER);
        body.setPadding(new Insets(34, 30, 34, 30));
        body.setStyle(
            "-fx-background-color: #191b1f;" +
            "-fx-background-radius: 0 0 26 26;"
        );

        root.getChildren().addAll(header, body);

        // ===== DRAG =====
        final double[] offset = {0, 0};
        header.setOnMousePressed((MouseEvent ev) -> {
            offset[0] = ev.getSceneX();
            offset[1] = ev.getSceneY();
        });
        header.setOnMouseDragged((MouseEvent ev) -> {
            errorStage.setX(ev.getScreenX() - offset[0]);
            errorStage.setY(ev.getScreenY() - offset[1]);
        });

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        errorStage.setScene(scene);
        errorStage.showAndWait();
    }
}