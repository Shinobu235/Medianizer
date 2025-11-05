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

/**
 * Listener für die ThirdGUI
 * @param <T>
 */
public class ThirdGUIListenerFX<T extends Media> implements EventHandler<ActionEvent> {
	
	 private TableView<T> tableView;
	 private String sType;
	 private Media media;
	 
	 public ThirdGUIListenerFX(TableView<T> tvMedia) {
		 this.tableView = tvMedia;
	}

	@Override
	public void handle(ActionEvent evt) {
		
		T selected = tableView.getSelectionModel().getSelectedItem();
		
		//Falls keine Zeile gewählt wurde
	    if (selected == null) {
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle("Keine Auswahl");
	        alert.setHeaderText(null);
	        alert.setContentText("Ohne Auswahl ist weder Bearbeiten noch Löschen möglich.");
	        alert.showAndWait();
	        return;
	    }
		
		Button btn = (Button) evt.getSource();
        String text = btn.getText();
        media = tableView.getSelectionModel().getSelectedItem();
        sType = media.getType();
        
        //Unterscheidung Bearbeiten/löschen
        switch (text) {
            case Constants.S_EDIT -> handleUpdate();
            case Constants.S_DELETE -> handleDelete();
            default -> new Alert(Alert.AlertType.WARNING, "Unbekannter Button: " + text).showAndWait();
        }
        
        
	}
	
	/**
	 * Eintrag bearbeiten
	 */
	private void handleUpdate() {
		//Tabelle refreshen
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
	}
	
	/**
	 * Eintrag löschen
	 */
	private void handleDelete() {
		boolean bSuccess = false;
		//Unterscheidung Film/CD
	    if (sType.equals(Constants.S_FILM) && media instanceof Film film) {
	    	bSuccess = DatabaseManager.getInstance().delete(film);
	    } else if (sType.equals(Constants.S_CD) && media instanceof CD cd) {
	    	bSuccess = DatabaseManager.getInstance().delete(cd);
	    }
	    tableView.getItems().remove(media);
	    
	    //Dialog je nachdem, ob delete erfolgreich
	    if (bSuccess) {
	    	new Alert(Alert.AlertType.INFORMATION, "Eintrag erfolgreich gelöscht.").showAndWait();
	    } else {
	    	new Alert(Alert.AlertType.ERROR, "Löschen fehlgeschlagen.").showAndWait();
	    }
	}
}
