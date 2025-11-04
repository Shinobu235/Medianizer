package com.moni.medianizer.app.model;

import java.sql.*;
import java.util.ArrayList;

/**
 * Datenbankverbindungen 
 */
public class DatabaseManager {
	private static final String DB_URL = "jdbc:sqlite:src/main/resources/data/library.db";
	
	private static DatabaseManager instance;
	
	public static DatabaseManager getInstance() {
		
		if (instance == null) {
			instance = new DatabaseManager();
		}
		
		return instance;
	}
	
	private DatabaseManager() {
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
			
		} catch (SQLException e) {
			System.err.println("Fehler beim Erstellen der Tabelle: " + e.getMessage());
		}
	}
	
	public boolean insert(Media media) {
		if (media instanceof Film) {
			return insertFilm((Film) media);
		} else if (media instanceof CD) {
			return insertCD((CD) media);
		} else {
			return false;
		}
	}
	
	public boolean update(Media media) {
		if (media instanceof Film) {
			return updateFilm((Film) media);
		} else if (media instanceof CD) {
			return updateCD((CD) media);
		} else {
			return false;
		}
	}
	
	public boolean delete(Media media) {
		if (media instanceof Film) {
			return deleteFilm((Film) media);
		} else if (media instanceof CD) {
			return deleteCD((CD) media);
		} else {
			return false;
		}
	}
	
	/**
	 * Film einfügen
	 * @param title
	 * @param amount
	 */
	private boolean insertFilm(Film film) {
		
		String sInsertMedia = "INSERT INTO 	media (title, amount, type) VALUES (?, ?, 'Film');";
		String sInsertFilms = "INSERT INTO films (media_id) VALUES (last_insert_rowid());";
		
		try (Connection conn = connect(); PreparedStatement psMedia = conn.prepareStatement(sInsertMedia); 
				PreparedStatement psFilms = conn.prepareStatement(sInsertFilms)) {
			
			//Einfügen in media
			psMedia.setString(1, film.getTitle());
			psMedia.setInt(2, film.getAmount());
			psMedia.executeUpdate();
			
			//Einfügen in films
			psFilms.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			return false;
		}
		
	}
	
	/**
	 * CD einfügen
	 * @param title
	 * @param interpret
	 * @param amount
	 */
	private boolean insertCD(CD cd) {
		String sInsertMedia = "INSERT INTO 	media (title, amount, type) VALUES (?, ?, 'CD');";
		String sInsertCDs = "INSERT INTO cds (media_id, interpret) VALUES (last_insert_rowid(), ?);";
		
		try (Connection conn = connect(); PreparedStatement psMedia = conn.prepareStatement(sInsertMedia); 
				PreparedStatement psFilms = conn.prepareStatement(sInsertCDs)) {
			
			//Einfügen in media
			psMedia.setString(1, cd.getTitle());
			psMedia.setInt(2, cd.getAmount());
			psMedia.executeUpdate();
			
			//Einfügen in cds
			psFilms.setString(1, cd.getinterpret());
			psFilms.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			return false;
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
	
	/**
	 * Film aktualisieren
	 * @param id       ID des Films
	 * @param newTitle Neuer Titel
	 * @param newAmount Neue Anzahl
	 * @return true, wenn erfolgreich
	 */
	private boolean updateFilm(Film film) {
	    String sql = "UPDATE media SET title = ?, amount = ? WHERE id = ? AND type = 'Film'";
	    try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, film.getTitle());
	        ps.setInt(2, film.getAmount());
	        ps.setInt(3, film.getID());
	        int rows = ps.executeUpdate();
	        return rows > 0;
	    } catch (SQLException e) {
	        System.err.println("Fehler beim Aktualisieren des Films: " + e.getMessage());
	        return false;
	    }
	}

	/**
	 * CD aktualisieren
	 * @param id         ID der CD
	 * @param newTitle   Neuer Titel
	 * @param newInterpret Neuer Interpret
	 * @param newAmount  Neue Anzahl
	 * @return true, wenn erfolgreich
	 */
	private boolean updateCD(CD cd) {
	    String sqlMedia = "UPDATE media SET title = ?, amount = ? WHERE id = ? AND type = 'CD'";
	    String sqlCD = "UPDATE cds SET interpret = ? WHERE media_id = ?";
	    try (Connection conn = connect()) {
	        conn.setAutoCommit(false);

	        try (PreparedStatement psMedia = conn.prepareStatement(sqlMedia);
	             PreparedStatement psCD = conn.prepareStatement(sqlCD)) {

	            psMedia.setString(1, cd.getTitle());
	            psMedia.setInt(2, cd.getAmount());
	            psMedia.setInt(3, cd.getID());
	            psMedia.executeUpdate();

	            psCD.setString(1, cd.getinterpret());
	            psCD.setInt(2, cd.getID());
	            psCD.executeUpdate();

	            conn.commit();
	            return true;
	        } catch (SQLException e) {
	            conn.rollback();
	            System.err.println("Fehler beim Aktualisieren der CD: " + e.getMessage());
	            return false;
	        } finally {
	            conn.setAutoCommit(true);
	        }
	    } catch (SQLException e) {
	        System.err.println("Fehler beim Aktualisieren der CD (Verbindung): " + e.getMessage());
	        return false;
	    }
	}

	/**
	 * Film löschen
	 * @param id ID des Films
	 * @return true, wenn erfolgreich
	 */
	private boolean deleteFilm(Film film) {
	    String sql = "DELETE FROM media WHERE id = ? AND type = 'Film'";
	    try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, film.getID());
	        int rows = ps.executeUpdate();
	        return rows > 0;
	    } catch (SQLException e) {
	        System.err.println("Fehler beim Löschen des Films: " + e.getMessage());
	        return false;
	    }
	}

	/**
	 * CD löschen
	 * @param id ID der CD
	 * @return true, wenn erfolgreich
	 */
	private boolean deleteCD(CD cd) {
	    String sql = "DELETE FROM media WHERE id = ? AND type = 'CD'";
	    try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, cd.getID());
	        int rows = ps.executeUpdate();
	        return rows > 0;
	    } catch (SQLException e) {
	        System.err.println("Fehler beim Löschen der CD: " + e.getMessage());
	        return false;
	    }
	}

}
