package com.moni.medianizer.app.controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.model.CD;
import com.moni.medianizer.app.model.DatabaseManager;
import com.moni.medianizer.app.model.Film;
import com.moni.medianizer.app.view.InputProvider;
import com.moni.medianizer.app.view.SecondGUI;

/**
 * Listener für den OK-Button der Benutzerschnittstelle 1 
 */
public class OkButtonListener implements ActionListener {
	
	private DatabaseManager dm = new DatabaseManager();
	private InputProvider input;
	private String sType;
	private String sTitle;
	private String sInterpret;
	
	public OkButtonListener(InputProvider input) {
		this.input = input;
		//DummyDaten - löschen nicht vergessen, sobald insert funktioniert!
		dm.insertFilm("Film eins", 1);
		dm.insertCD("CD eins", "Interpret eins", 2);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		sType = input.getType();
		sTitle = input.getTitle();
		sInterpret = input.getInterpret();
		
		if (sType == Constants.S_FILM) {
			handleFilm();
		} else if (sType == Constants.S_CD) {
			handleCD();
		}
	}
	
	private void handleCD() {
		ArrayList<CD> alCDs = new ArrayList<CD>();
		
		if ((sTitle == null) || (sTitle.isEmpty())) {
			if ((sInterpret == null) || (sInterpret.isEmpty())) {
				System.out.println("Bitte Titel oder Interpret eigeben");
			} else {
				alCDs = dm.selectCD(null, sInterpret);
			}
		} else {
			if ((sInterpret != null) && (!sTitle.isEmpty())) {
				alCDs = dm.selectCD(sTitle, sInterpret);
			} else {
				alCDs = dm.selectCD(sTitle, null);
			}
		}
		
		if (alCDs.isEmpty()) {
			new SecondGUI(sType, sTitle, sInterpret);
		} else {
			System.out.println("Titel: " + alCDs.get(0).getTitle() + " Interpret: " + alCDs.get(0).getinterpret() + " Anzahl: " + alCDs.get(0).getAmount());
		}
	}
	
	private void handleFilm() {
		
		ArrayList<Film> alFilme = new ArrayList<Film>();
		
		if ((sTitle != null) && (!sTitle.isEmpty())) {
			alFilme = dm.selectFilm(sTitle);
		} else {
			System.out.println("Bitte Titel eingeben");
		}
		
		if (alFilme.isEmpty()) {
			new SecondGUI(sType, sTitle);
		} else {
			System.out.println("Titel: " + alFilme.get(0).getTitle() + " Anzahl: " + alFilme.get(0).getAmount());
		}
	}
}
