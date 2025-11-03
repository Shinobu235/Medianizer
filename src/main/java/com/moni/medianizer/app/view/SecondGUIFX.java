package com.moni.medianizer.app.view;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.controller.InsertButtonListenerFX;
import com.moni.medianizer.app.controller.MediaListenerFX;

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

    private final String sType;
    private final String sTitle;
    private final String sInterpret;

    private final SelectPanelFX sPanel = new SelectPanelFX();
    private final TextField tfAmount = new TextField();
    private final Button btnInsert = new Button(Constants.S_INSERT);

    public SecondGUIFX(String type, String title, String interpret) {
        this.sType = type;
        this.sTitle = title;
        this.sInterpret = interpret;
        createGUI();
    }

    public SecondGUIFX(String type, String title) {
        this(type, title, null);
    }

    private void createGUI() {
        
        Stage stage = new Stage();
        stage.setTitle(Constants.S_APP_NAME);

        sPanel.setType(sType);
        sPanel.setTitle(sTitle);
        if (sInterpret != null) {
            sPanel.setInterpret(sInterpret);
        }

        MediaListenerFX mediaListener = new MediaListenerFX(sPanel, selectedType -> {});
        sPanel.getComboBox().valueProperty().addListener(mediaListener);

        String currentType = sPanel.getType();
        if (currentType != null) {
            mediaListener.changed(null, null, currentType);
        }

        Label lblAmount = new Label(Constants.S_AMOUNT);
        tfAmount.setPromptText("z. B. 3");
        tfAmount.setPrefWidth(80);

        tfAmount.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                tfAmount.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        btnInsert.setOnAction(e -> {
            try {
                int iAmount = Integer.parseInt(tfAmount.getText());
                InsertButtonListenerFX insertListener =
                        new InsertButtonListenerFX(sPanel, iAmount);
                insertListener.handleInsert();
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