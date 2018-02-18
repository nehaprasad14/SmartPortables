import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DeleteOrder extends HttpServlet {
    
    protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
        HttpSession session=request.getSession(true);
        LoginUtil loginUtil = new LoginUtil(request);
        CartUtil cartUtil = new CartUtil(request);
        
        String username = "";
        if(loginUtil.isLoggedin() && session.getAttribute("user")!= null){                
               User user = (User)session.getAttribute("user");
               username = user.getUsername(); 
               
               String orderId = request.getParameter("orderId");
               String dlvDate = request.getParameter("dlvDate");
               
               long chkDate = cartUtil.chkCancelDate(dlvDate);
               
               if(chkDate > 5){
                    cartUtil.deleteCustomerOrder(username,orderId);
               }else{
                  session.setAttribute("dateError", "Order can be cancelled only 5 business days before the delivery date.  Order is already shipped.");
               }
               response.sendRedirect("Account");
        }else{
            session.setAttribute("missing", "Please login before deleting any order.");
            response.sendRedirect("Login");
        }		
	}

}