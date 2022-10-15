package jdbcsql;
import java.sql.*;
import java.util.*;

import com.mysql.jdbc.PreparedStatement;

public class JDBCTransaction {
	public static void main(String[] args) {
		Connection myconnection;
		Statement mystatement;
		
		try {
			//1.Get a connection to database
			myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_services" , "root","root");			
			//Turn aff auto commmit
			myconnection.setAutoCommit(false);
			//show all table data
			mystatement = myconnection.createStatement();
			
			ResultSet result = mystatement.executeQuery("SELECT id,name,salary,Department from employee_payroll");
			//add new employee in payroll
			ResultSet sql= mystatement.executeQuery("insert into employee_payroll (name,salary,gender, start ) VALUES ('Elon', '900000','M', '2020-11-12')");
			System.out.println(sql);
			while (result.next()) {
	            System.out.println("name: "+result.getString("name")+" Department: "+result.getString("Department")
	            +" salary: "+result.getInt("salary"));  
	        }
			//Show salaries BEFORE
			System.out.println("Salaries BEFORE\n");
			showSalaries(myconnection,"Computer Science");
			showSalaries(myconnection,"Mechanical Engneering");
			
			//Transaction step:1 select all  computer science employee
			result = mystatement.executeQuery("SELECT * from employee_payroll where Department = 'Computer Science'");
			while (result.next()) {
	            System.out.println("name: "+result.getString("name")+" id: "+result.getInt("id"));  
	        }
			
			//Transaction step2: retrive and update salaries for Information technology
			result = mystatement.executeQuery("SELECT name,salary from employee_payroll where Department = 'Information Technology'");
			System.out.println("count of rows: "+result.getFetchSize());
			while (result.next()) {
	            System.out.println("name: "+result.getString("name")+" salary: "+result.getInt("salary"));  
	        }
			
			mystatement.executeUpdate("update employee_payroll set salary= '4000' where Department = 'Information Technology'");
			result = mystatement.executeQuery("SELECT name,salary from employee_payroll where Department = 'Information Technology'");
			while (result.next()) {
	            System.out.println("name: "+result.getString("name")+" salary: "+result.getInt("salary"));  
	        }
			//Transaction step3: delete employee from database
			String query = "delete from employee_payroll where name=?";
			PreparedStatement preparedst = (PreparedStatement)myconnection.prepareStatement(query);
			preparedst.setString(1, "jaon");
			System.out.println("\n>> Transaction steps are ready.\n");
			
			//ask user if it is okay to save
			boolean ok = askUserIfOKToSave();			
			if (ok) {
				//store in database
				myconnection.commit();
				System.out.println("\n>> Transaction COMMITED.\n");
			}
			else {
				//discard
				myconnection.rollback();
				System.out.println("\n>> Transaction ROLLED BACK.\n");
			}
			//show salaries AFTER
			System.out.println("Salaries AFTER\n");
			showSalaries(myconnection,"Computer Science");
			showSalaries(myconnection,"Mechanical Enginnering");
		}
		catch (Exception exe) {
			exe.printStackTrace();
		}
		
		finally {
			
		}
	}

	private static boolean askUserIfOKToSave() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter 1 to commit transaction.");
		int input = sc.nextInt();
		if(input ==1)
			return true;
		else
			return false;
	}

	private static void showSalaries(Connection myconnection, String string) throws SQLException {
		Statement mystatement = myconnection.createStatement();
		String query = "SELECT name,salary from employee_payroll where Department = "+"'"+string+"'";
		ResultSet result  = mystatement.executeQuery(query);
		while (result.next()) {
            System.out.println("name: "+result.getString("name")+" salary: "+result.getInt("salary"));  
        }
	}

}
