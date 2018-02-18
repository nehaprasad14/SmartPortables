import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class CheckOut extends HttpServlet {

      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            Utility utility = new Utility(request,out);
            CartUtil cartUtil = new CartUtil(request);

    		    utility.printHtml("header.html");

            out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Order Details</h2><hr style='width: 95%'>");

            if(cartUtil.CartCount() > 0){
                int i = 1;
                int total = 0;
                out.print("<div><table id='bestseller'><tr><td><b>No.</b></td><td><b>Item Name</b></td><td><b>Item Category</b></td><td><b>Brand</b></td><td><b>Item Price</b></td><td><b>Delete Item</b></td></tr>");
                for (OrderItem oi : cartUtil.getCustomerOrders()) {
                    out.print("<tr><td>"+i+".</td><td>"+oi.getName()+"</td><td>"+oi.getCategory()+"</td><td>"+oi.getBrand()+"</td><td> "+oi.getPrice()+"</td><td><form method='post' action='Cart'><form method='post' action='Cart'><input type='hidden' name='itemid' value='"+oi.getId()+"'><input type='hidden' name='opt' value='delete'><input type='submit' class='btn1' value='Delete'></form></td></tr>");
                    total = total +oi.getPrice();
                    i++;
                }
                out.print("<tr><td></td><td></td><td></td><td><b>Total</b></td><td><b>"+total+"</b></td>");
                out.print("<tr><td></td><td></td><td></td><td></td><td></td></tr>");
                out.print("</table></div>");
		    }

            out.print("<br><hr style='width: 95%'><h2 style='font-size: 35px;'>Please Enter Your Billing Details</h2><hr style='width: 95%'>");

            out.print("<div><table id='bestseller'><form action='CheckOut' method='post'>"

            + "<tr><td>&nbsp;</td><td></td><td>Full Name</td><td><input type='text' name='fullname' required='required'/></td></tr>"
            + "<tr><td>&nbsp;</td><td></td><td>Credit Card Number</td><td><input type='text' name='creditcard' required=required'/></td></tr>"
            + "<tr><td>&nbsp;</td><td></td><td>CVV</td><td><input type='text' name='cvv' required='required'/></td></tr>"
            + "<tr><td>&nbsp;</td><td></td><td>Address Line 1</td><td><input type='text' name='address1' required='required'/></td></tr>"
            + "<tr><td>&nbsp;</td><td></td><td>Address Line 2</td><td><input type='text' name='address2' required='required'/></td></tr>"
            + "<tr><td>&nbsp;</td><td></td><td>Zipcode</td><td><input type='text' name='zipcode' required='required'/></td></tr>"
            + "<tr><td>&nbsp;</td><td><input type='hidden' name='option' value='add'></td><td>Total Amount</td><td><input type='number' name='total' readonly='readonly' value='"+cartUtil.getCartTotal()+"'/></td></tr>"
            + "<tr><td>&nbsp;</td><td></td><td><input type='submit' class='btn1' value='Place Order'></td><td>&nbsp;</td><td></td></tr>"
            );

            out.print("</form></table></div>");

            utility.printHtml("left-nav.html");
		    utility.printHtml("footer.html");

        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                HttpSession session=request.getSession(true);
                Utility utility = new Utility(request,out);
                CartUtil cartUtil = new CartUtil(request);
                LoginUtil loginUtil = new LoginUtil(request);

                String username = "";
                if(loginUtil.isLoggedin() && session.getAttribute("user")!= null){
                       User user = (User)session.getAttribute("user");
                       username = user.getUsername();

                       String option = request.getParameter("option");
                       option.trim();
                       String oid = "";

                       if(option!=null && option.equals("add")){
                            String uname = request.getParameter("username");
                            String fullname = request.getParameter("fullname");
                            String creditcard = request.getParameter("creditcard");
                            int cvv = Integer.parseInt(request.getParameter("cvv"));
                            String address1 = request.getParameter("address1");
                            String address2 = request.getParameter("address2");
                            String zipcode = request.getParameter("zipcode");
                            int total = Integer.parseInt(request.getParameter("total"));

                            String button = request.getParameter("add");
                            if(button == null)
                                  button = "CheckOut";

                            if(button.equalsIgnoreCase("Add Order")){
                               oid = cartUtil.addCartToOrder(fullname, creditcard, cvv, address1,address2,zipcode,total,uname);
                               session.setAttribute("message", "Order added successfully !");
                               response.sendRedirect("SalesmanAccount");
                               return;
                            }else{
                               System.out.println("Place Order...");
                               oid = cartUtil.addCartToOrder(fullname, creditcard, cvv, address1,address2,zipcode,total,username);
                            }
                       }


                       displayOrder(request, response, out, oid);
                }else{
                    session.setAttribute("missing", "Please login before placing order.");
                    response.sendRedirect("Login");
                }
        }

        protected void displayOrder(HttpServletRequest request, HttpServletResponse response, PrintWriter out, String oid) throws ServletException, IOException {

            Utility utility = new Utility(request,out);
            LoginUtil loginUtil = new LoginUtil(request);
            CartUtil cartUtil = new CartUtil(request);

            if(!loginUtil.isLoggedin()){
                HttpSession session = request.getSession(true);
                session.setAttribute("missing", "Please login before placing order.");
                response.sendRedirect("Login");
                return;
            }

            utility.printHtml("header.html");
            out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'>Order Placed Successfully. </h2><hr style='width: 95%'>");

            if(cartUtil.getOrderCount() > 0){

                out.print("<div><table id='bestseller' width='80%'>");
                out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td style='font-size: 20px;'><b>Confirmation Number: </b></td><td style='font-size: 20px;'><b>"+oid+"</b></td></tr>");
                out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td style='font-size: 20px;'><b>Estimated delivery date: </b></td><td style='font-size: 20px;'><b>"+cartUtil.getEstimatedDeliveryDate()+"</b></td></tr>");
                out.print("</table><hr style='width: 95%'></div>");
            }else{
                out.print("<div><h4 style='color:red'>Your Cart is empty. Please add item to the cart.</h4></div>");
            }

            utility.printHtml("left-nav.html");
            utility.printHtml("footer.html");

    }

}
