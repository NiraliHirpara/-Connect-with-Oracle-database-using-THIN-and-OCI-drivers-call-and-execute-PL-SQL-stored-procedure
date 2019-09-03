import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.sql.PreparedStatement;

/*
 	           Author Name : Nirali Hirpara
     Professor's full name : Prof. Tevin Apenteng
	Assignment Description : Lab 4 - Call stored procedure with Oracle Database
         Class Description : Connection to Oracle Database using Oracle OCI driver
           Submission Date : 30 Jan 2019
*/

public class ShowEmpInfoByID {

    // Database credentials are moved to the db.properties 
	public static void main(String args[]) {

		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;

		//accept a user input of department ID
		System.out.print("Please enter an employee ID>> ");
		Scanner keyboard = new Scanner( System.in );
		String userInput = keyboard.nextLine();
		keyboard.close();

		try {
			// Establish the connection.
			connection = getDBConnection();
			System.out.println("Connected database successfully...");
			
			//Create a prepared statement
			System.out.println("Creating a prepared statement...");
			String query = "SELECT FIRST_NAME, LAST_NAME, EMAIL, SALARY FROM EMPLOYEES WHERE EMPLOYEE_ID = ?"; 
			pStatement = connection.prepareStatement(query);
			
			// set up query parameters
			pStatement.setInt(1, Integer.parseInt(userInput));
			
			// Execute a query
			resultSet = pStatement.executeQuery();
			
			// process ResultSet
			System.out.println("\nEmployee Name\tEmail\t\tSalary");
			System.out.println("=============\t=====\t\t======");
			while (resultSet.next()) {
				System.out.print(resultSet.getString("First_Name").trim() + " ");
				System.out.print(resultSet.getString("Last_Name").trim() + "\t");
				System.out.print(resultSet.getString("Email").trim() + "\t\t");
				System.out.println(resultSet.getDouble("Salary") + "\t");
			}
	     
		}
		// Catch the exceptions.
		 catch (NumberFormatException ne) {
			System.out.println("Please enter an integer number");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Exception: " + e.getMessage());
		}

		// Close the connection in finally block.
		finally {
			try {
				if (resultSet != null)
					resultSet.close();
				
				if (pStatement != null)
					pStatement.close();

				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}
	}
	
	private static Connection getDBConnection() {
		Properties props = new Properties();
		FileInputStream fis = null;
		Connection dbConnection = null;

		try {
			fis = new FileInputStream("db.properties");
			props.load(fis);

			Class.forName(props.getProperty("DB_DRIVER_CLASS"));

			dbConnection = DriverManager.getConnection(
					  props.getProperty("DB_OCI_URL")
					, props.getProperty("DB_USERNAME")
					, props.getProperty("DB_PASSWORD"));
			
		} catch (IOException | ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}

		return dbConnection;
	}

}
