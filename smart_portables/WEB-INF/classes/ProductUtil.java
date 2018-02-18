import java.util.*;
import java.io.*;
import javax.servlet.http.*;

public class ProductUtil {

    HttpServletRequest request;
    HttpSession session;
    SaxParserUtil saxParser;
    MySQLDataStoreUtilities sqlUtil = new MySQLDataStoreUtilities();

  	public ProductUtil(HttpServletRequest request){
          this.request = request;
          this.session = request.getSession(true);
          this.saxParser = new SaxParserUtil();
  	}

    public void addProduct(String pname,int pprice,String pimage,String pcategory,String pbrand,String pdiscount){

        HashMap<String,Product> productList;
        Product prod = new Product(pname, pprice, pimage, "normal", pbrand,  pdiscount, pcategory);
        int pid = sqlUtil.addProduct(prod);
        productList = saxParser.getProductCatalog();

    }

    public void deleteProduct(String prodId){
       // HashMap<String,Product> productList;
        sqlUtil.deleteProduct(prodId);

       // productList = saxParser.getProductCatalog();

    }

    public void updateProduct(String prodId,String pname,int pprice,String pbrand,String pdiscount){
        HashMap<String,Product> productList;
        productList = saxParser.getProductCatalog();
        if(productList.containsKey(prodId)){
            Product pd = productList.get(prodId);
            if(pd!=null){
                pd.setName(pname);
                pd.setPrice(pprice);
                pd.setBrand(pbrand);
                pd.setDiscount(pdiscount);
                //Update into database
                sqlUtil.updateProduct(pd);
            }
        }
    }

    public HashMap<String,Product> getProductList(){
        HashMap<String,Product> productList;
        productList = saxParser.getProductCatalog();
        return productList;
    }


}
