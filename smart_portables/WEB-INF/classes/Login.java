import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.HashMap;
import java.sql.*;

public class Login extends HttpServlet {

    MySQLDataStoreUtilities sqlUtil = new MySQLDataStoreUtilities();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
    LoginUtil loginUtil = new LoginUtil(request);

    String username = request.getParameter("username");
		String password = request.getParameter("password");
		String role = request.getParameter("role");

        boolean isValid = loginUtil.validation(username, password);
        User user = null;

        if(isValid){
            try{
                user = sqlUtil.getUser(username);
            }catch(NullPointerException e){
				 e.printStackTrace();
            }
            catch(ClassNotFoundException e){
				e.printStackTrace();
            }
            catch (SQLException e){
				 e.printStackTrace();
            }

            if(user!=null){
                String userPassword = user.getPassword();
                String userRole = user.getRole();

                if (password.equals(userPassword) && role.equals(userRole)) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("logged", true);
                    session.setAttribute("user",user);
                    session.setAttribute("username",username);
                    response.sendRedirect("Home");
                    return;
                }else{
                   showLoginForm(request, response, out, true);
                }
            }else{
                  showLoginForm(request, response, out, true);
            }
        }else{
            response.sendRedirect("Login");
        }
	}

	protected void doGet(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
        LoginUtil loginUtil = new LoginUtil(request);

        if(!loginUtil.isLoggedin())
		      showLoginForm(request, response, out, false);
        else
            response.sendRedirect("Home");
	}

	protected void showLoginForm(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, boolean error)
			throws ServletException, IOException {

        Utility utility = new Utility(request,out);
		utility.printHtml("header.html");

		out.print("<h2>Sign in</h2>");
		if (error)
			out.print("<h4 style='color:red'>Please check your username, password and role </h4>");

		HttpSession session = request.getSession(true);

		if(session.getAttribute("missing")!= null){
			out.print("<h4 style='color:red'>"+session.getAttribute("missing")+"</h4>");
			session.removeAttribute("missing");
		}
        if(session.getAttribute("login_msg")!= null){
			out.print("<h4 style='color:red'>"+session.getAttribute("login_msg")+"</h4>");
			session.removeAttribute("login_msg");
		}
		out.print("<form method='post' action='Login'>"
				+ "<table id='bestseller' width='80%'>"
				+ "<tr><td><h3>Username</h3></td><td><input type='text' name='username' required></td></tr>"

				+ "<tr><td><h3>Password</h3></td><td><input type='password' name='password' required></td></tr>"

				+ "<tr><td><h3>Role</h3></td><td>"
                  +"<select name='role'>"
                  +"<option value='customer' selected>Customer</option>"
                  +"<option value='retailer'>Store Manager</option>"
                  +"<option value='salesman'>Salesman</option>"
                  +"</select>"
				+ "</td></tr>"

				+ "<tr><td></td><td><input type='submit' value='Login' style=''></td></tr>"

				+ "<tr><td></td><td><strong><a href='SignUp' style='float: right;height: 20px margin: 20px;'>New to SmartPortals? &nbsp; Sign up here!</a></strong></td></tr>"

				+ "</table>"
				+ "</form>");
        utility.printHtml("left-nav.html");
		utility.printHtml("footer.html");
	}


}
