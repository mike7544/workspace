<%@ page import="java.sql.*"%>
<%
	String userName = request.getParameter("user_name");
	String password = request.getParameter("password");
	String email = request.getParameter("email");
	
	Class.forName("com.mysql.jdbc.Driver");
	//Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/MikeGames","root","pumkin");
	Connection connect = DriverManager.getConnection("jdbc:mysql://127.10.12.130:3306/MikeGames","adminDAmUjhP","Z4A9yzKQr_X5");

	Statement statement = connect.createStatement();

	int result = statement
			.executeUpdate("INSERT INTO members(user_name,email,password,date) values('"
					+ userName
					+ "','"
					+ email
					+ "','"
					+ password
					+ "', CURDATE())");

	if (result > 0)
		response.sendRedirect("success.html");
	else
		response.sendRedirect("login.html");
%>