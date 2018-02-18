import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Account extends HttpServlet {	

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
        HttpSession session=request.getSession(true);
        LoginUtil loginUtil = new LoginUtil(request);
        
		if (!loginUtil.isLoggedin()) {
			response.sendRedirect("Login");
			return;
		}

		if(session.getAttribute("user")!= null){                
               User user = (User)session.getAttribute("user");                
               String userRole = user.getRole();
               userRole.trim();
            
               if (userRole.equals("customer")) {
			         displayCustomer(request, response);
		       }else if (userRole.equals("retailer")) {
			         response.sendRedirect("RetailerAccount");
		       }else if (userRole.equals("salesman")) {
                     response.sendRedirect("SalesmanAccount");
               }
         }  
      }
    
      protected void displayCustomer(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
         
            PrintWriter out = response.getWriter();  
            Utility utility = new Utility(request,out); 
            CartUtil cartUtil = new CartUtil(request);
          
            utility.printHtml("header.html");
            out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Your Orders </h2><hr style='width: 95%'>");  
          
            HttpSession session = request.getSession(true);
        
            if(session.getAttribute("dateError")!= null){
                out.print("<h4 style='color:red'>"+session.getAttribute("dateError")+"</h4>");
                session.removeAttribute("dateError");
            }
          
            if(cartUtil.getOrderCount() > 0){                
                out.println("<div><table id='bestseller'>");
			    out.println("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td><b>Order #No.</b></td><td><b>Order Date</b></td><td><b>Delivery Date</b></td><td><b>Shipping Address</b></td><td><b>Total Items</b></td><td><b>Total Price</b></td><td></td></tr>");                
                for (OrderDetails od : cartUtil.getOrderList()) {
                    out.println("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>"+od.getOid()+"</td><td>"+od.getDate()+"</td><td>"+od.getDeliveryDate()+"</td><td>"+od.getAddress1()+", "+od.getAddress2()+"</td><td>"+od.getItemscount()+"</td><td> $"+od.getTotal()+"</td><td><form method='post' action='DeleteOrder'><input type='hidden' name='orderId' value='"+od.getOid()+"'><input type='hidden' name='dlvDate' value='"+od.getDeliveryDate()+"'><input type='submit' class='btn1' value='Cancel Order'></form></td></tr>");
                }
                out.print("</table></div>");
            }else{
                out.print("<div><table id='bestseller'><tr><td><h4 style='color:red'>You have no order. Please add item to the cart and place order.</h4></td></tr></table></div>");
            }
        
            utility.printHtml("left-nav.html");
		    utility.printHtml("footer.html");
        }
    
}
    