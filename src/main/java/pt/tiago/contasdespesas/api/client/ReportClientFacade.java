package pt.tiago.contasdespesas.api.client;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 *
 * @author Tiago Carvalho
 */
@Component
public class ReportClientFacade {

    private MongoClientURI clientURI;
    private MongoClient client;
    private DB db;
    private DBCollection collection;
    private static final String uri = "mongodb://tiago:tiago@ds055690.mongolab.com:55690/contasdespesas";

    /**
     * Close the connection to mongoDB
     */
    private void closeConnectionMongoDB() {
        client.close();
        db = null;
        collection = null;
        clientURI = null;
    }

    /**
     * Create the connection to mongoDB
     */
    private void createConnectionMongoDB() {
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
     * @param personID
     * @param categoryID
     * @param limit
     * @return
     */
    public double[] findTotalPersonByNameByMonth(String personID, String categoryID, int limit) {
        createConnectionMongoDB();
        double[] purchase = new double[12];
        collection = db.getCollection("Purchase");
        BasicDBList and = new BasicDBList();
        if (!categoryID.isEmpty()) {
            BasicDBObject cateObj = new BasicDBObject("categoryID", new ObjectId(categoryID));
            and.add(cateObj);
        }
        BasicDBObject personObj = new BasicDBObject("personID", new ObjectId(personID));
        and.add(personObj);
        DBObject andCriteria = new BasicDBObject("$and", and);
        DBObject matchCriteria = new BasicDBObject("$match", andCriteria);
        DBObject group = new BasicDBObject(
                "$group", new BasicDBObject("_id", null).append(
                        "total", new BasicDBObject("$sum", "$price")
                )
        );
        group.put("$group", new BasicDBObject("_id", new BasicDBObject("month", new BasicDBObject("$month", "$dateOfPurchase"))).append("total", new BasicDBObject("$sum", "$price")));
        AggregationOutput output = collection.aggregate(matchCriteria, group);
        for (DBObject result : output.results()) {
            double total = ((BasicDBObject) result).getDouble("total");
            DBObject basicObj = result;
            BasicDBObject basicObj2 = (BasicDBObject) basicObj.get("_id");
            int month = basicObj2.getInt("month") - 1;
            purchase[month] += total;
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
    public Map<String, Double> findTotalPersonByNameByYear(int year, int limit) {
        Map<String, Double> map = new HashMap<String, Double>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            DBObject dbobj;
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            BasicDBObject priceObj = new BasicDBObject("price", new BasicDBObject("$lte", 2000));
            BasicDBObject dateObj = new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime()));
            BasicDBList and = new BasicDBList();
            and.add(priceObj);
            and.add(dateObj);
            DBObject andCriteria = new BasicDBObject("$and", and);
            DBObject matchCriteria = new BasicDBObject("$match", andCriteria);
            BasicDBObject group = new BasicDBObject(
                    "$group", new BasicDBObject("_id", "$personID").append(
                            "total", new BasicDBObject("$sum", "$price")
                    )
            );
            AggregationOutput output = collection.aggregate(matchCriteria, group);
            for (DBObject result : output.results()) {
                double total = ((BasicDBObject) result).getDouble("total");
                dbobj = result;
                BasicDBObject basicObj2 = (BasicDBObject) dbobj.get("_id");
                String personID = basicObj2.getString("_id");
                map.put(personID, total);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return map;
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
    public Map<String, Double> findTotalCategorySumByYear(int year, int limit) {
        Map<String, Double> map = new HashMap<String, Double>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            DBObject dbobj;
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            BasicDBObject priceObj = new BasicDBObject("price", new BasicDBObject("$lte", 2000));
            BasicDBObject dateObj = new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime()));
            BasicDBList and = new BasicDBList();
            and.add(priceObj);
            and.add(dateObj);
            DBObject andCriteria = new BasicDBObject("$and", and);
            DBObject matchCriteria = new BasicDBObject("$match", andCriteria);
            BasicDBObject group = new BasicDBObject(
                    "$group", new BasicDBObject("_id", "$categorynID").append(
                            "total", new BasicDBObject("$sum", "$price")
                    )
            );
            AggregationOutput output = collection.aggregate(matchCriteria, group);
            for (DBObject result : output.results()) {
                double total = ((BasicDBObject) result).getDouble("total");
                dbobj = result;
                BasicDBObject basicObj2 = (BasicDBObject) dbobj.get("_id");
                String categoryID = basicObj2.getString("_id");
                map.put(categoryID, total);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return map;
    }

}
