package com.moni.medianizer.app.controller;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.model.DatabaseManager;
import com.moni.medianizer.app.model.Media;
import com.moni.medianizer.app.view.InputProvider;

import javafx.scene.control.Alert;

/**
 * Listener für den Speichern-Button der SecondGUI
 */
public class InsertButtonListenerFX {

    private final InputProvider input;
    private final Media media;

    public InsertButtonListenerFX(InputProvider input, Media media) {
        this.input = input;
        this.media = media;
    }
    
    /**
     * Unterscheidung Medien
     * Aufrufe spezifischer Methoden
     */
    public void handleInsert() {
    	//Medium muss gewählt und Titel eingegeben sein
        if (input.getType() == null || input.getTitle() == null || input.getTitle().isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Bitte gib mindestens den Titel ein und wähle ein Medium.").showAndWait();
            return;
        }
        
        //Unterscheidung Film/CD
        if (Constants.S_FILM.equals(input.getType())) {
            handleFilm();
        } else if (Constants.S_CD.equals(input.getType())) {
            handleCD();
        } else {
            new Alert(Alert.AlertType.WARNING,
                    "Unbekannter Medientyp: " + input.getType()).showAndWait();
        }
    }
    
    /**
     * Film einfügen/ändern
     */
    private void handleFilm() {
    	//Anzahl darf nicht leer sein
        if (media.getAmount() < 0) {
            new Alert(Alert.AlertType.WARNING,
                    "Bitte eine positive Anzahl eingeben.").showAndWait();
            return;
        }
        
        //Unterschied Insert/Update
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
    
    /**
     * CD einfügen/ändern
     */
    private void handleCD() {
    	//Anzahl darf nicht leer sein
        if (media.getAmount() < 0) {
            new Alert(Alert.AlertType.WARNING,
                    "Bitte eine positive Anzahl eingeben.").showAndWait();
            return;
        }
        
        //Unterschied Insert/Update
        if (media.getID() == 0) {
	        DatabaseManager.getInstance().insert(media);
	        new Alert(Alert.AlertType.INFORMATION,
	                "CD \"" + input.getTitle() + "\" von \"" + input.getInterpret() + "\" erfolgreich eingefügt.").showAndWait();
        } else {
        	DatabaseManager.getInstance().update(media);
        	
	        new Alert(Alert.AlertType.INFORMATION,
	                "CD \"" + input.getTitle() + "\" von \"" + input.getInterpret() + "\" erfolgreich geändert.").showAndWait();
        }
    }
}