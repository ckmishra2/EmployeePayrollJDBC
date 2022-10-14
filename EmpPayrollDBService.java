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
import com.mysql.jdbc.Statement;

class Employee{
	int id;
	String Name;
	String gender;
	int PhNum;
	String Dep;
	Employee(){
		
	}
	Employee(int id, String name,String gender,	int PhNum,String Dep)
	{
		this.id = id;
		this.Name = name;
		this.gender = gender;
		this.PhNum = PhNum;
		this.Dep = Dep;
	}
	void printEmployeeDetails()
	{
		System.out.println("Id: "+this.id+" Name: "+this.Name+" gender: "+this.gender+
				" Phone Number: "+this.PhNum+" Department: "+this.Dep);
	}
}

public class EmpPayrollDBService {

	public static void main(String[] args) {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_services";
		String UserName = "root";
		String password = "root";
		Connection connection;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in th classpath!", e);
		}

		listDrivers();

		try {
			System.out.println("Connecting to database:" + jdbcURL);
			connection = (Connection) DriverManager.getConnection(jdbcURL, UserName, password);
			System.out.println("Connection is successful!: " + connection);
			Statement stm = (Statement) connection.createStatement();
			ResultSet result = stm.executeQuery("Select * from employee_payroll");

			
			List<Employee> employeeList = new LinkedList<Employee>();	
			while (result.next()) {				
				Employee employee = new Employee(result.getInt(1), result.getString(2), result.getString(3),
						result.getInt(4),result.getString(5));
				employeeList.add(employee);
			}
			for (Employee employee : employeeList) {
				employee.printEmployeeDetails();
			}
			
		
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void listDrivers() {
		Enumeration<java.sql.Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			System.out.println(" " + driverClass.getClass().getName());

		}

	}
}
