<%@ page import="java.sql.*"%>
<%
	String userName = request.getParameter("user_name");
	String password = request.getParameter("password");

	Class.forName("com.mysql.jdbc.Driver");
	Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/MikeGames","root","pumkin");

	Statement statement = connect.createStatement();

	ResultSet result;
	result = statement
			.executeQuery("SELECT * FROM members WHERE user_name = '"
					+ userName + "' AND password = '" + password + "'");

	if (result.next()) {
		session.setAttribute("userName", userName);
		response.sendRedirect("index.jsp");
	} else {
		out.println("Invalid password");
		//out.println("Invalid password <a href='login.jsp'>try again</a>");
	}
%>