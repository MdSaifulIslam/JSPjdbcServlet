package org.java.jdbc;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil studentDbUtil = null;

	@Resource(name = "jdbc/web_student_traker")
	DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response){

		String theCommand = request.getParameter("command");
		
		if(theCommand == null) {
			theCommand = "LIST";
		}
		
		switch (theCommand) {
		case "LIST": 
			try {
				listStudent(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "ADD":
			try {
				addStudent(request, response);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "LOAD":
			try {
				loadStudent(request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		case "UPDATE":
			try {
				updateStudent(request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		case "DELETE":
			try {
				deleteStudent(request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		default:
			try {
				listStudent(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String studentID = request.getParameter("ID");
		
		studentDbUtil.deleteStudent(studentID);
		listStudent(request, response);
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String ids = request.getParameter("studentId");
		int id = Integer.parseInt(ids);
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student theStudent = new Student(id, firstName, lastName, email);
		
		studentDbUtil.updateStudent(theStudent);
		
		listStudent(request, response);
		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String theStudentId = request.getParameter("ID");
		Student theStudent = studentDbUtil.getStudent(theStudentId);
		
		request.setAttribute("THE_STUDENT", theStudent);
		
		RequestDispatcher dispatch = 
				request.getRequestDispatcher("/update-student-form.jsp");
		
		dispatch.forward(request, response);
		
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student tempStudent = new Student(firstName, lastName, email);
		
		studentDbUtil.addStudent(tempStudent);
		listStudent(request, response);
		
	}

	private void listStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Student> students = studentDbUtil.getStudents();

		request.setAttribute("STUDENT_LIST", students);

		RequestDispatcher dispatch = request.getRequestDispatcher("/list_students.jsp");
		dispatch.forward(request, response);

	}

}
