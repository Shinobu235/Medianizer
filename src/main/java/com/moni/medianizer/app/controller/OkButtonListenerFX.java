package com.moni.medianizer.app.controller;

import java.util.ArrayList;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.model.CD;
import com.moni.medianizer.app.model.DatabaseManager;
import com.moni.medianizer.app.model.Film;
import com.moni.medianizer.app.view.InputProvider;
import com.moni.medianizer.app.view.SecondGUIFX;
import com.moni.medianizer.app.view.ThirdGUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

/**
 * Listener für den OK-Button der FirstGUI
 */
public class OkButtonListenerFX implements EventHandler<ActionEvent> {
	
	private InputProvider input;
	private String sType;
    private String sTitle;
    private String sInterpret;

	public OkButtonListenerFX(InputProvider input) {
        this.input = input;
    }

    @Override
    public void handle(ActionEvent e) {
        sType = input.getType();
        sTitle = input.getTitle();
        sInterpret = input.getInterpret();
        
        //Unterschied Film/CD bei getroffener Wahl
        if (sType != null) {
	        if (sType.equals(Constants.S_FILM)) {
	            handleFilm();
	        } else if (sType.equals(Constants.S_CD)) {
	            handleCD();
	        }
        }
    }
    
    /**
     * Film-Suche und anschließende GUI Reaktion
     */
    private void handleFilm() {
        ArrayList<Film> alFilme = new ArrayList<>();
        
        //Leeren Titel abfangen
        if ((sTitle != null) && (!sTitle.isEmpty())) {
            alFilme = DatabaseManager.getInstance().selectFilm(sTitle);
        } else {
        	new Alert(Alert.AlertType.WARNING, "Bitte Titel eingeben.").showAndWait();
        	return;
        }
        
        //Gui Aufrufe, je nach Ergebnis
        if (alFilme.isEmpty()) {
            new SecondGUIFX(new Film(0, sTitle, 0));
        } else {
            new ThirdGUI<Film>(alFilme);
        }
    }
    
    /**
     * CD-Suche und anschließende GUI Reaktion
     */
    private void handleCD() {
        String title = (sTitle == null) ? null : sTitle.trim();
        String interpret = (sInterpret == null) ? null : sInterpret.trim();

        ArrayList<CD> alCDs;

        // Nichts eingegeben
        if ((title == null || title.isEmpty()) && (interpret == null || interpret.isEmpty())) {
            new Alert(Alert.AlertType.WARNING, "Bitte gib Titel oder Interpret ein.").showAndWait();
            return;
        }

        // Nur Interpret
        if (title == null || title.isEmpty()) {
            alCDs = DatabaseManager.getInstance().selectCD(null, interpret);
        }
        // Nur Titel
        else if (interpret == null || interpret.isEmpty()) {
            alCDs = DatabaseManager.getInstance().selectCD(title, null);
        }
        // Titel UND Interpret – mit Fallback auf nur Titel
        else {
            alCDs = DatabaseManager.getInstance().selectCD(title, interpret);
            if (alCDs.isEmpty()) {
                // Fallback, falls der Interpret in der DB anders gespeichert wurde
                alCDs = DatabaseManager.getInstance().selectCD(title, null);
            }
        }

        if (alCDs.isEmpty()) {
            // Nichts gefunden - Anlegen
            new SecondGUIFX(new CD(0, title, 0, interpret));
        } else {
            // Treffer - anzeigen
            new ThirdGUI<>(alCDs);
        }
    }

}


