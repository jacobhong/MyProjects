import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConnectDB {
	private String DRIVER = "com.mysql.jdbc.Driver";
	private String URL = "jdbc:mysql://localhost/addressbook";
	private String USER = "root";
	private String PASS = "root";
	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet rs;

	public ConnectDB() {
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void add(Person person) {
		String sql = "INSERT INTO person(firstname, lastname, number, address, "
				+ "city, zipcode" + ") VALUES(?,?,?,?,?,?);";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, person.getFirstName());
			stmt.setString(2, person.getLastName());
			stmt.setLong(3, person.getNumber());
			stmt.setString(4, person.getAddress());
			stmt.setString(5, person.getCity());
			stmt.setInt(6, person.getZipcode());
			System.out.println(stmt.executeUpdate()
					+ " row(s) affected!, user added");
		} catch (SQLException e) {
			System.out.println("adding user failed");
		} finally {
			disconnect();
		}

	}

	public void edit(Object value, int id, String column) {
		String sql = "UPDATE person SET " + column + " = ?" + " where id = "
				+ id;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setObject(1, value);
			System.out.println(stmt.executeUpdate()
					+ " row(s) affected, user changed");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	public List<Object> search(String lastName) {
		String sql = "select firstname, lastname, number, address, city, "
				+ "zipcode from person where lastname = ?";
		ArrayList<Object> result = new ArrayList<Object>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, lastName);
			rs = stmt.executeQuery();
			while (rs.next()) {
				for (int i = 0; i < 6; i++) {
					result.add(rs.getObject(i + 1));
				}
			}
			System.out.println(result);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return result;
	}

	public List<List<Object>> getTableData() {
		/*
		 * return an arraylist of data to create table model
		 */
		List<List<Object>> person = new ArrayList<List<Object>>();
		System.out.println("fetching table data");
		try {
			Statement stmt = conn.createStatement();
			String sql = "select id, firstname, lastname, number, "
					+ "address, city, zipcode from person;";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ArrayList<Object> temp = new ArrayList<Object>();
				for (int column = 0; column < 7; column++) {
					temp.add(rs.getObject(column + 1));
				}
				person.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return person;
	}

	public void disconnect() {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
