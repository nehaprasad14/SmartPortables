import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class ViewReview extends HttpServlet {

      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          response.setContentType("text/html");
          PrintWriter out = response.getWriter();
          Utility utility = new Utility(request,out);

  		    String productModelName = request.getParameter("pname");
          HashMap<String,ArrayList<ProductReview>> prHashMap = new HashMap<String,ArrayList<ProductReview>>();
          ArrayList<ProductReview> prList = null;
          MongoDBDataStoreUtilities mgUtil = new MongoDBDataStoreUtilities();
          prHashMap = mgUtil.getProductReview();

          if(!prHashMap.containsKey(productModelName)){
              prList = null;
          }else{
              prList = prHashMap.get(productModelName);
          }

          utility.printHtml("header.html");
          if(prList == null){
              out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> No reviews for product '"+productModelName+"' yet...</h2><hr style='width: 95%'></article>");
          }else{
              out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Product Reviews </h2><hr style='width: 95%'>");
              out.print("<table id='bestseller' style='width:80%'>");
              out.print("<tr><td>&nbsp;</td><td><b>Product ModelName </b></td><td><b>Product Category </b></td><td><b>Product Price </b></td><td><b>User ID </b></td><td><b>Review Rating </b></td><td><b>Review Date </b></td><td><b>Review Text </b></td></tr>");
              for (ProductReview pr : prList) {
                    out.print("<tr><td>&nbsp;</td><td>"+pr.getProdModelName()+"</td>"
                        + "<td>"+pr.getProdCategory()+"</td>"
                        + "<td>"+pr.getProdPrice()+"</td>"
                        + "<td>"+pr.getUserID()+"</td>"
                        + "<td>"+pr.getReviewRating()+"</td>"
                        + "<td>"+pr.getReviewDate()+"</td>"
                        + "<td>"+pr.getReviewText()+"</td></tr>");
                }
              out.print("</table>");
        }
      utility.printHtml("left-nav.html");
      utility.printHtml("footer.html");
      }

}
