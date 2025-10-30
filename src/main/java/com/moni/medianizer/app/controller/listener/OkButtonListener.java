package com.moni.medianizer.app.controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.model.DatabaseManager;
import com.moni.medianizer.app.view.InputProvider;

/**
 * Listener für den OK-Button der Benutzerschnittstelle 1 
 */
public class OkButtonListener implements ActionListener {
	
	private DatabaseManager dm = new DatabaseManager();
	private InputProvider input;
	
	public OkButtonListener(InputProvider input) {
		this.input = input;
		//DummyDaten - löschen nicht vergessen, sobald insert funktioniert!
		dm.insertFilm("Film eins", 1);
		dm.insertCD("CD eins", "Interpret eins", 2);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String sType = input.getType();
		String sTitle = input.getTitle();
		String sInterpret = input.getInterpret();
		
		if (sType == Constants.S_FILM) {
			if ((sTitle != null) && (!sTitle.isEmpty())) {
				dm.selectFilm(sTitle);
			} else {
				System.out.println("Bitte Titel eingeben");
			}
		} else if (sType == Constants.S_CD) {
			if ((sTitle == null) || (sTitle.isEmpty())) {
				if ((sInterpret == null) || (sInterpret.isEmpty())) {
					System.out.println("Bitte Titel oder Interpret eigeben");
				} else {
					dm.selectCD(sTitle, null);
				}
			} else {
				if ((sInterpret != null) && (!sTitle.isEmpty())) {
					dm.selectCD(sTitle, sInterpret);
				} else {
					dm.selectCD(sTitle, null);
				}
			}
		}
	}
}
