import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SalesReport extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                Utility utility = new Utility(request,out);
                HttpSession session = request.getSession(true);
                MySQLDataStoreUtilities sqlUtil = new MySQLDataStoreUtilities();
                ArrayList<Product> prodSoldList = sqlUtil.getProductSalesStat();
                ArrayList<Product> dailySalesTran = sqlUtil.getDailySalesTransaction();

                utility.printHtml("header.html");
                out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Sales Report</h2><hr style='width: 95%'>");
                out.print("<ul>");
    						out.print("<li><a href='#report1' style='font-size: 20px;color:black'><b>Product List with sold quantity</b></a></li>");
    						out.print("<li><a href='#report2' style='font-size: 20px;color:black'><b>Bar Chart that shows the product names and the total sales for every product</b></a></li>");
                out.print("<li><a href='#report3' style='font-size: 20px;color:black'><b> Total daily sales transactions  </b></a></li>");
    						out.print("</ul>");

                out.print("<article id='report1'><hr style='width: 95%'><h2 style='font-size: 25px;'> Product List with sold quantity </h2><hr style='width: 95%'>");
                out.print("<table id='bestseller' style='width:80%'>");
                out.print("<tr><td>&nbsp;</td><td><b>Product Name </b></td><td><b>Product Price </b></td><td><b># Items sold</b></td><td><b>Total Sales </b></td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
                for(Product prod : prodSoldList){
                      out.print("<tr><td>&nbsp;</td>"
                          + "<td>"+prod.getName()+"</td>"
                          + "<td> $ "+prod.getPrice()+"</td>"
                          + "<td> "+prod.getQuantity()+"</td>"
                          + "<td>"+prod.getDiscount()+"</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
                  }
                out.print("</table><article>");

                out.print("<article id='report2'><hr style='width: 95%'><h2 style='font-size: 25px;'> Bar Chart with the product names and the total sales for every product </h2><hr style='width: 95%'>");
                out.println("<script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>");
                out.println("<script type='text/javascript'>");
            		out.println("google.charts.load('current', {packages: ['corechart', 'bar']});");
            		out.println("google.charts.setOnLoadCallback(drawBasic);");
            		out.println("function drawBasic() {");
            		out.println("var data = google.visualization.arrayToDataTable([");
            		out.println("['Product Name', 'Total Sales'],");
                for(Product pr : prodSoldList){
                   String name = pr.getName();
                   int sales = Integer.parseInt(pr.getDiscount());
                   out.println("[' " +name+ " ', "+sales+ "],");
                }
                out.println("]);");
                out.println("var options = {");
            		out.println("title: 'product names and the total sales',");
            		out.println("chartArea: {width: '65%', height: 400},");
            		out.println("hAxis: {");
            		out.println("title: 'Total Sales',");
            		out.println("minValue: 0");
            		out.println("},");
            		out.println("vAxis: {");
            		out.println("title: 'Product Name'");
            		out.println("}");
            		out.println("};");
            		out.println("var chart = new google.visualization.BarChart(document.getElementById('chart_div'));");
            		out.println("chart.draw(data, options);");
            		out.println("}");
                out.println("</script>");
                out.println("<div id='chart_div' style='width:900px; height:500px'></div>");

                  out.print("<article id='report3'><hr style='width: 95%'><h2 style='font-size: 25px;'>Total daily sales transactions </h2><hr style='width: 95%'>");
                  out.print("<table id='bestseller' style='width:80%'>");
                  out.print("<tr><td>&nbsp;</td><td><b> Sale Date</b></td><td><b>Total Sales</b></td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
                  for(Product prod : dailySalesTran){
                        out.print("<tr><td>&nbsp;</td>"
                            + "<td>"+prod.getName()+"</td>"
                            + "<td> $"+prod.getQuantity()+"</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
                    }
                  out.print("</table><article>");

                utility.printHtml("left-nav.html");
        		    utility.printHtml("footer.html");
        }
}
