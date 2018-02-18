import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UpdateOrder extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
		response.setContentType("text/html");
        PrintWriter out = response.getWriter(); 
        HttpSession session = request.getSession(true);
        CartUtil cartUtil = new CartUtil(request);
        MySQLDataStoreUtilities sqlUtil = new MySQLDataStoreUtilities(); 
        
        String userName = request.getParameter("username");
        String orderNum = request.getParameter("ordernum");
		String zipcode = request.getParameter("zipcode");
		String address1 = request.getParameter("address1");
		String address2 = request.getParameter("address2");

        //Update in database
        sqlUtil.updateOrders(address1,address2,zipcode,orderNum);

		session.setAttribute("custUOrder", "Order "+orderNum+" has been Updated !");
        response.sendRedirect("SalesmanAccount");			

	}

}
