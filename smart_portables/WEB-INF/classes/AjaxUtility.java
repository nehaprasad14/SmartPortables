import java.sql.*;
import java.util.*;

public class AjaxUtility{
       Connection con = null;
       MySQLDataStoreUtilities mySQL = new MySQLDataStoreUtilities();

       public void AjaxUtility(){}

       // getProductData() gets the products data from MySQl database and store it in HashMap
       public HashMap<String,Product> getProductData(){
            Product prod;
            HashMap<String,Product> productList = new HashMap<String,Product>();
            try{
                con = mySQL.getConnection();
                con.setAutoCommit(true);
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("select * from smartportablesdb.products");

                while(rs.next()){
                    prod = new Product();
                    prod.setId(rs.getInt("prodid")+"");
                    prod.setName(rs.getString("pname"));
                    prod.setPrice(rs.getInt("price"));
                    prod.setImage(rs.getString("pimage"));
                    prod.setCategory(rs.getString("category"));
                    prod.setDeals(rs.getString("deals"));
                    prod.setBrand(rs.getString("brand"));
                    prod.setDiscount(rs.getString("discount"));
                    prod.setQuantity(rs.getInt("quantity"));
                    prod.setProdOnSale(rs.getString("prodOnSale"));
                    prod.setManuRebate(rs.getString("manuRebate"));

                    Statement stmt1=con.createStatement();
                    ResultSet rs1 = stmt1.executeQuery("select aid from smartportablesdb.accessories where prodid="+Integer.parseInt(prod.getId()));
                    List<String> accessories = new ArrayList<String>();
                    while(rs1.next()){
                        accessories.add(rs1.getInt("aid")+"");
                    }
                    rs1.close();
                    stmt1.close();
                    prod.setAccessories(accessories);
                    productList.put(prod.getId(),prod);
                }
                rs.close();
                stmt.close();

                //adding accessories also in products HashMap
                Statement stmt2=con.createStatement();
                ResultSet rs2=stmt2.executeQuery("select * from smartportablesdb.accessories group by aid;");
                while(rs2.next()){
                    prod = new Product();
                    prod.setId(rs2.getInt("aid")+"");
                    prod.setName(rs2.getString("aname"));
                    prod.setPrice(rs2.getInt("price"));
                    prod.setImage(rs2.getString("aimage"));
                    prod.setCategory(rs2.getString("acategory"));
                    prod.setDeals(rs2.getString("adeals"));
                    prod.setBrand(rs2.getString("abrand"));
                    prod.setDiscount(rs2.getString("adiscount"));
                    prod.setQuantity(rs2.getInt("quantity"));
                    prod.setProdOnSale(rs2.getString("prodOnSale"));
                    prod.setManuRebate(rs2.getString("manuRebate"));
                    productList.put(prod.getId(),prod);
                }
                rs2.close();
                stmt2.close();
            }catch(SQLException exception){
                exception.printStackTrace();
            }
            finally{
                mySQL.closeConnection();
            }
            return productList;
       }

       //readData() get the products starting with letter typed from HashMap into StringBuffer
       public StringBuffer readData(String searchId){
               System.out.println("searchId: "+searchId);
               StringBuffer sb = new StringBuffer();
               HashMap<String,Product> productList = getProductData();
               Iterator it = productList.entrySet().iterator();
               while(it.hasNext()){
                    Map.Entry prodEntry = (Map.Entry)it.next();
                    Product prod = (Product)prodEntry.getValue();
                    if(prod.getName().toLowerCase().startsWith(searchId.toLowerCase())){
                      System.out.println("prod.getName(): "+prod.getName());
                      sb.append("<product>");
                      sb.append("<productID>" + prod.getId() + "</productID>");
                      sb.append("<productName>" + prod.getName() + "</productName>");
                      sb.append("</product>");
                    }
               }
            return sb;
       }
}
