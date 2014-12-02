package pt.tiago.contasdespesas.api.client;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.dto.CategoryDto;
import pt.tiago.contasdespesas.dto.PersonDto;
import pt.tiago.contasdespesas.dto.PurchaseDto;
import pt.tiago.contasdespesas.dto.SubCategoryDto;

/**
 *
 * @author Tiago Carvalho
 */
@SuppressWarnings("CallToPrintStackTrace")
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
            if (!name.isEmpty() && !person.isEmpty() && !category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("ItemName", name));
                obj.add(new BasicDBObject("PersonID", person));
                obj.add(new BasicDBObject("CategoryID", category));
                obj.add(new BasicDBObject("Year", year));
                basicObj.put("$and", obj);
            } else if (!name.isEmpty() && !person.isEmpty() && category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("ItemName", name));
                obj.add(new BasicDBObject("PersonID", person));
                obj.add(new BasicDBObject("Year", year));
                basicObj.put("$and", obj);
            } else if (!name.isEmpty() && person.isEmpty() && !category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("ItemName", name));
                obj.add(new BasicDBObject("CategoryID", category));
                obj.add(new BasicDBObject("Year", year));
                basicObj.put("$and", obj);
            } else if (!name.isEmpty() && person.isEmpty() && category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("ItemName", name));
                obj.add(new BasicDBObject("Year", year));
                basicObj.put("$and", obj);
            } else if (name.isEmpty() && !person.isEmpty() && !category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("PersonID", person));
                obj.add(new BasicDBObject("CategoryID", category));
                obj.add(new BasicDBObject("Year", year));
                basicObj.put("$and", obj);
            } else if (name.isEmpty() && !person.isEmpty() && category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("PersonID", person));
                obj.add(new BasicDBObject("Year", year));
                basicObj.put("$and", obj);
            } else if (name.isEmpty() && person.isEmpty() && !category.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("CategoryID", category));
                obj.add(new BasicDBObject("Year", year));
                basicObj.put("$and", obj);
            }
            DBCursor cursor = collection.find(basicObj);
            while (cursor.hasNext()) {
                purchaseDto = new PurchaseDto();
                purchaseDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                purchaseDto.setItemName(basicObj.getString("ItemName"));
                purchaseDto.setCategoryID(basicObj.getString("CategoryID"));
                purchaseDto.setPersonID(basicObj.getString("PersonID"));
                purchaseDto.setSubCategoryID(basicObj.getString("SubCategoryID"));
                purchaseDto.setPrice(basicObj.getDouble("Price"));
                purchaseDto.setDateOfPurchase(basicObj.getDate("DateOfPurchase"));
                lista.add(purchaseDto);
            }
            closeConnectionMongoDB();
            if (!lista.isEmpty()) {
                for (PurchaseDto purchase : lista) {
                    SubCategoryDto sub = null;
                    createConnectionMongoDB();
                    collection = db.getCollection("Category");
                    basicObj = new BasicDBObject("_id", java.util.regex.Pattern.compile(purchase.getCategoryID()));
                    cursor = collection.find(basicObj);
                    while (cursor.hasNext()) {
                        DBObject obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        categoryDto = new CategoryDto();
                        categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        categoryDto.setName(basicObj.getString("name"));
                        categoryDto.setDescription(basicObj.getString("description"));
                        purchase.setCategory(categoryDto);
                    }
                    closeConnectionMongoDB();
                    createConnectionMongoDB();
                    collection = db.getCollection("SubCategory");
                    basicObj = new BasicDBObject("_id", java.util.regex.Pattern.compile(purchase.getCategoryID()));
                    cursor = collection.find(basicObj);
                    while (cursor.hasNext()) {
                        DBObject obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        subCategoryDto = new SubCategoryDto();
                        subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        subCategoryDto.setName(basicObj.getString("name"));
                        subCategoryDto.setDescription(basicObj.getString("description"));
                        subCategoryDto.setCategoryID(basicObj.getString("CategoryID"));
                        purchase.setSubCategory(subCategoryDto);
                    }
                    closeConnectionMongoDB();
                    createConnectionMongoDB();
                    collection = db.getCollection("Person");
                    basicObj = new BasicDBObject("_id", java.util.regex.Pattern.compile(purchase.getCategoryID()));
                    cursor = collection.find(basicObj);
                    while (cursor.hasNext()) {
                        DBObject obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        personDto = new PersonDto();
                        personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        personDto.setName(basicObj.getString("name"));
                        personDto.setSurname(basicObj.getString("surname"));
                        purchase.setPerson(personDto);
                    }
                }
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<PurchaseDto> findAll(int year) {
        List<PurchaseDto> lista = new ArrayList<PurchaseDto>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject("DateOfPurchase", year);
            DBCursor cursor = collection.find(basicObj);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                purchaseDto = new PurchaseDto();
                purchaseDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                purchaseDto.setItemName(basicObj.getString("ItemName"));
                purchaseDto.setCategoryID(basicObj.getString("CategoryID"));
                purchaseDto.setPersonID(basicObj.getString("PersonID"));
                purchaseDto.setSubCategoryID(basicObj.getString("SubCategoryID"));
                purchaseDto.setPrice(basicObj.getDouble("Price"));
                purchaseDto.setDateOfPurchase(basicObj.getDate("DateOfPurchase"));
                lista.add(purchaseDto);
            }
            closeConnectionMongoDB();
            if (!lista.isEmpty()) {
                for (PurchaseDto purchase : lista) {
                    SubCategoryDto sub = null;
                    createConnectionMongoDB();
                    collection = db.getCollection("Category");
                    basicObj = new BasicDBObject("_id", java.util.regex.Pattern.compile(purchase.getCategoryID()));
                    cursor = collection.find(basicObj);
                    while (cursor.hasNext()) {
                        DBObject obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        categoryDto = new CategoryDto();
                        categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        categoryDto.setName(basicObj.getString("name"));
                        categoryDto.setDescription(basicObj.getString("description"));
                        purchase.setCategory(categoryDto);
                    }
                    closeConnectionMongoDB();
                    createConnectionMongoDB();
                    collection = db.getCollection("SubCategory");
                    basicObj = new BasicDBObject("_id", java.util.regex.Pattern.compile(purchase.getCategoryID()));
                    cursor = collection.find(basicObj);
                    while (cursor.hasNext()) {
                        DBObject obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        subCategoryDto = new SubCategoryDto();
                        subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        subCategoryDto.setName(basicObj.getString("name"));
                        subCategoryDto.setDescription(basicObj.getString("description"));
                        subCategoryDto.setCategoryID(basicObj.getString("CategoryID"));
                        purchase.setSubCategory(subCategoryDto);
                    }
                    closeConnectionMongoDB();
                    createConnectionMongoDB();
                    collection = db.getCollection("Person");
                    basicObj = new BasicDBObject("_id", java.util.regex.Pattern.compile(purchase.getCategoryID()));
                    cursor = collection.find(basicObj);
                    while (cursor.hasNext()) {
                        DBObject obj = cursor.next();
                        basicObj = (BasicDBObject) obj;
                        personDto = new PersonDto();
                        personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                        personDto.setName(basicObj.getString("name"));
                        personDto.setSurname(basicObj.getString("surname"));
                        purchase.setPerson(personDto);
                    }
                }
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public PurchaseDto findByID(String id) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject("_id", java.util.regex.Pattern.compile(id));
            DBCursor cursor = collection.find(basicObj);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                purchaseDto = new PurchaseDto();
                categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                purchaseDto.setItemName(basicObj.getString("ItemName"));
                purchaseDto.setDateOfPurchase(basicObj.getDate("DateOfPurchase"));
                purchaseDto.setPersonID(basicObj.getString("PersonID"));
                purchaseDto.setCategoryID(basicObj.getString("CategoryID"));
                purchaseDto.setPrice(basicObj.getDouble("Price"));
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchaseDto;
    }

    public void create(PurchaseDto dto) {
        try {
            createConnectionMongoDB();
            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = mapper.writeValueAsString(dto);
            DBObject dbObject = (DBObject) JSON.parse(jsonObject);
            collection = db.getCollection("Purchase");
            collection.insert(dbObject);
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void remove(PurchaseDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            collection.remove(new BasicDBObject().append("_id", dto.getID()));
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void edit(PurchaseDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("ItemName", dto.getItemName());
            newDocument.put("CategoryID", dto.getCategoryID());
            newDocument.put("PersonID", dto.getPersonID());
            newDocument.put("Price", dto.getPrice());
            newDocument.put("SubCategoryID", dto.getSubCategoryID());
            BasicDBObject searchQuery = new BasicDBObject().append("_id", dto.getID());
            collection.update(searchQuery, newDocument);
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double findTotalYear(int ano, String subCategoryID, String categoryID) {
        double total = 0.0;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
            obj.add(new BasicDBObject("DateOfPurchase", ano));
            obj.add(new BasicDBObject("PersonID", subCategoryID));
            obj.add(new BasicDBObject("CategoryID", categoryID));
            basicObj.put("$and", obj);
            DBCursor cursor = collection.find(basicObj);
            while (cursor.hasNext()) {
                total = basicObj.getDouble("Sumatorio");
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public ArrayList<Integer> findYears() {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                int valor = basicObj.getInt("ano");
                lista.add(valor);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public double findCategoryTotalByYear(int ano, String categoria) {
        double total = 0.0;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
            obj.add(new BasicDBObject("CategoryID", categoria));
            obj.add(new BasicDBObject("DateOfPurchase", ano));
            basicObj.put("$and", obj);
            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                total = basicObj.getDouble("Sumatorio");
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            total = 0.0f;
            e.printStackTrace();
        }
        return total;
    }

    public double findPersonTotalByYear(int ano, String pessoa) {
        double total = 0.0;
        try {
            createConnectionMongoDB();
             collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject();
            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
            obj.add(new BasicDBObject("PersonID", pessoa));
            obj.add(new BasicDBObject("DateOfPurchase", ano));
            basicObj.put("$and", obj);
            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                total = basicObj.getDouble("Sumatorio");
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            total = 0.0f;
            e.printStackTrace();
        }
        return total;
    }
}
