import java.io.*;
import java.util.*;

public class DealMatches implements java.io.Serializable {
        ArrayList<String> tweetsList = new ArrayList<String>();
        Product prod;
        HashMap<String,Product> selectedProductList = new HashMap<String,Product>();


        public HashMap<String,Product> getTweets() {
           MySQLDataStoreUtilities sqlUtil = new MySQLDataStoreUtilities();
           HashMap<String,Product> productList = sqlUtil.getProductCatalog();
           try{
                File file = new File("C:\\apache-tomcat-7.0.34\\webapps\\smart_portables\\DealMatches.txt");
                String path = file.getPath();
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
                String str;
                while ((str = in.readLine()) != null){
                  tweetsList.add(str);
                }
                in.close();

                int count = 0;
                for(String prodID: productList.keySet()){
                     prod = (Product)productList.get(prodID);
                     for (String deal : tweetsList){
                         if (count >= 2)
                           break;
                         if (deal.contains(prod.getName())) {
                            System.out.println("Product Name: "+prod.getName());
                           selectedProductList.put(deal,prod);
                           count++;
                         }
                     }
                }

            }catch(Exception e){
                 e.printStackTrace();
            }

            return selectedProductList;
        }
}
