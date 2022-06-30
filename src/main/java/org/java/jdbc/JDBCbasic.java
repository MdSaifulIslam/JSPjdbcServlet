package org.java.jdbc;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;


/**
 * Servlet implementation class JDBCbasic
 */
public class JDBCbasic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(name ="jdbc/web_student_traker") 
	DataSource dataSource;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		Connection conn = null;
		Statement myStmnt = null;
		ResultSet res = null;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "select * from student";
			myStmnt = conn.createStatement();
			res = myStmnt.executeQuery(sql);
			
			while (res.next()) {
				String email = res.getString("email");
				out.println(email);
			}
			
		} catch (Exception e) {
			out.print(e);
		}
		
	}

}
