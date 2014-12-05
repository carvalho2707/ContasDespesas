package pt.tiago.contasdespesas.api.client;

import com.mongodb.AggregationOutput;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.dto.PurchaseDto;

/**
 *
 * @author Tiago Carvalho
 */
@Component
public class PurchaseClientFacade {

    private MongoClientURI clientURI;
    private MongoClient client;
    private DB db;
    private DBCollection collection;
    private static final String uri = ResourceBundle.getBundle("/Services").getString("db.uri");

    private void closeConnectionMongoDB() {
        client.close();
        db = null;
        collection = null;
        clientURI = null;
    }

    private void createConnectionMongoDB() {
        try {
            clientURI = new MongoClientURI(uri);
            client = new MongoClient(clientURI);
            db = client.getDB(clientURI.getDatabase());
        } catch (UnknownHostException ex) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<PurchaseDto> findByName(String name, String person, String category, int year) {
        List<PurchaseDto> lista = new ArrayList<PurchaseDto>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            BasicDBObject query = new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime()));
            List<BasicDBObject> objFilter = new ArrayList<BasicDBObject>();
            if (!name.isEmpty()) {
                objFilter.add(new BasicDBObject("itemName", java.util.regex.Pattern.compile(name)));
            }
            if (!person.isEmpty()) {
                objFilter.add(new BasicDBObject("personID", new ObjectId(person)));
            }
            if (!category.isEmpty()) {
                objFilter.add(new BasicDBObject("categoryID", new ObjectId(category)));
            }
            objFilter.add(query);
            basicObj.put("$and", objFilter);
            DBCursor cursor = collection.find(basicObj);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                PurchaseDto purchaseDto = new PurchaseDto();
                purchaseDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                purchaseDto.setItemName(basicObj.getString("itemName"));
                purchaseDto.setCategoryID(basicObj.getString("categoryID"));
                purchaseDto.setPersonID(basicObj.getString("personID"));
                purchaseDto.setSubCategoryID(basicObj.getString("subCategoryID"));
                purchaseDto.setPrice(basicObj.getDouble("price"));
                purchaseDto.setDateOfPurchase(basicObj.getDate("dateOfPurchase"));
                purchaseDto.setCategoryName(basicObj.getString("categoryName"));
                purchaseDto.setPersonName(basicObj.getString("personName"));
                purchaseDto.setSubCategoryName(basicObj.getString("subCategoryName"));
                lista.add(purchaseDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

    public List<PurchaseDto> findAll(int year) {
        List<PurchaseDto> lista = new ArrayList<PurchaseDto>();
        try {
            createConnectionMongoDB();
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            BasicDBObject query = new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime()));
            collection = db.getCollection("Purchase");
            DBCursor cursor = collection.find(query);
            DBObject obj;
            BasicDBObject basicObj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                PurchaseDto purchaseDto = new PurchaseDto();
                purchaseDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                purchaseDto.setItemName(basicObj.getString("itemName"));
                purchaseDto.setCategoryID(basicObj.getString("categoryID"));
                purchaseDto.setPersonID(basicObj.getString("personID"));
                purchaseDto.setSubCategoryID(basicObj.getString("subCategoryID"));
                purchaseDto.setPrice(basicObj.getDouble("price"));
                purchaseDto.setDateOfPurchase(basicObj.getDate("dateOfPurchase"));
                purchaseDto.setCategoryName(basicObj.getString("categoryName"));
                purchaseDto.setPersonName(basicObj.getString("personName"));
                purchaseDto.setSubCategoryName(basicObj.getString("subCategoryName"));
                lista.add(purchaseDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

    public PurchaseDto findByID(String id) {
        PurchaseDto purchaseDto = null;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject("_id", new ObjectId(id));
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                purchaseDto = new PurchaseDto();
                purchaseDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                purchaseDto.setItemName(basicObj.getString("itemName"));
                purchaseDto.setDateOfPurchase(basicObj.getDate("dateOfPurchase"));
                purchaseDto.setPersonID(basicObj.getString("personID"));
                purchaseDto.setCategoryID(basicObj.getString("categoryID"));
                purchaseDto.setSubCategoryID(basicObj.getString("subCategoryID"));
                purchaseDto.setPrice(basicObj.getDouble("price"));
                purchaseDto.setCategoryName(basicObj.getString("categoryName"));
                purchaseDto.setPersonName(basicObj.getString("personName"));
                purchaseDto.setSubCategoryName(basicObj.getString("subCategoryName"));
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return purchaseDto;
    }

    public void create(PurchaseDto dto) {
        try {
            createConnectionMongoDB();
            BasicDBObject doc = new BasicDBObject()
                    .append("itemName", dto.getItemName())
                    .append("dateOfPurchase", dto.getDateOfPurchase())
                    .append("price", dto.getPrice())
                    .append("categoryID", new ObjectId(dto.getCategoryID()))
                    .append("subCategoryID", new ObjectId(dto.getSubCategoryID()))
                    .append("personID", new ObjectId(dto.getPersonID()))
                    .append("personName", dto.getPersonName())
                    .append("categoryName", dto.getCategoryName())
                    .append("subCategoryName", dto.getSubCategoryName());
            collection = db.getCollection("Purchase");
            collection.insert(doc);
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class
                    .getName()).log(Level.SEVERE, null, e);
        }
    }

    public void remove(PurchaseDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            collection.remove(new BasicDBObject("_id", new ObjectId(dto.getID())));
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class
                    .getName()).log(Level.SEVERE, null, e);
        }
    }

    public void edit(PurchaseDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("itemName", dto.getItemName());
            newDocument.put("categoryID", dto.getCategoryID());
            newDocument.put("personID", dto.getPersonID());
            newDocument.put("price", dto.getPrice());
            newDocument.put("subCategoryID", dto.getSubCategoryID());
            newDocument.put("personName", dto.getPersonName());
            newDocument.put("categoryName", dto.getCategoryName());
            newDocument.put("subCategoryName", dto.getSubCategoryName());
            BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(dto.getID()));
            collection.update(searchQuery, newDocument);
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class
                    .getName()).log(Level.SEVERE, null, e);
        }
    }

    public double findTotalYear(int year, String subCategoryID, String categoryID) {
        double total = 0.0;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj;
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            BasicDBObject cateObj = new BasicDBObject("categoryID", new ObjectId(categoryID));
            BasicDBObject subCateObj = new BasicDBObject("subCategoryID", new ObjectId(subCategoryID));
            BasicDBObject dateObj = new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime()));
            BasicDBObject match = new BasicDBObject("$match", cateObj);
            match.put("$match", dateObj);
            match.put("$match", subCateObj);
            DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", null).append("total", new BasicDBObject("$sum", "$price")));
            AggregationOutput output = collection.aggregate(match, group);
            basicObj = (BasicDBObject) output.results();
            total = basicObj.getDouble("total");

            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return total;
    }

    /**
     * SELECT DISTINCT(YEAR(DateOfPurchase)) AS ano FROM Purchase
     *
     * @return
     */
    public ArrayList<Integer> findYears() {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", new BasicDBObject("year", new BasicDBObject("$year", "$dateOfPurchase"))).append("total", new BasicDBObject("$sum", 1)));
            AggregationOutput output = collection.aggregate(group);
            BasicDBObject basicObj;
            for (DBObject result : output.results()) {
                basicObj = (BasicDBObject) result;
                basicObj = (BasicDBObject) basicObj.get("_id");
                lista.add(basicObj.getInt("year"));
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

    /**
     * Find the value spend with one category
     *
     * SELECT SUM(Price) AS total FROM Purchase WHERE categoryID = ? AND
     * Year(DateOfPurchase) = ?
     *
     * @param year The year to search
     * @param categoria The id of the category to search
     * @return The amount spent
     */
    public double findCategoryTotalByYear(int year, String categoria) {
        double total = 0.0;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            BasicDBObject cateObj = new BasicDBObject("categoryID", new ObjectId(categoria));
            BasicDBObject dateObj = new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime()));
            BasicDBObject match = new BasicDBObject("$match", cateObj);
            match.put("$match", dateObj);
            DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", null).append("total", new BasicDBObject("$sum", "$price")));
            AggregationOutput output = collection.aggregate(match, group);
            BasicDBObject basicObj = (BasicDBObject) output.results();
            total = basicObj.getDouble("total");
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return total;
    }

    /**
     * Find the value spend with one person
     *
     * SELECT SUM(Price) as total FROM Purchase WHERE personID = ? AND
     * Year(DateOfPurchase) = ?
     *
     * @param year The year to search
     * @param person The id of the person to search
     * @return The amount spent
     */
    public double findPersonTotalByYear(int year, String person) {
        double total = 0.0;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            BasicDBObject cateObj = new BasicDBObject("personID", new ObjectId(person));
            BasicDBObject dateObj = new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime()));
            BasicDBObject match = new BasicDBObject("$match", cateObj);
            match.put("$match", dateObj);
            DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", null).append("total", new BasicDBObject("$sum", "$price")));
            AggregationOutput output = collection.aggregate(match, group);
            BasicDBObject basicObj = (BasicDBObject) output.results();
            total = basicObj.getDouble("total");
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return total;
    }
}
