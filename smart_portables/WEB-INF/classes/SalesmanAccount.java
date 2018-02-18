import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SalesmanAccount extends HttpServlet {	

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
        displayCustomerAcc(request, response, out);
		
    }
    
    protected void displayCustomerAcc(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws ServletException, IOException {
          
            Utility utility = new Utility(request,out); 
            HttpSession session = request.getSession(true); 
            CartUtil cartUtil = new CartUtil(request);
          
            utility.printHtml("header.html");
         
            if(session.getAttribute("msg")!= null){
                out.print("<h4 style='color:red'>"+session.getAttribute("msg")+"</h4>");
                session.removeAttribute("msg");
		    }
         
            if(session.getAttribute("login_msg")!= null){
                out.print("<h4 style='color:red'>"+session.getAttribute("login_msg")+"</h4>");
                session.removeAttribute("login_msg");
		    }
            //******************************************************************
            out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'>Create User </h2><hr style='width: 95%'>"); 
            out.print("<form method='post' action='SignUp'>"
				+ "<table id='bestseller' style='width:70%'>"
                + "<tr><td><h3>Username</h3></td><td><input type='text' name='username' required></td></tr>"
                      
				+ "<tr><td><h3>Email address</h3></td><td><input type='text' name='email' required></td></tr>"

				+ "<tr><td><h3>Password</h3></td><td><input type='password' name='password' required></td></tr>"

                + "<tr><td><input type='hidden' name='role' value='customer'></td><td></td></tr>" 
                + "<td><input type='submit' class='btn1' name='signUp' value='Create User Account' style='float: none;height: 20px margin: 20px; margin-right: 10px;'></td>"     
		
				+ "</table>"
				
				+ "</form>");
        
        
             //******************************************************************
            out.print("<hr style='width: 95%'><h2 style='font-size: 35px;'>Customers Orders</h2><hr style='width: 95%'>");
            if(session.getAttribute("custUOrder")!= null){
                out.print("<h4 style='color:red'>"+session.getAttribute("custUOrder")+"</h4>");
                session.removeAttribute("custUOrder");
		    } 
            if(session.getAttribute("error_msg")!=null){
				out.print("<h4 style='color:red'>"+session.getAttribute("error_msg")+"</h4>");
				session.removeAttribute("error_msg");
			}
        
            boolean empty = true;
            out.print("<div><table id='bestseller' width='80%' >");
         
            out.println("<tr><td><b>Order #No.</b></td><td><b>UserName</b></td><td><b>Order Date</b></td><td><b>Delivery Date</b></td><td><b>Address 1</b></td><td><b>Address 2</b></td><td><b>ZipCode</b></td><td><b>Total Price</b></td><td><b>Action</b></td></tr>");
        
            for (OrderDetails od : cartUtil.getAllCustomersOrders()) {

                            out.println("<form method='post'><tr>");
                
							out.println("<input type='hidden' name='username' value='"+od.getUsername()+"'>");  
                
							out.println("<input type='hidden' name='ordernum' value='"+od.getOid()+"'>");
                            out.println("<input type='hidden' name='orderId' value='"+od.getOid()+"'>");
                            out.println("<input type='hidden' name='deliverydate' value='"+od.getDeliveryDate()+"'>");
                
							out.println("<input type='hidden' name='dlvDate' value='"+od.getDeliveryDate()+"'>");
                            
                            out.println("<td>"+od.getOid()+"</td>");
							out.println("<td>"+od.getUsername()+"</td>");
                            out.println("<td>"+od.getDate()+"</td>");
                            out.println("<td>"+od.getDeliveryDate()+"</td>");                         
                
                            out.println("<td><input type='text' name='address1' value='"+od.getAddress1()+"'></td>");
                            out.println("<td><input type='text' name='address2' value='"+od.getAddress2()+"'></td>");          
							
							out.println("<td><input type='text' name='zipcode' value='"+od.getZipcode()+"'</td>");							
							
							out.println("<td><input type='text' name='total' value=' "+od.getTotal()+"' style='width:40px;' readonly></td>");
                
							out.println("<td><input type='submit' class='btn1' name='delete' value='Delete' onclick='form.action=\"UpdateCOrder\";'><input type='submit' class='btn1' name='update' value='Update' onclick='form.action=\"UpdateOrder\";'></td></tr></form>");
							empty = false;
				}        
        
             out.print("</table></div>"); 
             if (empty) {
			     out.print("<h4 style='color:red'>No orders are found</h4>");
		      } 
        
             //******************************************************************
            if(session.getAttribute("message")!=null){
				out.print("<h4 style='color:red'>"+session.getAttribute("message")+"</h4>");
				session.removeAttribute("message");
			}
            out.print("<hr style='width: 95%'><h2 style='font-size: 35px;'>Add Customer Orders </h2><hr style='width: 95%'>");
            out.print("<div><form method='post' action='CheckOut'><table id='bestseller' width='80%'>");
            out.println("<tr><th>UserName</th><th>Fullname</th><th>Credit Card.</th><th>CVV</th><th>Address1</th><th>Address2</th><th>Zip Code</th><th>Price</th><th>Action</th></tr>");
		
            out.println("<td><input type='text' name='username' value=''</td>");
            out.println("<td><input type='text' name='fullname' value=''</td>");
            out.println("<td><input type='text' name='creditcard' value=''</td>");
            out.println("<td><input type='text' name='cvv' value=''</td>");
            out.println("<td><input type='text' name='address1' value=''</td>");
            out.println("<td><input type='text' name='address2' value=''</td>");
            out.println("<td><input type='text' name='zipcode' value=''</td>");           
            out.println("<td><input type='text' name='total' value=''</td>");
            out.println("<td><input type='hidden' name='option' value='add'><input type='submit' class='btn1' name='add' value='Add Order'</td>");

            out.print("</table></form></div>"); 

            out.println("</form></table>");        
        
            utility.printHtml("left-nav.html");
		    utility.printHtml("footer.html");
    }
}