package mains;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

/**
 * JDBC ƒTƒ“ƒvƒ‹
 * Main
 *
 * @author SUHWAN-IM
 */
public class main {
	static String url = null;
	static String id = null;
	static String pass = null;

	public static void main(final String[] args) {
		
		System.out.println("Start");

		if (args == null) {
			System.out.println("propFile is null");
			System.exit(1);
		}

		final String propFile = args[0];
		System.err.println("path => " + propFile);

		setupJDBCParams(propFile);

		System.err.println("url => " + url);
		System.err.print("id => " + id);
		System.err.println(" // pass => " + pass);

		System.err.println("");
		System.err.println("===========");
		System.err.println("");

		Scanner scan = new Scanner(System.in);

		boolean running = true;

		while (running) {
			System.err.println("EXIT = 0 ");
			System.err.print("sql => ");

			String sql = scan.nextLine();

			if ("0".equals(sql)) {
				running = false;
			} else {
				sendSQL(sql);
			}
		}
		System.out.println("End");
	}

	public static void setupJDBCParams(String path) {
		Properties props = new Properties();
		try (BufferedReader r = Files.newBufferedReader(Paths.get(path))) {
			props.load(r);
			url = props.getProperty("spring.datasource.url");
			id = props.getProperty("spring.datasource.username");
			pass = props.getProperty("spring.datasource.password");
		} catch (IOException e) {
			e.printStackTrace();
			throw new UncheckedIOException(e);
		}
	}

	public static void sendSQL(String sql) {

		System.out.println("sql => " + sql);
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(url, id, pass);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				System.out.println(rs.getString("email"));
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ee) {
					ee.printStackTrace();
				}
			}
		}
	}
}
