package com.moni.medianizer.app.controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.model.DatabaseManager;
import com.moni.medianizer.app.view.InputProvider;
import com.moni.medianizer.app.view.InsertProvider;

public class InsertButtonListener implements ActionListener {
	
	private InputProvider input;
	private InsertProvider insert;
	private String sType;
	private String sTitle;
	private String sInterpret;
	private int iAmount;
	private DatabaseManager dm = new DatabaseManager();
	
	public InsertButtonListener(InputProvider input, InsertProvider insert) {
		this.input = input;
		this.insert = insert;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		sType = input.getType();
		sTitle = input.getTitle();
		sInterpret = input.getInterpret();
		iAmount = insert.getAmount();
		System.out.println("drin");
		if (sType.equals(Constants.S_FILM)) {
			dm.insertFilm(sTitle, iAmount);
			
		} else if (sType.equals(Constants.S_CD)) {
			dm.insertCD(sTitle, sInterpret, iAmount);
		}

	}

}
