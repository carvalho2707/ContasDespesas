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

    private CategoryDto categoryDto = null;
    private SubCategoryDto subCategoryDto = null;
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
            BasicDBObject basicObj = new BasicDBObject("name", name);
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                categoryDto = new CategoryDto();
                categoryDto.setObjID(basicObj.getObjectId("_id"));
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
                categoryDto = new CategoryDto();
                categoryDto.setObjID(basicObj.getObjectId("_id"));
                categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                categoryDto.setName(basicObj.getString("name"));
                categoryDto.setDescription("description");
                lista.add(categoryDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }

        return lista;
    }

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
                subCategoryDto = new SubCategoryDto();
                subCategoryDto.setObjID(basicObj.getObjectId("_id"));
                subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                subCategoryDto.setName(basicObj.getString("name"));
                subCategoryDto.setDescription(basicObj.getString("description"));
                subCategoryDto.setDescription("description");
                subCategoryDto.setCategoryObjID(basicObj.getObjectId("categoryID"));
                subCategoryDto.setCategoryName("not supported");
                subCategoryDto.setCategoryDescription("not supported");
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
                subCategoryDto = new SubCategoryDto();
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                subCategoryDto.setObjID(basicObj.getObjectId("_id"));
                subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                subCategoryDto.setName(basicObj.getString("name"));
                subCategoryDto.setDescription(basicObj.getString("description"));
                subCategoryDto.setCategoryID(String.valueOf(basicObj.getObjectId("categoryID")));
                subCategoryDto.setCategoryObjID(basicObj.getObjectId("categoryID"));
                lista.add(subCategoryDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

    public CategoryDto findByID(String id) {
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
                categoryDto.setObjID(basicObj.getObjectId("_id"));
                categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                categoryDto.setName(basicObj.getString("name"));
                categoryDto.setDescription("description");
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return categoryDto;
    }

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

    public void createSub(SubCategoryDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("SubCategory");
            ObjectId obj = new ObjectId(dto.getCategoryID());
            BasicDBObject doc = new BasicDBObject()
                    .append("name", dto.getName())
                    .append("description", dto.getDescription())
                    .append("categoryID", obj);
            collection.insert(doc);
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
    }

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
