package com.moni.medianizer.app.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
		//Drop befehle löschen, wenn Duplikate nicht mehr  möglich sind!
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
		
		String sInsertMedia = "INSERT INTO 	media (title, amount, type) VALUES (?, ?, 'Film');";
		String sInsertFilms = "INSERT INTO films (media_id) VALUES (last_insert_rowid());";
		
		try (Connection conn = connect(); PreparedStatement psMedia = conn.prepareStatement(sInsertMedia); 
				PreparedStatement psFilms = conn.prepareStatement(sInsertFilms)) {
			
			//Einfügen in media
			psMedia.setString(1, title);
			psMedia.setInt(2, amount);
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
		String sInsertMedia = "INSERT INTO 	media (title, amount, type) VALUES (?, ?, 'CD');";
		String sInsertCDs = "INSERT INTO cds (media_id, interpret) VALUES (last_insert_rowid(), ?);";
		
		try (Connection conn = connect(); PreparedStatement psMedia = conn.prepareStatement(sInsertMedia); 
				PreparedStatement psFilms = conn.prepareStatement(sInsertCDs)) {
			
			//Einfügen in media
			psMedia.setString(1, title);
			psMedia.setInt(2, amount);
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
	 * @return arrayListFilme
	 */
	public ArrayList<Film> selectFilm(String title) {
		ArrayList<Film> alFilme = new ArrayList<Film>();
		String sSQL = "SELECT m.id, m.title, m.amount FROM media m JOIN films f ON m.id = f.media_id "
				+ "WHERE m.title LIKE ?";
		try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sSQL)) {
			
			ps.setString(1, "%" + title + "%");
			
			try (ResultSet rs = ps.executeQuery()) {
				
				while (rs.next()) {
					int iID = rs.getInt("id");
					String sTitle = rs.getString("title");
					int iAmount = rs.getInt("amount");
					
					Film film = new Film(iID, sTitle, iAmount);
					alFilme.add(film);
				}
			}
		} catch (SQLException e) {
			System.out.println("Fehler beim Auslesen der Filme: " + e.getMessage());
		}
		return alFilme;
	}
	
	/**
	 * CD in DB suchen
	 * @param title
	 * @param interpret
	 * @return arrayListCDs
	 */
	public ArrayList<CD> selectCD(String title, String interpret) {
		ArrayList<CD> alCDs = new ArrayList<CD>();
		String sSQL = "SELECT m.id, m.title, m.amount, c.interpret FROM media m JOIN cds c ON m.id = c.media_id WHERE 1 = 1";
		
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
					int iID = rs.getInt("id");
					String sTitle = rs.getString("title");
					int iAmount = rs.getInt("amount");
					String sInterpret = rs.getString("interpret");
					
					CD cd = new CD(iID, sTitle, iAmount, sInterpret);
					alCDs.add(cd);
				}
			}
		} catch (SQLException e) {
			System.out.println("Fehler beim Auslesen der CDs: " + e.getMessage());
		}
		return alCDs;
	}
}
