import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AutoComplete extends HttpServlet {
      AjaxUtility aUtil = new AjaxUtility();

      public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
          String action = request.getParameter("action");
          String searchId = request.getParameter("id");
          try{
                StringBuffer sb = new StringBuffer();
                boolean namesAdded = false;
                if(action.equals("complete")){
                  if (!searchId.equals("")){
                      sb = aUtil.readData(searchId);
                      if(sb!=null || !sb.equals("")){
                        namesAdded=true;
                      }
                      if(namesAdded){
                        System.out.println("namesAdded : true");
                        response.setContentType("text/xml");
                        response.setHeader("Cache-Control", "no-cache");
                        response.getWriter().write("<products>" + sb.toString() + "</products>");
                      }
                }
              }
          }catch(Exception exception){
              exception.printStackTrace();
          }

          if (action.equals("lookup")) {
            String sId = request.getParameter("searchId");
            System.out.println("lookup:: "+ sId);
              if (sId != null) {
  				        request.setAttribute("productID", sId);
                  doPost(request,response);
              }
          }
       }

        public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            Utility utility = new Utility(request,out);

            String productID = (String) request.getAttribute("productID");

            utility.printHtml("header.html");
            out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Product Searched </h2><hr style='width: 95%'><div>");
            HashMap<String,Product> productList = aUtil.getProductData();
            Iterator it = productList.entrySet().iterator();
            while(it.hasNext()){
                 Map.Entry prodEntry = (Map.Entry)it.next();
                 Product prod = (Product)prodEntry.getValue();
                 String prodStr = "";
                 if(prod.getId().equals(productID)){
                                out.print("<div><table id='bestseller'><tr><td><div id='shop_item'>");
                                  prodStr = prodStr +"<h3>"+prod.getName()+"</h3>"
                                 +"<strong>"+prod.getPrice()+"$</strong>"
                                 +"<ul>"
                                 +"<li id='item'><img src='"+prod.getImage()+"' width='60%' height='70%'/>"
                                 +"</li>"
                                 +"<li>"
                                 +"<form method='post' action='Cart'>"
                                 +"<input type='hidden' name='cid' value='"+prod.getId()+"'>"
                                 +"<input type='hidden' name='cname' value='"+prod.getName()+"'>"
                                 +"<input type='hidden' name='cprice' value='"+prod.getPrice()+"'>"
                                 +"<input type='hidden' name='ccategory' value='"+prod.getCategory()+"'>"
                                 +"<input type='hidden' name='cbrand' value='"+prod.getBrand()+"'>"
                                 +"<input type='hidden' name='opt' value='add'>"
                                 +"<input type='submit' class='inpbtn' value='Buy Now'>"
                                 +"</form>"
                                 +"</li>"
                                 +"<li>"
                                 +"<form method='get' action='WriteReview'>"
                                 +"<input type='hidden' name='pname' value='"+prod.getName()+"'>"
                                 +"<input type='hidden' name='price' value='"+prod.getPrice()+"'>"
                                 +"<input type='hidden' name='pcategory' value='"+prod.getCategory()+"'>"
                                 +"<input type='hidden' name='pbrand' value='"+prod.getBrand()+"'>"
                                 +"<input type='submit' class='inpbtn' value='Write Reviews'>"
                                 +"</form>"
                                 +"</li>"
                                 +"<li>"
         												 +"<form method='get' action='ViewReview'>"
                                 +"<input type='hidden' name='pname' value='"+prod.getName()+"'>"
                                 +"<input type='submit' class='inpbtn' value='View Reviews'>"
                                 +"</form>"
                                 +"</li></ul>"
                                 +"</div></td>";
                                 out.print(prodStr);
                                 out.print("</tr></table></div>");
                 }
            }
            utility.printHtml("left-nav.html");
            utility.printHtml("footer.html");
        }
}
