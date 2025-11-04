package com.moni.medianizer.app.controller;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.model.DatabaseManager;
import com.moni.medianizer.app.model.Media;
import com.moni.medianizer.app.view.InputProvider;

import javafx.scene.control.Alert;

public class InsertButtonListenerFX {

    private final InputProvider input;
    private final Media media;

    public InsertButtonListenerFX(InputProvider input, Media media) {
        this.input = input;
        this.media = media;
    }

    public void handleInsert() {

        if (input.getType() == null || input.getTitle() == null || input.getTitle().isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Bitte gib mindestens den Titel ein.").showAndWait();
            return;
        }

        if (Constants.S_FILM.equals(input.getType())) {
            handleFilm();
        } else if (Constants.S_CD.equals(input.getType())) {
            handleCD();
        } else {
            new Alert(Alert.AlertType.WARNING,
                    "Unbekannter Medientyp: " + input.getType()).showAndWait();
        }
    }

    private void handleFilm() {
        if (media.getAmount() < 0) {
            new Alert(Alert.AlertType.WARNING,
                    "Bitte eine positive Anzahl eingeben.").showAndWait();
            return;
        }
        if (media.getID() == 0) {
        	DatabaseManager.getInstance().insert(media);
	        new Alert(Alert.AlertType.INFORMATION,
	                "Film \"" + input.getTitle() + "\" erfolgreich eingefügt.").showAndWait();
        } else {
        	
        	DatabaseManager.getInstance().update(media);
        	new Alert(Alert.AlertType.INFORMATION,
	                "Film \"" + input.getTitle() + "\" erfolgreich geändert.").showAndWait();
        }
    }

    private void handleCD() {
        if (media.getAmount() < 0) {
            new Alert(Alert.AlertType.WARNING,
                    "Bitte eine positive Anzahl eingeben.").showAndWait();
            return;
        }
        
        if (media.getID() != 0) {
	        DatabaseManager.getInstance().insert(media);
	        new Alert(Alert.AlertType.INFORMATION,
	                "CD \"" + input.getTitle() + "\" von \"" + input.getInterpret() + "\" erfolgreich eingefügt.").showAndWait();
        } else {
        	DatabaseManager.getInstance().update(media);
        	
	        new Alert(Alert.AlertType.INFORMATION,
	                "CD \"" + input.getTitle() + "\" von \"" + input.getInterpret() + "\" erfolgreich eingefügt.").showAndWait();
        }
    }
}