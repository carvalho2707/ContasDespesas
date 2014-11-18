package pt.tiago.contasdespesas.api.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.dto.CategoryDto;

/**
 *
 * @author Tiago Carvalho
 */
@Component
public class CategoryClientFacade {

    private String dbURL = "jdbc:derby://localhost:1527/ContasDespesas;create=true;user=tiago;password=tiago";
    private Connection conn = null;
    private CategoryDto categoryDto = null;
    private final String url = "jdbc:mysql://localhost:3306/";
    private final String dbName = "ContasDespesas";
    private final String driver = "com.mysql.jdbc.Driver";
    private final String userName = "root";
    private final String password = "tiago";

    private Statement createConenctionMySql() {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName,
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

    public List<CategoryDto> findByName(String name) {
        List<CategoryDto> lista = new ArrayList<CategoryDto>();
        try {
            createConenctionMySql();
            PreparedStatement query = conn
                    .prepareStatement("SELECT * FROM Category WHERE Name LIKE ?");
            query.setString(1, "%" + name + "%");
            ResultSet res = query.executeQuery();
            while (res.next()) {
                categoryDto = new CategoryDto();
                int id = res.getInt("ID");
                String nome = res.getString("Name");
                String description = res.getString("Descricao");
                categoryDto.setID(id);
                categoryDto.setName(nome);
                categoryDto.setDescription(description);
                lista.add(categoryDto);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<CategoryDto> findAll() {
        List<CategoryDto> lista = new ArrayList<CategoryDto>();
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT * FROM Category");
            while (res.next()) {
                categoryDto = new CategoryDto();
                int id = res.getInt("ID");
                String name = res.getString("Name");
                String description = res.getString("Descricao");
                categoryDto.setID(id);
                categoryDto.setName(name);
                categoryDto.setDescription(description);
                lista.add(categoryDto);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public CategoryDto findByID(int id) {
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st
                    .executeQuery("SELECT * FROM Category where ID = " + id);
            while (res.next()) {
                categoryDto = new CategoryDto();
                int identificador = res.getInt("ID");
                String name = res.getString("Name");
                String description = res.getString("Descricao");
                categoryDto.setID(identificador);
                categoryDto.setName(name);
                categoryDto.setDescription(description);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryDto;
    }

    public void create(CategoryDto dto) {
        try {
            Statement st = createConenctionMySql();
            String query = "INSERT INTO Category (Name,Descricao) VALUES ("
                    + "'"
                    + dto.getName()
                    + "'"
                    + ","
                    + "'"
                    + dto.getDescription() + "'" + ")";
            st.executeUpdate(query);
            conn.close();
        } catch (Exception e) {
            System.err.println("Execpcao no create category");
            e.printStackTrace();
        }

    }

    public void remove(CategoryDto dto) {
        try {
            Statement st = createConenctionMySql();
            String query = "DELETE FROM Category WHERE ID =" + dto.getID();
            st.executeUpdate(query);
            conn.close();
        } catch (Exception e) {
            System.err.println("Execpcao no remove category");
            e.printStackTrace();
        }
    }

    public void edit(CategoryDto dto) {
        try {
            createConenctionMySql();
            PreparedStatement query = conn
                    .prepareStatement("UPDATE Category SET Name = ? , Descricao = ? WHERE ID = ?");
            query.setString(1, dto.getName());
            query.setString(2, dto.getDescription());
            query.setInt(3, dto.getID());
            query.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.err.println("Execpcao no edit category");
            e.printStackTrace();
        }
    }

    public int findIDByName(String name) {
        Integer identificador = 0;
        try {
            createConenctionMySql();
            String nameEnclosed = name.replaceAll("\\s+", "%20");
            PreparedStatement query = conn
                    .prepareStatement("SELECT * FROM Category WHERE Name LIKE ?");
            query.setString(1, "%" + nameEnclosed + "%");
            ResultSet res = query.executeQuery();
            identificador = res.getInt("ID");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return identificador;
    }

    public List<String> findAllNames() {
        List<String> lista = new ArrayList<String>();
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT Name FROM Category");
            while (res.next()) {
                String name = res.getString("Name");
                lista.add(name);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Integer> findYears() {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT DISTINCT(YEAR(DateOfPurchase)) AS ano FROM Purchase");
            while (res.next()) {
                int valor = res.getInt("ano");
                lista.add(valor);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public double findCategoryTotalByYear(int ano, int categoria) {
        double total = 0.0;
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT SUM(Price) AS Sumatorio FROM Purchase WHERE CategoryID = " + categoria + " AND Year(DateOfPurchase) = " + ano);
            while (res.next()) {
                total = res.getDouble("Sumatorio");
            }
            conn.close();
        } catch (Exception e) {
            total = 0.0;
            e.printStackTrace();
        }
        return total;
    }

}
