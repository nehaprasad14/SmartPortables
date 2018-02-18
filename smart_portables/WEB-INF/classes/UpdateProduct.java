import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UpdateProduct extends HttpServlet {
    
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
               String pname = request.getParameter("pname");
               int pprice = Integer.parseInt(request.getParameter("pprice"));                  
               String pbrand = request.getParameter("pbrand");
               String pdiscount = request.getParameter("pdiscount");
            
               ProductUtil prodUtil = new ProductUtil(request);
               
               prodUtil.updateProduct(prodId,pname,pprice,pbrand,pdiscount);
               
               response.sendRedirect("RetailerAccount");
        }else{
            session.setAttribute("missing", "Please login before deleting any order.");
            response.sendRedirect("Login");
        }		
	}

}