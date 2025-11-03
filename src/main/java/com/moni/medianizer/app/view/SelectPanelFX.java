package com.moni.medianizer.app.view;

import com.moni.medianizer.app.Constants;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SelectPanelFX extends HBox implements InputProvider {

    private ComboBox<String> cbTypes;
    private Label lblTitle;
    private TextField tfTitle;
    private Label lblInterpret;
    private TextField tfInterpret;

    public SelectPanelFX() {
        init();
    }

    private void init() {
    	 setSpacing(10);
         setPadding(new Insets(10));

         cbTypes = new ComboBox<>(FXCollections.observableArrayList(Constants.SA_TYPES));

         lblTitle = new Label(Constants.S_TITLE);
         tfTitle = new TextField();
         tfTitle.setPrefWidth(120);
         tfTitle.setDisable(true);

         lblInterpret = new Label(Constants.S_INTERPRET);
         tfInterpret = new TextField();
         tfInterpret.setPrefWidth(120);
         tfInterpret.setDisable(true);

         getChildren().addAll(cbTypes, lblTitle, tfTitle, lblInterpret, tfInterpret);
    }
    
    public void addTypeChangeListener(ChangeListener<String> changeListener) {
        cbTypes.valueProperty().addListener(changeListener);
    }

    public void addTypeChangeListener(java.util.function.Consumer<String> consumer) {
        cbTypes.valueProperty().addListener((obs, oldVal, newVal) -> consumer.accept(newVal));
    }
  
    public ComboBox<String> getComboBox() {
        return cbTypes;
    }

    @Override
    public String getType() {
        return cbTypes.getValue();
    }

    @Override
    public void setType(String type) {
        cbTypes.setValue(type);
    }

    @Override
    public String getTitle() {
        return tfTitle.getText().trim();
    }

    @Override
    public void setTitle(String title) {
        tfTitle.setText(title);
    }

    @Override
    public String getInterpret() {
        return tfInterpret.getText().trim();
    }

    @Override
    public void setInterpret(String interpret) {
        tfInterpret.setText(interpret);
    }

    @Override
    public void setInterpretEnabled(boolean enabled) {
        tfInterpret.setDisable(!enabled);
    }

    @Override
    public void setTitleEnabled(boolean enabled) {
        tfTitle.setDisable(!enabled);
    }

    @Override
    public void clearInterpret() {
        tfInterpret.clear();
    }

    @Override
    public void clearTitle() {
        tfTitle.clear();
    }
}
