//DataAnalyticsUtilities
import java.util.*;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.DBCollection;
import com.mongodb.AggregationOutput;
import java.text.DecimalFormat;

@SuppressWarnings("deprecation")
public class DataAnalyticsUtilities {

        public DataAnalyticsUtilities(){}

        MongoDBDataStoreUtilities mdb = new MongoDBDataStoreUtilities();

        public ArrayList<ProductReview> getReviewDataByFilter(BasicDBObject query){
                DB db = mdb.getConnection();
                DBCollection productReviews = db.getCollection("productReviews");
                DBCursor cursor = productReviews.find(query);

                ArrayList<ProductReview> prodList = new ArrayList<ProductReview>();
                ProductReview pr = null;
            		while (cursor.hasNext()){
              			BasicDBObject bobj = (BasicDBObject) cursor.next();
                    pr = new ProductReview();
                    pr.setProdModelName(bobj.getString("ProductModelName"));
                    pr.setProdCategory(bobj.getString("ProductCategory"));
                    pr.setProdPrice(Integer.parseInt(bobj.getString("ProductPrice")));
                    pr.setReviewRating(Integer.parseInt(bobj.getString("ReviewRating")));
                    pr.setRetailerCity(bobj.getString("RetailerCity"));
                    pr.setReviewText(bobj.getString("ReviewText"));
                    prodList.add(pr);
            		}
            return prodList;
        }

        public ArrayList<ProductReview> getReviewByGroup(DBObject group,DBObject orderBy,DBObject limit){
                  DB db = mdb.getConnection();
                  DBCollection productReviews = db.getCollection("productReviews");

                  AggregationOutput aggregate = productReviews.aggregate(group,orderBy,limit);

                  ArrayList<ProductReview> prodList = new ArrayList<ProductReview>();
                  ProductReview pr = null;

                  for(DBObject result: aggregate.results()){
                      BasicDBObject bobj = (BasicDBObject) result;
                      pr = new ProductReview();
                      pr.setCount(Integer.parseInt(bobj.getString("ReviewValue")));
                      pr.setProdModelName(bobj.getString("ReviewKey"));
                      pr.setUserGender(bobj.getString("Median"));
                      prodList.add(pr);
                  }
              return prodList;
        }

        public HashMap<String, ArrayList<Object>> getReviewByMaxGroup(DBObject group,DBObject orderBy,DBObject limit){
                  DB db = mdb.getConnection();
                  DBCollection productReviews = db.getCollection("productReviews");

                  AggregationOutput aggregate = productReviews.aggregate(group,orderBy,limit);
                  HashMap<String, ArrayList<Object>> hmReview = new HashMap<String, ArrayList<Object>>();
                  String _id = "";
              		int maxValue = 0;
              		String productModelName = "";
                  for (DBObject result : aggregate.results()){
                      BasicDBObject bobj = (BasicDBObject) result;
                			_id = bobj.getString("_id");
                			maxValue = Integer.parseInt(bobj.getString("maxValue"));
                			productModelName = bobj.getString("ProductModelName");
                			ArrayList<Object> values = new ArrayList<Object>();
                			values.add(_id);
                			values.add(maxValue);
                			values.add(productModelName);
                			hmReview.put(_id, values);
              		}
              return hmReview;
        }

        public HashMap<String, ArrayList<ProductReview>> getProdListByGroup(String groupByOpt){
                  DB db = mdb.getConnection();
                  DBCollection productReviews = db.getCollection("productReviews");
                  AggregationOutput result1 = productReviews.aggregate(new BasicDBObject("$group",new BasicDBObject("_id", '$'+groupByOpt)));

                  HashMap<String, ArrayList<ProductReview>> hm = new HashMap<String, ArrayList<ProductReview>>();
                  String grpByValue="";
              		for (DBObject doc : result1.results()){
                  			grpByValue = (String) doc.get("_id");
                  			BasicDBObject query = new BasicDBObject(groupByOpt, new BasicDBObject("$eq", grpByValue));
                  			DBCursor cursor = productReviews.find(query);
                        ArrayList<ProductReview> prList = new ArrayList<ProductReview>();
                  			while (cursor.hasNext()){
                  				BasicDBObject obj= (BasicDBObject) cursor.next();
                          ProductReview pr = new ProductReview();
                          pr.setProdModelName(obj.get("ProductModelName").toString());
                          pr.setProdCategory(obj.get("ProductCategory").toString());
                          pr.setProdPrice(Integer.parseInt(obj.get("ProductPrice").toString()));
                          pr.setRetailerName(obj.get("RetailerName").toString());
                          pr.setRetailerZip(obj.get("RetailerZip").toString());
                          pr.setRetailerCity(obj.get("RetailerCity").toString());
                          pr.setRetailerState(obj.get("RetailerState").toString());
                          pr.setProdOnSale(obj.get("ProductOnSale").toString());
                          pr.setManuName(obj.get("ManufactureName").toString());
                          pr.setManuRebate(obj.get("ManufactureRebate").toString());
                          pr.setUserID(obj.get("UserID").toString());
                          pr.setUserAge(Integer.parseInt(obj.get("UserAge").toString()));
                          pr.setUserGender(obj.get("UserGender").toString());
                          pr.setUserOccupation(obj.get("UserOccupation").toString());
                          pr.setReviewRating(Integer.parseInt(obj.get("ReviewRating").toString()));
                          pr.setReviewDate(obj.get("ReviewDate").toString());
                          pr.setReviewText(obj.get("ReviewText").toString());
                          prList.add(pr);
                  			}
                  			hm.put(grpByValue, prList);
              		}
              return hm;
        }

        public LinkedHashMap<String, LinkedHashMap<String, ArrayList<Object>>> getTop5ListOfLikedProduct(String groupByOpt, String filter){
          DB db = mdb.getConnection();
          DBCollection productReviews = db.getCollection("productReviews");
          LinkedHashMap<String, LinkedHashMap<String, ArrayList<Object>>> top5ProdList = new LinkedHashMap<String, LinkedHashMap<String, ArrayList<Object>>>();

      		AggregationOutput grpResult = productReviews.aggregate(new BasicDBObject("$group", new BasicDBObject("_id", '$'+groupByOpt)));

      		String grpByValue="";
      		for (DBObject doc : grpResult.results()){
            			grpByValue = (String) doc.get("_id");

                  DBObject groupFields = new BasicDBObject("_id",0);
                  groupFields.put("_id","$ProductModelName");
                  groupFields.put("avgRating",new BasicDBObject("$avg", "$ReviewRating"));

                  DBObject group = new BasicDBObject("$group",groupFields);

                  DBObject sort = new BasicDBObject();
                  if(filter.equalsIgnoreCase("liked"))
                      sort.put("avgRating",-1);
                  else
                      sort.put("avgRating",1);
                  DBObject orderBy = new BasicDBObject();
                  orderBy = new BasicDBObject("$sort", sort);

                  DBObject limit = new BasicDBObject();
                  limit = new BasicDBObject("$limit", 5);

                  DBObject match = new BasicDBObject("$match",  new BasicDBObject(groupByOpt ,
                        new BasicDBObject("$eq", grpByValue)));

            			AggregationOutput output =  productReviews.aggregate(match, group, orderBy, limit);

            			String productName="";
            			double avg = 0;
                  DecimalFormat df = new DecimalFormat("###.##");
            			LinkedHashMap<String, ArrayList<Object>> productsListHashMap = new LinkedHashMap<String, ArrayList<Object>>();
            			for (DBObject bobj2 : output.results()){
                				productName = (String) bobj2.get("_id");
                				avg = (Double) bobj2.get("avgRating");
                				ArrayList<Object> values = new ArrayList<Object>();
                				values.add(grpByValue);
                				values.add(productName);
                				values.add(df.format(avg));
                				productsListHashMap.put(productName, values);
            			}
                  top5ProdList.put(grpByValue, productsListHashMap);
      		}

      		return top5ProdList;
      	}

        public LinkedHashMap<String, LinkedHashMap<String, ArrayList<Object>>> getReviewByGroupSelect(DBObject query, DBObject group,DBObject orderBy,DBObject limit){
              DB db = mdb.getConnection();
              DBCollection productReviews = db.getCollection("productReviews");
              LinkedHashMap<String, LinkedHashMap<String, ArrayList<Object>>> top5ProdList = new LinkedHashMap<String, LinkedHashMap<String, ArrayList<Object>>>();

              AggregationOutput grpResult = productReviews.aggregate(group);
              String retailerCity="";
          		for (DBObject doc : grpResult.results()){
              			retailerCity = (String) doc.get("_id");
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    System.out.println(">>>>>>>>>>>>>> "+ retailerCity +"  >>>>>>>>>>>>>>");
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
              }

              return top5ProdList;
        }
}
