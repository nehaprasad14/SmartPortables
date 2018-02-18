import java.util.*;
import javax.servlet.http.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.Date;

public class CartUtil {
    
    HttpServletRequest request;
    HttpSession session;
    public static HashMap<String, ArrayList<OrderItem>> orders = new HashMap<String, ArrayList<OrderItem>>();
    public static HashMap<String, ArrayList<OrderDetails>> orderList = new HashMap<String, ArrayList<OrderDetails>>();
    
    MySQLDataStoreUtilities sqlUtil = new MySQLDataStoreUtilities(); 

        
	public CartUtil(HttpServletRequest request){
        this.request = request;
        this.session = request.getSession(true);
	}
    
	public void addToCart(String cid, String cname, int cprice, String ccategory,String cbrand,String username){
        
        OrderItem orderItem = new OrderItem(cid, cname, cprice, ccategory, cbrand);
        if(!orders.containsKey(username)){            
            ArrayList<OrderItem> cartItemList = new ArrayList<OrderItem>();
            orders.put(username, cartItemList);
	    }

	    ArrayList<OrderItem> cartItems = orders.get(username);
	    cartItems.add(orderItem);
        
    }
    
    public void deleteFromCart(String username,String itemid){
        if(orders.containsKey(username)){
            
            ArrayList<OrderItem> cartItemList = orders.get(username);
            if(!cartItemList.isEmpty()){               
                
                Iterator<OrderItem> it = cartItemList.iterator();
                while (it.hasNext()) {
                    OrderItem orderItem = it.next();
                    if(orderItem.getId().equalsIgnoreCase(itemid))
                       it.remove();
                }                 
             } 
        }
    }
    
    public void deleteAllFromCart(String username){
        if(orders.containsKey(username)){            
            ArrayList<OrderItem> cartItemList = orders.get(username);
            if(!cartItemList.isEmpty()){               
                cartItemList.clear();                              
             } 
        }
    }
    
    public String addCartToOrder(String fullname, String creditcard, int cvv, String address1,String address2,String zipcode,int total,String username){
        String orderID = "0";
        try{  
            //Get orderlist from database
            orderList = sqlUtil.getOrdersByUsername(username); 

            List<OrderItem> order = new ArrayList<OrderItem>();
            if(CartCount() > 0){
                for (OrderItem orderItem : getCustomerOrders()) {
                    order.add(orderItem);
                }
            }        
            OrderDetails orderDetails = new OrderDetails(username,fullname, creditcard,cvv, address1, address2,  zipcode, getCurrentDate(),CartCount(),total,order);           
            String deliveryDate = getEstimatedDeliveryDate(); 
            orderDetails.setDeliveryDate(deliveryDate);   

            // INSERT into database 
            orderID = sqlUtil.insertCustomerOrder(orderDetails);
            orderDetails.setOid(orderID);  
            
            //Update hashmap list
            if(!orderList.containsKey(username)){            
                ArrayList<OrderDetails> od = new ArrayList<OrderDetails>();
                orderList.put(username, od);
            }                      
            
            ArrayList<OrderDetails> orderDetail = orderList.get(username);
            orderDetail.add(orderDetails);
            
        }catch(NullPointerException e){
				 e.printStackTrace();
        }
        catch(ClassNotFoundException e){
				e.printStackTrace();
        } 
        catch (SQLException e){
				 e.printStackTrace();
        }       
        
        deleteAllFromCart(username);  
        
        return orderID;
    }
    
    public void deleteCustomerOrder(String username, String orderId){       
        //remove from database
        try{    
             sqlUtil.deleteCustomerOrder(username,orderId);
        }catch(ClassNotFoundException e){
             e.printStackTrace();
        } 
        catch (SQLException e){
             e.printStackTrace();
        }                     
    }
    
    public ArrayList<OrderDetails> getOrderList(){
		ArrayList<OrderDetails> od = new ArrayList<OrderDetails>();
        //Read orders from database
        try{
            orderList = sqlUtil.getOrdersByUsername((String)session.getAttribute("username"));
        }catch(NullPointerException e){
				 e.printStackTrace();
        }
            
		if(orderList.containsKey((String)session.getAttribute("username")))
			od = orderList.get((String)session.getAttribute("username"));
		return od;
	}


	public ArrayList<OrderItem> getCustomerOrders(){
		ArrayList<OrderItem> cartItems = new ArrayList<OrderItem>();
		if(orders.containsKey((String)session.getAttribute("username")))
			cartItems = orders.get((String)session.getAttribute("username"));
		return cartItems;
	}


	public int getCartTotal(){
		int total = 0;
		for (OrderItem oi : getCustomerOrders()) {
			total = total + oi.getPrice();
		}
		return total;
	}

	public int CartCount(){
        LoginUtil loginUtil = new LoginUtil(request);
		if(loginUtil.isLoggedin()){
		      return getCustomerOrders().size();            
        }
		return 0;
	}
    
    public int getOrderCount(){
        LoginUtil loginUtil = new LoginUtil(request);
		if(loginUtil.isLoggedin()){
		      return getOrderList().size();            
        }
		return 0;
	}
    
    public String getCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		return dateFormat.format(date).toString(); 
	}
    
    public String getEstimatedDeliveryDate(){
        int noOfDays = 14; //i.e two weeks
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());            
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        Date date = calendar.getTime();        
        SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
		String delDate = fmt.format(date);
        
        return delDate;
    }
    
    public long chkCancelDate(String dlvDate){
        String currDate = getCurrentDate();
        long days = 0;
        try {
                    SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date date1 = myFormat.parse(dlvDate);
                    Date date2 = myFormat.parse(currDate);
                    long diff = date1.getTime() - date2.getTime();
                    days = diff / (1000*60*60*24);
                    //System.out.println ("Days:: " + (diff / (1000*60*60*24)));
            } catch (Exception e) {
                    e.printStackTrace();
            }
        return days;
    }
    
    public ArrayList<OrderDetails> getAllCustomersOrders(){
		ArrayList<OrderDetails> salesOrders = new ArrayList<OrderDetails>();
		ArrayList<OrderDetails> allOrders = new ArrayList<OrderDetails>();
		
        orderList = sqlUtil.getAllOrders();
        
		for(String key : orderList.keySet()){
			
			if(orderList.containsKey(key)){
				salesOrders = orderList.get(key);
				for(OrderDetails od : salesOrders)
					allOrders.add(od);
			}
		}
		return allOrders;
	}
    
     public ArrayList<OrderDetails> getOrderList(String userName){
		ArrayList<OrderDetails> od = new ArrayList<OrderDetails>();
        orderList = sqlUtil.getOrdersByUsername(userName);
		if(orderList.containsKey(userName))
            System.out.println("~~~~~~~~~~~~~~~~~~~");
			od = orderList.get(userName);
		return od;
	}
    
	
}