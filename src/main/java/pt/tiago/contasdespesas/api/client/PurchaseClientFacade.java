/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author NB20708
 */
@Component
public class PurchaseClientFacade {

    private Connection conn = null;
    private final String url = "jdbc:mysql://localhost:3306/";
    private final String dbName = "ContasDespesas";
    private final String driver = "com.mysql.jdbc.Driver";
    private final String userName = "root";
    private final String password = "tiago";
    private PurchaseDto purchaseDto = null;
    private CategoryDto categoryDto = null;
    private PersonDto personDto = null;

    private Statement createConenctionMySql() {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName,
                    userName, password);
            return conn.createStatement();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<PurchaseDto> findByName(String name, Integer person, Integer category) {
        List<PurchaseDto> lista = new ArrayList<PurchaseDto>();
        PreparedStatement query = null;
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
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<PurchaseDto> findAll() {
        List<PurchaseDto> lista = new ArrayList<PurchaseDto>();
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT * FROM Purchase");
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
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public PurchaseDto findByID(int id) {
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT * FROM Purchase where ID = " + id);
            while (res.next()) {
                purchaseDto = new PurchaseDto();
                int identificador = res.getInt("ID");
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
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchaseDto;
    }

    public void create(PurchaseDto dto) {
        try {
            Statement st = createConenctionMySql();
            String query = "INSERT INTO Purchase (ItemName,DateOfPurchase,PersonID,CategoryID,Price) VALUES (" + "'" + dto.getItemName() + "'" + "," + "'" + dto.getDateOfPurchase() + "'" + "," + dto.getPersonID() + "," + dto.getPersonID() + "," + dto.getCategoryID() + "," + dto.getPrice() + ")";
            System.out.println("Query to execute = " + query);
            int output = st.executeUpdate(query);
            conn.close();
        } catch (Exception e) {
            System.err.println("Execpcao no create Purchase");
            e.printStackTrace();
        }

    }

    public void remove(PurchaseDto dto) {
        try {
            Statement st = createConenctionMySql();
            String query = "DELETE FROM Purchase WHERE ID =" + dto.getID();
            System.out.println("Query to execute = " + query);
            int output = st.executeUpdate(query);
            System.out.println("Result query remove Purchase = " + output);
            conn.close();
        } catch (Exception e) {
            System.err.println("Execpcao no remove Purchase");
            e.printStackTrace();
        }
    }

    public void edit(PurchaseDto dto) {
        try {
            Statement st = createConenctionMySql();
            PreparedStatement query = conn.prepareStatement("UPDATE Purchase SET ItemName = ? , DateOfPurchase = ?, PersonID = ?, CategoryID = ?, Price = ? WHERE ID = ?");
            query.setString(1, dto.getItemName());
            query.setDate(2, (Date.valueOf(dto.getDateOfPurchase().toString())));
            query.setDouble(3, dto.getPersonID());
            query.setInt(4, dto.getCategoryID());
            query.setDouble(5, dto.getPrice());
            query.setInt(6, dto.getID());
            int output = query.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.err.println("Execpcao no edit Purchase");
            e.printStackTrace();
        }
    }

    public double findTotalYear(int ano, int id) {
        double total = 0.0;
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT SUM(Price) AS Sumatorio FROM Purchase WHERE CategoryID = " + id + " AND Year(DateOfPurchase) = " + ano);
            while (res.next()) {
                total = res.getDouble("Sumatorio");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}
