import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDB {
	/*
	 * run this to create the addressbook database
	 * used by the ConnectDB class
	 */
	public static void main(String[] args) {
		create();
	}

	public static void create() {
		System.out.println("connecting");
		String DRIVER = "com.mysql.jdbc.Driver";
		String URL = "jdbc:mysql://localhost";
		String USER = "root";
		String PASS = "root";
		String SQL = "CREATE TABLE person(id int not null AUTO_INCREMENT PRIMARY KEY, firstname varchar(30), "
				+ "lastname varchar(30), number bigint, address varchar(50), city varchar(15), zipcode int);";
		Connection conn;
		Statement stmt;

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASS);
			stmt = conn.createStatement();
			stmt.executeUpdate("DROP DATABASE IF EXISTS addressbook;");
			stmt.executeUpdate("CREATE DATABASE addressbook;");
			System.out.println("database created");
			stmt.executeUpdate("use addressbook;");
			stmt.executeUpdate(SQL);
			System.out.println("table created");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
