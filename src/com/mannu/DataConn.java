package com.mannu;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataConn {
	static Connection con;
	
	public static Connection getConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con=DriverManager.getConnection("jdbc:sqlserver://192.168.84.98;user=sa;password=Karvy@123;database=pan");
		} catch (Exception e) {
			return con;
		}
		
		return con;
	}

}
