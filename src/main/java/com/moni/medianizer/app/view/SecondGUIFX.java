package com.moni.medianizer.app.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.controller.InsertButtonListenerFX;
import com.moni.medianizer.app.controller.MediaListenerFX;
import com.moni.medianizer.app.model.CD;
import com.moni.medianizer.app.model.DatabaseManager;
import com.moni.medianizer.app.model.Film;
import com.moni.medianizer.app.model.Media;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Benutzerschnittstelle zwei
 */
public class SecondGUIFX {

    private final Media media;
    private final Runnable callBack;
    private final SelectPanelFX sPanel = new SelectPanelFX();
    private final TextField tfAmount = new TextField();
    private final Button btnInsert = new Button(Constants.S_SAVE);
    
    public SecondGUIFX(Media media, Runnable callBack) {
    	this.media = media;
    	this.callBack = callBack;
    	createGUI();
    }
    
    public SecondGUIFX(Media media) {
        this(media, null);
    }

    private void createGUI() {
        
        Stage stage = new Stage();
        stage.setTitle(Constants.S_APP_NAME);

        sPanel.setType(media.getType());
        sPanel.getComboBox().setDisable(true);
        sPanel.setTitle(media.getTitle());
        
        //Interpret bei CD berücksichtigen
        if (media instanceof CD) {
        	
        	if (((CD) media).getInterpret() != null) {
        		sPanel.setInterpret(((CD) media).getInterpret());
        	}
        }
        
        //Listener für Medienwahl
        MediaListenerFX mediaListener = new MediaListenerFX(sPanel, selectedType -> {});
        sPanel.getComboBox().valueProperty().addListener(mediaListener);

        String currentType = sPanel.getType();
        if (currentType != null) {
            mediaListener.changed(null, null, currentType);
        }
        
        //Textfeld Anzahl initialisieren
        Label lblAmount = new Label(Constants.S_AMOUNT);
        tfAmount.setPromptText("z. B. 3");
        if (media.getAmount() != 0) {
        	tfAmount.setText(Integer.toString(media.getAmount()));
        }
        tfAmount.setPrefWidth(80);

        tfAmount.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                tfAmount.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
        
        //Speichern-Button - Validierungen
        btnInsert.setOnAction(e -> {
            try {
                String title = sPanel.getTitle();
                String interpret = sPanel.getInterpret();
                String sAmount = tfAmount.getText();

                List<String> errors = new ArrayList<>();

                //Eingabevalidierung
                if (title == null || title.trim().isEmpty()) {
                    errors.add("Titel darf nicht leer sein.");
                }

                if (sAmount == null || sAmount.trim().isEmpty()) {
                    errors.add("Bitte gib eine Anzahl ein.");
                } else {
                    try {
                        int testAmount = Integer.parseInt(sAmount);
                        if (testAmount < 0) {
                            errors.add("Die Anzahl darf nicht negativ sein.");
                        }
                    } catch (NumberFormatException ex) {
                        errors.add("Bitte gib eine gültige Zahl für die Anzahl ein.");
                    }
                }

                if (media instanceof CD && (interpret == null || interpret.trim().isEmpty())) {
                    errors.add("Bitte gib einen Interpreten ein.");
                }

                if (!errors.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Eingabefehler");
                    alert.setHeaderText("Bitte überprüfe deine Eingaben:");
                    String formattedErrors = errors.stream()
                            .map(err -> "• " + err)
                            .collect(Collectors.joining("\n"));
                    alert.setContentText(formattedErrors);
                    alert.getDialogPane().setStyle("-fx-font-size: 13px;");
                    alert.showAndWait();
                    return;
                }

                //Existenzprüfung in der Datenbank (case-insensitive)
                boolean exists = false;
                if (media instanceof Film) {
                    ArrayList<Film> result = DatabaseManager.getInstance().selectFilm(title);
                    // Case-insensitive prüfen
                    exists = result.stream()
                            .anyMatch(f -> f.getTitle().equalsIgnoreCase(title) 
                            		&& f.getAmount() == Integer.parseInt(sAmount));
                } else if (media instanceof CD) {
                    ArrayList<CD> result = DatabaseManager.getInstance().selectCD(title, interpret);
                    exists = result.stream()
                            .anyMatch(cd ->
                                    cd.getTitle().equalsIgnoreCase(title)
                                    && cd.getInterpret().equalsIgnoreCase(interpret) 
                                    && cd.getAmount() == Integer.parseInt(sAmount));
                }

                if (exists) {
                    new Alert(Alert.AlertType.ERROR,
                            "Ein Eintrag mit diesem Titel" +
                                    (media instanceof CD ? " und Interpret" : "") +
                                    " existiert bereits.").showAndWait();
                    return;
                }

                //Werte übernehmen
                media.setTitle(title);
                media.setAmount(Integer.parseInt(sAmount));
                if (media instanceof CD cd) {
                    cd.setInterpret(interpret);
                }

                //Speichern
                InsertButtonListenerFX insertListener =
                        new InsertButtonListenerFX(sPanel, media);
                insertListener.handleInsert();

                if (callBack != null) {
                    callBack.run();
                }

                stage.close();

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Ein unerwarteter Fehler ist aufgetreten: " + ex.getMessage()).showAndWait();
                ex.printStackTrace();
            }
        });

        HBox layout = new HBox(15, sPanel, lblAmount, tfAmount, btnInsert);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();
    }
}