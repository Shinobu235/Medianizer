package com.moni.medianizer.app.view;

/**
 * Interface für SelectPanel 
 */
public interface InputProvider {
	
	String getType();
	void setType(String type);
	String getTitle();
	void setTitle(String title);
	String getInterpret();
	void setInterpret(String interpret);
	void setInterpretEnabled(boolean enabled);
	void setTitleEnabled(boolean enabled);
	void clearInterpret();
	void clearTitle();
	
}
