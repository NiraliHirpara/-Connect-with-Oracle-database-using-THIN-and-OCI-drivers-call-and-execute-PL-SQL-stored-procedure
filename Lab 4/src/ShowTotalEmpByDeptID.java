import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

/*
 	           Author Name : Nirali Hirpara
     Professor's full name : Prof. Tevin Apenteng
	Assignment Description : Lab 4 - Call stored procedure with Oracle Database
         Class Description : Connection to Oracle Database using Oracle Thin driver
           Submission Date : 30 Jan 2019
*/

public class ShowTotalEmpByDeptID {

    // Database credentials are moved to the db.properties 

	public static void main(String[] argv) {

		try {
			callOracleStoredProcOUTParameter();

		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}
	}

	private static void callOracleStoredProcOUTParameter() throws SQLException {

		Connection dbConnection = null;
		CallableStatement callableStatement = null;

		//Constructing the String to call the Store Procedure with 4 Parameter Place Holders 
		String procSql = "{call total_emp_by_dept_id(?,?)}";

		//accept a user input of department ID
		System.out.print("Please enter a department ID>> ");
		Scanner keyboard = new Scanner( System.in );
		String userInput = keyboard.nextLine();
		keyboard.close();
		
		try {
			System.out.println("Connecting to database...");
			dbConnection = getDBConnection();
			callableStatement = dbConnection.prepareCall(procSql);

			callableStatement.setInt(1, Integer.parseInt(userInput));
			callableStatement.registerOutParameter(2, java.sql.Types.NUMERIC);
			
			// execute getEMPLOYEEByEmployeeId store procedure
			callableStatement.executeUpdate();

            int count = callableStatement.getInt(2);
            
			System.out.println("Total number of employees in department " + userInput + " is: " + count);

		} catch (NumberFormatException ne) {
			System.out.println("Please enter an integer number");
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
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
					  props.getProperty("DB_THIN_URL")
					, props.getProperty("DB_USERNAME")
					, props.getProperty("DB_PASSWORD"));
			
		} catch (IOException | ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}
}