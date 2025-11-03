package com.moni.medianizer.app.controller;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.model.DatabaseManager;
import com.moni.medianizer.app.view.InputProvider;

import javafx.scene.control.Alert;

public class InsertButtonListenerFX {

    private final InputProvider input;
    private final int amount;

    public InsertButtonListenerFX(InputProvider input, int amount) {
        this.input = input;
        this.amount = amount;
    }

    public void handleInsert() {
        String sType = input.getType();
        String sTitle = input.getTitle();
        String sInterpret = input.getInterpret();

        if (sType == null || sTitle == null || sTitle.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Bitte gib mindestens den Titel ein.").showAndWait();
            return;
        }

        if (Constants.S_FILM.equals(sType)) {
            handleFilm(sTitle);
        } else if (Constants.S_CD.equals(sType)) {
            handleCD(sTitle, sInterpret);
        } else {
            new Alert(Alert.AlertType.WARNING,
                    "Unbekannter Medientyp: " + sType).showAndWait();
        }
    }

    private void handleFilm(String title) {
        if (amount <= 0) {
            new Alert(Alert.AlertType.WARNING,
                    "Bitte eine positive Anzahl eingeben.").showAndWait();
            return;
        }

        DatabaseManager.getInstance().insertFilm(title, amount);
        new Alert(Alert.AlertType.INFORMATION,
                "Film \"" + title + "\" erfolgreich eingefügt.").showAndWait();
    }

    private void handleCD(String title, String interpret) {
        if (amount <= 0) {
            new Alert(Alert.AlertType.WARNING,
                    "Bitte eine positive Anzahl eingeben.").showAndWait();
            return;
        }

        DatabaseManager.getInstance().insertCD(title, interpret, amount);
        new Alert(Alert.AlertType.INFORMATION,
                "CD \"" + title + "\" von \"" + interpret + "\" erfolgreich eingefügt.").showAndWait();
    }
}