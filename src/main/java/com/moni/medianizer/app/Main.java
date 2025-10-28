package com.moni.medianizer.app;

import com.moni.medianizer.app.model.DatabaseManager;

public class Main {

	public static void main(String[] args) {
		DatabaseManager db = new DatabaseManager();
		//db.insert("Testcd", "Testinterpret", 1, "CD");
		db.select("Testcd", "Testinterpret", "CD");
	}

}
