import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

@MultipartConfig(fileSizeThreshold=1024*1024*2,
				maxFileSize=1024*1024*10,
				maxRequestSize=1024*1024*50)
public class RetailerAccount extends HttpServlet {

    protected void doGet(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
					response.setContentType("text/html");
					PrintWriter out = response.getWriter();
			    HttpSession session=request.getSession(true);
			    LoginUtil loginUtil = new LoginUtil(request);
			    ProductUtil prodUtil = new ProductUtil(request);

					if (!loginUtil.isLoggedin()) {
						response.sendRedirect("Login");
						return;
					}
			    displayRetailerAcc(request, response, out, prodUtil);
    }

     protected void displayRetailerAcc(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,ProductUtil prodUtil) throws ServletException, IOException {

            Utility utility = new Utility(request,out);
            HttpSession session = request.getSession(true);

            utility.printHtml("header.html");

            if(session.getAttribute("msg")!= null){
                out.print("<h4 style='color:red'>"+session.getAttribute("msg")+"</h4>");
                session.removeAttribute("msg");
		    		}
						out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Reports </h2><hr style='width: 95%'>");
						out.print("<ul>");
						out.print("<li><a href='InventoryReport' style='font-size: 20px;color:black'><b>Invertory Report</b></a></li>");
						out.print("<li><a href='SalesReport' style='font-size: 20px;color:black'><b>Sales Report</b></a></li>");
						out.print("</ul>");
            out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'>Add Products </h2><hr style='width: 95%'>");

           // Add Products
            out.print("<div><table id='bestseller'><form method='post' action='RetailerAccount' enctype='multipart/form-data' >");

            out.print("<tr><td>&nbsp;</td><td>Product Name </td><td><input type='text' name='pname' value='' required></td></tr>");
            out.print("<tr><td>&nbsp;</td><td>Product Price($) </td><td><input type='text' name='pprice' value='' required></td></tr>");
            out.print("<tr><td>&nbsp;</td><td>Upload Image </td><td><input type='file' name='pimage' value='' required></td></tr>");
            out.print("<tr><td>&nbsp;</td><td>Select Category </td><td>"
                      +"<select name='pcategory'>"
                        +"<option value='smartWatches' selected>Smart Watches</option>"
                        +"<option value='speakers'>Speakers</option>"
                        +"<option value='headphones'>Headphones</option>"
                        +"<option value='phones' selected>Phones</option>"
                        +"<option value='laptops'>Laptops</option>"
                        +"<option value='externalStorage'>External Storage</option>"
                      +"</select>"
                      +" </td></tr>");
            out.print("<tr><td>&nbsp;</td><td>Product Brand </td><td><input type='text' name='pbrand' value='' required></td></tr>");
            out.print("<tr><td>&nbsp;</td><td>Product Discount </td><td><input type='text' name='pdiscount' value='' required></td></tr>");
            out.print("<tr><td>&nbsp;</td><td><input type='hidden' name='option' value='add'></td><td><input type='submit' class='btn1' value='Add Product'></td><td>&nbsp;</td></tr>");

            out.print("</form></table></div>");

           // update Products

            HashMap<String,Product> productList = prodUtil.getProductList();

            out.print("<hr style='width: 95%'><h2 style='font-size: 35px;'>Products List </h2><hr style='width: 95%'>");

            out.print("<div><table id='bestseller' width='80%'>");

            out.print("<tr><td><b>Product Name</b></td><td><b>Product Price($)</b> </td><td> <b>Category </b></td><td><b>Product Brand </b></td><td><b> Discount % </b></td><td><b>Action</b></td></tr>");

            for(String prodID: productList.keySet()){
               Product prod = (Product)productList.get(prodID);

                  out.print("<form method='post'><tr><td><input type='hidden' name='pid' value='"+prod.getId()+"'><input type='text' name='pname' value='"+prod.getName()+"' required></td><td><input type='text' name='pprice' value='"+prod.getPrice()+"' required></td><td><input type='hidden' name='pcategory' value='"+prod.getCategory()+"'>"+prod.getCategory()+"</td><td><input type='text' name='pbrand' value='"+prod.getBrand()+"' required></td><td><input type='text' name='pdiscount' value='"+prod.getDiscount()+"' required></td>"+
                            "<td><input type='submit' class='btn1' name='update' value='Update' onclick='form.action=\"UpdateProduct\";'>"
                            +"<input type='submit' class='btn1' name='delete' value='Delete' onclick='form.action=\"DeleteProduct\";'></td></tr></form>");
            }

            out.print("</table></div>");

            utility.printHtml("left-nav.html");
		    utility.printHtml("footer.html");
     }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session=request.getSession(true);
        LoginUtil loginUtil = new LoginUtil(request);
        ProductUtil prodUtil = new ProductUtil(request);

        String username = "";
        if(loginUtil.isLoggedin() && session.getAttribute("user")!= null){
               User user = (User)session.getAttribute("user");
               username = user.getUsername();


               String opt = request.getParameter("option");
               opt.trim();

               if(opt!=null && opt.equals("add")){

                    String pname = request.getParameter("pname");
                    int pprice = Integer.parseInt(request.getParameter("pprice"));
                    String pcategory = request.getParameter("pcategory");
                    String pbrand = request.getParameter("pbrand");
                    String pdiscount = request.getParameter("pdiscount");
                    String pimage = "";

                    String folder = "";
                    if(pcategory.equals("smartWatches")){
                        folder = "SmartWatches";
                    }else if(pcategory.equals("speakers")){
                        folder = "Speakers";
                    }else if(pcategory.equals("headphones")){
                        folder =  "Headphones";
                    }else if(pcategory.equals("phones")){
                        folder =  "Phones";
                    }else if(pcategory.equals("laptops")){
                        folder =  "Laptops";
                    }else if(pcategory.equals("externalStorage")){
                        folder =  "ExternalStorage";
                    }

                    Part part = request.getPart("pimage");
                    String fileName = extractFileName(part);

                    if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                       // part.write(savePath + fileName);
                        part.write("C:\\apache-tomcat-7.0.34\\webapps\\smart_portables\\images\\products\\"+folder+"\\"+fileName);

                    }

                    pimage = "images/products/"+folder+"/"+fileName;
                    prodUtil.addProduct(pname,pprice,pimage,pcategory,pbrand,pdiscount);
                    session.setAttribute("msg", "Product added successfully.");

               }

             displayRetailerAcc(request, response, out, prodUtil);
        }else{
            session.setAttribute("missing", "Please login before adding any item to cart.");
            response.sendRedirect("Login");
        }
     }

        private String extractFileName(Part part) {
            String contentDisp = part.getHeader("content-disposition");
            String[] items = contentDisp.split(";");
            for (String s : items) {
                if (s.trim().startsWith("filename"))
                    return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
            return "";
        }


}
