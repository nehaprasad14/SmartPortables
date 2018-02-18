import java.util.ArrayList;
import java.util.List;

public class OrderDetails implements java.io.Serializable {
    
    String oid,username,fullname,creditcard,address1,address2,zipcode,date,deliveryDate;
	int itemscount , cvv; 
    int total;
	List<OrderItem> order;
    
    public OrderDetails() {
         oid = null;
  	     username = null;
         fullname = null; 
         creditcard = null;
         cvv = 0;
         address1 = null;
         address2 = null;
         zipcode = null;
         date = null;
  	     itemscount = 0;
         total = 0;
         order = null;         
  	}
    
    public OrderDetails(String oid, String username,String fullname,String creditcard,int cvv,String address1,String address2, String zipcode, String date,int itemscount,int total,List<OrderItem> order) {
         this.oid = oid;
  	     this.username = username;
         this.fullname = fullname; 
         this.creditcard = creditcard;
         this.cvv = cvv;
         this.address1 = address1;
         this.address2 = address2;
         this.zipcode = zipcode;
         this.date = date;
  	     this.itemscount = itemscount;
         this.total = total;
         this.order = order; 
  	}
    
    public OrderDetails(String username,String fullname,String creditcard,int cvv,String address1,String address2, String zipcode, String date,int itemscount,int total,List<OrderItem> order) {
  	     this.username = username;
         this.fullname = fullname; 
         this.creditcard = creditcard;
         this.cvv = cvv;
         this.address1 = address1;
         this.address2 = address2;
         this.zipcode = zipcode;
         this.date = date;
  	     this.itemscount = itemscount;
         this.total = total;
         this.order = order; 
  	}
    
    public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getCreditcard() {
		return creditcard;
	}
	public void setCreditcard(String creditcard) {
		this.creditcard = creditcard;
	}
	public int getCvv() {
		return cvv;
	}
	public void setCvv(int cvv) {
		this.cvv = cvv;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getDate() {
		return date;
	}
    public void setDate(String date) {
		this.date = date;
	}
    
    public String getDeliveryDate() {
		return deliveryDate;
	}
    public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public int getItemscount() {
		return itemscount;
	}
	public void setItemscount(int itemscount) {
		this.itemscount = itemscount;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public List<OrderItem> getOrder() {
		return order;
	}
	public void setOrder(List<OrderItem> order) {
		this.order = order;
	}	
    
}