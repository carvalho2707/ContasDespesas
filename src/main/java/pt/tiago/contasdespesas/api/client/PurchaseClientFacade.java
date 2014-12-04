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
import pt.tiago.contasdespesas.dto.CategoryDto;
import pt.tiago.contasdespesas.dto.PersonDto;
import pt.tiago.contasdespesas.dto.PurchaseDto;
import pt.tiago.contasdespesas.dto.SubCategoryDto;

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
            DBObject obj2;
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
                lista.add(purchaseDto);
            }
            closeConnectionMongoDB();
            if (!lista.isEmpty()) {
                createConnectionMongoDB();
                for (PurchaseDto purchase : lista) {
                    collection = db.getCollection("Category");
                    BasicDBObject field = new BasicDBObject();
                    field.put("_id", new ObjectId(purchase.getCategoryID()));
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj2 = cursor.next();
                        basicObj = (BasicDBObject) obj2;
                        CategoryDto categoryDto = new CategoryDto();
                        categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        categoryDto.setName(basicObj.getString("name"));
                        categoryDto.setDescription(basicObj.getString("description"));
                        purchase.setCategory(categoryDto);
                    }
                    collection = db.getCollection("SubCategory");
                    field = new BasicDBObject();
                    field.put("_id", new ObjectId(purchase.getSubCategoryID()));
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj2 = cursor.next();
                        basicObj = (BasicDBObject) obj2;
                        SubCategoryDto subCategoryDto = new SubCategoryDto();
                        subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        subCategoryDto.setName(basicObj.getString("name"));
                        subCategoryDto.setDescription(basicObj.getString("description"));
                        subCategoryDto.setCategoryID(basicObj.getString("CategoryID"));
                        purchase.setSubCategory(subCategoryDto);
                    }
                    collection = db.getCollection("Person");
                    field = new BasicDBObject();
                    field.put("_id", new ObjectId(purchase.getPersonID()));
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj2 = cursor.next();
                        basicObj = (BasicDBObject) obj2;
                        PersonDto personDto = new PersonDto();
                        personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        personDto.setName(basicObj.getString("name"));
                        personDto.setSurname(basicObj.getString("surname"));
                        purchase.setPerson(personDto);
                    }
                }
                closeConnectionMongoDB();
            }
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
                lista.add(purchaseDto);
            }
            closeConnectionMongoDB();
            if (!lista.isEmpty()) {
                createConnectionMongoDB();
                for (PurchaseDto purchase : lista) {
                    collection = db.getCollection("Category");
                    BasicDBObject field = new BasicDBObject();
                    field.put("_id", new ObjectId(purchase.getCategoryID()));
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        CategoryDto categoryDto = new CategoryDto();
                        categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        categoryDto.setName(basicObj.getString("name"));
                        categoryDto.setDescription(basicObj.getString("description"));
                        purchase.setCategory(categoryDto);
                    }
                    collection = db.getCollection("SubCategory");
                    field = new BasicDBObject();
                    field.put("_id", new ObjectId(purchase.getSubCategoryID()));
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        SubCategoryDto subCategoryDto = new SubCategoryDto();
                        subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        subCategoryDto.setName(basicObj.getString("name"));
                        subCategoryDto.setDescription(basicObj.getString("description"));
                        subCategoryDto.setCategoryID(basicObj.getString("categoryID"));
                        purchase.setSubCategory(subCategoryDto);
                    }
                    collection = db.getCollection("Person");
                    field = new BasicDBObject();
                    field.put("_id", new ObjectId(purchase.getPersonID()));
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        PersonDto personDto = new PersonDto();
                        personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        personDto.setName(basicObj.getString("name"));
                        personDto.setSurname(basicObj.getString("surname"));
                        purchase.setPerson(personDto);
                    }
                }
                closeConnectionMongoDB();
            }
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
            BasicDBObject basicObj = new BasicDBObject("_id", id);
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
                    .append("personID", new ObjectId(dto.getPersonID()));
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
            collection.remove(new BasicDBObject().append("_id", dto.getID()));
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
            BasicDBObject searchQuery = new BasicDBObject().append("_id", dto.getID());
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
            BasicDBObject basicObj = new BasicDBObject();
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            List<BasicDBObject> objFilter = new ArrayList<BasicDBObject>();
            objFilter.add(new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime())));
            objFilter.add(new BasicDBObject("subCategoryID", new ObjectId(subCategoryID)));
            objFilter.add(new BasicDBObject("categoryID", new ObjectId(categoryID)));
            basicObj.put("$and", objFilter);
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                total += basicObj.getDouble("price");
            }
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
     * @param year The year to search
     * @param categoria The id of the category to search
     * @return
     */
    public double findCategoryTotalByYear(int year, String categoria) {
        double total = 0.0;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            List<BasicDBObject> objFilter = new ArrayList<BasicDBObject>();
            objFilter.add(new BasicDBObject("categoryID", new ObjectId(categoria)));
            objFilter.add(new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime())));
            basicObj.put("$and", objFilter);
            DBCursor cursor = collection.find();
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                total += basicObj.getDouble("price");
            }
            closeConnectionMongoDB();

        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return total;
    }

    public double findPersonTotalByYear(int year, String pessoa) {
        double total = 0.0;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 31);
            BasicDBObject basicObj = new BasicDBObject();
            List<BasicDBObject> objFilter = new ArrayList<BasicDBObject>();
            objFilter.add(new BasicDBObject("personID", new ObjectId(pessoa)));
            objFilter.add(new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime())));
            basicObj.put("$and", objFilter);
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                total += basicObj.getDouble("price");
            }
            closeConnectionMongoDB();

        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return total;
    }
}
