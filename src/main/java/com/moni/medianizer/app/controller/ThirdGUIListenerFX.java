package com.moni.medianizer.app.controller;

import java.util.ArrayList;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.model.CD;
import com.moni.medianizer.app.model.DatabaseManager;
import com.moni.medianizer.app.model.Film;
import com.moni.medianizer.app.model.Media;
import com.moni.medianizer.app.view.SecondGUIFX;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class ThirdGUIListenerFX<T extends Media> implements EventHandler<ActionEvent> {
	
	 private TableView<T> tableView;
	 private String sType;
	 private Media media;
	 private boolean bSuccess;
	 
	 public ThirdGUIListenerFX(TableView<T> tvMedia) {
		 this.tableView = tvMedia;
	}

	@Override
	public void handle(ActionEvent evt) {

		Button btn = (Button) evt.getSource();
        String text = btn.getText();
        media = tableView.getSelectionModel().getSelectedItem();
        sType = media.getType();
        
        bSuccess = false;

        switch (text) {
            case Constants.S_EDIT -> handleUpdate();
            case Constants.S_DELETE -> handleDelete();
            default -> new Alert(Alert.AlertType.WARNING, "Unbekannter Button: " + text).showAndWait();
        }
        
        
	}

	private void handleUpdate() {
		
		Runnable refreshTable = () -> {
	        Platform.runLater(() -> {
	            if (sType.equals(Constants.S_FILM)) {
	                ArrayList<Film> updatedFilms = DatabaseManager.getInstance().selectFilm("");
	                @SuppressWarnings("unchecked")
	                TableView<Film> filmTable = (TableView<Film>) tableView;
	                filmTable.setItems(FXCollections.observableArrayList(updatedFilms));
	            } else if (sType.equals(Constants.S_CD)) {
	                ArrayList<CD> updatedCDs = DatabaseManager.getInstance().selectCD("", "");
	                @SuppressWarnings("unchecked")
	                TableView<CD> cdTable = (TableView<CD>) tableView;
	                cdTable.setItems(FXCollections.observableArrayList(updatedCDs));
	            }
	        });
	    };
	    
		new SecondGUIFX(media, refreshTable);
		
//		if (bSuccess) {
//	    	new Alert(Alert.AlertType.INFORMATION, "Eintrag erfolgreich bearbeitet.").showAndWait();
//	    } else {
//	    	new Alert(Alert.AlertType.ERROR, "Bearbeiten fehlgeschlagen.").showAndWait();
//	    }
	}

	private void handleDelete() {

	    if (sType.equals(Constants.S_FILM) && media instanceof Film film) {
	    	bSuccess = DatabaseManager.getInstance().delete(film);
	    	System.out.println("delete " + ((Film) media).getTitle());
	    } else if (sType.equals(Constants.S_CD) && media instanceof CD cd) {
	    	bSuccess = DatabaseManager.getInstance().delete(cd);
	    	System.out.println("delete " + ((CD) media).getTitle());
	    }
	    tableView.getItems().remove(media);
	    
//	    if (bSuccess) {
//	    	new Alert(Alert.AlertType.INFORMATION, "Eintrag erfolgreich gelöscht.").showAndWait();
//	    } else {
//	    	new Alert(Alert.AlertType.ERROR, "Löschen fehlgeschlagen.").showAndWait();
//	    }
	}
}
