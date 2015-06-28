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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.dto.CategoryDto;
import pt.tiago.contasdespesas.dto.SubCategoryDto;

/**
 *
 * @author Tiago Carvalho
 */
@Component
public class CategoryClientFacade {

    private MongoClientURI clientURI;
    private MongoClient client;
    private DB db;
    private DBCollection collection;
    private static final String uri = ResourceBundle.getBundle("/Services").getString("db.uri");

    /**
     * Close the connection to MongoDB
     */
    private void closeConnectionMongoDB() {
        client.close();
        db = null;
        collection = null;
        clientURI = null;
    }

    /**
     * Create the connection to MongoDB
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
     * Find all Categories by name
     *
     * @param name The name of the category to search
     *
     * @return The list of categories
     */
    public List<CategoryDto> findByName(String name) {
        List<CategoryDto> lista = new ArrayList<CategoryDto>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Category");
            BasicDBObject basicObj = new BasicDBObject("name", java.util.regex.Pattern.compile(name));
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                categoryDto.setName(basicObj.getString("name"));
                categoryDto.setDescription(basicObj.getString("description"));
                lista.add(categoryDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

    /**
     * Find all Categories
     *
     * @return The list of categories
     */
    public List<CategoryDto> findAll() {
        List<CategoryDto> lista = new ArrayList<CategoryDto>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Category");
            DBCursor cursor = collection.find();
            DBObject obj;
            BasicDBObject basicObj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                categoryDto.setName(basicObj.getString("name"));
                categoryDto.setDescription(basicObj.getString("description"));
                lista.add(categoryDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }

        return lista;
    }

    /**
     * Find all the sub categories
     * 
     * @return List of all subcategories in the DB
     */
    public List<SubCategoryDto> findAllSub() {
        List<SubCategoryDto> lista = new ArrayList<SubCategoryDto>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("SubCategory");
            DBCursor cursor = collection.find();
            DBObject obj;
            BasicDBObject basicObj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                SubCategoryDto subCategoryDto = new SubCategoryDto();
                subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                subCategoryDto.setName(basicObj.getString("name"));
                subCategoryDto.setDescription(basicObj.getString("description"));
                lista.add(subCategoryDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

    /**
     * The the SubCategories of one Category
     *
     * @param id The Identificator of the Main Category
     *
     * @return The list of sub categories of the given category
     */
    public List<SubCategoryDto> findAllSubByCategoryID(String id) {
        List<SubCategoryDto> lista = new ArrayList<SubCategoryDto>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("SubCategory");
            BasicDBObject field = new BasicDBObject();
            ObjectId identificator = new ObjectId(id);
            field.put("categoryID", identificator);
            DBCursor cursor = collection.find(field);
            DBObject obj;
            BasicDBObject basicObj;
            while (cursor.hasNext()) {
                SubCategoryDto subCategoryDto = new SubCategoryDto();
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                subCategoryDto.setName(basicObj.getString("name"));
                subCategoryDto.setDescription(basicObj.getString("description"));
                subCategoryDto.setCategoryID(String.valueOf(basicObj.getObjectId("categoryID")));
                lista.add(subCategoryDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

    /**
     * Find the element identified by id
     * 
     * @param id Identificator of the object
     * @return The Object
     */
    public CategoryDto findByID(String id) {
        CategoryDto categoryDto = null;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Category");
            ObjectId objID = new ObjectId(id);
            BasicDBObject basicObj = new BasicDBObject("_id", objID);
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                categoryDto = new CategoryDto();
                categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                categoryDto.setName(basicObj.getString("name"));
                categoryDto.setDescription(basicObj.getString("description"));
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return categoryDto;
    }
    
    /**
     * Find the element identified by id
     * 
     * @param id Identificator of the object
     * @return The Object
     */
    public SubCategoryDto findSubByID(String id) {
        SubCategoryDto subCategoryDto = null;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("SubCategory");
            ObjectId objID = new ObjectId(id);
            BasicDBObject basicObj = new BasicDBObject("_id", objID);
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                subCategoryDto = new SubCategoryDto();
                subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                subCategoryDto.setName(basicObj.getString("name"));
                subCategoryDto.setDescription(basicObj.getString("description"));
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return subCategoryDto;
    }

    /**
     *  Create and send one category object to the DB
     * @param dto The object to send
     */
    public void create(CategoryDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Category");
            BasicDBObject doc = new BasicDBObject()
                    .append("name", dto.getName())
                    .append("description", dto.getDescription());
            collection.insert(doc);
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Create and send one subcategory object to the DB
     * @param dto The object to send
     */
    public void createSub(SubCategoryDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("SubCategory");
            BasicDBObject doc = new BasicDBObject()
                    .append("name", dto.getName())
                    .append("description", dto.getDescription())
                    .append("categoryID", new ObjectId(dto.getCategoryID()));
            collection.insert(doc);
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Remove one category from DB
     * @param dto Object to remove
     */
    public void remove(CategoryDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Category");
            collection.remove(new BasicDBObject().append("_id", dto.getID()));
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Edit one category
     * @param dto The object to be edited
     */
    public void edit(CategoryDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Category");
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("name", dto.getName());
            newDocument.put("descricao", dto.getDescription());
            BasicDBObject searchQuery = new BasicDBObject().append("_id", dto.getID());
            collection.update(searchQuery, newDocument);
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Find the ID of Object by Name
     * 
     * @param name
     * @return The ID
     */
    public String findIDByName(String name) {
        String identificador = "";
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Category");
            BasicDBObject basicObj = new BasicDBObject("name", name);
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                identificador = String.valueOf(basicObj.getObjectId("_id"));
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return (!identificador.isEmpty()) ? identificador : "NOTFOUNDERRORDONTSHOW";
    }

    /**
     * Fin all categories names
     * 
     * @return List with all names as String
     */
    public List<String> findAllNames() {
        List<String> lista = new ArrayList<String>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Category");
            DBCursor cursor = collection.find();
            DBObject obj;
            BasicDBObject basicObj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                String name = basicObj.getString("name");
                lista.add(name);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

}
