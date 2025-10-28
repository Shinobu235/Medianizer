package com.moni.medianizer.app.model;

import java.sql.*;

public class DatabaseManager {
	private static final String DB_URL = "jdbc:sqlite:src/main/resources/data/library.db";
	
	public DatabaseManager() {
		createTable();
	}
	
	private Connection connect() throws SQLException {
		return DriverManager.getConnection(DB_URL);
	}
	
	private void createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS media ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "title TEXT NOT NULL,"
				+ "artist TEXT,"
				+ "amount INTEGER,"
				+ "type TEXT NOT NULL);";
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
			System.out.println("Tabelle erstellt oder bereits vorhanden");
		} catch (SQLException e) {
			System.err.println("Fehler beim Erstellen der Tabelle: " + e.getMessage());
		}
	}
	
	public void insert(String title, String artist, int amount, String type) {
		String sql = "INSERT INTO media(title, artist, amount, type) VALUES(?,?,?,?)";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, title);
			pstmt.setString(2, artist);
			pstmt.setInt(3, amount);
			pstmt.setString(4, type);
			pstmt.executeUpdate();
			System.out.println("Eintrag gespeichert: " + title);
		} catch (SQLException e) {
			System.err.println("Fehler beim Einfügen: " + e.getMessage());
		}
	}
	
	public void select(String title, String artist, String type) {
		String sql = "SELECT title, artist, type FROM media WHERE title = '" + title + 
				"' AND artist = '" + artist + 
				"' AND type = '" + type + "'";
		try (Connection conn = connect(); 
				Statement stmt = conn.createStatement(); 
				ResultSet rs = stmt.executeQuery(sql)) {
			
			while (rs.next()) {
				System.out.printf("%s | %s | %s%n",
						rs.getString("title"),
						rs.getString("artist"),
						rs.getString("type"));
			}
		} catch (SQLException e) {
			System.out.println("sql = " + sql);
			System.err.println("Fehler beim Lesen: " + e.getMessage());
		}
	}
}
