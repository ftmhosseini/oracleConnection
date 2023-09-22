package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class OracleDBConnection {
	static String grade(int id) {
		switch (id) {
		case 0:
			return "Diploma";
		case 1:
			return "Bachelor";

		case 2:
			return "Master";

		case 3:
			return "PHD";

		}
		return null;
	}

	public static void registeration(Connection connection, Statement statement) throws SQLException {
		Scanner obj = new Scanner(System.in);
		System.out.println("Please enter student id:");
		int id = obj.nextInt();
		if (search(statement, id)) {
			System.out.println("This student id is existed!!");
		} else {
			PreparedStatement prepareStatement = connection.prepareStatement(
					"INSERT INTO students (id, std_id, name, age, grade)" + " VALUES (?, ?, ?, ?, ?)");
// 			find last id and set new id
			prepareStatement.setInt(1, search(statement));
			prepareStatement.setInt(2, id);
			System.out.println("Please enter name:");
			String name = obj.next();
			prepareStatement.setString(3, name);
			System.out.println("Please enter age:");
			id = obj.nextInt();
			prepareStatement.setInt(4, id);
			String s;
			System.out.println("Please enter grade (0: Diploma, 1: Bachelor, 2: Master, 3: PHD)");
			id = obj.nextInt();
			while (id > 3 || id < 0) {
				System.out.println("please enter the correct grade number");
				id = obj.nextInt();
			}
			s = grade(id);
			prepareStatement.setString(5, s);
			prepareStatement.execute();
			System.out.println("Insert was successfully");
		}
	}

	public static String edit() {
		Scanner obj = new Scanner(System.in);
		String s = "";
		System.out.println("Please enter name:");
		String name = obj.next();
		s = "name = '" + name + "'";
		System.out.println("Please enter age:");
		int id = obj.nextInt();
		s = s + ", age =" + id;
		System.out.println("Please enter grade (0: Diploma, 1: Bachelor, 2: Master, 3: PHD)");
		id = obj.nextInt();
		while (id > 3 || id < 0) {
			System.out.println("please enter the correct grade number");
			id = obj.nextInt();
		}
		s = s + ", grade = '" + grade(id) + "'";
		obj.close();
		System.out.println(s);
		return s;
	}

	public static void print_query(Statement statement, String s) throws SQLException {
		ResultSet result = statement.executeQuery(s);
		while (result.next()) {
			System.out.format("id = %s,\t student id = %s,\t name = %s, \t age = %s,\t grade = %s\n",
					result.getString("id"), result.getString("std_id"), result.getString("name"),
					result.getString("age"), result.getString("grade"));
			/*
			 * System.out.println(result.getString("id"));
			 * System.out.println(result.getString("std_id"));
			 * System.out.println(result.getString("name"));
			 * System.out.println(result.getString("age"));
			 * System.out.println(result.getString("grade"));
			 */}
	}

	public static int search(Statement statement) throws SQLException {
		String s = "SELECT id FROM students";
		ResultSet result = statement.executeQuery(s);
		int id = 0;
		while (result.next()) {
			if (id < Integer.parseInt(result.getString("id"))) {
				id = Integer.parseInt(result.getString("id"));
			}
		}
		return ++id;
	}

	public static boolean search(Statement statement, int id) throws SQLException {

		String s = "SELECT * FROM students WHERE std_id =" + id;
		System.out.println(s);
		ResultSet result = statement.executeQuery(s);
		while (result.next()) {
			return true;
		}
		System.out.println("the student info was not found");

		return false;
	}

	public static void main(String[] args) throws SQLException {
		// Database connection parameters
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();

		// Get the database connection
		Connection connection = dbConnection.getConnection();
		Scanner obj = new Scanner(System.in);
		Scanner scanner = new Scanner(System.in);
		int id;

		Statement statement = connection.createStatement();

		outer: while (true) {
			System.out.println("please select your operation:\n" + "1 : Register a student data\n"
					+ "2 : Search a student with student id\n" + "3 : Edit a student information\n"
					+ "4 : View all student information\n" + "5 : Remove a student info\n" + "0 : Exit\n");

			switch (scanner.nextInt()) {
			case 1:
				registeration(connection, statement);
				break;
			case 2:

				System.out.println("please enter a student id that you search about");
				id = obj.nextInt();
				if (search(statement, id)) {

					System.out.println("the student data is");
					String s = "SELECT * FROM students WHERE std_id =" + id;
					print_query(statement, s);
				}
				break;
			case 3:
				System.out.println("please enter a student id that you search about");
				id = obj.nextInt();
				if (search(statement, id)) {
					String str = edit();
					String s = "UPDATE students SET " + str + " WHERE std_id = " + id;
					statement.executeUpdate(s);
					System.out.println("UPDATE was successfully");
				}
				break;
			case 4:
//				String s = "SELECT * FROM students";
				String s = "SELECT * FROM students ORDER BY std_id";
				print_query(statement, s);
				break;
			case 5:
				System.out.println("please enter a student id that you search about");
				id = obj.nextInt();
				if (search(statement, id)) {
					statement.executeUpdate("DELETE FROM students WHERE std_id = " + id);

					System.out.println("delete was successfully");
				}
				break;
			case 0:
				obj.close();
				scanner.close();
				connection.close();
				System.out.println("Thank you for using this system!!!");
				break outer;
			default:
				System.out.println("Please enter number in range of 0-5");
			}
		}
	}
}

