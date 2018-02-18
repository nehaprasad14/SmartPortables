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
public class MongoDBDataStoreUtilities {

        public MongoDBDataStoreUtilities(){}

        public DB getConnection() {
          try{
              @SuppressWarnings("resource")
              MongoClient mongo = new MongoClient("localhost", 27017);
              @SuppressWarnings("deprecation")
              DB db = mongo.getDB("smartportablesdb");
              return db;
          }catch(Exception e){
              System.out.println("MongoDB server down !!");
              return null;
          }
        }

        public void insertProductReview(ProductReview pr) {
            DB db = getConnection();
            DBCollection productReviews = db.getCollection("productReviews");

            BasicDBObject doc = new BasicDBObject("title", "productReviews")
                    .append("ProductModelName", pr.getProdModelName())
                    .append("ProductCategory", pr.getProdCategory())
                    .append("ProductPrice", pr.getProdPrice())
                    .append("RetailerName", pr.getRetailerName())
                    .append("RetailerZip", pr.getRetailerZip())
                    .append("RetailerCity", pr.getRetailerCity())
                    .append("RetailerState", pr.getRetailerState())
                    .append("ProductOnSale", pr.getProdOnSale())
                    .append("ManufactureName", pr.getManuName())
                    .append("ManufactureRebate", pr.getManuRebate())
                    .append("UserID", pr.getUserID())
                    .append("UserAge", pr.getUserAge())
                    .append("UserGender", pr.getUserGender())
                    .append("UserOccupation", pr.getUserOccupation())
                    .append("ReviewRating", pr.getReviewRating())
                    .append("ReviewDate", pr.getReviewDate())
                    .append("ReviewText", pr.getReviewText());
            productReviews.insert(doc);
        }

        public HashMap<String,ArrayList<ProductReview>> getProductReview() {
          		DB db = getConnection();
          		DBCollection productReviews = db.getCollection("productReviews");
          		HashMap<String,ArrayList<ProductReview>> prHashMap = new HashMap<String,ArrayList<ProductReview>>();
          		DBCursor cursor = productReviews.find();
              ArrayList<ProductReview> prList = null;
              ProductReview pr = null;
          		while (cursor.hasNext()) {
                    BasicDBObject obj = (BasicDBObject)cursor.next();

                    if(!prHashMap.containsKey(obj.get("ProductModelName").toString())){
                        ArrayList<ProductReview> prList1 = new ArrayList<ProductReview>();
                        prHashMap.put(obj.get("ProductModelName").toString(), prList1);
                    }
                    prList = prHashMap.get(obj.get("ProductModelName").toString());
              			pr = new ProductReview();
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
        		return prHashMap;
        	}

        //Top 5 most liked products

        public ArrayList<ProductReview> getTopProductsByRating(){
                DB db = getConnection();
                DBCollection productReviews = db.getCollection("productReviews");

                DBObject groupFields = new BasicDBObject("_id",0);
                groupFields.put("_id","$ProductModelName");
                groupFields.put("ProductModelName",new BasicDBObject("$first", "$ProductModelName"));
                groupFields.put("ReviewRating",new BasicDBObject("$avg", "$ReviewRating"));
                groupFields.put("ProductCategory",new BasicDBObject("$first", "$ProductCategory"));
                groupFields.put("ProductPrice",new BasicDBObject("$first", "$ProductPrice"));

                DBObject group = new BasicDBObject("$group",groupFields);

                DBObject sort = new BasicDBObject();
                sort.put("ReviewRating",-1);
                DBObject orderBy = new BasicDBObject();
                orderBy = new BasicDBObject("$sort", sort);

                DBObject limit = new BasicDBObject();
                limit = new BasicDBObject("$limit", 5);

                AggregationOutput aggregate = productReviews.aggregate(group,orderBy,limit);
                DecimalFormat df = new DecimalFormat("###.##");
                ArrayList<ProductReview> prodList = new ArrayList<ProductReview>();
                ProductReview pr = null;
                for(DBObject result: aggregate.results()){
                    BasicDBObject bobj = (BasicDBObject) result;
                    pr = new ProductReview();
                    pr.setProdModelName(bobj.getString("ProductModelName"));
                    pr.setProdCategory(bobj.getString("ProductCategory"));
                    pr.setProdPrice(Integer.parseInt(bobj.getString("ProductPrice")));
                    Double avg = (Double) bobj.get("ReviewRating");
                    pr.setProdOnSale(df.format(avg)+"");
                    prodList.add(pr);
                }
            return prodList;
        }

        //Top 5 zip-codes (maximum products sold)
        public ArrayList<ProductReview> getTopProductsByZipcodes(){
                  DB db = getConnection();
                  DBCollection productReviews = db.getCollection("productReviews");

                  DBObject groupFields = new BasicDBObject("_id",0);
                  groupFields.put("_id","$RetailerZip");
                  groupFields.put("Count",new BasicDBObject("$sum", 1));
                  groupFields.put("RetailerZip",new BasicDBObject("$first", "$RetailerZip"));

                  DBObject group = new BasicDBObject("$group",groupFields);

                  DBObject sort = new BasicDBObject();
                  sort.put("Count",-1);
                  DBObject orderBy = new BasicDBObject();
                  orderBy = new BasicDBObject("$sort", sort);

                  DBObject limit = new BasicDBObject();
                  limit = new BasicDBObject("$limit", 5);

                  AggregationOutput aggregate = productReviews.aggregate(group,orderBy,limit);

                  ArrayList<ProductReview> prodList = new ArrayList<ProductReview>();
                  ProductReview pr = null;
                  for(DBObject result: aggregate.results()){
                      BasicDBObject bobj = (BasicDBObject) result;
                      pr = new ProductReview();
                      pr.setProdPrice(Integer.parseInt(bobj.getString("Count")));
                      pr.setRetailerZip(bobj.getString("RetailerZip"));
                      prodList.add(pr);
                  }
              return prodList;
        }

        //Top 5 most sold products regardless of the rating
        public ArrayList<ProductReview> getTopProductsSold(){
                DB db = getConnection();
                DBCollection productReviews = db.getCollection("productReviews");

                DBObject groupFields = new BasicDBObject("_id",0);
                groupFields.put("_id","$ProductModelName");
                groupFields.put("Count",new BasicDBObject("$sum", 1));
                groupFields.put("ReviewRating",new BasicDBObject("$max", "$ReviewRating"));
                groupFields.put("ProductModelName",new BasicDBObject("$first", "$ProductModelName"));
                groupFields.put("ProductPrice",new BasicDBObject("$first", "$ProductPrice"));

                DBObject group = new BasicDBObject("$group",groupFields);

                DBObject sort = new BasicDBObject();
                sort.put("Count",-1);
                DBObject orderBy = new BasicDBObject();
                orderBy = new BasicDBObject("$sort", sort);

                DBObject limit = new BasicDBObject();
                limit = new BasicDBObject("$limit", 5);

                AggregationOutput aggregate = productReviews.aggregate(group,orderBy,limit);

                ArrayList<ProductReview> prodList = new ArrayList<ProductReview>();
                ProductReview pr = null;
                for(DBObject result: aggregate.results()){
                    BasicDBObject bobj = (BasicDBObject) result;
                    pr = new ProductReview();
                    pr.setProdPrice(Integer.parseInt(bobj.getString("ProductPrice")));
                    pr.setUserAge(Integer.parseInt(bobj.getString("Count")));
                    pr.setProdModelName(bobj.getString("ProductModelName"));
                    pr.setReviewRating(Integer.parseInt(bobj.getString("ReviewRating")));
                    prodList.add(pr);
                }
            return prodList;
        }

        //Top 5 zip-codes (maximum products sold)
      /*  public ArrayList<ProductReview> getProductCount(){
                  DB db = getConnection();
                  DBCollection productReviews = db.getCollection("productReviews");

                  DBObject groupFields = new BasicDBObject("_id",0);
                  groupFields.put("_id","$ProductModelName");
                  groupFields.put("Count",new BasicDBObject("$sum", 1));
                  groupFields.put("ProductModelName",new BasicDBObject("$first", "$ProductModelName"));

                  DBObject group = new BasicDBObject("$group",groupFields);

                  DBObject sort = new BasicDBObject();
                  sort.put("Count",-1);
                  DBObject orderBy = new BasicDBObject();
                  orderBy = new BasicDBObject("$sort", sort);

                //  DBObject limit = new BasicDBObject();
                  //limit = new BasicDBObject("$limit", 5);

                  AggregationOutput aggregate = productReviews.aggregate(group,orderBy);

                  ArrayList<ProductReview> prodList = new ArrayList<ProductReview>();
                  ProductReview pr = null;
                  for(DBObject result: aggregate.results()){
                      BasicDBObject bobj = (BasicDBObject) result;
                      pr = new ProductReview();
                      pr.setCount(Integer.parseInt(bobj.getString("Count")));
                      pr.setProdModelName(bobj.getString("ProductModelName"));
                      prodList.add(pr);
                  }
              return prodList;
        }
*/


}
