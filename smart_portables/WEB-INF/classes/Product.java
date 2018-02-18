import java.util.ArrayList;
import java.util.List;

public class Product implements java.io.Serializable {

    private String id = null;
  	private String name = null;
  	private int price = 0;
  	private String image = null;
    private String category = null;
  	private String deals = null;
    private String brand = null;
    private String discount = null;
    private int quantity = 0;
    private String prodOnSale = null;
    private String manuRebate = null;
    private List<String> accessories = null;

  	public Product() {
        accessories = new ArrayList<String>();
  	}

    public Product(String name, int price, String image, String deals,String brand, String discount, String category) {
  		super();
        this.name = name;
        this.price = price;
        this.image = image;
  		  this.category = category;
        this.deals = deals;
        this.brand = brand;
        this.discount = discount;
  	}


    public Product(String id, String name, int price, String image, String deals,String brand, String discount, String category,int quantity,String prodOnSale,String manuRebate) {
  		super();
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
  		  this.category = category;
        this.deals = deals;
        this.brand = brand;
        this.discount = discount;
        this.quantity = quantity;
        this.prodOnSale = prodOnSale;
        this.manuRebate = manuRebate;
  	}

    public Product(String id, String name, int price, String image, String deals,String brand, String discount, String category) {
  		super();
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
  		  this.category = category;
        this.deals = deals;
        this.brand = brand;
        this.discount = discount;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDeals() {
		return deals;
	}

	public void setDeals(String deals) {
		this.deals = deals;
	}

    public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

  public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public List<String> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<String> accessories) {
		this.accessories = accessories;
	}

  public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

  public String getProdOnSale() {
		return prodOnSale;
	}

	public void setProdOnSale(String prodOnSale) {
		this.prodOnSale = prodOnSale;
	}

  public String getManuRebate() {
    return manuRebate;
  }

  public void setManuRebate(String manuRebate) {
    this.manuRebate = manuRebate;
  }

}
