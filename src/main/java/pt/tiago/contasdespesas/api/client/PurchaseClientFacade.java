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

    private PurchaseDto purchaseDto = null;
    private CategoryDto categoryDto = null;
    private SubCategoryDto subCategoryDto = null;
    private PersonDto personDto = null;
    private final static String user = "tiago";
    private static final String pass = "tiago";
    private static final String dbName = "contasdespesas";
    private MongoClientURI clientURI;
    private MongoClient client;
    private DB db;
    private DBCollection collection;
    private String uri;

    private void closeConnectionMongoDB() {
        client.close();
        db = null;
        collection = null;
        uri = null;
        clientURI = null;
    }

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

    public List<PurchaseDto> findByName(String name, String person, String category, int year) {
        List<PurchaseDto> lista = new ArrayList<PurchaseDto>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            ObjectId objIdPerson;
            ObjectId objIdCategory;
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 0, 0, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 11, 31, 23, 59);
            BasicDBObject query = new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime()));
            DBObject obj2;
            if (!name.isEmpty() && !person.isEmpty() && !category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                objIdPerson = new ObjectId(person);
                objIdCategory = new ObjectId(category);
                obj.add(new BasicDBObject("itemName", name));
                obj.add(new BasicDBObject("personID", objIdPerson));
                obj.add(new BasicDBObject("categoryID", objIdCategory));
                obj.add(query);
                basicObj.put("$and", obj);
            } else if (!name.isEmpty() && !person.isEmpty() && category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                objIdPerson = new ObjectId(person);
                obj.add(new BasicDBObject("itemName", name));
                obj.add(new BasicDBObject("personID", objIdPerson));
                obj.add(query);
                basicObj.put("$and", obj);
            } else if (!name.isEmpty() && person.isEmpty() && !category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                objIdCategory = new ObjectId(category);
                obj.add(new BasicDBObject("itemName", name));
                obj.add(new BasicDBObject("categoryID", objIdCategory));
                obj.add(query);
                basicObj.put("$and", obj);
            } else if (!name.isEmpty() && person.isEmpty() && category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("itemName", name));
                obj.add(query);
                basicObj.put("$and", obj);
            } else if (name.isEmpty() && !person.isEmpty() && !category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                objIdPerson = new ObjectId(person);
                objIdCategory = new ObjectId(category);
                obj.add(new BasicDBObject("personID", objIdPerson));
                obj.add(new BasicDBObject("categoryID", objIdCategory));
                obj.add(query);
                basicObj.put("$and", obj);
            } else if (name.isEmpty() && !person.isEmpty() && category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                objIdPerson = new ObjectId(person);
                obj.add(new BasicDBObject("personID", objIdPerson));
                obj.add(query);
                basicObj.put("$and", obj);
            } else if (name.isEmpty() && person.isEmpty() && !category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                objIdCategory = new ObjectId(category);
                obj.add(new BasicDBObject("categoryID", objIdCategory));
                obj.add(query);
                basicObj.put("$and", obj);
            }
            DBCursor cursor = collection.find(basicObj);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                purchaseDto = new PurchaseDto();
                purchaseDto.setObjID(basicObj.getObjectId("_id"));
                purchaseDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                purchaseDto.setItemName(basicObj.getString("itemName"));
                purchaseDto.setCategoryID(basicObj.getString("categoryID"));
                purchaseDto.setCategoryObjID(basicObj.getObjectId("categoryID"));
                purchaseDto.setPersonID(basicObj.getString("personID"));
                purchaseDto.setPersonObjID(basicObj.getObjectId("personID"));
                purchaseDto.setSubCategoryID(basicObj.getString("subCategoryID"));
                purchaseDto.setSubCategoryObjID(basicObj.getObjectId("subCategoryID"));
                purchaseDto.setPrice(basicObj.getDouble("price"));
                purchaseDto.setDateOfPurchase(basicObj.getDate("dateOfPurchase"));
                lista.add(purchaseDto);
            }
            closeConnectionMongoDB();
            if (!lista.isEmpty()) {
                for (PurchaseDto purchase : lista) {
                    createConnectionMongoDB();
                    collection = db.getCollection("Category");
                    BasicDBObject field = new BasicDBObject();
                    ObjectId identificator = new ObjectId(purchase.getCategoryID());
                    field.put("_id", identificator);
                    //pode ser mudado para findone mas dps tem de se mudar obj
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj2 = cursor.next();
                        basicObj = (BasicDBObject) obj2;
                        categoryDto = new CategoryDto();
                        categoryDto.setObjID(basicObj.getObjectId("_id"));
                        categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        categoryDto.setName(basicObj.getString("name"));
                        categoryDto.setDescription(basicObj.getString("description"));
                        purchase.setCategory(categoryDto);
                    }
                    closeConnectionMongoDB();
                    createConnectionMongoDB();
                    collection = db.getCollection("SubCategory");
                    field = new BasicDBObject();
                    identificator = new ObjectId(purchase.getSubCategoryID());
                    field.put("_id", identificator);
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj2 = cursor.next();
                        basicObj = (BasicDBObject) obj2;
                        subCategoryDto = new SubCategoryDto();
                        subCategoryDto.setObjID(basicObj.getObjectId("_id"));
                        subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        subCategoryDto.setName(basicObj.getString("name"));
                        subCategoryDto.setDescription(basicObj.getString("description"));
                        subCategoryDto.setCategoryID(basicObj.getString("CategoryID"));
                        purchase.setSubCategory(subCategoryDto);
                    }
                    closeConnectionMongoDB();
                    createConnectionMongoDB();
                    collection = db.getCollection("Person");
                    field = new BasicDBObject();
                    identificator = new ObjectId(purchase.getPersonID());
                    field.put("_id", identificator);
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj2 = cursor.next();
                        basicObj = (BasicDBObject) obj2;
                        personDto = new PersonDto();
                        personDto.setObjID(basicObj.getObjectId("_id"));
                        personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        personDto.setName(basicObj.getString("name"));
                        personDto.setSurname(basicObj.getString("surname"));
                        purchase.setPerson(personDto);
                    }
                    closeConnectionMongoDB();
                }
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
            cal.set(year, 0, 0, 0, 0, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(year, 11, 11, 31, 23, 59);
            BasicDBObject query = new BasicDBObject("dateOfPurchase", new BasicDBObject("$gte", cal.getTime()).append("$lt", cal2.getTime()));
            collection = db.getCollection("Purchase");
            DBCursor cursor = collection.find(query);
            DBObject obj;
            BasicDBObject basicObj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                purchaseDto = new PurchaseDto();
                purchaseDto.setObjID(basicObj.getObjectId("_id"));
                purchaseDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                purchaseDto.setItemName(basicObj.getString("itemName"));
                purchaseDto.setCategoryID(basicObj.getString("categoryID"));
                purchaseDto.setCategoryObjID(basicObj.getObjectId("_id"));
                purchaseDto.setPersonID(basicObj.getString("personID"));
                purchaseDto.setSubCategoryID(basicObj.getString("subCategoryID"));
                purchaseDto.setPrice(basicObj.getDouble("price"));
                purchaseDto.setDateOfPurchase(basicObj.getDate("dateOfPurchase"));
                lista.add(purchaseDto);
            }
            closeConnectionMongoDB();
            if (!lista.isEmpty()) {
                for (PurchaseDto purchase : lista) {
                    createConnectionMongoDB();
                    collection = db.getCollection("Category");
                    BasicDBObject field = new BasicDBObject();
                    ObjectId identificator = new ObjectId(purchase.getCategoryID());
                    field.put("_id", identificator);
                    //pode ser mudado para findone mas dps tem de se mudar obj
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        categoryDto = new CategoryDto();
                        categoryDto.setObjID(basicObj.getObjectId("_id"));
                        categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        categoryDto.setName(basicObj.getString("name"));
                        categoryDto.setDescription(basicObj.getString("description"));
                        purchase.setCategory(categoryDto);
                    }
                    closeConnectionMongoDB();
                    createConnectionMongoDB();
                    collection = db.getCollection("SubCategory");
                    field = new BasicDBObject();
                    identificator = new ObjectId(purchase.getSubCategoryID());
                    field.put("_id", identificator);
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        subCategoryDto = new SubCategoryDto();
                        subCategoryDto.setObjID(basicObj.getObjectId("_id"));
                        subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        subCategoryDto.setName(basicObj.getString("name"));
                        subCategoryDto.setDescription(basicObj.getString("description"));
                        subCategoryDto.setCategoryID(basicObj.getString("CategoryID"));
                        purchase.setSubCategory(subCategoryDto);
                    }
                    closeConnectionMongoDB();
                    createConnectionMongoDB();
                    collection = db.getCollection("Person");
                    field = new BasicDBObject();
                    identificator = new ObjectId(purchase.getPersonID());
                    field.put("_id", identificator);
                    cursor = collection.find(field);
                    while (cursor.hasNext()) {
                        obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        personDto = new PersonDto();
                        personDto.setObjID(basicObj.getObjectId("_id"));
                        personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        personDto.setName(basicObj.getString("name"));
                        personDto.setSurname(basicObj.getString("surname"));
                        purchase.setPerson(personDto);
                    }
                    closeConnectionMongoDB();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }

        return lista;
    }

    public PurchaseDto findByID(String id) {
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
                purchaseDto.setObjID(basicObj.getObjectId("_id"));
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
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return purchaseDto;
    }

    public void create(PurchaseDto dto) {
        try {
            createConnectionMongoDB();
            ObjectId objPerson = new ObjectId(dto.getPersonID());
            ObjectId objCategory = new ObjectId(dto.getCategoryID());
            ObjectId objSubCategory = new ObjectId(dto.getSubCategoryID());
            BasicDBObject doc = new BasicDBObject()
                    .append("itemName", dto.getItemName())
                    .append("dateOfPurchase", dto.getDateOfPurchase())
                    .append("price", dto.getPrice())
                    .append("categoryID", objCategory)
                    .append("subCategoryID", objSubCategory)
                    .append("personID", objPerson);
            collection = db.getCollection("Purchase");
            collection.insert(doc);
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void remove(PurchaseDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            collection.remove(new BasicDBObject().append("_id", dto.getID()));
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public double findTotalYear(int ano, String subCategoryID, String categoryID) {
        double total = 0.0;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            List<BasicDBObject> objFilter = new ArrayList<BasicDBObject>();
            objFilter.add(new BasicDBObject("dateOfPurchase", ano));
            objFilter.add(new BasicDBObject("subCategoryID", subCategoryID));
            objFilter.add(new BasicDBObject("categoryID", categoryID));
            basicObj.put("$and", objFilter);
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                total = basicObj.getDouble("Sumatorio");
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return total;
    }

    public ArrayList<Integer> findYears() {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            DBObject obj;
            BasicDBObject basicObj;
            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                Calendar cal = Calendar.getInstance();
                cal.setTime(basicObj.getDate("dateOfPurchase"));
                int year = cal.get(Calendar.YEAR);
                if (!lista.contains(year)) {
                    lista.add(year);
                }
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

    public double findCategoryTotalByYear(int ano, String categoria) {
        double total = 0.0;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            List<BasicDBObject> objFilter = new ArrayList<BasicDBObject>();
            objFilter.add(new BasicDBObject("categoryID", categoria));
            objFilter.add(new BasicDBObject("dateOfPurchase", ano));
            basicObj.put("$and", objFilter);
            DBCursor cursor = collection.find();
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                total = basicObj.getDouble("Sumatorio");
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            total = 0.0f;
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return total;
    }

    public double findPersonTotalByYear(int ano, String pessoa) {
        double total = 0.0;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            List<BasicDBObject> objFilter = new ArrayList<BasicDBObject>();
            objFilter.add(new BasicDBObject("personID", pessoa));
            objFilter.add(new BasicDBObject("dateOfPurchase", ano));
            basicObj.put("$and", objFilter);
            DBCursor cursor = collection.find();
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                total = basicObj.getDouble("Sumatorio");
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            total = 0.0f;
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return total;
    }
}
