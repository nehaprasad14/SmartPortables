import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import java.util.regex.Pattern;
import java.text.DecimalFormat;

public class DataAnalytics extends HttpServlet {

      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          response.setContentType("text/html");
          PrintWriter out = response.getWriter();
          Utility utility = new Utility(request,out);

          utility.printHtml("header.html");

          out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Data Analytics on Reviews </h2><hr style='width: 95%'>");

          out.print("<table id='bestseller' style='width:100%'>");
          out.print("<form method='post' action='DataAnalytics'>");
          out.print("<tr>");
          out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type='checkbox' name='select' value='$ProductModelName'><b>&nbsp; Select</b></td>");
          out.print("<td>Product Name &nbsp; <select name='prodOpt'><option value='allProds'>All Products</option></select></td>");
          out.print("</tr>");

          out.print("<tr>");
          out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type='checkbox' name='select' value='$ProductPrice'><b>&nbsp; Select</b></td>");
          out.print("<td>Product Price &nbsp; &nbsp; <input type='text' name='prodPrice' value='0'/></td>");
          out.print("<td><input type='radio' name='filterPrice' value='$eq' checked> Equals &nbsp; <input type='radio' name='filterPrice' value='$gt'> Greater Than &nbsp; <input type='radio' name='filterPrice' value='$lt'> Less Than </td>");
          out.print("</tr>");

          out.print("<tr>");
          out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type='checkbox' name='select' value='$ReviewRating'><b>&nbsp; Select</b></td>");
          out.print("<td>Review Rating &nbsp; <select name='ratingOpt'><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option></select></td>");
          out.print("<td><input type='radio' name='filterRating' value='$eq' checked> Equals &nbsp; <input type='radio' name='filterRating' value='$gt'> Greater Than &nbsp; <input type='radio' name='filterRating' value='$lt'> Less Than </td>");
          out.print("</tr>");

          out.print("<tr>");
          out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type='checkbox' name='select' value='$RetailerCity'><b>&nbsp; Select</b></td>");
          out.print("<td>Retailer City &nbsp; &nbsp; &nbsp; <input type='text' name='retailerCity' value=''/></td>");
          out.print("</tr>");

          out.print("<tr>");
          out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type='checkbox' name='select' value='$UserAge'><b>&nbsp; Select</b></td>");
          out.print("<td>User Age &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <input type='text' name='userAge' value='0'/></td>");
          out.print("<td><input type='radio' name='filterUserAge' value='$eq' checked> Equals &nbsp; <input type='radio' name='filterUserAge' value='$gt'> Greater Than &nbsp; <input type='radio' name='filterUserAge' value='$lt'> Less Than </td>");
          out.print("</tr>");

          out.print("<tr>");
          out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type='checkbox' name='select' value='$ReviewText'><b>&nbsp; Select</b></td>");
          out.print("<td>Review Text &nbsp; &nbsp; &nbsp; <input type='text' name='reviewText' value=''/></td>");
          out.print("</tr>");

          out.print("<tr>");
          out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type='checkbox' name='select' value='groupBY'><b>&nbsp; Group By</b></td>");
          out.print("<td><select name='groupByOpt'><option value='ProductModelName'>Product ModelName</option><option value='RetailerCity'>Retailer City</option><option value='RetailerZip'>Retailer Zip</option></select></td>");
          out.print("<td><input type='radio' name='filterAggr' value='$sum'> Count &nbsp; <input type='radio' name='filterAggr' value='median'> Median &nbsp; <input type='radio' name='filterAggr' value='detail'> Detail List &nbsp; <input type='radio' name='filterAggr' value='liked'>Top 5 Most Liked &nbsp; <input type='radio' name='filterAggr' value='disliked'>Top 5 Most Disliked </td>");
          out.print("</tr>");

          out.print("<tr>");
          out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type='checkbox' name='select' value='$max'><b>&nbsp; Max/Highest</b></td>");
          out.print("<td><select name='maxOpt'><option value='ProductPrice'>Product Price</option><option value='ReviewRating'>Review Rating</option></select></td>");
          out.print("</tr>");

          out.print("<tr>");
          out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type='checkbox' name='select' value='$Limit'><b>&nbsp; Limit</b></td>");
          out.print("<td><select name='limitOpt'><option value='10'>Top 10</option><option value='5'>Top 5</option><option value='2'>Top 2</option></select></td>");
          out.print("</tr>");

          out.print("<tr>");
          out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type='checkbox' name='select' value='orderBY'><b>&nbsp; Order By</b></td>");
          out.print("<td><select name='orderByOpt'><option value='ReviewRating' selected>Review Rating</option><option value='RetailerName'>Retailer Name</option><option value='ManufactureName'>Manufacture Name</option></select></td>");
          out.print("<td><b>Sort</b> &nbsp; <input type='radio' name='filterSort' value='1' checked> Ascending &nbsp; <input type='radio' name='filterSort' value='-1'> Descending &nbsp;</td>");
          out.print("</tr>");

          out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td><input type='submit' class='inpbtn' value='Find Data'></td><td>&nbsp;</td><td>&nbsp;</td></tr>" );
          out.print("</form>");
          out.print("</table><article>");

          utility.printHtml("left-nav.html");
          utility.printHtml("footer.html");

      }

      protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          response.setContentType("text/html");
          PrintWriter out = response.getWriter();
          Utility utility = new Utility(request,out);
          DataAnalyticsUtilities daUtil = new DataAnalyticsUtilities();
          ArrayList<ProductReview> prList = null;
          LinkedHashMap<String, LinkedHashMap<String, ArrayList<Object>>> top5ProdList = null;
          HashMap<String, ArrayList<ProductReview>> detailProd = null;

          ArrayList<String> selectList = new ArrayList<String>();
          String[] selectVal = request.getParameterValues("select");

          if(selectVal != null){
            for(String val:selectVal){
               selectList.add(val);
            }
          }

          if(selectList.size()!= 0){
                BasicDBObject query = new BasicDBObject();
                DBObject groupFields = new BasicDBObject("_id",0);
                DBObject limit = new BasicDBObject();
                DBObject sort = new BasicDBObject();
                DBObject orderBy = new BasicDBObject();
                DBObject group = new BasicDBObject();
                String groupByOpt = "";
                String grpTitle = "";
                String maxOpt = "";
                boolean isGroupBy = false;
                boolean isSelect = false;
                boolean isMax = false;
                for(String opt : selectList){
                    switch(opt){
                         case "$ProductModelName":
                                    displayAllReviews(utility,out);
                                    isGroupBy = false;
                                    isSelect = true;
                                    break;
                         case "$ProductPrice":
                                   String filterPrice = request.getParameter("filterPrice");
                                   int prodPrice = Integer.parseInt(request.getParameter("prodPrice"));
                                   query.put("ProductPrice", new BasicDBObject(filterPrice, prodPrice));
                                   isGroupBy = false;
                                   isSelect = true;
                                   break;
                         case "$ReviewRating":
                                   String filterRating = request.getParameter("filterRating");
                                   int ratingOpt = Integer.parseInt(request.getParameter("ratingOpt"));
                                   query.put("ReviewRating", new BasicDBObject(filterRating, ratingOpt));
                                   isGroupBy = false;
                                   isSelect = true;
                                   break;
                         case "$RetailerCity":
                                   String retailerCity = request.getParameter("retailerCity");
                                   query.put("RetailerCity", new BasicDBObject("$eq", retailerCity));
                                   isGroupBy = false;
                                   isSelect = true;
                                   break;
                         case "$UserAge":
                                   String filterUserAge = request.getParameter("filterUserAge");
                                   int userAge = Integer.parseInt(request.getParameter("userAge"));
                                   query.put("UserAge", new BasicDBObject(filterUserAge, userAge));
                                   isGroupBy = false;
                                   isSelect = true;
                                   break;
                         case "$ReviewText":
                                   String reviewText = request.getParameter("reviewText");
                                   query.put("ReviewText", Pattern.compile(reviewText, Pattern.CASE_INSENSITIVE));
                                   isGroupBy = false;
                                   isSelect = true;
                                   break;
                         case "groupBY":
                                   isGroupBy = true;
                                   groupByOpt = request.getParameter("groupByOpt");
                                   groupFields.put("_id",'$'+groupByOpt);
                                   grpTitle = groupByOpt;
                                   String filterAggr = request.getParameter("filterAggr");
                                   if(filterAggr != null && filterAggr!=" " && filterAggr!="" && !filterAggr.isEmpty()){
                                       if(filterAggr.equalsIgnoreCase("$sum")){
                                              groupFields.put("ReviewValue",new BasicDBObject("$sum", 1));
                                              grpTitle = "Count";
                                       }else if(filterAggr.equalsIgnoreCase("median")){
                                              groupFields.put("ReviewValue",new BasicDBObject("$sum", 1));
                                              groupFields.put("Median",new BasicDBObject("$avg", "$ProductPrice"));
                                              grpTitle = "Median";
                                       }else if(filterAggr.equalsIgnoreCase("detail")){
                                              detailProd = daUtil.getProdListByGroup(groupByOpt);
                                              displayDetailedProdReview(utility, out, detailProd);
                                              return;
                                       }else if(filterAggr.equalsIgnoreCase("liked")){
                                              top5ProdList = daUtil.getTop5ListOfLikedProduct(groupByOpt,filterAggr);
                                              displayReviewOfLikedProduct(utility, out, top5ProdList);
                                              return;
                                       }else if(filterAggr.equalsIgnoreCase("disliked")){
                                               top5ProdList = daUtil.getTop5ListOfLikedProduct(groupByOpt,filterAggr);
                                               displayReviewOfLikedProduct(utility, out, top5ProdList);
                                               return;
                                       }
                                       sort.put("ReviewValue", -1);
                                       orderBy = new BasicDBObject("$sort", sort);
                                   }
                                   groupFields.put("ReviewKey",new BasicDBObject("$first", '$'+groupByOpt));
                                   limit = new BasicDBObject("$limit",100);
                                   break;
                         case "$max":
                                   isMax = true;
                                   maxOpt = request.getParameter("maxOpt");
                                   groupFields.put("maxValue", new BasicDBObject("$max", '$'+maxOpt));
                                   groupFields.put("ProductModelName",new BasicDBObject("$first", "$ProductModelName"));
                                   break;
                         case "orderBY":
                                   isGroupBy = true;
                                   int filterSort = Integer.parseInt(request.getParameter("filterSort"));
                                   String orderByOpt = request.getParameter("orderByOpt");
                                   sort.put(orderByOpt, filterSort);
                                   orderBy = new BasicDBObject("$sort", sort);
                                   break;
                        case "$Limit":
                                   isGroupBy = true;
                                   String limitOpt = request.getParameter("limitOpt");
                                   limit = new BasicDBObject("$limit", Integer.parseInt(limitOpt));
                                   break;

                    }
                }

                if(!isGroupBy && isSelect){
                    prList =  daUtil.getReviewDataByFilter(query);
                    displayReviewByFilters(utility, out, prList);
                }else if(isGroupBy && !isMax && !isSelect){
                    group.put("$group",groupFields);
                    prList =  daUtil.getReviewByGroup(group,orderBy,limit);
                    displayReviewByCounts(utility, out, prList ,grpTitle);
                }else if(isGroupBy && isMax && !isSelect){
                    group.put("$group",groupFields);
                    sort.put(grpTitle, 1);
                    orderBy = new BasicDBObject("$sort", sort);
                    HashMap<String, ArrayList<Object>> hmReview =  daUtil.getReviewByMaxGroup(group,orderBy,limit);
                    displayReviewByMaxGroup(utility, out, hmReview, grpTitle, maxOpt);
                }else if(isGroupBy && isSelect){
                    group.put("$group",groupFields);
                    daUtil.getReviewByGroupSelect(query,group,orderBy,limit);
                  //  displayReviewByCounts(utility, out, prList ,grpTitle);
                }else{
                    displayError(utility, out);
                }


          }else{
              displayAllReviews(utility,out);
          }

      }

      void displayReviewByFilters(Utility utility, PrintWriter out, ArrayList<ProductReview> prList){
              utility.printHtml("header.html");
              if(prList.size() < 1){
                  out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> No review data found ...</h2><hr style='width: 95%'></article>");
              }else{
                out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Data Analytics on Reviews </h2><hr style='width: 95%'>");
                out.print("<table id='bestseller' style='width:80%'>");
                out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td><b>Product ModelName </b></td><td><b>Product Category </b></td><td><b>Product Price </b></td><td><b>Review Rating </b></td><td><b>Retailer City </b></td><td><b>Review Text </b></td></tr>");
                for (ProductReview pr : prList) {
                      out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td>"+pr.getProdModelName()+"</td>"
                          + "<td>"+pr.getProdCategory()+"</td>"
                          + "<td> $ "+pr.getProdPrice()+"</td>"
                          + "<td>"+pr.getReviewRating()+"</td>"
                          + "<td>"+pr.getRetailerCity()+"</td>"
                          + "<td>"+pr.getReviewText()+"</td>"
                          + "</tr>");
                  }
                out.print("</table><article>");
            }
          utility.printHtml("left-nav.html");
          utility.printHtml("footer.html");
       }

      void displayAllReviews(Utility utility, PrintWriter out){
            HashMap<String,ArrayList<ProductReview>> prHashMap = new HashMap<String,ArrayList<ProductReview>>();
            MongoDBDataStoreUtilities mgUtil = new MongoDBDataStoreUtilities();
            utility.printHtml("header.html");
            if(prHashMap == null){
                out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> No review data found ... </h2><hr style='width: 95%'></article>");
            }else{
                prHashMap = mgUtil.getProductReview();
                ArrayList<ProductReview> prList = null;
                out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Data Analytics on Reviews </h2><hr style='width: 95%'>");
                out.print("<table id='bestseller' style='width:100%'>");
                out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td><b>Product ModelName </b></td><td><b>Product Category </b></td><td><b>Product Price </b></td><td><b>User ID </b></td><td><b>Review Rating </b></td><td><b>Review Date </b></td><td><b>Review Text </b></td></tr>");
                for(String productModelName: prHashMap.keySet()){
                     prList = prHashMap.get(productModelName);
                        for (ProductReview pr : prList) {
                              out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>"+pr.getProdModelName()+"</td>"
                                  + "<td>"+pr.getProdCategory()+"</td>"
                                  + "<td>"+pr.getProdPrice()+"</td>"
                                  + "<td>"+pr.getUserID()+"</td>"
                                  + "<td>"+pr.getReviewRating()+"</td>"
                                  + "<td>"+pr.getReviewDate()+"</td>"
                                  + "<td>"+pr.getReviewText()+"</td></tr>");
                          }
                  }
                out.print("</table>");
          }
        utility.printHtml("left-nav.html");
        utility.printHtml("footer.html");
      }

      void displayReviewByCounts(Utility utility, PrintWriter out, ArrayList<ProductReview> prList,String grpTitle){
              utility.printHtml("header.html");
              if(prList.size() < 1){
                  out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> No review data found ...</h2><hr style='width: 95%'></article>");
              }else{
                out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Data Analytics on Reviews </h2><hr style='width: 95%'>");
                out.print("<table id='bestseller' style='width:80%'>");
                out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td><b>Name </b></td><td><b> "+grpTitle+" </b></td></tr>");
                DecimalFormat df = new DecimalFormat("###.##");

                for (ProductReview pr : prList) {
                      out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td>"+pr.getProdModelName()+"</td>");

                          if(grpTitle.equalsIgnoreCase("Median"))
                              out.print("<td>"+df.format(Double.parseDouble(pr.getUserGender()))+"</td>");
                          else
                              out.print("<td>"+pr.getCount()+"</td>");

                          out.print("</tr>");
                  }
                out.print("</table><article>");
            }
            utility.printHtml("left-nav.html");
            utility.printHtml("footer.html");
      }

      void displayError(Utility utility, PrintWriter out){
            utility.printHtml("header.html");
            out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'>Please select correct options</h2><hr style='width: 95%'></article>");
            utility.printHtml("left-nav.html");
            utility.printHtml("footer.html");

      }

      void displayReviewByMaxGroup(Utility utility,PrintWriter out,HashMap<String, ArrayList<Object>> hmReview,String grpTitle, String maxOpt){
              utility.printHtml("header.html");
              if(hmReview.size() < 1){
                  out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> No review data found ...</h2><hr style='width: 95%'></article>");
              }else{
                out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Data Analytics on Reviews By "+grpTitle +"</h2><hr style='width: 95%'>");
                out.print("<table id='bestseller' style='width:80%'>");
                out.print("<tr><td>&nbsp;</td><td>&nbsp;</td><td><b> # </b></td><td><b> "+grpTitle+" </b></td><td><b> ProductModelName </b></td><td><b> "+maxOpt+" </b></td></tr>");
                int number = 1;
                for(Map.Entry<String, ArrayList<Object>> m :hmReview.entrySet()){
            				String key = m.getKey();
            				ArrayList<Object> values = m.getValue();
            				out.println("<tr><td>&nbsp;</td><td>&nbsp;</td><td>"+number+".</td>");
            				out.println("<td>"+values.get(0)+"</td>");
            				out.println("<td>"+values.get(2)+"</td>");
            				out.println("<td>"+values.get(1)+"</td></tr>");
            				number++;
          			}
                out.print("</table><article>");
            }
            utility.printHtml("left-nav.html");
            utility.printHtml("footer.html");
      }

      void displayReviewOfLikedProduct(Utility utility,PrintWriter out,LinkedHashMap<String, LinkedHashMap<String, ArrayList<Object>>>  top5ProdList){
              utility.printHtml("header.html");
              if(top5ProdList.size() < 1){
                  out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> No review data found ...</h2><hr style='width: 95%'></article>");
              }else{
                out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Data Analytics on Reviews </h2><hr style='width: 95%'><article>");
                for(Map.Entry<String, LinkedHashMap<String, ArrayList<Object>>> p :top5ProdList.entrySet()){
          				String key1 = p.getKey();
          				LinkedHashMap<String, ArrayList<Object>> productsListHashMap = p.getValue();
          				out.println("<h3 align=\"center\">"+key1+"</h3>");
          				int number=1;
          				out.print("<table id='bestseller' style='width:80%'>");
          				out.println("<tr><td>&nbsp;</td><td>&nbsp;</td><td><b>No.</b></td><td><b>Product Name</b></td><td><b>Rating</b></td></tr>");
          					for(Map.Entry<String, ArrayList<Object>> m :productsListHashMap.entrySet()){
          						String key = m.getKey();
          						ArrayList<Object> values = m.getValue();
          						out.println("<tr><td>&nbsp;</td><td>&nbsp;</td><td>"+number+".</td>");
          						out.println("<td>"+values.get(1)+"</td>");
          						out.println("<td>"+values.get(2)+"</td></tr>");
          						number++;
          					}
          				out.print("</table>");
                }
            }
            utility.printHtml("left-nav.html");
            utility.printHtml("footer.html");
      }

      void displayDetailedProdReview(Utility utility,PrintWriter out, HashMap<String, ArrayList<ProductReview>> detailProd){
              utility.printHtml("header.html");
              if(detailProd.size() < 1){
                  out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> No review data found ...</h2><hr style='width: 95%'></article>");
              }else{
                out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Data Analytics on Reviews </h2><hr style='width: 95%'><article>");

                ArrayList<ProductReview> prList = new ArrayList<ProductReview>();
                for(String key: detailProd.keySet()){
                     out.println("<h3 align=\"center\">"+key+"</h3>");
                     prList = detailProd.get(key);
                     int number=1;
                     out.print("<table id='bestseller' style='width:100%'>");
                     out.println("<tr><td>&nbsp;</td><td>&nbsp;</td><td><b>No.</b></td><td><b>Product Name</b></td><td><b>Price</b></td><td><b>Rating</b></td><td><b>Review Text</b></td><td><b>City</b></td><td><b>Zip-code</b></td></tr>");
                     for(ProductReview pr: prList){
                        out.println("<tr><td>&nbsp;</td><td>&nbsp;</td><td>"+number+".</td>");
             						out.println("<td>"+pr.getProdModelName()+"</td>");
                        out.println("<td>"+pr.getProdPrice()+"</td>");
             						out.println("<td>"+pr.getReviewRating()+"</td>");
                        out.println("<td>"+pr.getReviewText()+"</td>");
                        out.println("<td>"+pr.getRetailerCity()+"</td>");
                        out.println("<td>"+pr.getRetailerZip()+"</td></tr>");
                        number++;
                     }
                     out.print("</table>");
                }
            }
            utility.printHtml("left-nav.html");
            utility.printHtml("footer.html");
      }

}
