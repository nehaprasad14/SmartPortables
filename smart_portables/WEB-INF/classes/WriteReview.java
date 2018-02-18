import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class WriteReview extends HttpServlet {

      protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            try {
                  String prodModelName = request.getParameter("prodModelName");
                  String prodCategory = request.getParameter("prodCategory");
                  int prodPrice = Integer.parseInt(request.getParameter("prodPrice"));
                  String retailerName = request.getParameter("retailerName");
                  String retailerZip = request.getParameter("retailerZip");
                  String retailerCity = request.getParameter("retailerCity");
                  String retailerState = request.getParameter("retailerState");
                  String prodOnSale = request.getParameter("prodOnSale");
                  String manuName = request.getParameter("manuName");
                  String manuRebate = request.getParameter("manuRebate");
                  String userID = request.getParameter("userID");
                  int userAge = Integer.parseInt(request.getParameter("userAge"));
                  String userGender = request.getParameter("userGender");
                  String userOccupation = request.getParameter("userOccupation");
                  int reviewRating = Integer.parseInt(request.getParameter("reviewRating"));
                  String reviewDate = request.getParameter("reviewDate");
                  String reviewText = request.getParameter("reviewText");

                  ProductReview pr = new ProductReview();
                  pr.setProdModelName(prodModelName);
                  pr.setProdCategory(prodCategory);
                  pr.setProdPrice(prodPrice);
                  pr.setRetailerName(retailerName);
                  pr.setRetailerZip(retailerZip);
                  pr.setRetailerCity(retailerCity);
                  pr.setRetailerState(retailerState);
                  pr.setProdOnSale(prodOnSale);
                  pr.setManuName(manuName);
                  pr.setManuRebate(manuRebate);
                  pr.setUserID(userID);
                  pr.setUserAge(userAge);
                  pr.setUserGender(userGender);
                  pr.setUserOccupation(userOccupation);
                  pr.setReviewRating(reviewRating);
                  pr.setReviewDate(reviewDate);
                  pr.setReviewText(reviewText);

                  MongoDBDataStoreUtilities mgUtil = new MongoDBDataStoreUtilities();
                  mgUtil.insertProductReview(pr);

                  HttpSession session = request.getSession(true);
                  session.setAttribute("reviewmsg", "Review for "+prodModelName+" submitted successfully!");
                  response.sendRedirect("WriteReview");

            }catch(Exception e){
                  e.printStackTrace();
            }
      }

      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                showReviewForm(request, response, out);
      }

      protected void showReviewForm(HttpServletRequest request, HttpServletResponse response, PrintWriter out)throws ServletException, IOException {

                Utility utility = new Utility(request,out);
                LoginUtil loginUtil = new LoginUtil(request);
                HttpSession session = request.getSession(true);

                if(!loginUtil.isLoggedin()){
                    session.setAttribute("missing", "Please Login before writing reviews.");
                    response.sendRedirect("Login");
                    return;
                }

                utility.printHtml("header.html");

                if(session.getAttribute("reviewmsg")!= null){
                    out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'>"+session.getAttribute("reviewmsg")+"</h2><hr style='width: 95%'></article>");
                    session.removeAttribute("reviewmsg");
                }else{
                    out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Product Review Form </h2><hr style='width: 95%'><div>");

                    String pname = request.getParameter("pname");
                    String price = request.getParameter("price");
                    String pcategory = request.getParameter("pcategory");
                    String pbrand = request.getParameter("pbrand");
                    String username = "";
                    if(session.getAttribute("user")!= null){
                        User user = (User)session.getAttribute("user");
                        username = user.getUsername();
                    }

                    out.print("<form method='post' action='WriteReview'>"
                        + "<table id='bestseller' style='width:80%'>"
                        + "<tr><td>&nbsp;</td><td><b>Product ModelName:</b></td><td><input type='text' name='prodModelName' value='"+pname+"' readonly></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Product Category:</b></td><td><input type='text' name='prodCategory' value='"+pcategory+"' readonly></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Product Price:</b></td><td><input type='text' name='prodPrice' value='"+price+"' readonly></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Retailer Name:</b></td><td><input type='text' name='retailerName' value='' required></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Retailer Zip:</b></td><td><input type='text' name='retailerZip' required></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Retailer City:</b></td><td><input type='text' name='retailerCity' required></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Retailer State:</b></td><td><input type='text' name='retailerState' required></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Product OnSale:</b></td><td><select name='prodOnSale'><option value='Yes' selected>Yes</option><option value='No'>No</option></select></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Manufacturer Name:</b></td><td><input type='text' name='manuName' value='"+pbrand+"' readonly></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Manufacturer Rebate:</b></td><td><select name='manuRebate'><option value='Yes' selected>Yes</option><option value='No'>No</option></select></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>User ID:</b></td><td><input type='text' name='userID' value='"+username+"' readonly></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>User Age:</b></td><td><input type='text' name='userAge' required></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>User Gender:</b></td><td><input type='radio' name='userGender' value='Male' checked> Male <input type='radio' name='userGender' value='Female'> Female <input type='radio' name='userGender' value='Other'> Other  </td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>User Occupation:</b></td><td><input type='text' name='userOccupation' required></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Review Rating:</b></td><td>"
                              +"<select name='reviewRating'><option value='1' selected>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option></select>"                 +"<img src='images/home/rating.png' height='11%' width='20%'></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Review Date:</b></td><td><input type='date' name='reviewDate' required></td></tr>"
                        + "<tr><td>&nbsp;</td><td><b>Review Text:</b></td><td><textarea rows='6' cols='50' name='reviewText'></textarea></td></tr>"
                        + "<tr><td>&nbsp;</td><td><input type='submit' name='subreview' class='inpbtn' value='Submit Review'></td><td>&nbsp;</td></tr>"
                        +"<input type='hidden' name='pname' value='"+pname+"'>"
                        + "</table>"
                        + "</form>");
                }
            utility.printHtml("left-nav.html");
            utility.printHtml("footer.html");
        }

}
