package com.moni.medianizer.app.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.*;

import com.moni.medianizer.app.util.AlertHelper;

/**
 * Unit-Tests
 */
public class DatabaseManagerTest {

    private static final String TEST_DB_PATH = "src/test/resources/test.db";

    @BeforeAll
    static void setup() {
        AlertHelper.enableTestMode();

        File dbFile = new File(TEST_DB_PATH);
        if (dbFile.exists()) {
            dbFile.delete();
        }

        System.setProperty("DB_PATH", TEST_DB_PATH);

        try {
            java.lang.reflect.Field instanceField = DatabaseManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception ignored) {}

        System.out.println("Testmodus aktiviert – Verwende Testdatenbank: " + TEST_DB_PATH);
    }



    @AfterAll
    static void teardown() {
        // Testmodus deaktivieren
        AlertHelper.disableTestMode();
    }


    @Test
    void testInsertAndSelectFilm() {
        DatabaseManager db = DatabaseManager.getInstance();
        Film film = new Film(0, "Testfilm", 3);

        assertTrue(db.insert(film));

        ArrayList<Film> results = db.selectFilm("Testfilm");
        assertFalse(results.isEmpty());
        assertEquals("Testfilm", results.get(0).getTitle());
        assertEquals(3, results.get(0).getAmount());
    }

    @Test
    void testInsertAndSelectCD() {
        DatabaseManager db = DatabaseManager.getInstance();
        CD cd = new CD(0, "Test-CD", 2, "Tester");

        assertTrue(db.insert(cd));

        ArrayList<CD> results = db.selectCD("Test-CD", "Tester");
        assertFalse(results.isEmpty());
        assertEquals("Test-CD", results.get(0).getTitle());
        assertEquals("Tester", results.get(0).getInterpret());
    }

    @Test
    void testUpdateFilm() {
        DatabaseManager db = DatabaseManager.getInstance();
        Film film = new Film(0, "Alt", 1);
        db.insert(film);

        ArrayList<Film> results = db.selectFilm("Alt");
        Film existing = results.get(0);
        existing.setTitle("Neu");
        existing.setAmount(5);

        assertTrue(db.update(existing));
        ArrayList<Film> updated = db.selectFilm("Neu");
        assertEquals(5, updated.get(0).getAmount());
    }

    @Test
    void testDeleteCD() {
        DatabaseManager db = DatabaseManager.getInstance();
        CD cd = new CD(0, "DeleteMe", 1, "X");
        db.insert(cd);

        ArrayList<CD> results = db.selectCD("DeleteMe", "X");
        assertFalse(results.isEmpty());
        CD existing = results.get(0);

        assertTrue(db.delete(existing));
        assertTrue(db.selectCD("DeleteMe", "X").isEmpty());
    }
}