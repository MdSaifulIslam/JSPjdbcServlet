package org.java.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;


public class StudentDbUtil {
	private DataSource dataSource;

	public StudentDbUtil(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmnt = null;
		ResultSet myRes = null;
		
		try {
			myConn = dataSource.getConnection();
			
			String sql = "select * from student";
			myStmnt = myConn.createStatement();
			
			myRes = myStmnt.executeQuery(sql);
			
			while (myRes.next()) {
				int id = myRes.getInt("id");
				String firstName = myRes.getString("first_Name");
				String lastName = myRes.getString("last_Name");
				String email = myRes.getString("email");
				
				Student tempStudent = new Student(id, firstName, lastName, email);
				
				students.add(tempStudent);
			}
			return students;
		} finally {
			close(myConn, myStmnt, myRes);
		}
		
		
	}

	private void close(Connection myConn, Statement myStmnt, ResultSet myRes) {
		try {
			if(myRes != null) {
				myRes.close();
			}
			if(myStmnt != null) {
				myStmnt.close();
			}
			if(myConn != null) {
				myConn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void addStudent(Student tempStudent) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmnt = null;
		
		try {
			myConn = dataSource.getConnection();
			String sql = "insert into student "
					+ " (first_name, last_name, email) "
					+ "values (?, ?, ?)";
			myStmnt = myConn.prepareStatement(sql);
			
			myStmnt.setString(1, tempStudent.getFirstName());
			myStmnt.setString(2, tempStudent.getLastName());
			myStmnt.setString(3, tempStudent.getEmail());
			
			myStmnt.execute();
			
		} finally {
			close(myConn, myStmnt, null);
		}
		
	}

	public Student getStudent(String theStudentId) throws Exception {
		Student theStudent = null;
		
		Connection myCon = null;
		PreparedStatement myStmnt = null;
		ResultSet myRes = null;
		int studentId;
		
		try {
			studentId = Integer.parseInt(theStudentId);
			
			myCon = dataSource.getConnection();
			String sql = "Select * from student where id = ?";
			
			myStmnt = myCon.prepareStatement(sql);
			myStmnt.setInt(1, studentId);
			
			myRes = myStmnt.executeQuery();
			if(myRes.next()) {
				String firstName = myRes.getString("first_name");
				String lastName = myRes.getString("last_name");
				String email = myRes.getString("email");
				
				theStudent = new Student(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find id: " + theStudentId);
			}
			
			return theStudent;
			
		} finally {
			close(myCon, myStmnt, myRes);
		}
		
	}

	public void updateStudent(Student theStudent) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmnt = null;
		
		try {
			myConn = dataSource.getConnection();
			String sql = "update student "
					+ " set first_name=?, last_name=?, email=? "
					+ "where id = ?";
			myStmnt = myConn.prepareStatement(sql);
			
			myStmnt.setString(1, theStudent.getFirstName());
			myStmnt.setString(2, theStudent.getLastName());
			myStmnt.setString(3, theStudent.getEmail());
			myStmnt.setInt(4, theStudent.getId());
			
			myStmnt.execute();
			
		} finally {
			close(myConn, myStmnt, null);
		}
		
	}

	public void deleteStudent(String studentID) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmnt = null;
		int theStudentId = Integer.parseInt(studentID);
		
		try {
			myConn = dataSource.getConnection();
			String sql = "delete from student "
					+ "where id = ?";
			myStmnt = myConn.prepareStatement(sql);
			
			myStmnt.setInt(1, theStudentId);
			
			myStmnt.execute();
			
		} finally {
			close(myConn, myStmnt, null);
		}
		
	}
	
	
}
