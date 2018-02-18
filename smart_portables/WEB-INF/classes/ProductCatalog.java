import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ProductCatalog extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
        HttpSession session=request.getSession(true);

        HashMap<String,Product> productList;
        HashMap<String,Product> productCatList = null;
        HashMap<String,Accessory> accList = null;

        MySQLDataStoreUtilities sqlUtil = new MySQLDataStoreUtilities();
        productList = sqlUtil.getProductCatalog();
        accList = sqlUtil.getAccessoryCatalog();

        String category = request.getParameter("category");
        boolean acc = Boolean.parseBoolean(request.getParameter("acc"));
        boolean cat = Boolean.parseBoolean(request.getParameter("cat"));
        String contentTitle = "";

        if(category != null && category.equalsIgnoreCase("smartWatches"))
            contentTitle = "Smart Watches";
        else if(category != null && category.equalsIgnoreCase("speakers"))
            contentTitle = "Speakers";
        else if(category != null && category.equalsIgnoreCase("headphones"))
            contentTitle = "Headphones";
        else if(category != null && category.equalsIgnoreCase("phones"))
            contentTitle = "Phones";
        else if(category != null && category.equalsIgnoreCase("laptops"))
            contentTitle = "Laptops";
        else if(category != null && category.equalsIgnoreCase("externalStorage"))
            contentTitle = "External Storage";

		if(category==null){
            response.sendRedirect("Home");
		}

        if(category!=null && acc==false && cat==true){
            productCatList = productFilterByCat(productList,category,productCatList);
            if(productCatList != null)
                displayContent(request, response,out,productCatList,contentTitle);
            else
                response.sendRedirect("Home");
        }

        if(category!=null && acc==true && cat==false){
            contentTitle = contentTitle + " Accessories";
            productCatList = productFilterByAcc(accList,category,productCatList);
            if(productCatList != null)
                displayContent(request, response,out,productCatList,contentTitle);
            else
                response.sendRedirect("Home");
        }

	}

    public HashMap<String,Product> productFilterByCat(HashMap<String,Product> productList,String category,HashMap<String,Product> productCatList){
        productCatList = new HashMap<String,Product>();
        Product prod = null;
        for(String prodID: productList.keySet()){
             prod = (Product)productList.get(prodID);
             if(prod.getCategory().equalsIgnoreCase(category)){
                productCatList.put(prod.getId(),prod);
             }
        }
        return productCatList;
    }

    public HashMap<String,Product> productFilterByAcc(HashMap<String,Accessory> accList,String category,HashMap<String,Product> productCatList){
        productCatList = new HashMap<String,Product>();
        Product prod = null;
        Accessory acc = null;
        for(String aid: accList.keySet()){
             acc = (Accessory)accList.get(aid);
             if(acc.getCategory().equalsIgnoreCase(category)){
                prod = new Product(acc.getId(), acc.getName(), acc.getPrice(), acc.getImage(), acc.getDeals(),acc.getBrand(), acc.getDiscount(), acc.getCategory());
                productCatList.put(prod.getId(),prod);
             }
        }
        return productCatList;
    }

    public void displayContent(HttpServletRequest request, HttpServletResponse response, PrintWriter out,HashMap<String,Product> productList, String contentTitle){


        Utility utility = new Utility(request,out);
        Product prod = null;
        String prodStr = "";
        int count = productList.size();
        int i = 1;

		utility.printHtml("header.html");

        out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> "+contentTitle+" </h2><hr style='width: 95%'><div><table id='bestseller'>");

        for(String prodID: productList.keySet()){
             prod = (Product)productList.get(prodID);
             if(i%3 == 1)
                  prodStr = prodStr + "<tr>";

             prodStr = prodStr + "<td> <div id='shop_item'>"
                        +"<h3>"+prod.getName()+"</h3>"
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

            if(i%3 == 0 || i == count)
                prodStr = prodStr + "</tr>";
            i++;
        }

        prodStr = prodStr + "</table></div>";
        out.print(prodStr);
        utility.printHtml("left-nav.html");
		utility.printHtml("footer.html");
    }

}
