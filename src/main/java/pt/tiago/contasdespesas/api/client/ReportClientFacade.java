package pt.tiago.contasdespesas.api.client;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.dto.CategorySumByYearDto;
import pt.tiago.contasdespesas.dto.PurchaseSumByMonthDto;
import pt.tiago.contasdespesas.dto.PurchaseSumByYearDto;

/**
 *
 * @author Tiago Carvalho
 */
@SuppressWarnings("CallToPrintStackTrace")
@Component
public class ReportClientFacade {

    private PurchaseSumByMonthDto categoryByMonth = null;
    private int[] mes = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private final static String user = "tiago";
    private static final String pass = "tiago";
    private static final String dbName = "contasdespesas";
    private MongoClientURI clientURI;
    private MongoClient client;
    private DB db;
    private DBCollection collection;
    private String uri;

    public int[] getMes() {
        return this.mes;
    }

    public void setMes(int[] mes) {
        this.mes = mes;
    }

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

    public List<PurchaseSumByMonthDto> findAllByMonth() {
        List<PurchaseSumByMonthDto> lista = new ArrayList<PurchaseSumByMonthDto>();
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Category");
            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                BasicDBObject basicObj = (BasicDBObject) obj;
                categoryByMonth = new PurchaseSumByMonthDto();
                categoryByMonth.setID(String.valueOf(basicObj.getObjectId("_id")));
                categoryByMonth.setName(basicObj.getString("name"));
                categoryByMonth.setMonth(basicObj.getDate("DateOfPurchase"));
                categoryByMonth.setTotal(basicObj.getDouble("DateOfPurchase"));
                lista.add(categoryByMonth);
            }
            if (!lista.isEmpty()) {
                for (PurchaseSumByMonthDto categoria : lista) {
                    createConnectionMongoDB();
                    collection = db.getCollection("Category");
                    BasicDBObject basicObj = new BasicDBObject("_id", java.util.regex.Pattern.compile(categoria.getID()));
                    cursor = collection.find(basicObj);
                    while (cursor.hasNext()) {
                        String nome = basicObj.getString("Name");
                        categoria.setName(nome);
                    }
                }
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public PurchaseSumByMonthDto[] findTotalPersonByNameByMonth(int identificador, int categoryID, int limit) {
        PurchaseSumByMonthDto[] purchase = new PurchaseSumByMonthDto[12];
        PurchaseSumByMonthDto temp;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Person");
            BasicDBObject basicObj = new BasicDBObject();
            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
            if (categoryID == -1) {
                obj.add(new BasicDBObject("PersonID", identificador));
                obj.add(new BasicDBObject("limit", limit));
                basicObj.put("$and", obj);
            } else {
                obj.add(new BasicDBObject("PersonID", identificador));
                obj.add(new BasicDBObject("CategoryID", categoryID));
                obj.add(new BasicDBObject("limit", limit));
                basicObj.put("$and", obj);
            }
            DBCursor cursor = collection.find(basicObj);
            while (cursor.hasNext()) {
                temp = new PurchaseSumByMonthDto();
                temp.setID(String.valueOf(basicObj.getObjectId("_id")));
                temp.setTotal(basicObj.getString("Sumatorio"));
                temp.setMonth(basicObj.getString("Mes"));
                purchase[pos] = temp;
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return purchase;
    }

    public PurchaseSumByYearDto[] findTotalPersonByNameByYear(int identificador, int dataInicio, int dataFinal) {
        int tamanho = dataFinal - dataInicio;
        PurchaseSumByYearDto[] purchase = new PurchaseSumByYearDto[tamanho + 1];
        PurchaseSumByYearDto temp;
        try {
            createConnectionMongoDB();
            collection = db.getCollection("Purchase");
            BasicDBObject basicObj = new BasicDBObject("PersonID", identificador);
            DBCursor cursor = collection.find(basicObj);
            while (cursor.hasNext()) {
                temp = new PurchaseSumByYearDto();
                DBObject obj = cursor.next();
                basicObj = (BasicDBObject) obj;
                temp.setID(String.valueOf(basicObj.getObjectId("_id")));
                temp.setYear(basicObj.getDate("Ano"));
                temp.setTotal(basicObj.getDouble("surname"));
                int pos = basicObj.getDate("Ano") - dataInicio;
                purchase[pos] = temp;
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return purchase;
    }

    public int findMinYear() {
        int pos = 0;
        try {
            createConnectionMongoDB();
            query = conn
                    .prepareStatement("SELECT MIN(YEAR(DateOfPurchase)) AS inicial FROM Purchase");
            System.out.println(query.toString());
            res = query.executeQuery();
            while (res.next()) {
                pos = res.getInt("inicial");
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pos;
    }

    public List<PurchaseSumByMonthDto> findTotalPersonByNameByMonth(String monthSelected) {
        List<PurchaseSumByMonthDto> purchase = new ArrayList<PurchaseSumByMonthDto>();
        PurchaseSumByMonthDto temp;
        try {
            Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthSelected);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int monthInt = cal.get(Calendar.MONTH) + 1;
            createConnectionMongoDB();
            query = conn
                    .prepareStatement("SELECT SUM(Price) AS Sumatorio, PersonID AS pessoa FROM Purchase WHERE MONTH(DateOfPurchase) = ? GROUP BY PersonID ");
            query.setInt(1, monthInt);
            System.out.println(query.toString());
            res = query.executeQuery();
            while (res.next()) {
                temp = new PurchaseSumByMonthDto();
                float total = res.getFloat("Sumatorio");
                int idP = res.getInt("pessoa");
                temp.setTotal(total);
                temp.setID(idP);
                purchase.add(temp);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchase;
    }

    public List<PurchaseSumByYearDto> findTotalPersonByNameByYear(int ano, int limit) {
        List<PurchaseSumByYearDto> purchase = new ArrayList<PurchaseSumByYearDto>();
        PurchaseSumByYearDto temp;
        try {
            createConnectionMongoDB();
            query = conn
                    .prepareStatement("SELECT SUM(Price) AS Sumatorio, PersonID AS pessoa FROM Purchase WHERE YEAR(DateOfPurchase) = ? AND Price <= ?  GROUP BY PersonID ");
            query.setInt(1, ano);
            query.setInt(2, limit);
            System.out.println(query.toString());
            res = query.executeQuery();
            while (res.next()) {
                temp = new PurchaseSumByYearDto();
                float total = res.getFloat("Sumatorio");
                int idP = res.getInt("pessoa");
                temp.setTotal(total);
                temp.setID(idP);
                purchase.add(temp);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchase;
    }

    public List<CategorySumByYearDto> findTotalCategorySumByYear(int ano, int limit) {
        List<CategorySumByYearDto> purchase = new ArrayList<CategorySumByYearDto>();
        CategorySumByYearDto temp;
        try {
            createConnectionMongoDB();
            query = conn
                    .prepareStatement("SELECT SUM(Price) AS Sumatorio, CategoryID AS categoria FROM Purchase WHERE YEAR(DateOfPurchase) = ? AND Price <= ? GROUP BY CategoryID");
            query.setInt(1, ano);
            query.setInt(2, limit);
            System.out.println(query.toString());
            res = query.executeQuery();
            while (res.next()) {
                temp = new CategorySumByYearDto();
                float total = res.getFloat("Sumatorio");
                int idP = res.getInt("categoria");
                temp.setTotal(total);
                temp.setID(idP);
                purchase.add(temp);
            }
            closeConnectionMongoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchase;
    }

}
