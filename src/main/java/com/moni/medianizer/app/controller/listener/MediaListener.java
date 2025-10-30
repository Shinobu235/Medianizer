package com.moni.medianizer.app.controller.listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.view.InputProvider;
import com.moni.medianizer.app.view.TypeSelectionCallback;

/**
 * Listener für Drop-down 
 */
public class MediaListener implements ItemListener {
	
	private InputProvider input;
	private TypeSelectionCallback call;
	private String test;
	
	public MediaListener(InputProvider input, TypeSelectionCallback call) {
		this.input = input;
		this.call = call;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if (e.getStateChange() == ItemEvent.SELECTED) {
			
			String sType = input.getType();
			
			if (call != null) {
				call.selectionChanged(sType);
			}
			
			if (sType == Constants.S_CD) {
				input.setTitleEnabled(true);
				input.setInterpretEnabled(true);
			} else if (sType == Constants.S_FILM){
				input.setTitleEnabled(true);
				input.clearInterpret();
				input.setInterpretEnabled(false);
			} else {
				input.clearTitle();
				input.clearInterpret();
				input.setTitleEnabled(false);
				input.setInterpretEnabled(false);
			}
		}
	}
}
