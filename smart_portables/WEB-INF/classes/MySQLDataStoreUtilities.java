import java.sql.*;
import java.util.*;

public class MySQLDataStoreUtilities{
	   Connection con = null;

     private static final String Drivername="com.mysql.jdbc.Driver";
	   private static final String USERNAME="root";
	   private static final String PASSWORD="root";
	   private static final String URL="jdbc:mysql://localhost:3306/smartportablesdb?useSSL=false";

       public void MySQLDataStoreUtilities(){}

       public Connection getConnection(){
            try{
              Class.forName(Drivername);
              con=DriverManager.getConnection(URL,USERNAME,PASSWORD);
            }catch(NullPointerException e){
						 e.printStackTrace();
		        }
		        catch(ClassNotFoundException e){
						e.printStackTrace();
		        }
		        catch (SQLException e){
						 e.printStackTrace();
		        }
            return con;
       }

       public void closeConnection(){
            try{
                con.close();
            }
            catch (SQLException se){
                se.printStackTrace();
            }
       }

       public void cleanDB(){
           try{
               con = getConnection();
               con.setAutoCommit(true);
               Statement stmt=con.createStatement();
               String cmd1 = "truncate table smartportablesdb.accessories";
               String cmd2 = "truncate table smartportablesdb.products";
               stmt.executeUpdate(cmd1);
               stmt.executeUpdate(cmd2);

           }catch(SQLException exception){
                exception.printStackTrace();
           }
           finally{
                closeConnection();
           }
       }

       public int addXMLProducts(HashMap<String,Product> productList,HashMap<String,Accessory> accList){
               int result = 0;

               Set keys = productList.keySet();
               for(Object obj:keys){
                    String prodID=(String)obj;
                    Product prod = (Product)productList.get(prodID);
                    // add products
                    if(Integer.parseInt(prod.getId()) < 501){
                        int pID = addProduct(prod);
                        if(pID !=0 ){
                            List<String> accessories = prod.getAccessories();
                            for(String aid:accessories){
                                Accessory acc = (Accessory)accList.get(aid);
                                //add Accessory
                                result = addAccessory(acc, pID);
                            }
                        }
                    }
               }

              return result;
       }

       public int addProduct(Product prod){
            con = getConnection();
            PreparedStatement ps1=null;
            String command1=null;
            int result1=0;
            int prodID = 0;

            if(ps1==null){
                try{
                    command1 = "insert into products(pname,price,pimage,category,brand,deals,discount,quantity,prodOnSale,manuRebate) values(?,?,?,?,?,?,?,?,?,?)";

                    ps1=con.prepareStatement(command1);

                    ps1.setString(1, prod.getName());
                    ps1.setInt(2, prod.getPrice());
                    ps1.setString(3, prod.getImage());
                    ps1.setString(4, prod.getCategory());
                    ps1.setString(5, prod.getBrand() );
                    ps1.setString(6, prod.getDeals());
                    ps1.setString(7, prod.getDiscount());
										ps1.setInt(8, prod.getQuantity());
										ps1.setString(9, prod.getProdOnSale());
										ps1.setString(10, prod.getManuRebate());
                    result1=ps1.executeUpdate();
                    ps1.close();

                    if(result1 != 0){
                        Statement stmt=con.createStatement();
                        ResultSet rs=stmt.executeQuery("Select prodid from products order by prodid desc limit 1");

                        while(rs.next()){
                            prodID = rs.getInt("prodid");
                        }
                    }
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                finally{
                    closeConnection();
                }
            }
            return prodID;
       }

       public void updateProduct(Product prod){
            con = getConnection();
            PreparedStatement ps1=null;
            String command1=null;
            int result1=0;

            if(ps1==null){
                try{
                    command1 = "update products set pname = ?, price = ?, brand = ?,discount = ? where prodid = ? ";
                    ps1=con.prepareStatement(command1);

                    ps1.setString(1, prod.getName());
                    ps1.setInt(2, prod.getPrice());
                    ps1.setString(3, prod.getBrand() );
                    ps1.setString(4, prod.getDiscount());
                    ps1.setInt(5, Integer.parseInt(prod.getId()));

                    result1=ps1.executeUpdate();
                    ps1.close();

                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                finally{
                    closeConnection();
                }
            }

       }

       public void deleteProduct(String prodId){
            con = getConnection();
            PreparedStatement ps1=null;
            String command1=null;

            if(ps1==null){
                try{
                    command1 = "delete from smartportablesdb.products where prodid = ?";

                    ps1=con.prepareStatement(command1);
                    ps1.setInt(1, Integer.parseInt(prodId));
                    ps1.executeUpdate();
                    ps1.close();

                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                finally{
                    closeConnection();
                }
            }
       }

       public int addAccessory(Accessory acc, int prodID){
            con = getConnection();
            PreparedStatement ps1=null;
            String command1=null;
            int result = 0;

            if(ps1==null){
                try{
                    command1 = "insert into accessories(aname,price,aimage,acategory,abrand,adeals,adiscount,prodid,aid,quantity,prodOnSale,manuRebate) values(?,?,?,?,?,?,?,?,?,?,?,?)";

                    ps1=con.prepareStatement(command1);

                    ps1.setString(1, acc.getName());
                    ps1.setInt(2, acc.getPrice());
                    ps1.setString(3, acc.getImage());
                    ps1.setString(4, acc.getCategory());
                    ps1.setString(5, acc.getBrand() );
                    ps1.setString(6, acc.getDeals());
                    ps1.setString(7, acc.getDiscount());
                    ps1.setInt(8, prodID);
                    ps1.setInt(9, Integer.parseInt(acc.getId()));
										ps1.setInt(10, acc.getQuantity());
										ps1.setString(11, acc.getProdOnSale());
										ps1.setString(12, acc.getManuRebate());

                    result = ps1.executeUpdate();
                    ps1.close();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                finally{
                    closeConnection();
                }
            }

           return result;
       }

       public User getUser(final String username) throws ClassNotFoundException,SQLException{
            User user = null;
            try{
                con = getConnection();
                con.setAutoCommit(true);
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("Select * from user where username='"+username+"'");
                while(rs.next()){
                    user=new User();
                    user.setUid(rs.getInt("userid"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                }
                rs.close();
                stmt.close();
            }catch(SQLException exception){
                exception.printStackTrace();
            }
            finally{
                closeConnection();
            }
            return user;
	   }

       public int addUser(User user) throws ClassNotFoundException,SQLException{
            con = getConnection();
            PreparedStatement ps1=null;
            String command1=null;
            int result1=0;

            if(ps1==null){
                try{
                    command1 = "insert into user(username,email,password,role) values(?,?,?,?)";

                    ps1=con.prepareStatement(command1);

                    ps1.setString(1, user.getUsername());
                    ps1.setString(2, user.getEmail());
                    ps1.setString(3, user.getPassword());
                    ps1.setString(4, user.getRole());

                    result1=ps1.executeUpdate();
                    ps1.close();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                finally{
                    closeConnection();
                }
            }
            return result1;
	   }

       public HashMap<String, ArrayList<OrderDetails>> getOrdersByUsername(String username){

         HashMap<String, ArrayList<OrderDetails>> orderList = new HashMap<String, ArrayList<OrderDetails>>();

            OrderDetails od = null;
            try{
                con = getConnection();
                con.setAutoCommit(true);
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("Select * from orders where username='"+username+"'");
                ArrayList<OrderDetails> od1 = new ArrayList<OrderDetails>();

                while(rs.next()){
                    od = new OrderDetails();
                    od.setOid(rs.getInt("oid")+"");
                    od.setUsername(rs.getString("username"));
                    od.setFullname(rs.getString("fullname"));
                    od.setAddress1(rs.getString("address1"));
                    od.setAddress2(rs.getString("address2"));
                    od.setZipcode(rs.getString("zipcode"));
                    od.setDate(rs.getString("date_place"));
                    od.setDeliveryDate(rs.getString("delivery_date"));
                    od.setCreditcard(rs.getString("creditcard"));
                    od.setCvv(rs.getInt("cvv"));
                    od.setTotal(rs.getInt("total_price"));
                    od.setItemscount(rs.getInt("itemscount"));

                    //get order items for each order
                    Statement stmt1=con.createStatement();
                    ResultSet rs1=stmt1.executeQuery("Select * from order_items where oid="+Integer.parseInt(od.getOid()));
                    List<OrderItem> order = new ArrayList<OrderItem>();
                    while(rs1.next()){
                        OrderItem oi = new OrderItem((rs1.getInt("item_id")+""),rs1.getString("item_name"), rs1.getInt("item_price"), rs1.getString("category"),rs1.getString("brand"));
                        order.add(oi);
                    }
                    rs1.close();
                    stmt1.close();
                    od.setOrder(order);
                    od1.add(od);

                }
                rs.close();
                orderList.put(username,od1);

                stmt.close();
            }
            catch(SQLException exception){
                exception.printStackTrace();
            }
            finally{
                closeConnection();
            }
            return orderList;
       }

       public HashMap<String, ArrayList<OrderDetails>> getAllOrders(){

         HashMap<String, ArrayList<OrderDetails>> orderList = new HashMap<String, ArrayList<OrderDetails>>();

            OrderDetails od = null;
            try{
                con = getConnection();
                con.setAutoCommit(true);
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("Select * from orders");
                ArrayList<OrderDetails> od1 = new ArrayList<OrderDetails>();

                while(rs.next()){
                    od = new OrderDetails();
                    od.setOid(rs.getInt("oid")+"");
                    od.setUsername(rs.getString("username"));
                    od.setFullname(rs.getString("fullname"));
                    od.setAddress1(rs.getString("address1"));
                    od.setAddress2(rs.getString("address2"));
                    od.setZipcode(rs.getString("zipcode"));
                    od.setDate(rs.getString("date_place"));
                    od.setDeliveryDate(rs.getString("delivery_date"));
                    od.setCreditcard(rs.getString("creditcard"));
                    od.setCvv(rs.getInt("cvv"));
                    od.setTotal(rs.getInt("total_price"));
                    od.setItemscount(rs.getInt("itemscount"));

                    //get order items for each order
                    Statement stmt1=con.createStatement();
                    ResultSet rs1=stmt1.executeQuery("Select * from order_items where oid="+Integer.parseInt(od.getOid()));
                    List<OrderItem> order = new ArrayList<OrderItem>();
                    while(rs1.next()){
                        OrderItem oi = new OrderItem((rs1.getInt("item_id")+""),rs1.getString("item_name"), rs1.getInt("item_price"), rs1.getString("category"),rs1.getString("brand"));
                        order.add(oi);
                    }
                    rs1.close();
                    stmt1.close();
                    od.setOrder(order);
                    od1.add(od);

                }
                rs.close();
                orderList.put(od.getUsername(),od1);

                stmt.close();
            }
            catch(SQLException exception){
                exception.printStackTrace();
            }
            finally{
                closeConnection();
            }
            return orderList;
       }

       public void updateOrders(String address1,String address2,String zipcode,String orderNum){
            con = getConnection();
            PreparedStatement ps1=null;
            String command1=null;
            int result1=0;

            if(ps1==null){
                try{
                    command1 = "update orders set address1 = ?, address2 = ?, zipcode = ? where oid = ? ";
                    ps1=con.prepareStatement(command1);

                    ps1.setString(1, address1);
                    ps1.setString(2, address2);
                    ps1.setString(3, zipcode);
                    ps1.setInt(4, Integer.parseInt(orderNum));

                    result1=ps1.executeUpdate();
                    ps1.close();

                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                finally{
                    closeConnection();
                }
            }
       }

       public String insertCustomerOrder(OrderDetails od)throws ClassNotFoundException,SQLException{
            String orderID = "0";
            con = getConnection();
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;

            String command1 = null;
            String command2 = null;

            int result1 = 0;
            int result2 = 0;

            if(ps1==null || ps2==null){
                try{
                    command1 = "insert into orders(username,fullname,address1,address2,zipcode,date_place,delivery_date,creditcard,cvv,total_price,itemscount) values(?,?,?,?,?,?,?,?,?,?,?)";

                    ps1=con.prepareStatement(command1);

                    ps1.setString(1, od.getUsername());
                    ps1.setString(2, od.getFullname());
                    ps1.setString(3, od.getAddress1());
                    ps1.setString(4, od.getAddress2());
                    ps1.setString(5, od.getZipcode());
                    ps1.setString(6, od.getDate());
                    ps1.setString(7, od.getDeliveryDate());
                    ps1.setString(8, od.getCreditcard());
                    ps1.setInt(9,od.getCvv());
                    ps1.setInt(10,od.getTotal());
                    ps1.setInt(11,od.getItemscount());

                    result1 = ps1.executeUpdate();
                    ps1.close();

                    if(result1!=0){
                        command2 = "insert into order_items(item_id, item_name,item_price,category,brand,oid) values(?,?,?,?,?,?)";
                        ps2=con.prepareStatement(command2);

                        Statement stmt=con.createStatement();
                        ResultSet rs=stmt.executeQuery("Select oid from orders where username='"+od.getUsername()+"' order by oid desc limit 1");

                        while(rs.next()){
                            od.setOid(rs.getInt("oid")+"");
                            orderID = od.getOid();
                        }

                        for (OrderItem oi : od.getOrder()){
                            ps2.setInt(1,Integer.parseInt(oi.getId()));
                            ps2.setString(2,oi.getName());
                            ps2.setInt(3,oi.getPrice());
                            ps2.setString(4,oi.getCategory());
                            ps2.setString(5,oi.getBrand());
                            ps2.setInt(6,Integer.parseInt(orderID));
                            result2=ps2.executeUpdate();

														//************  Update product quantities  *******************
														updateProductDetails(oi.getId());
                        }

                         ps2.close();

				    			 }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }catch (SQLException e) {
                    e.printStackTrace();
                }finally{
                    closeConnection();
                }
           }
           return orderID;
       }

			 public void updateProductDetails(String oid){
						 int oID = Integer.parseInt(oid);
						 con = getConnection();
						 if(oID < 500){
							  try{
											Statement stmt1 = con.createStatement();
											ResultSet rs1 = stmt1.executeQuery("select quantity from products where prodid = "+oID);
											int quantity = 0;
											while(rs1.next()){
												quantity = rs1.getInt("quantity");
											}
											String command1 = "update products set quantity = ? where prodid = ?";
											PreparedStatement ps1 = con.prepareStatement(command1);
											quantity--;
											ps1.setInt(1,quantity);
											ps1.setInt(2,oID);
											ps1.execute();
									}catch (NullPointerException e){
	                    e.printStackTrace();
	                }catch (SQLException e) {
	                    e.printStackTrace();
	                }finally{
	                    closeConnection();
	                }
						}else{
							     try{
												Statement stmt1 = con.createStatement();
												ResultSet rs1 = stmt1.executeQuery("select quantity from accessories where aid = "+oID);
												int quantity = 0;
												while(rs1.next()){
													quantity = rs1.getInt("quantity");
												}
												String command1 = "update accessories set quantity = ? where aid = ?";
												PreparedStatement ps1 = con.prepareStatement(command1);
												quantity--;
 											  ps1.setInt(1,quantity);
												ps1.setInt(2,oID);
												ps1.execute();
										}catch (NullPointerException e){
												e.printStackTrace();
										}catch (SQLException e) {
												e.printStackTrace();
										}finally{
												closeConnection();
										}
						}
			 }


       public void deleteCustomerOrder(String username,String orderId) throws ClassNotFoundException,SQLException{
            con = getConnection();
            PreparedStatement ps1=null;
            PreparedStatement ps2=null;
            String command1=null;
            String command2=null;

            if(ps1==null){
                try{
                    command1 = "delete from order_items where oid = ?";
                    command2 = "delete from orders where oid = ? and username = ?";

										//check items from the deleted order.
							      Statement stmt = con.createStatement();
							      ResultSet rs = stmt.executeQuery("select item_id from order_items where oid = "+Integer.parseInt(orderId));
							      int[] order_items = new int[70];
							      int i = 0;
							      while(rs.next()){
							        order_items[i] = rs.getInt("item_id");
							        i++;
							      }

                    ps1=con.prepareStatement(command1);
                    ps1.setInt(1, Integer.parseInt(orderId));
                    ps1.executeUpdate();
                    ps1.close();

                    ps2=con.prepareStatement(command2);
                    ps2.setInt(1, Integer.parseInt(orderId));
                    ps2.setString(2, username);
                    ps2.executeUpdate();
                    ps2.close();

										updateProductQuantity(order_items);
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                finally{
                    closeConnection();
                }
            }
       }

			 public void updateProductQuantity(int[] order_items){
				 for(int id : order_items){
								 if(id < 500){
									 try{
											 Statement stmt1 = con.createStatement();
											 ResultSet rs1 = stmt1.executeQuery("select quantity from products where prodid = "+id);
											 int quantity = 0;
											 while(rs1.next()){
												 quantity = rs1.getInt("quantity");
											 }
											 String command1 = "update products set quantity = ? where prodid = ?";
											 PreparedStatement ps1 = con.prepareStatement(command1);
											 quantity++;
											 ps1.setInt(1,quantity);
											 ps1.setInt(2,id);
											 ps1.execute();
									 }catch (NullPointerException e){
											 e.printStackTrace();
									 }catch (SQLException e) {
											 e.printStackTrace();
									 }finally{
											 closeConnection();
									 }
								 }
								 else{
									 try{
											 Statement stmt1 = con.createStatement();
											 ResultSet rs1 = stmt1.executeQuery("select quantity from accessories where aid = "+id);
											 int quantity = 0;
											 while(rs1.next()){
												 quantity = rs1.getInt("quantity");
											 }
											 String command1 = "update accessories set quantity = ? where aid = ?";
											 PreparedStatement ps1 = con.prepareStatement(command1);
											 quantity++;
											 ps1.setInt(1,quantity);
											 ps1.setInt(2,id);
											 ps1.execute();
									 }catch (NullPointerException e){
											 e.printStackTrace();
									 }catch (SQLException e) {
											 e.printStackTrace();
									 }finally{
											 closeConnection();
									 }
								 }
						 }
			 }

       public HashMap<String,Product> getProductCatalog(){
            Product prod;
            HashMap<String,Product> productList = new HashMap<String,Product>();
            try{
                con = getConnection();
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
            }catch(SQLException exception){
                exception.printStackTrace();
            }
            finally{
                closeConnection();
            }

            return productList;
       }

       public HashMap<String,Accessory> getAccessoryCatalog(){
            Accessory acc = null;
            HashMap<String,Accessory> accList = new HashMap<String,Accessory>();

            try{
                con = getConnection();
                con.setAutoCommit(true);
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("select distinct aname, aid, price, aimage, acategory,abrand,adeals,adiscount,quantity,prodOnSale,manuRebate from smartportablesdb.accessories group by aname");

                while(rs.next()){
                    acc = new Accessory();
                    acc.setId(rs.getInt("aid")+"");
                    acc.setName(rs.getString("aname"));
                    acc.setPrice(rs.getInt("price"));
                    acc.setImage(rs.getString("aimage"));
                    acc.setCategory(rs.getString("acategory"));
                    acc.setDeals(rs.getString("adeals"));
                    acc.setBrand(rs.getString("abrand"));
                    acc.setDiscount(rs.getString("adiscount"));
										acc.setQuantity(rs.getInt("quantity"));
										acc.setProdOnSale(rs.getString("prodOnSale"));
										acc.setManuRebate(rs.getString("manuRebate"));
                    accList.put(acc.getId(),acc);
                }
                rs.close();
                stmt.close();
            }catch(SQLException exception){
                exception.printStackTrace();
            }
            finally{
                closeConnection();
            }

            return accList;
       }

			 public ArrayList<Product> getProductSalesStat(){
						 Product prod;
						 ArrayList<Product> prodSoldList = new ArrayList<Product>();
						 try{
								 con = getConnection();
								 con.setAutoCommit(true);
								 Statement stmt=con.createStatement();
								 ResultSet rs=stmt.executeQuery("select item_name,item_price,count(item_name) as items_sold,(item_price * count(item_name)) as tot_sales from smartportablesdb.order_items group by item_id");
								 while(rs.next()){
										 prod = new Product();
										 prod.setName(rs.getString("item_name"));
										 prod.setPrice(rs.getInt("item_price"));
										 prod.setQuantity(rs.getInt("items_sold"));
										 prod.setDiscount(rs.getInt("tot_sales")+"");
										 prodSoldList.add(prod);
								 }
								 rs.close();
								 stmt.close();
						 }catch(SQLException exception){
								 exception.printStackTrace();
						 }
						 finally{
								 closeConnection();
						 }
						 return prodSoldList;
			 }

			 public ArrayList<Product> getDailySalesTransaction(){
						 Product prod;
						 ArrayList<Product> salesTransList = new ArrayList<Product>();
						 try{
								 con = getConnection();
								 con.setAutoCommit(true);
								 Statement stmt=con.createStatement();
								 ResultSet rs=stmt.executeQuery("select date_place,sum(total_price) as total_sales from smartportablesdb.orders group by date_place");
								 while(rs.next()){
										 prod = new Product();
										 prod.setName(rs.getString("date_place"));
										 prod.setQuantity(rs.getInt("total_sales"));
										 salesTransList.add(prod);
								 }
								 rs.close();
								 stmt.close();
						 }catch(SQLException exception){
								 exception.printStackTrace();
						 }
						 finally{
								 closeConnection();
						 }
						 return salesTransList;
			 }
}
