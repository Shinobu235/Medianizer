package com.moni.medianizer.app.controller;

import javax.swing.SwingUtilities;

import com.moni.medianizer.app.view.FirstGUI;

/**
 * Main-Klasse Medianizer 
 */
public class Main {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new FirstGUI();
		});
	}
}
