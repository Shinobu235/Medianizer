package com.moni.medianizer.app.model;

public abstract class Media {
	
	private int iID;
	private String sTitle;
	private int iAmount;
	private String sType;
	
	public Media(int id, String title, int amount, String type) {
		this.iID = id;
		this.sTitle = title;
		this.iAmount = amount;
		this.sType = type;
	}
	
	public int getID() {
		return iID;
	}
	public String getTitle() {
		return sTitle;
	}
	public int getAmount() {
		return iAmount;
	}
	public String getType() {
		return sType;
	}

	public void setAmount(int amount) {
		this.iAmount = amount;
	}

	public void setTitle(String title) {
		this.sTitle = title;
	}
	
}
