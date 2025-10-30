package com.moni.medianizer.app.controller;

import javax.swing.SwingUtilities;

import com.moni.medianizer.app.view.MedianizerGUI;

/**
 * Main-Klasse Medianizer 
 */
public class Main {
	private String test;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MedianizerGUI.getInstance();
		});
	}
}
