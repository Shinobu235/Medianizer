package com.moni.medianizer.app.model;

import com.moni.medianizer.app.Constants;

/**
 * Vorlage Film
 */
public class Film extends Media {
	
	public Film(int id, String title, int amount) {
		super(id, title, amount, Constants.S_FILM);
	}

}
