import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class Logout extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LoginUtil loginUtil = new LoginUtil(request);
		loginUtil.logout();
		response.sendRedirect("Home");
	}

}
