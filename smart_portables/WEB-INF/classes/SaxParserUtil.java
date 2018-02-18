import java.io.*;
import java.util.*;
import java.text.ParseException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SaxParserUtil{
       
        String xmlFileName = null;
        Product prod;
        static HashMap<String,Product> productList;
        ProductXMLHandler prodXML = null;    
        Accessory acc = null;
        static HashMap<String,Accessory> accList;
    
        MySQLDataStoreUtilities sqlUtil = new MySQLDataStoreUtilities(); 
        
        public SaxParserUtil(){
            
        }
    
        public SaxParserUtil(String xmlFileName) {
            this.xmlFileName = xmlFileName;
            productList = new HashMap<String, Product>(); 
            accList = new HashMap<String,Accessory>();
            parseDocument();
            loadDatabase();
            //printProductCatalog();
            //printAccessoryCatalog();
        }
    
        public static void addProductHashMap(){
            String TOMCAT_HOME = System.getProperty("catalina.home");
            new SaxParserUtil(TOMCAT_HOME + "\\webapps\\smart_portables\\WEB-INF\\classes\\ProductCatalog.xml");
        }
    
        private void parseDocument() {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                SAXParser parser = factory.newSAXParser();
                prodXML = new ProductXMLHandler();
                parser.parse(xmlFileName, prodXML);                
                productList = prodXML.getProductCatalog();
                accList = prodXML.getAccessoryCatalog();
            } catch (ParserConfigurationException e) {
                System.out.println("ParserConfig error");
            } catch (SAXException e) {
                System.out.println("SAXException : xml not well formed");
            } catch (IOException e) {
                System.out.println("IO error");
            }
        }   
    
        private void loadDatabase(){
            System.out.println("..................................................");
            System.out.println(" LOAD XML into DATABASE ");
            System.out.println("..................................................");
            //truncate tables
            sqlUtil.cleanDB();            
            //insert data from xml to database
            System.out.println(" INSERTING into DB ... ");
            int result = sqlUtil.addXMLProducts(productList,accList); 
            
            System.out.println("..................................................");
            if(result != 0){
                System.out.println(" PRODUCTS loaded into DATABASE ");
                System.out.println(".................................................."); 
            }
        }
        
        private void printProductCatalog(){
            Set keys = productList.keySet();
            for(Object obj:keys){
                String prodID=(String)obj;
                prod = (Product)productList.get(prodID);
                System.out.println(".................... ");
                System.out.print(prod.getId()+" ... "+prod.getName() +" ... "+prod.getBrand());
                List<String> accessories = prod.getAccessories();
                for(String accessory:accessories){
                    System.out.print("=> "+accessory);
                }
                System.out.println(".................... ");
                
            }
        }
        
        private void printAccessoryCatalog(){
            Set keys = accList.keySet();
            for(Object obj:keys){
                String aID=(String)obj;
                acc = (Accessory)accList.get(aID);
                System.out.println(".......printAccessoryCatalog............. ");
                System.out.print(acc.getId()+" ... "+acc.getName() +" ... "+acc.getBrand());
                System.out.println(".................... ");
                
            }
        }
    
        public HashMap<String,Product> getProductCatalog(){
            return productList = sqlUtil.getProductCatalog();
        } 
    
        public HashMap<String,Accessory> getAccessoryCatalog(){
            return accList = sqlUtil.getAccessoryCatalog();
        } 
    
       /* public List<String> getAccessoryByCatagory(String id){
            List<String> accessories = new ArrayList<String>();
            
             Accessory a1 = (Accessory)accList.get(id);
             String category = a1.getCategory();
            
             Set keys = accList.keySet();
             for(Object obj:keys){
                String accID=(String)obj;
                a1 = (Accessory)accList.get(accID);
                if(a1.getCategory().equalsIgnoreCase(category)) 
                     accessories.add(a1.getId());
             }
            return accessories;
        }  */
    
}