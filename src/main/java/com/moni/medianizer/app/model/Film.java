package com.moni.medianizer.app.model;

import com.moni.medianizer.app.Constants;

public class Film extends Media {
	
	public Film(int id, String title, int amount) {
		super(id, title, amount, Constants.S_FILM);
	}

}
