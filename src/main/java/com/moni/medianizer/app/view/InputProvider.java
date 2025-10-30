package com.moni.medianizer.app.view;

public interface InputProvider {
	
	String getType();
	String getTitle();
	String getInterpret();
	void setInterpretEnabled(boolean enabled);
	void setTitleEnabled(boolean enabled);
	void clearInterpret();
	void clearTitle();
}
