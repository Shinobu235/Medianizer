package com.moni.medianizer.app.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.*;

import com.moni.medianizer.app.model.*;
import com.moni.medianizer.app.util.AlertHelper;
import com.moni.medianizer.app.view.InputProvider;

/**
 * Integrationstests
 */
public class DatabaseIntegrationTest {

    private static final String TEST_DB_PATH = "src/test/resources/test.db";

    @BeforeAll
    static void setup() {
        AlertHelper.enableTestMode();

        File dbFile = new File(TEST_DB_PATH);
        if (dbFile.exists()) {
            dbFile.delete();
        }

        System.setProperty("DB_PATH", TEST_DB_PATH);

        // Singleton zurücksetzen, damit neue DB geladen wird
        try {
            var instanceField = DatabaseManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception ignored) {}

        System.out.println("Starte Integrationstests mit Testdatenbank: " + TEST_DB_PATH);
    }

    @AfterAll
    static void teardown() {
        AlertHelper.disableTestMode();
    }

    @Test
    void testInsertFilmEndToEnd() {
        DatabaseManager db = DatabaseManager.getInstance();
        Film film = new Film(0, "Integration Film", 5);

        boolean success = db.insert(film);
        assertTrue(success);

        ArrayList<Film> results = db.selectFilm("Integration Film");
        assertFalse(results.isEmpty());
        assertEquals(5, results.get(0).getAmount());
    }

    @Test
    void testUpdateExistingFilm() {
        DatabaseManager db = DatabaseManager.getInstance();
        Film film = new Film(0, "Old Title", 1);
        db.insert(film);

        ArrayList<Film> results = db.selectFilm("Old Title");
        assertFalse(results.isEmpty());

        Film existing = results.get(0);
        existing.setTitle("Updated Title");
        existing.setAmount(10);

        boolean updated = db.update(existing);
        assertTrue(updated);

        ArrayList<Film> updatedResult = db.selectFilm("Updated Title");
        assertFalse(updatedResult.isEmpty());
        assertEquals(10, updatedResult.get(0).getAmount());
    }

    @Test
    void testDeleteCDIntegration() {
        DatabaseManager db = DatabaseManager.getInstance();
        CD cd = new CD(0, "DeleteTestCD", 1, "X");
        db.insert(cd);

        ArrayList<CD> results = db.selectCD("DeleteTestCD", "X");
        assertFalse(results.isEmpty());
        CD existing = results.get(0);

        boolean deleted = db.delete(existing);
        assertTrue(deleted);

        ArrayList<CD> afterDelete = db.selectCD("DeleteTestCD", "X");
        assertTrue(afterDelete.isEmpty());
    }

    @Test
    void testInsertViaController() {
        // Pseudo InputProvider simuliert GUI-Eingaben
        InputProvider mockInput = new InputProvider() {
            @Override public String getType() { return "CD"; }
            @Override public void setType(String type) {}
            @Override public String getTitle() { return "Controller CD"; }
            @Override public void setTitle(String title) {}
            @Override public String getInterpret() { return "Mock Artist"; }
            @Override public void setInterpret(String interpret) {}
            @Override public void setInterpretEnabled(boolean enabled) {}
            @Override public void setTitleEnabled(boolean enabled) {}
            @Override public void clearInterpret() {}
            @Override public void clearTitle() {}
        };

        CD cd = new CD(0, "Controller CD", 7, "Mock Artist");
        InsertButtonListenerFX controller = new InsertButtonListenerFX(mockInput, cd);
        controller.handleInsert();

        ArrayList<CD> results = DatabaseManager.getInstance().selectCD("Controller CD", "Mock Artist");
        assertFalse(results.isEmpty());
        assertEquals(7, results.get(0).getAmount());
    }
}