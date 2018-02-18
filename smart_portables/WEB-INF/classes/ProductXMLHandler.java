import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ProductXMLHandler extends DefaultHandler {

  	private boolean bName = false;
  	private boolean bPrice = false;
  	private boolean bImage = false;
    private boolean bCategory = false;
  	private boolean bDeals = false;
    private boolean bAccessories = false;
    private boolean bAccessory = false;
    private boolean bbrand = false;
    private boolean bdiscount = false;
    private boolean bquantity = false;
    private boolean bprodOnSale = false;
    private boolean bmanuRebate = false;

    private boolean baName = false;
  	private boolean baPrice = false;
  	private boolean baImage = false;
    private boolean baBrand = false;
    private boolean baDeals = false;
    private boolean baDiscount = false;
    private boolean baCategory = false;
    private boolean baquantity = false;
    private boolean baprodOnSale = false;
    private boolean bamanuRebate = false;

    Product prod = null;
    Accessory acc = null;
    HashMap<String,Product> productList = null;
    HashMap<String,Accessory> accList = null;
    List<String> accessories = null;

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

          if(qName.equalsIgnoreCase("product")) {
                prod = new Product();
                accessories = new ArrayList<String>();
                prod.setId(attributes.getValue("id"));

                if (productList == null){
                    productList = new HashMap<String,Product>();
                }
          } else if (qName.equalsIgnoreCase("name")) {
             bName = true;
          } else if (qName.equalsIgnoreCase("price")) {
             bPrice = true;
          } else if (qName.equalsIgnoreCase("image")) {
             bImage = true;
          } else if (qName.equalsIgnoreCase("category")) {
             bCategory = true;
          } else if (qName.equalsIgnoreCase("deals")) {
             bDeals = true;
          } else if (qName.equalsIgnoreCase("Accessories")) {
             bAccessories = true;
          } else if (qName.equalsIgnoreCase("accessory")) {
             bAccessory = true;
          } else if (qName.equalsIgnoreCase("brand")) {
             bbrand = true;
          } else if (qName.equalsIgnoreCase("discount")) {
             bdiscount = true;
          } else if (qName.equalsIgnoreCase("quantity")) {
             bquantity = true;
          } else if (qName.equalsIgnoreCase("prodOnSale")) {
             bprodOnSale = true;
          } else if (qName.equalsIgnoreCase("manuRebate")) {
             bmanuRebate = true;
          } else if(qName.equalsIgnoreCase("acc")) {
                acc = new Accessory();
                acc.setId(attributes.getValue("id"));
                if (accList == null){
                    accList = new HashMap<String,Accessory>();
                }
          } else if (qName.equalsIgnoreCase("aname")) {
             baName = true;
          } else if (qName.equalsIgnoreCase("aprice")) {
             baPrice = true;
          } else if (qName.equalsIgnoreCase("aimage")) {
             baImage = true;
          } else if (qName.equalsIgnoreCase("abrand")) {
             baBrand = true;
          } else if (qName.equalsIgnoreCase("adeals")) {
             baDeals = true;
          } else if (qName.equalsIgnoreCase("adiscount")) {
             baDiscount = true;
          } else if (qName.equalsIgnoreCase("acategory")) {
             baCategory = true;
          } else if (qName.equalsIgnoreCase("aquantity")) {
             baquantity = true;
          } else if (qName.equalsIgnoreCase("aprodOnSale")) {
             baprodOnSale = true;
          } else if (qName.equalsIgnoreCase("amanuRebate")) {
             bamanuRebate = true;
          }


     }

   @Override
   public void endElement(String uri,
   String localName, String qName) throws SAXException {
      if (qName.equalsIgnoreCase("product")) {
          prod.setAccessories(accessories);
          productList.put(prod.getId(),prod);
      }
      if (qName.equalsIgnoreCase("acc")) {
          accList.put(acc.getId(),acc);
          prod = new Product(acc.getId(),  acc.getName(),  acc.getPrice(), acc.getImage(), acc.getDeals(),acc.getBrand(), acc.getDiscount(), acc.getCategory(),acc.getQuantity(),acc.getProdOnSale(),acc.getManuRebate());
          productList.put(prod.getId(),prod);
      }
   }

   @Override
   public void characters(char ch[], int start, int length) throws SAXException {

        if (bName) {
             prod.setName(new String(ch, start, length));
             bName = false;
          } else if (bPrice) {
             prod.setPrice(Integer.parseInt(new String(ch, start, length)));
             bPrice = false;
          } else if (bImage) {
             prod.setImage(new String(ch, start, length));
             bImage = false;
          } else if (bCategory) {
             prod.setCategory(new String(ch, start, length));
             bCategory = false;
          } else if (bDeals) {
             prod.setDeals(new String(ch, start, length));
             bDeals = false;
          } else if (bAccessories) {
             bAccessories = false;
          } else if (bAccessory) {
             accessories.add(new String(ch, start, length));
             bAccessory = false;
          } else if (bbrand) {
             prod.setBrand(new String(ch, start, length));
             bbrand = false;
          } else if (bdiscount) {
             prod.setDiscount(new String(ch, start, length));
             bdiscount = false;
          } else if (baName) {
             acc.setName(new String(ch, start, length));
             baName = false;
          } else if (baPrice) {
             acc.setPrice(Integer.parseInt(new String(ch, start, length)));
             baPrice = false;
          } else if (baImage) {
             acc.setImage(new String(ch, start, length));
             baImage = false;
          } else if (baBrand) {
             acc.setBrand(new String(ch, start, length));
             baBrand = false;
          } else if (baDeals) {
             acc.setDeals(new String(ch, start, length));
             baDeals = false;
          } else if (baDiscount) {
             acc.setDiscount(new String(ch, start, length));
             baDiscount = false;
          } else if (baCategory) {
             acc.setCategory(new String(ch, start, length));
             baCategory = false;
          } else if (bquantity) {
             prod.setQuantity(Integer.parseInt(new String(ch, start, length)));
             bquantity = false;
          } else if (bprodOnSale) {
             prod.setProdOnSale(new String(ch, start, length));
             bprodOnSale = false;
          } else if (bmanuRebate) {
             prod.setManuRebate(new String(ch, start, length));
             bmanuRebate = false;
          } else if (baquantity) {
             acc.setQuantity(Integer.parseInt(new String(ch, start, length)));
             baquantity = false;
          } else if (baprodOnSale) {
             acc.setProdOnSale(new String(ch, start, length));
             baprodOnSale = false;
          } else if (bamanuRebate) {
             acc.setManuRebate(new String(ch, start, length));
             bamanuRebate = false;
          }

   }

   public HashMap<String,Product> getProductCatalog(){
        return productList;
   }

    public HashMap<String,Accessory> getAccessoryCatalog(){
        return accList;
   }
}
