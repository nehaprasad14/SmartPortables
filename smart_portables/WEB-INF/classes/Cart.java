import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class Cart extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
        HttpSession session=request.getSession(true);
        LoginUtil loginUtil = new LoginUtil(request);
        CartUtil cartUtil = new CartUtil(request);

        String username = "";
        if(loginUtil.isLoggedin() && session.getAttribute("user")!= null){
               User user = (User)session.getAttribute("user");
               username = user.getUsername();

               String opt = request.getParameter("opt");
               opt.trim();

               if(opt!=null && opt.equals("add")){
                    String cid = request.getParameter("cid");
                    String cname = request.getParameter("cname");
                    int cprice = Integer.parseInt(request.getParameter("cprice"));
                    String ccategory = request.getParameter("ccategory");
                    String cbrand = request.getParameter("cbrand");
                    cartUtil.addToCart(cid, cname, cprice, ccategory,cbrand,username);
               }

               if(opt!=null && opt.equals("delete")){
                    String itemid = request.getParameter("itemid");
                    cartUtil.deleteFromCart(username,itemid);
               }

               displayCart(request, response, out);
        }else{
            session.setAttribute("missing", "Please login before adding any item to cart.");
            response.sendRedirect("Login");
        }

	}


	protected void doGet(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
        LoginUtil loginUtil = new LoginUtil(request);
        HttpSession session = request.getSession(true);

        if(loginUtil.isLoggedin()){
		      displayCart(request, response, out);
        }else{
            session.setAttribute("missing", "Please login before adding any item to cart.");
            response.sendRedirect("Login");
        }
	}

    protected void displayCart(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException {

		Utility utility = new Utility(request,out);
        LoginUtil loginUtil = new LoginUtil(request);
        CartUtil cartUtil = new CartUtil(request);

		if(!loginUtil.isLoggedin()){
			HttpSession session = request.getSession(true);
			session.setAttribute("missing", "Please Login to add items to cart");
			response.sendRedirect("Login");
			return;
		}

        utility.printHtml("header.html");
        out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Cart( "+cartUtil.CartCount()+" )</h2><hr style='width: 95%'>");

        if(cartUtil.CartCount() > 0){
            int i = 1;
            int total = 0;
            out.print("<div><table id='bestseller'><tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td><b>No.</b></td><td><b>Item Name</b></td><td><b>Item Category</b></td><td><b>Brand</b></td><td><b>Item Price</b></td><td><b>Delete Item</b></td></tr>");

            for (OrderItem oi : cartUtil.getCustomerOrders()) {
                out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>"+i+".</td><td>"+oi.getName()+"</td><td>"+oi.getCategory()+"</td><td>"+oi.getBrand()+"</td><td> "+oi.getPrice()+"</td><td><form method='post' action='Cart'><input type='hidden' name='itemid' value='"+oi.getId()+"'><input type='hidden' name='opt' value='delete'><input type='submit' class='btn1' value='Delete'></form></td></tr>");
                total = total +oi.getPrice();
                i++;
            }
            out.print("<tr><td></td><td></td><td></td><td><b>Total</b></td><td><b>"+total+"</b></td>");
            out.print("<tr><td></td><td></td><td></td><td><a href='CheckOut' class='btn1'>Check Out</a></td><td></td></tr>");
            out.print("</table></div>");

            displayCartCarousel(request, response, out,cartUtil.getCustomerOrders());
		}else{
			out.print("<div><h4 style='color:red'>Your Cart is empty. Please add item to the cart.</h4></div>");
		}


        utility.printHtml("left-nav.html");
		utility.printHtml("footer.html");

    }

    protected void displayCartCarousel(HttpServletRequest request, HttpServletResponse response, PrintWriter out, ArrayList<OrderItem> oi) throws ServletException, IOException {

           OrderItem oitem = new OrderItem();
           String prodID = "0";

           if(oi.size() >= 1){
                   oitem = oi.get(oi.size()-1);
                   prodID = oitem.getId();

                   int k = 2;
                   while(Integer.parseInt(prodID) > 500){
                       if((oi.size()-k) >=0){
                           oitem = oi.get(oi.size()-k);
                           prodID = oitem.getId();
                           k++;
                       }else{
                           break;
                       }
                   }
           }

           SaxParserUtil saxParser = new SaxParserUtil();
           HashMap<String,Product> productList = saxParser.getProductCatalog();
           HashMap<String,Accessory> accList = saxParser.getAccessoryCatalog();

           String prodName = "";
           Product prod = null;

           prod = (Product)productList.get(prodID);

           int i=0;
           if(prod != null){
               prodName = prod.getName();
               List<String> accessories = prod.getAccessories();

               if(accessories != null){

           out.print("<div ><hr style='width: 95%'><h3 style='font-size: 35px;'>"+ prodName + " Accessories</h3><hr style='width: 95%'></div>");
           //*******************************************************

           out.print(" <div class='.container1' ");
              out.print("<div class='row'> ");
                out.print("<div class='col-md-12'>");
                  out.print("<div class='carousel carousel-showmanymoveone slide' id='carousel123'>");
                    out.print("<div class='carousel-inner'>");


                            for(String accID:accessories){

                                Accessory acc = (Accessory)accList.get(accID.trim());
                                if(i==0)
                                    out.print("<div class='item active'>");
                                else
                                    out.print("<div class='item'>");

                                out.print("<div class='col-xs-12 col-sm-6 col-md-3' id='shop_item' class='img-responsive'>");

                                    out.print("<h3>"+acc.getName()+"</h3>" );
                                    out.print("<strong>"+acc.getPrice()+"$</strong>" );
                                    out.print("<ul>");
                                        out.print("<li id='item'><img src='"+acc.getImage()+"' width='60%' height='70%'/>");
                                        out.print("</li>");
                                        out.print("<li>" );
                                            out.print("<form method='post' action='Cart'>" );
                                            out.print("<input type='hidden' name='cid' value='"+acc.getId()+"'>");
                                            out.print("<input type='hidden' name='cname' value='"+acc.getName()+"'>");
                                            out.print("<input type='hidden' name='cprice' value='"+acc.getPrice()+"'>");
                                            out.print("<input type='hidden' name='ccategory' value='"+acc.getCategory()+"'>");
                                            out.print("<input type='hidden' name='cbrand' value='"+acc.getBrand()+"'>");
                                            out.print("<input type='hidden' name='opt' value='add'>" );
                                            out.print("<input type='submit' class='inpbtn' value='Buy Now'>" );
                                            out.print("</form>" );
                                        out.print("</li>" );
                                        out.print("<li>");
                                        out.print("<form method='get' action='WriteReview'><input type='hidden' name='pname' value='"+acc.getName()+"'><input type='hidden' name='price' value='"+acc.getPrice()+"'><input type='hidden' name='pcategory' value='"+acc.getCategory()+"'><input type='hidden' name='pbrand' value='"+acc.getBrand()+"'><input type='submit' class='inpbtn' value='Write Reviews'></form>");
                                        out.print("</li>");
                                        out.print("<li><form method='get' action='ViewReview'><input type='hidden' name='pname' value='"+acc.getName()+"'><input type='submit' class='inpbtn' value='View Reviews'></form>");
                                    out.print("</li></ul>" );
                                out.print("</div>");
                                out.print("</div>");
                              i++;
                            }

                   out.print("<a class='left carousel-control' href='#carousel123' data-slide='prev' style='opacity:1; '><i class='glyphicon glyphicon-chevron-left' style='color:#800000;'></i></a>");
                   out.print("<a class='right carousel-control' href='#carousel123' data-slide='next' style='opacity:1;'><i class='glyphicon glyphicon-chevron-right' style='color:#800000;'></i></a>");
                 out.print(" </div> ");
               out.print(" </div>");
             out.print(" </div> ");
        out.print("</div>");

                        }//end if
                    }
           //*******************************************************

           out.println("<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>");
           out.println("<script src='http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js'></script>");
           out.println("<script src='index.js'></script>");
        //}
    }


}
