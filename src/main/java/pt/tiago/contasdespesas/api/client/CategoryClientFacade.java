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
import pt.tiago.contasdespesas.dto.SubCategoryDto;

/**
 *
 * @author Tiago Carvalho
 */
@SuppressWarnings("CallToPrintStackTrace")
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
                categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                categoryDto.setName(basicObj.getString("name"));
                categoryDto.setDescription(basicObj.getString("description"));
                lista.add(categoryDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
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
                categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                categoryDto.setName(basicObj.getString("name"));
                categoryDto.setDescription("description");
                lista.add(categoryDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    //TODO
    //    public List<SubCategoryDto> findAllSub() {
    //        List<SubCategoryDto> lista = new ArrayList<SubCategoryDto>();
    //        try {
    //            createConnectionMongoDB();
    //            query = conn
    //                    .prepareStatement("SELECT * FROM SubCategory S INNER JOIN Category C ON S.CategoryID = C.ID");
    //            System.out.println(query.toString());
    //            res = query.executeQuery();
    //            while (res.next()) {
    //                subCategoryDto = new SubCategoryDto();
    //                int id = res.getInt("S.ID");
    //                String name = res.getString("S.Name");
    //                String description = res.getString("S.Descricao");
    //                int categoryid = res.getInt("C.ID");
    //                String categoryName = res.getString("C.Name");
    //                String categoryDescription = res.getString("C.Descricao");
    //                subCategoryDto.setID(id);
    //                subCategoryDto.setName(name);
    //                subCategoryDto.setDescription(description);
    //                subCategoryDto.setCategoryID(categoryid);
    //                subCategoryDto.setCategoryName(categoryName);
    //                subCategoryDto.setCategoryDescription(categoryDescription);
    //                lista.add(subCategoryDto);
    //            }
    //            closeConnections();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        return lista;
    //    }

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
            BasicDBObject basicObjSearch = new BasicDBObject("_id", java.util.regex.Pattern.compile(id));
            DBCursor cursor = collection.find(basicObjSearch);
            DBObject obj;
            BasicDBObject basicObj;
            while (cursor.hasNext()) {
                subCategoryDto = new SubCategoryDto();
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                subCategoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                subCategoryDto.setName(basicObj.getString("name"));
                subCategoryDto.setDescription(basicObj.getString("description"));
                subCategoryDto.setCategoryID(basicObj.getString("categoryID"));
                lista.add(subCategoryDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public CategoryDto findByID(String id) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Category");
            BasicDBObject basicObj = new BasicDBObject("_id", java.util.regex.Pattern.compile(id));
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                categoryDto = new CategoryDto();
                categoryDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                categoryDto.setName(basicObj.getString("name"));
                categoryDto.setDescription("description");
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryDto;
    }

    public void create(CategoryDto dto) {
        try {
            createConnectionMongoDB();
            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = mapper.writeValueAsString(dto);
            DBObject dbObject = (DBObject) JSON.parse(jsonObject);
            collection = db.getCollection("Category");
            collection.insert(dbObject);
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSub(SubCategoryDto dto) {
        try {
            createConnectionMongoDB();
            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = mapper.writeValueAsString(dto);
            DBObject dbObject = (DBObject) JSON.parse(jsonObject);
            collection = db.getCollection("SubCategory");
            collection.insert(dbObject);
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(CategoryDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Category");
            collection.remove(new BasicDBObject().append("_id", dto.getID()));
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return (identificador.equals("")) ? identificador : "";
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
            e.printStackTrace();
        }
        return lista;
    }

}
