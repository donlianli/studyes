package com.donlianli.es.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtils {
	public static Connection getOracleConnection() {
		String URL = "jdbc:oracle:thin:@10.9.14.143:1521:orcl";
//		String URL = "jdbc:oracle:thin:@192.168.1.114:1521:orcl";
		String user = "code";
		String password = "code";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			return DriverManager.getConnection(URL, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Connection getMysqlConnection() {
		String URL = "jdbc:mysql://10.9.14.132:3306/donlianli?characterEncoding=gbk";
//		String URL = "jdbc:mysql://192.168.1.115:3306/test";
		String user = "root";
		String password = "mysql";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(URL, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
