import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.text.*;

public class UpdateCOrder extends HttpServlet {

      protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            response.setContentType("text/html");
            PrintWriter out = response.getWriter(); 
            HttpSession session=request.getSession(true);        
            CartUtil cartUtil = new CartUtil(request);      

            String userName = request.getParameter("username");
            String orderNum = request.getParameter("ordernum");
            String deliveryDate = request.getParameter("deliverydate");   

            long chkDate = cartUtil.chkCancelDate(deliveryDate);

            if(chkDate > 5){
                cartUtil.deleteCustomerOrder(userName,orderNum);
            }else{
                      session.setAttribute("error_msg", "Order can be cancelled only 5 business days before the delivery date.  Order is already shipped.");
            }
            response.sendRedirect("SalesmanAccount"); 

      }
}
