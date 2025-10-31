package com.moni.medianizer.app.model;

import com.moni.medianizer.app.Constants;

public class CD extends Media {
	
	private String sInterpret;
	
	public CD(int id, String title, int amount, String interpret) {
		super(id, title, amount, Constants.S_CD);
		
		this.sInterpret = interpret;
	}

	public String getinterpret() {
		return sInterpret;
	}
	
}
