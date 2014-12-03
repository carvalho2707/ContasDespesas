package pt.tiago.contasdespesas.api.client;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.dto.CategorySumByYearDto;
import pt.tiago.contasdespesas.dto.PurchaseSumByMonthDto;
import pt.tiago.contasdespesas.dto.PurchaseSumByYearDto;

/**
 *
 * @author Tiago Carvalho
 */
@Component
public class ReportClientFacade {

    private final static String user = "tiago";
    private static final String pass = "tiago";
    private static final String dbName = "contasdespesas";
    private MongoClientURI clientURI;
    private MongoClient client;
    private DB db;
    private DBCollection collection;
    private String uri;

    /**
     * Close the connection to mongoDB
     */
    private void closeConnectionMongoDB() {
        client.close();
        db = null;
        collection = null;
        uri = null;
        clientURI = null;
    }

    /**
     * Create the connection to mongoDB
     */
    private void createConnectionMongoDB() {
        StringBuilder str = new StringBuilder();
        str.append("mongodb://");
        str.append(user);
        str.append(":");
        str.append(pass);
        str.append("@ds055690.mongolab.com:55690/");
        str.append(dbName);
        uri = str.toString();
        try {
            clientURI = new MongoClientURI(uri);
            client = new MongoClient(clientURI);
            db = client.getDB(clientURI.getDatabase());
        } catch (UnknownHostException ex) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Query 1 : SELECT SUM(Price) , MONTH(DateOfPurchase) FROM Purchase WHERE
     * PersonID = ? AND Price <= ? GROUP BY MONTH(DateOfPurchase)
     *
     * Query 2 : SELECT SUM(Price) , MONTH(DateOfPurchase) FROM Purchase WHERE
     * PersonID = ? AND CategoryID = ? AND Price <= ? GROUP BY
     * MONTH(DateOfPurchase)
     *
     * @param identificador
     * @param categoryID
     * @param limit
     * @return
     */
    public PurchaseSumByMonthDto[] findTotalPersonByNameByMonth(String identificador, String categoryID, int limit) {
        createConnectionMongoDB();
        PurchaseSumByMonthDto[] purchase = new PurchaseSumByMonthDto[12];
        collection = db.getCollection("Purchase");
        BasicDBObject basicObj = new BasicDBObject();
        List<BasicDBObject> objFilter = new ArrayList<BasicDBObject>();
        if (categoryID.isEmpty()) {
            objFilter.add(new BasicDBObject("personID", new ObjectId(identificador)));
            objFilter.add(new BasicDBObject("price", new BasicDBObject("$lte", limit)));
            basicObj.put("$and", objFilter);
        } else {
            objFilter.add(new BasicDBObject("personID", new ObjectId(identificador)));
            objFilter.add(new BasicDBObject("categoryID", new ObjectId(categoryID)));
            objFilter.add(new BasicDBObject("price", new BasicDBObject("$lte", limit)));
            basicObj.put("$and", objFilter);
        }
        DBCursor cursor = collection.find(basicObj);
        DBObject obj;
        while (cursor.hasNext()) {
            obj = cursor.next();
            basicObj = (BasicDBObject) obj;
            Calendar cal = Calendar.getInstance();
            cal.setTime(basicObj.getDate("dateOfPurchase"));
            int month = cal.get(Calendar.MONTH);
            double total = basicObj.getDouble("price");
            if (purchase[month] == null) {
                purchase[month] = new PurchaseSumByMonthDto();
                purchase[month].setMonth(month);
                purchase[month].setTotal(total);
            } else {
                purchase[month].setMonth(month);
                purchase[month].setTotal(purchase[month].getTotal() + total);
            }
        }
        closeConnectionMongoDB();
        return purchase;
    }

    /**
     *
     * Query : SELECT MIN(YEAR(DateOfPurchase)) FROM Purchase
     *
     * @return the first year with purchases
     */
    public int findMinYear() {
        int pos = 0;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            DBObject sort = new BasicDBObject();
            sort.put("dateOfPurchase", 1);
            DBCursor cursor = collection.find().sort(sort).limit(1);
            DBObject obj = cursor.next();
            BasicDBObject basicObj;
            basicObj = (BasicDBObject) obj;
            Calendar cal = Calendar.getInstance();
            cal.setTime(basicObj.getDate("dateOfPurchase"));
            pos = cal.get(Calendar.YEAR);
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return pos;
    }

    /**
     *
     * Query : SELECT SUM(Price) , PersonID FROM Purchase WHERE
     * YEAR(DateOfPurchase) = ? AND Price <= ? GROUP BY PersonID
     *
     * @param year the year of the purchases
     * @param limit the max value for purchases to search
     * @return list of the person purchases
     */
    public List<PurchaseSumByYearDto> findTotalPersonByNameByYear(int year, int limit) {
        //each element of list will contain one person with purchases
        List<PurchaseSumByYearDto> purchase = new ArrayList<PurchaseSumByYearDto>();
        PurchaseSumByYearDto temp;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            obj.add(new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime())));
            obj.add(new BasicDBObject("price", new BasicDBObject("$lte", limit)));
            basicObj.put("$and", obj);
            DBCursor cursor = collection.find(basicObj);
            while (cursor.hasNext()) {
                DBObject obj2 = cursor.next();
                basicObj = (BasicDBObject) obj2;
                String id = String.valueOf(basicObj.getObjectId("personID"));
                boolean condition = false;
                for (PurchaseSumByYearDto listPurchases : purchase) {
                    if (listPurchases.getID().equals(id)) {
                        condition = true;
                        double price = basicObj.getDouble("price");
                        listPurchases.setTotal(price + listPurchases.getTotal());
                    }
                }
                if (condition == false) {
                    temp = new PurchaseSumByYearDto();
                    temp.setID(String.valueOf(basicObj.getObjectId("personID")));
                    temp.setTotal(basicObj.getDouble("price"));
                    purchase.add(temp);
                }
                condition = false;
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return purchase;
    }

    /**
     * SELECT SUM(Price) , CategoryID FROM Purchase WHERE YEAR(DateOfPurchase) =
     * ? AND Price <= ? GROUP BY CategoryID"
     *
     *
     * @param year the year of the purchases
     * @param limit the max value for purchases to search
     * @return list of the categories purchases
     */
    public List<CategorySumByYearDto> findTotalCategorySumByYear(int year, int limit) {
        List<CategorySumByYearDto> purchase = new ArrayList<CategorySumByYearDto>();
        CategorySumByYearDto temp;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            obj.add(new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime())));
            obj.add(new BasicDBObject("price", new BasicDBObject("$lte", limit)));
            basicObj.put("$and", obj);
            DBCursor cursor = collection.find(basicObj);
            while (cursor.hasNext()) {
                DBObject obj2 = cursor.next();
                basicObj = (BasicDBObject) obj2;
                String id = String.valueOf(basicObj.getObjectId("categoryID"));
                boolean condition = false;
                for (CategorySumByYearDto listPurchases : purchase) {
                    if (listPurchases.getID().equals(id)) {
                        condition = true;
                        double price = basicObj.getDouble("price");
                        listPurchases.setTotal(price + listPurchases.getTotal());
                    }
                }
                if (condition == false) {
                    temp = new CategorySumByYearDto();
                    temp.setID(String.valueOf(basicObj.getObjectId("categoryID")));
                    temp.setTotal(basicObj.getDouble("price"));
                    purchase.add(temp);
                }
                condition = false;
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return purchase;
    }

}
