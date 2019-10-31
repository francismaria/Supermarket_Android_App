package com.acme.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

	public DBConnection() {}
	
	public Connection getConnection() {
		Connection connection;
		
		try {
			// Francisco path to db -> "jdbc:sqlite:///Users/francisco/Documents/FEUP/Projetos/CMOV/Server/acme.db"
		
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:///Users/francisco/Documents/FEUP/Projetos/CMOV/Server/acme.db");
			
		} catch(SQLException e) {
			System.err.println(e.getMessage());
			return null;
		}catch (ClassNotFoundException e1) {
			return null;
		}
		
		return connection;
	}
	
	
	
}
