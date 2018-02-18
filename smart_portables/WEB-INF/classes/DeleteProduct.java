import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DeleteProduct extends HttpServlet {
    
    protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
        HttpSession session=request.getSession(true);
        LoginUtil loginUtil = new LoginUtil(request);
        
        String username = "";
        if(loginUtil.isLoggedin() && session.getAttribute("user")!= null){                
               User user = (User)session.getAttribute("user");
               username = user.getUsername(); 
               String prodId = request.getParameter("pid");
               
               ProductUtil prodUtil = new ProductUtil(request);
               prodUtil.deleteProduct(prodId);
               
               response.sendRedirect("RetailerAccount");
        }else{
            session.setAttribute("missing", "Please login before deleting any order.");
            response.sendRedirect("Login");
        }		
	}

}