import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class TrendingProducts extends HttpServlet {

      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          response.setContentType("text/html");
          PrintWriter out = response.getWriter();
          Utility utility = new Utility(request,out);

          ArrayList<ProductReview> prList = null;
          MongoDBDataStoreUtilities mgUtil = new MongoDBDataStoreUtilities();
          prList = mgUtil.getTopProductsByRating();


          utility.printHtml("header.html");
          if(prList == null){
              out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> No trending products yet...</h2><hr style='width: 95%'></article>");
          }else{
              out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Top 5 most liked products </h2><hr style='width: 95%'>");
              out.print("<table id='bestseller' style='width:80%'>");
              out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td><b>Product ModelName </b></td><td><b>Product Category </b></td><td><b>Product Price </b></td><td><b>Review Rating </b></td></tr>");
              for (ProductReview pr : prList) {
                    out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td>"+pr.getProdModelName()+"</td>"
                        + "<td>"+pr.getProdCategory()+"</td>"
                        + "<td> $ "+pr.getProdPrice()+"</td>"
                        + "<td>"+pr.getProdOnSale()+"</td></tr>");
                }
              out.print("</table><article>");

              //Top 5 zip-codes (maximum products sold)
              prList = mgUtil.getTopProductsByZipcodes();
              out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Top 5 zip-codes (where maximum products sold) </h2><hr style='width: 95%'>");
              out.print("<table id='bestseller' style='width:80%'>");
              out.print("<tr><td>&nbsp;</td><td><b>Zip-codes </b></td><td><b>Count</b></td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
              for (ProductReview pr : prList) {
                    out.print("<tr><td>&nbsp;</td>"
                        + "<td>"+pr.getRetailerZip()+"</td>"
                        + "<td>"+pr.getProdPrice()+"</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
                }
              out.print("</table><article>");

              //Top 5 most sold products regardless of the rating
              prList = mgUtil.getTopProductsSold();
              out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Top 5 most sold products regardless of the rating </h2><hr style='width: 95%'>");
              out.print("<table id='bestseller' style='width:80%'>");
              out.print("<tr><td>&nbsp;</td><td><b>Product ModelName </b></td><td><b>Product Price </b></td><td><b>Review Rating</b></td><td><b>Count</b></td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
              for (ProductReview pr : prList) {
                    out.print("<tr><td>&nbsp;</td>"
                        + "<td>"+pr.getProdModelName()+"</td>"
                        + "<td> $ "+pr.getProdPrice()+"</td>"
                        + "<td>"+pr.getReviewRating()+"</td>"
                        + "<td>"+pr.getUserAge()+"</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
                }
              out.print("</table><article>");
        }
      utility.printHtml("left-nav.html");
      utility.printHtml("footer.html");



      }

}
