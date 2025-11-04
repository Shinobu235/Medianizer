package com.moni.medianizer.app.view;

import java.util.ArrayList;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.controller.ThirdGUIListenerFX;
import com.moni.medianizer.app.model.CD;
import com.moni.medianizer.app.model.Media;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ThirdGUI<T extends Media> {
	
	private ObservableList<T> olMedia;
	
	public ThirdGUI(ArrayList<T> media) {
		this.olMedia = FXCollections.observableArrayList(media);
		createGUI();
	}
	
	private void createGUI() {
		
		if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::createGUI);
            return;
        }
		
		BorderPane panel = new BorderPane();
		
		Button btnEdit = new Button(Constants.S_EDIT);
		Button btnDelete = new Button(Constants.S_DELETE);
		
		HBox hbBtnBox = new HBox(10, btnEdit, btnDelete);
		hbBtnBox.setPadding(new Insets(5));
		
		Stage stage = new Stage();
		TableView<T> tvMedia = new TableView<>();
		
		TableColumn<T, String> colTitel = new TableColumn<>(Constants.S_TITLE);
		colTitel.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitle()));
		tvMedia.getColumns().add(colTitel);
		
		if (olMedia.get(0) instanceof CD) {
			TableColumn<T, String> colInterpret = new TableColumn<>(Constants.S_INTERPRET);
			colInterpret.setCellValueFactory(cell -> new SimpleStringProperty(((CD)cell.getValue()).getinterpret()));
			tvMedia.getColumns().add(colInterpret);
		}
		
		TableColumn<T, String> colAmount = new TableColumn<>(Constants.S_AMOUNT);
		colAmount.setCellValueFactory(cell -> new SimpleStringProperty(Integer.toString(cell.getValue().getAmount())));
		tvMedia.getColumns().add(colAmount);
		
		tvMedia.setItems(olMedia);
		
		ThirdGUIListenerFX<T> listener = new ThirdGUIListenerFX<>(tvMedia);
		btnEdit.setOnAction(listener);
		btnDelete.setOnAction(listener);
		
		panel.setTop(hbBtnBox);
		panel.setCenter(tvMedia);
		
		stage.setScene(new Scene(panel, 500, 300));
		stage.setTitle(Constants.S_APP_NAME);
		stage.show();
	}
}
