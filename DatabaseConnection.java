package library;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
	// JDBC URL, username, and password of your MySQL server
	private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USERNAME = "system";
	private static final String PASSWORD = "Password";

	// Singleton instance
	private static DatabaseConnection instance;

	// Database connection
	private Connection connection;

	// Private constructor to prevent instantiation
	private DatabaseConnection() {
		
		try {
			try {
			    Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e) {
			    System.err.println("Oracle JDBC driver not found.");
			    e.printStackTrace();
			    return; // Exit or handle the error as appropriate.
			}
			// Create a database connection
			connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
			System.out.println("Database connected successfully!");
			TableCreation();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to connect to the database.");
		}
	}

	private boolean tableExists(String tableName) throws SQLException {
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet resultSet = meta.getTables(null, null, tableName.toUpperCase(), new String[] { "TABLE" });

		boolean bool = !resultSet.next();
//		System.out.println(bool);
		return bool;
	}

	private void TableCreation() {
		try {
			// Create a database connection
			Statement statement = connection.createStatement();
			/* Echo name of all Tables */
//			String query = "SELECT table_name FROM all_tables WHERE owner = '" + USERNAME.toUpperCase() + "'";
//			ResultSet resultSet = statement.executeQuery(query);
//
//			// Display the table names
//			while (resultSet.next()) {
//				String tableName = resultSet.getString("table_name");
//				System.out.println("Table: " + tableName);
//			}
			/* Drop a Table */

//			String dropTableSQL = "DROP TABLE STUDENTS";
//
//			// Execute the SQL statement to drop the table
//			statement.execute(dropTableSQL);
//
//			System.out.println("Table 'STUDENTS' dropped successfully.");

			// Define the SQL CREATE TABLE statement

			if (tableExists("students")) {
				String createTableSQL = "CREATE TABLE students(" + "id NUMBER PRIMARY KEY not null, " + "std_id NUMBER UNIQUE, "
						+ "name VARCHAR (20) NOT NULL, " + "age number, " + "grade VARCHAR (20) NOT NULL)";
				// Execute the CREATE TABLE statement
				statement.execute(createTableSQL);

				System.out.println("Table created successfully!");
			} else
				System.out.println("This table is existed!!");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to connect to the database.");
		}
	}

	// Method to get the singleton instance
	public static synchronized DatabaseConnection getInstance() {
		if (instance == null) {
			instance = new DatabaseConnection();
		}
		return instance;
	}

	// Method to get the database connection
	public Connection getConnection() {
		return connection;
	}
}

