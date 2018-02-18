import java.util.ArrayList;
import java.util.List;

public class OrderItem implements java.io.Serializable {
    
    private String id;
    private String name;
  	private int price;
    private String category;
    private String brand;
  	
  	public OrderItem() {
         id = null;
  	     name = null;
  	     price = 0;
         category = null;
         brand = null;
  	}
    
    public OrderItem(String id,String name, int price, String category,String brand) {
         this.id = id;
  	     this.name = name;
  	     this.price = price;
         this.category = category;
         this.brand = brand;
  	}
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
    
    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
    
    public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
    
}