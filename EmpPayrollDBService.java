package jdbc.com.jdbcjdbc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.PreparedStatement;

class EmployeePayroll {
	int id;
	String Name;
	String gender;
	int PhoneNumber;
	String Dep;

	EmployeePayroll() {

	}

	EmployeePayroll(int id, String name, String gender, int PhoneNumber, String Dep) {
		this.id = id;
		this.Name = name;
		this.gender = gender;
		this.PhoneNumber = PhoneNumber;
		this.Dep = Dep;
	}

	void printEmployeeDetails() {
		System.out.println("Id: " + this.id + " Name: " + this.Name + " gender: " + this.gender + " Phone Number: "
				+ this.PhoneNumber + " Department: " + this.Dep);
	}
}

public class EmpPayrollDBService {

	public static void main(String[] args) {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_services";
		String UserName = "root";
		String password = "root";
		Connection connection;
		try {
			// register driver with the jdbc driver
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in th classpath!", e);
		}

		listDrivers();

		try {
			// Get a connection to database
			System.out.println("Connecting to database:" + jdbcURL);
			connection = (Connection) DriverManager.getConnection(jdbcURL, UserName, password);
			System.out.println("Connection is successful!: " + connection);
			Statement mystatement = (Statement) connection.createStatement();
			/*
			 * update the salary of Employee Terisa to 3000000.00
			 */
			Integer query = mystatement.executeUpdate("update employee_payroll set salary= '300000' where nmae = 'Terisa'");
			
			PreparedStatement preparedst = (PreparedStatement)connection.prepareStatement(query);
			int recordupdate = preparedst.executeUpdate();
			System.out.println("Records updated " +recordupdate);
			ResultSet result = mystatement.executeQuery("Select * from employee_payroll");
			// Store EmployeeDetails with use of linkedlist
			List<EmployeePayroll> employeeList = new LinkedList<EmployeePayroll>();
			while (result.next()) {
				EmployeePayroll employee = new EmployeePayroll(result.getInt(1), result.getString(2),
						result.getString(3), result.getInt(4), result.getString(5));
				employeeList.add(employee);
			}
			for (EmployeePayroll employee : employeeList) {
				employee.printEmployeeDetails();
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void listDrivers() {
		// getting all drivers in driver manager
		Enumeration<java.sql.Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			System.out.println(" " + driverClass.getClass().getName());

		}

	}
}
