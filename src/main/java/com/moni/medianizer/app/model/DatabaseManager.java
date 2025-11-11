package com.moni.medianizer.app.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Datenbankverbindungen 
 */
public class DatabaseManager {
	private static final String DB_URL = "jdbc:sqlite:data/library.db";
	
	private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
	
	private static DatabaseManager instance;
	
	/**
	 * Singleton
	 * @return instance - Objekt des DatabaseManagers
	 */
	public static DatabaseManager getInstance() {
		
		if (instance == null) {
			instance = new DatabaseManager();
		}
		
		return instance;
	}
	
	private DatabaseManager() {
		createTables();
	}
	
	/**
	 * Datenbank-Verbindung
	 * @return conn
	 * @throws SQLException
	 */
	private Connection connect() throws SQLException {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URL);
		} catch (Exception e) {
			LOGGER.severe("Fehler beim Connect: " + e.getMessage());
		}
		return conn;
	}
	
	/**
	 * Tabelle erstellen
	 */
	private void createTables() {
		
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
			
			stmt.execute(sCreateMedia);
			stmt.execute(sCreateFilm);
			stmt.execute(sCreateCD);
			
		} catch (SQLException e) {
			LOGGER.severe("Fehler beim Erstellen der Datenbanktabellen: " + e.getMessage());
		}
	}
	
	/**
	 * Medium einfügen
	 * @param media
	 * @return true bei Erfolg
	 */
	public boolean insert(Media media) {
		if (media instanceof Film) {
			return insertFilm((Film) media);
		} else if (media instanceof CD) {
			return insertCD((CD) media);
		} else {
			return false;
		}
	}
	
	/**
	 * Medium bearbeiten
	 * @param media
	 * @return true bei Erfolg
	 */
	public boolean update(Media media) {
		if (media instanceof Film) {
			return updateFilm((Film) media);
		} else if (media instanceof CD) {
			return updateCD((CD) media);
		} else {
			return false;
		}
	}
	
	/**
	 * Medium löschen
	 * @param media
	 * @return true bei Erfolg
	 */
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
	 * @param film
	 * @return true bei Erfolg
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
			LOGGER.severe("Fehler beim Film einfügen: " + e.getMessage());
			return false;
		}
		
	}
	
	/**
	 * CD einfügen
	 * @param cd
	 * @return true bei Erfolg
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
			psFilms.setString(1, cd.getInterpret());
			psFilms.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			LOGGER.severe("Fehler beim CD einfügen: " + e.getMessage());
			return false;
		}	
	}

	/**
	 * Film in DB suchen
	 * @param title
	 * @return ArrayList mit Filmen
	 */
	public ArrayList<Film> selectFilm(String title) {
		ArrayList<Film> alFilme = new ArrayList<Film>();
		String sSQL = "SELECT m.id, m.title, m.amount FROM media m JOIN films f ON m.id = f.media_id "
				+ "WHERE m.title LIKE ?";
		try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sSQL)) {
			
			ps.setString(1, "%" + title + "%");
			
			try (ResultSet rs = ps.executeQuery()) {
				//Tabelle mit Ergebnissen füllen
				while (rs.next()) {
					int iID = rs.getInt("id");
					String sTitle = rs.getString("title");
					int iAmount = rs.getInt("amount");
					
					Film film = new Film(iID, sTitle, iAmount);
					alFilme.add(film);
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Fehler bei der Film-Suche: " + e.getMessage());
		}
		return alFilme;
	}
	
	/**
	 * CD in DB suchen
	 * @param title
	 * @param interpret
	 * @return ArrayList aus CDs
	 */
	public ArrayList<CD> selectCD(String title, String interpret) {
		ArrayList<CD> alCDs = new ArrayList<CD>();
		String sSQL = "SELECT m.id, m.title, m.amount, c.interpret FROM media m JOIN cds c ON m.id = c.media_id WHERE 1 = 1";
		
		//Leerer Titel
		if (title != null && !title.isEmpty()) {
			sSQL += " AND m.title LIKE ?";
		}
		
		//Leerer Interpret
		if (interpret != null && !interpret.isEmpty()) {
			sSQL += " AND c.interpret LIKE ?";
		}
		
		try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sSQL)) {
			
			int i = 1;
			//Leerer Titel
			if (title != null && !title.isEmpty()) {
				ps.setString(i++, "%" + title + "%");
			}
			//Leerer Interpret
			if (interpret != null && !interpret.isEmpty()) {
				ps.setString(i++, "%" + interpret + "%");
			}
			
			try (ResultSet rs = ps.executeQuery()) {
				//Tabelle mit Ergebnissen füllen
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
			LOGGER.severe("Fehler bei der CD-Suche: " + e.getMessage());
		}
		return alCDs;
	}
	
	/**
	 * Film aktualisieren
	 * @param film
	 * @return true bei Erfolg
	 */
	private boolean updateFilm(Film film) {
	    String sql = "UPDATE media SET title = ?, amount = ? WHERE id = ? AND type = 'Film'";
	    try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
	    	//Eintrag ändern
	        ps.setString(1, film.getTitle());
	        ps.setInt(2, film.getAmount());
	        ps.setInt(3, film.getID());
	        int rows = ps.executeUpdate();
	        return rows > 0;
	    } catch (SQLException e) {
	    	LOGGER.severe("Fehler beim Film-Update: " + e.getMessage());
	        return false;
	    }
	}

	/**
	 * CD aktualisieren
	 * @param cd
	 * @return true bei Erfolg
	 */
	private boolean updateCD(CD cd) {
	    String sqlMedia = "UPDATE media SET title = ?, amount = ? WHERE id = ? AND type = 'CD'";
	    String sqlCD = "UPDATE cds SET interpret = ? WHERE media_id = ?";
	    try (Connection conn = connect()) {
	        conn.setAutoCommit(false);

	        try (PreparedStatement psMedia = conn.prepareStatement(sqlMedia);
	             PreparedStatement psCD = conn.prepareStatement(sqlCD)) {
	        	
	        	//Aktualisierung in Tabelle media
	            psMedia.setString(1, cd.getTitle());
	            psMedia.setInt(2, cd.getAmount());
	            psMedia.setInt(3, cd.getID());
	            psMedia.executeUpdate();
	            
	            //Aktualisierung in Tabelle cd
	            psCD.setString(1, cd.getInterpret());
	            psCD.setInt(2, cd.getID());
	            psCD.executeUpdate();

	            conn.commit();
	            return true;
	        } catch (SQLException e) {
	            conn.rollback();
	            LOGGER.severe("Fehler beim CD-Update: " + e.getMessage());
	            return false;
	        } finally {
	            conn.setAutoCommit(true);
	        }
	    } catch (SQLException e) {
	    	LOGGER.severe("Fehler beim CD-Update: " + e.getMessage());
	        return false;
	    }
	}

	/**
	 * Film löschen
	 * @param id ID des Films
	 * @return true bei Erfolg
	 */
	private boolean deleteFilm(Film film) {
	    String sql = "DELETE FROM media WHERE id = ? AND type = 'Film'";
	    try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
	    	//Eintrag löschen
	        ps.setInt(1, film.getID());
	        int rows = ps.executeUpdate();
	        return rows > 0;
	    } catch (SQLException e) {
	    	LOGGER.severe("Fehler beim Film-Löschen: " + e.getMessage());
	        return false;
	    }
	}

	/**
	 * CD löschen
	 * @param id ID der CD
	 * @return true bei Erfolg
	 */
	private boolean deleteCD(CD cd) {
	    String sql = "DELETE FROM media WHERE id = ? AND type = 'CD'";
	    try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
	    	//Eintrag löschen
	        ps.setInt(1, cd.getID());
	        int rows = ps.executeUpdate();
	        return rows > 0;
	    } catch (SQLException e) {
	    	LOGGER.severe("Fehler beim CD-Löschen: " + e.getMessage());
	        return false;
	    }
	}

}
