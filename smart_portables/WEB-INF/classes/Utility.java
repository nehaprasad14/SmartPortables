import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.*;
import java.util.*;
import javax.servlet.http.*;

public class Utility {
	HttpServletRequest req;
	PrintWriter out;
	String url;
	HttpSession session;
    HashMap<String,Product> productList;
    Product prod;
    public static HashMap<String, ArrayList<OrderDetails>> orderList = new HashMap<String, ArrayList<OrderDetails>>();

	public Utility(HttpServletRequest req, PrintWriter out) {
		this.req = req;
		this.out = out;
		this.url = this.getFullURL();
		this.session = req.getSession(true);
	}

	public void printHtml(String file) {
		String result = HtmlToString(file);
        CartUtil cartUtil = new CartUtil(req);

		if (file == "header.html") {
            if(session.getAttribute("user")!= null){
               User user = (User)session.getAttribute("user");
                String username = user.getUsername();
								String role = user.getRole();
				        result = result
                        + "<li class='end'><a href='Logout'>Logout</a></li>"
                        + "<li class='end'><a href='Cart'>Cart( "+cartUtil.CartCount()+" )</a></li>";
											if(role.equalsIgnoreCase("retailer")){
													result = result + "<li class='end'><a href='DataAnalytics'>Data Analytics</a></li>";
											}
											result = result
                        + "<li class='end'><a href='Account'>My Account</a></li>"
                        + "<li class='end'><a>Hello, &nbsp;"+username+" </a></li></ul> </nav>"
                        + "<div id='body'> <section id='content'><article>";
			}
			else{
                result = result + "<li class='end'><a href='Cart'>Cart( "+cartUtil.CartCount()+" )</a></li>"
                          +"<li class='end'><a href='Login'>Login</a></li>"
													+" </ul> </nav>"
                          + "<div id='body'> <section id='content'><article>";
            }
            out.print(result);
		} else if (file == "content.html"){
						displayTweets(result);
						displayDeals();
						displayContent();
            //displayContent(result);
        } else {
						out.print(result);
        }
	}

	public String HtmlToString(String file) {
		String result = null;
		try {
			String webPage = url + file;
			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();
		} catch (Exception e) {
		}
		return result;
	}

	public String getFullURL() {
		String scheme = req.getScheme();
		String serverName = req.getServerName();
		int serverPort = req.getServerPort();
		String contextPath = req.getContextPath();
		StringBuffer url = new StringBuffer();
		url.append(scheme).append("://").append(serverName);

		if ((serverPort != 80) && (serverPort != 443)) {
			url.append(":").append(serverPort);
		}
		url.append(contextPath);
		url.append("/");
		return url.toString();
	}

    public void displayContent(){

        MySQLDataStoreUtilities sqlUtil = new MySQLDataStoreUtilities();
        productList = sqlUtil.getProductCatalog();
				String result = "";
        result = result
                + "<article><hr style='width: 95%'> <h2 style='font-size: 35px;'> Crazy Deals </h2> <hr style='width: 95%'>"
                    +"<div > "
                       +"<table id='bestseller'>"
                         +"<tr>";
        String prodStr="";
        for(String prodID: productList.keySet()){
             prod = (Product)productList.get(prodID);

             if(prod.getDeals().equalsIgnoreCase("Crazy")){

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
             }
        }
        result = result + prodStr;
        result = result + "</tr></table></div></article>"
                 +"<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Trending Offers </h2><hr style='width: 95%'><div><table id='bestseller'><tr>";
        prodStr="";

        for(String prodID: productList.keySet()){

             prod = (Product)productList.get(prodID);
             if(prod.getDeals().equalsIgnoreCase("Trending")){
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
             }
        }

        result = result + prodStr;
        result = result + "</tr></table></div>";

        out.print(result);
    }

		public void displayTweets(String result){
					result = result +"<article><hr style='width: 95%'><h2 style='font-size: 35px;'> We beat our competitors in all aspects.<br>Best Price Match guaranteed!! </h2><hr style='width: 95%'><div><table id='bestseller'>";
					String prodStr="";
					DealMatches dm = new DealMatches();
					HashMap<String,Product> selectedProductList = dm.getTweets();

					if(selectedProductList.size() != 0){
							for(String deals: selectedProductList.keySet()){
									 prodStr = prodStr + "<tr><td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+"<strong>"+deals+"</strong>"
													+"</td></tr>";
							}
					}else{
						prodStr = prodStr + "<tr><td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									 +"<strong> No Offers Found.. </strong>"
									 +"</td></tr>";
					}

					result = result + prodStr;
					result = result + "</table></div></article>";
					out.print(result);
			}

		public void displayDeals(){
			    String result = "";
					result = result +"<article><hr style='width: 95%'><h2 style='font-size: 35px;'> Deal Matches </h2><hr style='width: 95%'><div><table id='bestseller'><tr>";
					String prodStr="";
					DealMatches dm = new DealMatches();
					HashMap<String,Product> selectedProductList = dm.getTweets();

					if(selectedProductList.size() != 0){
								for(String deals: selectedProductList.keySet()){
										 prod = (Product)selectedProductList.get(deals);
										 //System.out.println("Tweet: "+deals);
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
								}
							}else{
								prodStr = prodStr + "<td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											 +"<strong> No Offers Found.. </strong>"
											 +"</td>";
							}

								result = result + prodStr;
								result = result + "</tr></table></div></article>";
					out.print(result);
			}

}
