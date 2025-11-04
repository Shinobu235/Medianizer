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

public class OkButtonListenerFX implements EventHandler<ActionEvent> {
	
	private InputProvider input;
	private String sType;
    private String sTitle;
    private String sInterpret;

	public OkButtonListenerFX(InputProvider input) {
        this.input = input;

        // DummyDaten
//        DatabaseManager.getInstance().insert(new Film(0, "Film eins", 1));
//        DatabaseManager.getInstance().insert(new Film(0, "Film zwei", 2));
//        DatabaseManager.getInstance().insert(new CD(0, "CD eins", 11, "Interpret eins"));
//        DatabaseManager.getInstance().insert(new CD(0, "CD zwei", 22, "Interpret zwei"));
    }

    @Override
    public void handle(ActionEvent e) {
        sType = input.getType();
        sTitle = input.getTitle();
        sInterpret = input.getInterpret();

        if (sType.equals(Constants.S_FILM)) {
            handleFilm();
        } else if (sType.equals(Constants.S_CD)) {
            handleCD();
        }
    }

    private void handleCD() {
        ArrayList<CD> alCDs = new ArrayList<>();

        if ((sTitle == null) || sTitle.isEmpty()) {
            if ((sInterpret == null) || sInterpret.isEmpty()) {
                System.out.println("Bitte Titel oder Interpret eingeben");
            } else {
                alCDs = DatabaseManager.getInstance().selectCD(null, sInterpret);
            }
        } else {
            if ((sInterpret != null) && (!sInterpret.isEmpty())) {
                alCDs = DatabaseManager.getInstance().selectCD(sTitle, sInterpret);
            } else {
                alCDs = DatabaseManager.getInstance().selectCD(sTitle, null);
            }
        }

        if (alCDs.isEmpty()) {
            new SecondGUIFX(new CD(0, sTitle, 0, sInterpret));
        } else {
            new ThirdGUI<CD>(alCDs);
        }
    }

    private void handleFilm() {
        ArrayList<Film> alFilme = new ArrayList<>();

        if ((sTitle != null) && (!sTitle.isEmpty())) {
            alFilme = DatabaseManager.getInstance().selectFilm(sTitle);
        } else {
            System.out.println("Bitte Titel eingeben");
        }

        if (alFilme.isEmpty()) {
            new SecondGUIFX(new Film(0, sTitle, 0));
        } else {
            new ThirdGUI<Film>(alFilme);
        }
    }
}


