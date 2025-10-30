package com.moni.medianizer.app.model;

import java.sql.*;

/**
 * Datenbankverbindungen
 */
public class DatabaseManager {
	private static final String DB_URL = "jdbc:sqlite:src/main/resources/data/library.db";
	
	public DatabaseManager() {
		createTables();
	}
	
	private Connection connect() throws SQLException {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URL);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	/**
	 * Tabelle erstellen
	 */
	private void createTables() {
		String sDropMedia = "DROP TABLE IF EXISTS media;";
		String sDropFilms = "DROP TABLE IF EXISTS films;";
		String sDropCDs = "DROP TABLE IF EXISTS cds;";
		
		String sCreateMedia = "CREATE TABLE IF NOT EXISTS media ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "title TEXT NOT NULL,"
				+ "amount INTEGER,"
				+ "type TEXT NOT NULL CHECK (type IN ('Film', 'CD')));";
		
		String sCreateFilm = "CREATE TABLE IF NOT EXISTS films ("
				+ "media_id INTEGER PRIMARY KEY,"
				+ "FOREIGN KEY(media_id) REFERENCES media(id) ON DELETE CASCADE);";
		
		String sCreateCD = "CREATE TABLE IF NOT EXISTS cds ("
				+ "media_id INTEGER PRIMARY KEY,"
				+ "interpret TEXT,"
				+ "FOREIGN KEY(media_id) REFERENCES media(id) ON DELETE CASCADE);";
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			
			stmt.execute(sDropMedia);
			stmt.execute(sDropFilms);
			stmt.execute(sDropCDs);
			stmt.execute(sCreateMedia);
			stmt.execute(sCreateFilm);
			stmt.execute(sCreateCD);
			
			System.out.println("Tabellen erstellt");
			
		} catch (SQLException e) {
			System.err.println("Fehler beim Erstellen der Tabelle: " + e.getMessage());
		}
	}
	
	/**
	 * Film einfügen
	 * @param title
	 * @param amount
	 */
	public void insertFilm(String title, int amount) {
		
		String sInsertMedia = "INSERT INTO 	media (title, type) VALUES (?, 'Film');";
		String sInsertFilms = "INSERT INTO films (media_id) VALUES (last_insert_rowid());";
		
		try (Connection conn = connect(); PreparedStatement psMedia = conn.prepareStatement(sInsertMedia); 
				PreparedStatement psFilms = conn.prepareStatement(sInsertFilms)) {
			
			//Einfügen in media
			psMedia.setString(1, title);
			psMedia.executeUpdate();
			
			//Einfügen in films
			psFilms.executeUpdate();
			
			System.out.println("Film eingefügt");
		} catch (SQLException e) {
			System.out.println("Fehler beim Einfügen des Films: " + e.getMessage());
		}
		
	}
	
	/**
	 * CD einfügen
	 * @param title
	 * @param interpret
	 * @param amount
	 */
	public void insertCD(String title, String interpret, int amount) {
		String sInsertMedia = "INSERT INTO 	media (title, type) VALUES (?, 'CD');";
		String sInsertCDs = "INSERT INTO cds (media_id, interpret) VALUES (last_insert_rowid(), ?);";
		
		try (Connection conn = connect(); PreparedStatement psMedia = conn.prepareStatement(sInsertMedia); 
				PreparedStatement psFilms = conn.prepareStatement(sInsertCDs)) {
			
			//Einfügen in media
			psMedia.setString(1, title);
			psMedia.executeUpdate();
			
			//Einfügen in cds
			psFilms.setString(1, interpret);
			psFilms.executeUpdate();
			
			System.out.println("CD eingefügt");
		} catch (SQLException e) {
			System.out.println("Fehler beim Einfügen der CD: " + e.getMessage());
		}	
	}

	/**
	 * Film in DB suchen
	 * @param title
	 */
	public void selectFilm(String title) {
		String sSQL = "SELECT m.id, m.title FROM media m JOIN films f ON m.id = f.media_id "
				+ "WHERE m.title LIKE ?";
		try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sSQL)) {
			
			ps.setString(1, "%" + title + "%");
			
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					System.out.printf("ID: %d, Titel: %s%n", 
							rs.getInt("id"),
							rs.getString("title"));
				}
			}
		} catch (SQLException e) {
			System.out.println("Fehler beim Auslesen der Filme: " + e.getMessage());
		}
	}
	
	/**
	 * CD in DB suchen
	 * @param title
	 * @param interpret
	 */
	public void selectCD(String title, String interpret) {
		
		String sSQL = "SELECT m.id, m.title, c.interpret FROM media m JOIN cds c ON m.id = c.media_id WHERE 1 = 1";
		
		if (title != null && !title.isEmpty()) {
			sSQL += " AND m.title LIKE ?";
		}
		if (interpret != null && !interpret.isEmpty()) {
			sSQL += " AND c.interpret LIKE ?";
		}
		
		try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sSQL)) {
			
			int i = 1;
			if (title != null && !title.isEmpty()) {
				ps.setString(i++, "%" + title + "%");
			}
			if (interpret != null && !interpret.isEmpty()) {
				ps.setString(i++, "%" + interpret + "%");
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					System.out.printf("ID: %d, Titel: %s, Interpret: %s,%n", 
							rs.getInt("id"),
							rs.getString("title"),
							rs.getString("interpret"));
				}
			}
		} catch (SQLException e) {
			System.out.println("Fehler beim Auslesen der CDs: " + e.getMessage());
		}
	}
}
