package com.moni.medianizer.app.model;

import com.moni.medianizer.app.Constants;

/**
 * Vorlage CD
 */
public class CD extends Media {
	
	private String sInterpret;
	
	public CD(int id, String title, int amount, String interpret) {
		super(id, title, amount, Constants.S_CD);
		
		this.sInterpret = interpret;
	}

	public String getInterpret() {
		return sInterpret;
	}
	
	public void setInterpret(String interpret) {
		this.sInterpret = interpret;
	}
	
}
