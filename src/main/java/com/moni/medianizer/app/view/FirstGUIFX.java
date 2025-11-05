package com.moni.medianizer.app.view;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.controller.MediaListenerFX;
import com.moni.medianizer.app.controller.OkButtonListenerFX;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Erste initiale Benutzerschnittstelle
 */
public class FirstGUIFX extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		
		SelectPanelFX sPane = new SelectPanelFX();
	    Button btnOK = new Button(Constants.S_OK);
        btnOK.setDisable(true);
        
        //Listener für die Dropbox
        MediaListenerFX mediaListener = new MediaListenerFX(sPane, selected -> {
            btnOK.setDisable(selected.equals(null));
        });

        sPane.getComboBox().valueProperty().addListener(mediaListener);

        btnOK.setOnAction(new OkButtonListenerFX(sPane));
   
        HBox root = new HBox(15); //Abstand Elemente
        root.setPadding(new Insets(15));
        root.getChildren().addAll(sPane, btnOK);
        root.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(Constants.S_APP_NAME);
        stage.show();

	}
	

}
