package pt.tiago.contasdespesas.api.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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

    private transient Connection conn;
    private ResultSet res = null;
    private PreparedStatement query = null;
    private PurchaseDto purchaseDto = null;
    private CategoryDto categoryDto = null;
    private PersonDto personDto = null;
    private static final String urlDbName = ResourceBundle.getBundle("/Services").getString("db.urlDB");
    private static final String driver = ResourceBundle.getBundle("/Services").getString("db.driver");
    private static final String userName = ResourceBundle.getBundle("/Services").getString("db.userName");
    private static final String password = ResourceBundle.getBundle("/Services").getString("db.password");

    private void createConenctionMySql() {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(urlDbName,
                    userName, password);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnections() throws SQLException {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) {
            }
        }
        if (query != null) {
            try {
                query.close();
            } catch (SQLException e) {
            }
        }
    }

    public List<PurchaseDto> findByName(String name, int person, int category,int year) {
        List<PurchaseDto> lista = new ArrayList<PurchaseDto>();
        try {
            createConenctionMySql();
            if (!name.isEmpty() && person != 0 && category != 0) {
                query = conn.prepareStatement("SELECT * FROM Purchase WHERE ItemName LIKE ? AND PersonID = ? AND CategoryID = ? AND YEAR(DateOfPurchase) = ? ORDER BY DateOfPurchase DESC LIMIT 50");
                query.setString(1, "%" + name + "%");
                query.setInt(2, person);
                query.setInt(3, category);
                query.setInt(4,year);
            } else if (!name.isEmpty() && person != 0 && category == 0) {
                query = conn.prepareStatement("SELECT * FROM Purchase WHERE ItemName LIKE ? AND PersonID = ? AND YEAR(DateOfPurchase) = ? ORDER BY DateOfPurchase DESC LIMIT 50 ");
                query.setString(1, "%" + name + "%");
                query.setInt(2, person);
                query.setInt(3,year);
            } else if (!name.isEmpty() && person == 0 && category != 0) {
                query = conn.prepareStatement("SELECT * FROM Purchase WHERE ItemName LIKE ? AND CategoryID = ? AND YEAR(DateOfPurchase) = ? ORDER BY DateOfPurchase DESC LIMIT 50");
                query.setString(1, "%" + name + "%");
                query.setInt(2, category);
                query.setInt(3,year);
            } else if (!name.isEmpty() && person == 0 && category == 0) {
                query = conn.prepareStatement("SELECT * FROM Purchase WHERE ItemName LIKE ? AND YEAR(DateOfPurchase) = ? ORDER BY DateOfPurchase DESC LIMIT 50");
                query.setString(1, "%" + name + "%");
                query.setInt(2,year);
            } else if (name.isEmpty() && person != 0 && category != 0) {
                query = conn.prepareStatement("SELECT * FROM Purchase WHERE PersonID = ? AND CategoryID = ? AND YEAR(DateOfPurchase) = ? ORDER BY DateOfPurchase DESC LIMIT 50");
                query.setInt(1, person);
                query.setInt(2, category);
                query.setInt(3,year);
            } else if (name.isEmpty() && person != 0 && category == 0) {
                query = conn.prepareStatement("SELECT * FROM Purchase WHERE PersonID = ? AND YEAR(DateOfPurchase) = ? ORDER BY DateOfPurchase DESC LIMIT 50");
                query.setInt(1, person);
                query.setInt(2,year);
            } else if (name.isEmpty() && person == 0 && category != 0) {
                query = conn.prepareStatement("SELECT * FROM Purchase WHERE CategoryID = ? AND YEAR(DateOfPurchase) = ? ORDER BY DateOfPurchase DESC LIMIT 50");
                query.setInt(1, category);
                query.setInt(2,year);
            }
            System.out.println(query.toString());
            res = query.executeQuery();
            while (res.next()) {
                purchaseDto = new PurchaseDto();
                int id = res.getInt("ID");
                String nome = res.getString("ItemName");
                Date data = res.getDate("DateOfPurchase");
                int personID = res.getInt("PersonID");
                int categoryID = res.getInt("CategoryID");
                int subCategoryID = res.getInt("SubCategoryID");
                float price = res.getFloat("Price");
                purchaseDto.setID(id);
                purchaseDto.setItemName(nome);
                purchaseDto.setDateOfPurchase(data);
                purchaseDto.setPersonID(personID);
                purchaseDto.setCategoryID(categoryID);
                purchaseDto.setPrice(price);
                purchaseDto.setSubCategoryID(subCategoryID);
                lista.add(purchaseDto);
            }
            closeConnections();
            if (!lista.isEmpty()) {
                for (PurchaseDto purchase : lista) {
                    SubCategoryDto sub = null;
                    createConenctionMySql();
                    query = conn
                            .prepareStatement("SELECT * FROM Category WHERE ID = ? ");
                    query.setInt(1, purchase.getCategoryID());
                    System.out.println(query.toString());
                    res = query.executeQuery();
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
                    closeConnections();
                    createConenctionMySql();
                    query = conn
                            .prepareStatement("SELECT * FROM SubCategory WHERE ID = ? ");
                    query.setInt(1, purchase.getSubCategoryID());
                    System.out.println(query.toString());
                    res = query.executeQuery();
                    while (res.next()) {
                        sub = new SubCategoryDto();
                        int id = res.getInt("ID");
                        String nome = res.getString("Name");
                        String description = res.getString("Descricao");
                        int categoryID = res.getInt("CategoryID");
                        sub.setID(id);
                        sub.setCategoryID(categoryID);
                        sub.setName(nome);
                        sub.setDescription(description);
                        purchase.setSubCategory(sub);
                    }
                    closeConnections();
                    createConenctionMySql();
                    query = conn
                            .prepareStatement("SELECT * FROM Person where ID = ? ");
                    query.setInt(1, purchase.getPersonID());
                    System.out.println(query.toString());
                    res = query.executeQuery();
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

    public List<PurchaseDto> findAll(int year) {
        List<PurchaseDto> lista = new ArrayList<PurchaseDto>();
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT * FROM Purchase WHERE YEAR(DateOfPurchase) = ? ORDER BY DateOfPurchase DESC LIMIT 50");
            query.setInt(1,year);
            System.out.println(query.toString());
            res = query.executeQuery();
            while (res.next()) {
                purchaseDto = new PurchaseDto();
                int id = res.getInt("ID");
                String nome = res.getString("ItemName");
                Date data = res.getDate("DateOfPurchase");
                int personID = res.getInt("PersonID");
                int categoryID = res.getInt("CategoryID");
                int subCategoryID = res.getInt("SubCategoryID");
                float price = res.getFloat("Price");
                purchaseDto.setID(id);
                purchaseDto.setItemName(nome);
                purchaseDto.setDateOfPurchase(data);
                purchaseDto.setPersonID(personID);
                purchaseDto.setCategoryID(categoryID);
                purchaseDto.setSubCategoryID(subCategoryID);
                purchaseDto.setPrice(price);
                lista.add(purchaseDto);
            }
            closeConnections();
            if (!lista.isEmpty()) {
                for (PurchaseDto purchase : lista) {
                    SubCategoryDto sub = null;
                    createConenctionMySql();
                    query = conn
                            .prepareStatement("SELECT * FROM Category WHERE ID = ? ");
                    query.setInt(1, purchase.getCategoryID());
                    System.out.println(query.toString());
                    res = query.executeQuery();
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
                    closeConnections();
                    createConenctionMySql();
                    query = conn
                            .prepareStatement("SELECT * FROM SubCategory WHERE ID = ? ");
                    query.setInt(1, purchase.getSubCategoryID());
                    System.out.println(query.toString());
                    res = query.executeQuery();
                    while (res.next()) {
                        sub = new SubCategoryDto();
                        int id = res.getInt("ID");
                        String nome = res.getString("Name");
                        String description = res.getString("Descricao");
                        int categoryID = res.getInt("CategoryID");
                        sub.setID(id);
                        sub.setCategoryID(categoryID);
                        sub.setName(nome);
                        sub.setDescription(description);
                        purchase.setSubCategory(sub);
                    }
                    closeConnections();
                    createConenctionMySql();
                    query = conn
                            .prepareStatement("SELECT * FROM Person where ID = ? ");
                    query.setInt(1, purchase.getPersonID());
                    System.out.println(query.toString());
                    res = query.executeQuery();
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
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT * FROM Purchase where ID = ? LIMIT 50 ");
            query.setInt(1, id);
            System.out.println(query.toString());
            res = query.executeQuery();
            while (res.next()) {
                purchaseDto = new PurchaseDto();
                int identificador = res.getInt("ID");
                String nome = res.getString("ItemName");
                Date data = res.getDate("DateOfPurchase");
                int personID = res.getInt("PersonID");
                int categoryID = res.getInt("CategoryID");
                float price = res.getFloat("Price");
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
            createConenctionMySql();
            String insertTableSQL = "INSERT INTO Purchase (ItemName,DateOfPurchase,PersonID,CategoryID,Price,SubCategoryID) VALUES " + "(?,?,?,?,?,?)";
            query = conn
                    .prepareStatement(insertTableSQL);
            query.setString(1, dto.getItemName());
            query.setString(2, dto.getDateOfPurchase().toString());
            query.setInt(3, dto.getPersonID());
            query.setInt(4, dto.getCategoryID());
            query.setFloat(5, dto.getPrice());
            query.setInt(6, dto.getSubCategoryID());
            System.out.println(query.toString());
            res = query.executeQuery();
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void remove(PurchaseDto dto) {
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("DELETE FROM Purchase WHERE ID = ? ");
            query.setInt(1, dto.getID());
            System.out.println(query.toString());
            res = query.executeQuery();
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
            query.setInt(3, dto.getPersonID());
            query.setInt(4, dto.getCategoryID());
            query.setFloat(5, dto.getPrice());
            query.setInt(6, dto.getID());
            System.out.println(query.toString());
            query.executeUpdate();
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float findTotalYear(int ano, int subCategoryID, int categoryID) {
        float total = 0.0f;
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT SUM(Price) AS Sumatorio FROM Purchase WHERE SubCategoryID = ? AND CategoryID = ? AND Year(DateOfPurchase) = ? ");
            query.setInt(1, subCategoryID);
            query.setInt(2, categoryID);
            query.setInt(3, ano);
            System.out.println(query.toString());
            res = query.executeQuery();
            while (res.next()) {
                total = res.getFloat("Sumatorio");
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}
