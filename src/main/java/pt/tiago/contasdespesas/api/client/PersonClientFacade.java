package pt.tiago.contasdespesas.api.client;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.dto.PersonDto;

/**
 *
 * @author Tiago Carvalho
 */
@SuppressWarnings("CallToPrintStackTrace")
@Component
public class PersonClientFacade implements Serializable {

    private static final long serialVersionUID = 1L;
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

    public List<PersonDto> findByName(String name, String surname) {
        List<PersonDto> lista = new ArrayList<PersonDto>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Person");
            BasicDBObject basicObj = new BasicDBObject();
            if (!name.isEmpty() && surname.isEmpty()) {
                basicObj.append("name", java.util.regex.Pattern.compile(name));
            } else if (name.isEmpty() && !surname.isEmpty()) {
                basicObj.append("surname", java.util.regex.Pattern.compile(surname));
            } else if (!name.isEmpty() && !surname.isEmpty()) {
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("name", name));
                obj.add(new BasicDBObject("surname", surname));
                basicObj.put("$and", obj);
            }
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                personDto = new PersonDto();
                personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                personDto.setName(basicObj.getString("name"));
                personDto.setSurname(basicObj.getString("surname"));
                lista.add(personDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<PersonDto> findAll() {
        List<PersonDto> lista = new ArrayList<PersonDto>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Person");
            DBCursor cursor = collection.find();
            DBObject obj;
            BasicDBObject basicObj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                personDto = new PersonDto();
                personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                personDto.setName(basicObj.getString("name"));
                personDto.setSurname("surname");
                lista.add(personDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public PersonDto findByID(String id) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Person");
            BasicDBObject basicObj = new BasicDBObject("_id", java.util.regex.Pattern.compile(id));
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                personDto = new PersonDto();
                personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                personDto.setName(basicObj.getString("name"));
                personDto.setSurname("surname");
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personDto;
    }

    public void create(PersonDto dto) {
        try {
            createConnectionMongoDB();
            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = mapper.writeValueAsString(dto);
            DBObject dbObject = (DBObject) JSON.parse(jsonObject);
            collection = db.getCollection("Person");
            collection.insert(dbObject);
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(PersonDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Person");
            collection.remove(new BasicDBObject().append("_id", dto.getID()));
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void edit(PersonDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Person");
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("name", dto.getName());
            newDocument.put("surname", dto.getSurname());
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
            String nameEnclosed = name.replaceAll("\\s+", "%20");
            collection = db.getCollection("Person");
            BasicDBObject basicObj = new BasicDBObject("name", java.util.regex.Pattern.compile(nameEnclosed));
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

}
