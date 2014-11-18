package pt.tiago.contasdespesas.api.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.dto.CategoryDto;
import pt.tiago.contasdespesas.dto.PersonDto;
import pt.tiago.contasdespesas.dto.PurchaseDto;

/**
 *
 * @author Tiago Carvalho
 */
@Component
public class PurchaseClientFacade {

    private transient Connection conn;
    private static final String urlDbName = "jdbc:mysql://localhost:3306/ContasDespesas";
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String userName = "root";
    private static final String password = "tiago";
    private ResultSet res = null;
    private PreparedStatement query = null;
    private Statement st = null;
    private PurchaseDto purchaseDto = null;
    private CategoryDto categoryDto = null;
    private PersonDto personDto = null;

    private Statement createConenctionMySql() {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(urlDbName,
                    userName, password);
            return conn.createStatement();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void closeConnections() throws SQLException {
        conn.close();
        res.close();
        query.close();
        st.close();
    }

    public List<PurchaseDto> findByName(String name, Integer person, Integer category) {
        List<PurchaseDto> lista = new ArrayList<PurchaseDto>();
        try {
            createConenctionMySql();
            if (!name.isEmpty()) {
                query = conn.prepareStatement("SELECT * FROM Purchase WHERE Name LIKE ?");
                query.setString(1, "%" + name + "%");
            }
            ResultSet res = query.executeQuery();
            while (res.next()) {
                purchaseDto = new PurchaseDto();
                int id = res.getInt("ID");
                String nome = res.getString("ItemName");
                Date data = res.getDate("DateOfPurchase");
                int personID = res.getInt("PersonID");
                int categoryID = res.getInt("CategoryID");
                double price = res.getDouble("Price");
                purchaseDto.setID(id);
                purchaseDto.setItemName(nome);
                purchaseDto.setDateOfPurchase(data);
                purchaseDto.setPersonID(personID);
                purchaseDto.setCategoryID(categoryID);
                purchaseDto.setPrice(price);
                lista.add(purchaseDto);
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<PurchaseDto> findAll() {
        List<PurchaseDto> lista = new ArrayList<PurchaseDto>();
        try {
            st = createConenctionMySql();
            res = st.executeQuery("SELECT * FROM Purchase");
            while (res.next()) {
                purchaseDto = new PurchaseDto();
                int id = res.getInt("ID");
                String nome = res.getString("ItemName");
                Date data = res.getDate("DateOfPurchase");
                int personID = res.getInt("PersonID");
                int categoryID = res.getInt("CategoryID");
                double price = res.getDouble("Price");
                purchaseDto.setID(id);
                purchaseDto.setItemName(nome);
                purchaseDto.setDateOfPurchase(data);
                purchaseDto.setPersonID(personID);
                purchaseDto.setCategoryID(categoryID);
                purchaseDto.setPrice(price);
                lista.add(purchaseDto);
            }
            if (!lista.isEmpty()) {
                for (PurchaseDto purchase : lista) {
                    st = createConenctionMySql();
                    res = st.executeQuery("SELECT * FROM Category where ID = " + purchase.getCategoryID());
                    while (res.next()) {
                        categoryDto = new CategoryDto();
                        int id = res.getInt("ID");
                        String nome = res.getString("Name");
                        String description = res.getString("Descricao");
                        categoryDto.setID(id);
                        categoryDto.setName(nome);
                        categoryDto.setDescription(description);
                        purchase.setCategory(categoryDto);
                    }
                    st = createConenctionMySql();
                    res = st.executeQuery("SELECT * FROM Person where ID = " + purchase.getPersonID());
                    while (res.next()) {
                        personDto = new PersonDto();
                        int id = res.getInt("ID");
                        String nome = res.getString("Name");
                        String surnameQ = res.getString("Surname");
                        personDto.setID(id);
                        personDto.setName(nome);
                        personDto.setSurname(surnameQ);
                        purchase.setPerson(personDto);
                    }
                }
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public PurchaseDto findByID(int id) {
        try {
            st = createConenctionMySql();
            res = st.executeQuery("SELECT * FROM Purchase where ID = " + id);
            while (res.next()) {
                purchaseDto = new PurchaseDto();
                int identificador = res.getInt("ID");
                String nome = res.getString("ItemName");
                Date data = res.getDate("DateOfPurchase");
                int personID = res.getInt("PersonID");
                int categoryID = res.getInt("CategoryID");
                double price = res.getDouble("Price");
                purchaseDto.setID(identificador);
                purchaseDto.setItemName(nome);
                purchaseDto.setDateOfPurchase(data);
                purchaseDto.setPersonID(personID);
                purchaseDto.setCategoryID(categoryID);
                purchaseDto.setPrice(price);
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchaseDto;
    }

    public void create(PurchaseDto dto) {
        try {
            st = createConenctionMySql();
            String queryTo = "INSERT INTO Purchase (ItemName,DateOfPurchase,PersonID,CategoryID,Price) VALUES (" + "'" + dto.getItemName() + "'" + "," + "'" + dto.getDateOfPurchase() + "'" + "," + dto.getPersonID() + "," + dto.getPersonID() + "," + dto.getCategoryID() + "," + dto.getPrice() + ")";
            st.executeUpdate(queryTo);
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void remove(PurchaseDto dto) {
        try {
            st = createConenctionMySql();
            String queryTo = "DELETE FROM Purchase WHERE ID =" + dto.getID();
            st.executeUpdate(queryTo);
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void edit(PurchaseDto dto) {
        try {
            createConenctionMySql();
            query = conn.prepareStatement("UPDATE Purchase SET ItemName = ? , DateOfPurchase = ?, PersonID = ?, CategoryID = ?, Price = ? WHERE ID = ?");
            query.setString(1, dto.getItemName());
            query.setDate(2, (Date.valueOf(dto.getDateOfPurchase().toString())));
            query.setDouble(3, dto.getPersonID());
            query.setInt(4, dto.getCategoryID());
            query.setDouble(5, dto.getPrice());
            query.setInt(6, dto.getID());
            query.executeUpdate();
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double findTotalYear(int ano, int id) {
        double total = 0.0;
        try {
            st = createConenctionMySql();
            res = st.executeQuery("SELECT SUM(Price) AS Sumatorio FROM Purchase WHERE CategoryID = " + id + " AND Year(DateOfPurchase) = " + ano);
            while (res.next()) {
                total = res.getDouble("Sumatorio");
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}
