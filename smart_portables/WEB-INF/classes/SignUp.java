import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.HashMap;
import java.sql.*;

public class SignUp extends HttpServlet {
    
      private String error_msg;
      MySQLDataStoreUtilities sqlUtil = new MySQLDataStoreUtilities();    
    
      protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        LoginUtil loginUtil = new LoginUtil(request);
        HttpSession session = request.getSession(true);  

        String username = null;  
        String email = null;
        String password = null;
        String role = null;
        String error_msg = null;

        User user=null;
        int result = 0;  
        try{
                username = request.getParameter("username");
                email = request.getParameter("email");
                password = request.getParameter("password");
                role = request.getParameter("role");

                username.trim();
                email.trim();
                password.trim();
                role.trim();        

                user = sqlUtil.getUser(username);

                if(user != null){
                    error_msg = " Username already exist.";
                    System.out.println(error_msg);
                    session.setAttribute("login_msg",error_msg);
                    response.sendRedirect("SignUp");
                }else{
                    user = new User(username,email,password,role);
                    result = sqlUtil.addUser(user);
	
                    if(result != 0){
                        session.setAttribute("login_msg", "Username '"+username+"' account is created. Please login");

                        if(loginUtil.isLoggedin() == true){
                            session = request.getSession(true);
                            session.setAttribute("login_msg", "Customer "+username+" account has been created!");
                            response.sendRedirect("SalesmanAccount"); 
                            return;
                        }
                        if(!loginUtil.isLoggedin()){
                            response.sendRedirect("Login"); 
                            return;
                        }
                        else {
                            response.sendRedirect("Home");
                            return;
                        }
                    }
                }
            }catch(NullPointerException e){
				 e.printStackTrace();
            }
            catch(ClassNotFoundException e){
				e.printStackTrace();
            } 
            catch (SQLException e){
				 e.printStackTrace();
            }
      }

      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            showSignUpForm(request, response, out, false);
        }

      protected void showSignUpForm(HttpServletRequest request, HttpServletResponse response, PrintWriter out, boolean error)
                throws ServletException, IOException {

            Utility utility = new Utility(request,out);
                utility.printHtml("header.html");
                out.print("<h2>Create Account</h2>");

          HttpSession session = request.getSession(true);

            if(session.getAttribute("login_msg")!= null){
                out.print("<h4 style='color:red'>"+session.getAttribute("login_msg")+"</h4>");
                session.removeAttribute("login_msg");
            }

            if (error)
                out.print("<h4 style='color:red'>"+error_msg+"</h4>");
                out.print("<form method='post' action='SignUp'>"
                    + "<table id='bestseller' style='width:80%'>"
                    + "<tr><td><h3>Username</h3></td><td><input type='text' name='username' required></td></tr>"

                    + "<tr><td><h3>Email address</h3></td><td><input type='text' name='email' required></td></tr>"

                    + "<tr><td><h3>Password</h3></td><td><input type='password' name='password' required></td></tr>"

                    + "<tr><td><h3>Role</h3></td><td>"
                      +"<select name='role'>"
                      +"<option value='customer' selected>Customer</option>"
                      +"<option value='retailer'>Store Manager</option>"
                      +"<option value='salesman'>Salesman</option>"
                      +"</select>"
                    + "</td></tr>" 
                    + "<tr><td><input type='submit' name='signUp' value='Create User Account' style='float: none;height: 20px margin: 20px; margin-right: 10px;'></td></tr>"     

                    + "</table>"

                    + "</form>");
            utility.printHtml("left-nav.html");
            utility.printHtml("footer.html");
        }

}
