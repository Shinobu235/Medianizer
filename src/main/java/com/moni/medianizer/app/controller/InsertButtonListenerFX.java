package com.moni.medianizer.app.controller;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.model.DatabaseManager;
import com.moni.medianizer.app.model.Media;
import com.moni.medianizer.app.view.InputProvider;
import com.moni.medianizer.app.util.AlertHelper;

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
        	AlertHelper.showWarning("Bitte gib mindestens den Titel ein.");
            return;
        }
        
        //Unterscheidung Film/CD
        if (Constants.S_FILM.equals(input.getType())) {
            handleFilm();
        } else if (Constants.S_CD.equals(input.getType())) {
            handleCD();
        } else {
        	AlertHelper.showWarning("Unbekannter Medientyp: " + input.getType());

        }
    }
    
    /**
     * Film einfügen/ändern
     */
    private void handleFilm() {
    	//Anzahl darf nicht leer sein
        if (media.getAmount() < 0) {
        	AlertHelper.showWarning("Bitte eine positive Anzahl eingeben.");
            return;
        }
        
        //Unterschied Insert/Update
        boolean bSuccess;
        if (media.getID() == 0) {
        	bSuccess = DatabaseManager.getInstance().insert(media);
        	AlertHelper.showInfo(bSuccess ? "Film \"" + input.getTitle() + "\" erfolgreich eingefügt."
                            : "Fehler beim Einfügen des Films.");

        } else {
        	
        	bSuccess = DatabaseManager.getInstance().update(media);
            AlertHelper.showInfo(bSuccess ? "Film \"" + input.getTitle() + "\" erfolgreich geändert."
                            : "Bearbeiten fehlgeschlagen.");

        }
    }
    
    /**
     * CD einfügen/ändern
     */
    private void handleCD() {
    	//Anzahl darf nicht leer sein
        if (media.getAmount() < 0) {
            AlertHelper.showWarning("Bitte eine positive Anzahl eingeben.");
            return;
        }
        
        //Unterschied Insert/Update
        boolean success;
        if (media.getID() == 0) {
            success = DatabaseManager.getInstance().insert(media);
            AlertHelper.showInfo(success ? "CD \"" + input.getTitle() + "\" von \"" + input.getInterpret() 
                            + "\" erfolgreich eingefügt."
                            : "Fehler beim Einfügen der CD.");
        } else {
            success = DatabaseManager.getInstance().update(media);
            AlertHelper.showInfo(success ? "CD \"" + input.getTitle() + "\" von \"" + input.getInterpret() 
                            + "\" erfolgreich geändert."
                            : "Bearbeiten fehlgeschlagen.");
        }

    }
}