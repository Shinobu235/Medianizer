package com.moni.medianizer.app.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Zentrale Hilfsklasse zur Anzeige von Alerts.
 * Ermöglicht das Deaktivieren von Dialogen während Unit-Tests.
 */
public class AlertHelper {

    private static boolean testMode = false;

    /** Aktiviert den Testmodus (keine echten JavaFX-Alerts). */
    public static void enableTestMode() {
        testMode = true;
    }

    /** Deaktiviert den Testmodus (echte JavaFX-Alerts). */
    public static void disableTestMode() {
        testMode = false;
    }

    /** Zeigt eine Info-Nachricht an. */
    public static void showInfo(String message) {
        showAlert(Alert.AlertType.INFORMATION, message);
    }

    /** Zeigt eine Warnung an. */
    public static void showWarning(String message) {
        showAlert(Alert.AlertType.WARNING, message);
    }

    /** Zeigt einen Fehler an. */
    public static void showError(String message) {
        showAlert(Alert.AlertType.ERROR, message);
    }

    /** Interne Hilfsmethode */
    private static void showAlert(Alert.AlertType type, String text) {
        if (testMode) {
            System.out.println("[TEST-ALERT " + type + "] " + text);
        } else {
            if (Platform.isFxApplicationThread()) {
                new Alert(type, text).showAndWait();
            } else {
                Platform.runLater(() -> new Alert(type, text).showAndWait());
            }
        }
    }
}
