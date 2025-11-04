package com.moni.medianizer.app.view;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.controller.InsertButtonListenerFX;
import com.moni.medianizer.app.controller.MediaListenerFX;
import com.moni.medianizer.app.model.CD;
import com.moni.medianizer.app.model.Media;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SecondGUIFX {

    private final Media media;
    private final Runnable callBack;
    private final SelectPanelFX sPanel = new SelectPanelFX();
    private final TextField tfAmount = new TextField();
    private final Button btnInsert = new Button(Constants.S_SAVE);
    
    public SecondGUIFX(Media media, Runnable callBack) {
    	this.media = media;
    	this.callBack = callBack;
    	createGUI();
    }
    
    public SecondGUIFX(Media media) {
        this(media, null);
    }

    private void createGUI() {
        
        Stage stage = new Stage();
        stage.setTitle(Constants.S_APP_NAME);

        sPanel.setType(media.getType());
        sPanel.setTitle(media.getTitle());
        if (media instanceof CD) {
        	if (((CD) media).getinterpret() != null) {
        		sPanel.setInterpret(((CD) media).getinterpret());
        	}
        }

        MediaListenerFX mediaListener = new MediaListenerFX(sPanel, selectedType -> {});
        sPanel.getComboBox().valueProperty().addListener(mediaListener);

        String currentType = sPanel.getType();
        if (currentType != null) {
            mediaListener.changed(null, null, currentType);
        }

        Label lblAmount = new Label(Constants.S_AMOUNT);
        tfAmount.setPromptText("z. B. 3");
        if (media.getAmount() != 0) {
        	tfAmount.setText(Integer.toString(media.getAmount()));
        }
        tfAmount.setPrefWidth(80);

        tfAmount.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                tfAmount.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        btnInsert.setOnAction(e -> {
            try {
                media.setAmount(Integer.parseInt(tfAmount.getText()));
                media.setTitle(sPanel.getTitle());
                InsertButtonListenerFX insertListener =
                        new InsertButtonListenerFX(sPanel, media);
                insertListener.handleInsert();
                
                if (callBack != null) {
                    callBack.run();
                }
                stage.close();
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Bitte eine gültige Zahl eingeben.").showAndWait();
            }
        });

        HBox layout = new HBox(15, sPanel, lblAmount, tfAmount, btnInsert);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();
    }
}