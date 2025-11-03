package com.moni.medianizer.app.controller;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.view.InputProvider;
import com.moni.medianizer.app.view.TypeSelectionCallback;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class MediaListenerFX implements ChangeListener<String> {

    private final InputProvider input;
    private final TypeSelectionCallback call;

    public MediaListenerFX(InputProvider input, TypeSelectionCallback call) {
        this.input = input;
        this.call = call;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue == null) return;

        if (call != null) {
            call.selectionChanged(newValue);
        }
        if (newValue.equals(Constants.S_CD)) {
            input.setTitleEnabled(true);
            input.setInterpretEnabled(true);
        } else if (newValue.equals(Constants.S_FILM)) {
            input.setTitleEnabled(true);
            input.clearInterpret();
            input.setInterpretEnabled(false);
        } else {
            input.clearTitle();
            input.clearInterpret();
            input.setTitleEnabled(false);
            input.setInterpretEnabled(false);
        }
    }
}