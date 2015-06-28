package pt.tiago.contasdespesas.api.client;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.dto.PersonDto;

/**
 *
 * @author Tiago Carvalho
 */
@Component
public class PersonClientFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String uri = ResourceBundle.getBundle("/Services").getString("db.uri");
    private MongoClientURI clientURI;
    private MongoClient client;
    private DB db;
    private DBCollection collection;

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

    public List<PersonDto> findByName(String name, String surname) {
        List<PersonDto> lista = new ArrayList<PersonDto>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Person");
            BasicDBObject basicObj = new BasicDBObject();
            List<BasicDBObject> objFilter = new ArrayList<BasicDBObject>();
            if (!name.isEmpty()) {
                objFilter.add(new BasicDBObject("name", java.util.regex.Pattern.compile(name)));
            } else if (!surname.isEmpty()) {
                objFilter.add(new BasicDBObject("surname", java.util.regex.Pattern.compile(surname)));
            }
            basicObj.put("$and", objFilter);
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                PersonDto personDto = new PersonDto();
                personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                personDto.setName(basicObj.getString("name"));
                personDto.setSurname(basicObj.getString("surname"));
                lista.add(personDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
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
                PersonDto personDto = new PersonDto();
                personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                personDto.setName(basicObj.getString("name"));
                personDto.setSurname(basicObj.getString("surname"));
                lista.add(personDto);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

    public PersonDto findByID(String id) {
        PersonDto personDto = null;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Person");
            ObjectId objID = new ObjectId(id);
            BasicDBObject basicObj = new BasicDBObject("_id", objID);
            DBCursor cursor = collection.find(basicObj);
            DBObject obj;
            while (cursor.hasNext()) {
                obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                personDto = new PersonDto();
                personDto.setID(String.valueOf(basicObj.getObjectId("_id")));
                personDto.setName(basicObj.getString("name"));
                personDto.setSurname(basicObj.getString("surname"));
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return personDto;
    }

    public void create(PersonDto dto) {
        try {
            createConnectionMongoDB();
            BasicDBObject doc = new BasicDBObject()
                    .append("name", dto.getName())
                    .append("surname", dto.getSurname());
            collection = db.getCollection("Person");
            collection.insert(doc);
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void remove(PersonDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Person");
            collection.remove(new BasicDBObject().append("_id", new ObjectId(dto.getID())));
            closeConnectionMongoDB();
        } catch (Exception e) {
            Logger.getLogger(CategoryClientFacade.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void edit(PersonDto dto) {
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Person");
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("name", dto.getName());
            newDocument.put("surname", dto.getSurname());
            BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(dto.getID()));
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
            collection = db.getCollection("Person");
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

}
