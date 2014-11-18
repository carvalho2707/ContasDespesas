package pt.tiago.contasdespesas.api.client;

import java.io.Serializable;
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
    private static final String urlDbName = "jdbc:mysql://localhost:3306/ContasDespesas";
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String userName = "root";
    private static final String password = "tiago";
    private Connection conn;
    private ResultSet res = null;
    private PreparedStatement query = null;
    private Statement st = null;

 

    private void closeConnections() throws SQLException {
        conn.close();
        res.close();
        query.close();
        st.close();
    }

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

    public List<CategoryDto> findByName(String name) {
        List<CategoryDto> lista = new ArrayList<CategoryDto>();
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT * FROM Category WHERE Name LIKE ?");
            query.setString(1, "%" + name + "%");
            res = query.executeQuery();
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
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<CategoryDto> findAll() {
        List<CategoryDto> lista = new ArrayList<CategoryDto>();
        try {
            st = createConenctionMySql();
            res = st.executeQuery("SELECT * FROM Category");
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
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<SubCategoryDto> findAllSub(int categoryID) {
        List<SubCategoryDto> lista = new ArrayList<SubCategoryDto>();
        try {
            st = createConenctionMySql();
            res = st.executeQuery("SELECT * FROM SubCategory where CategoryID = " + categoryID);
            while (res.next()) {
                subCategoryDto = new SubCategoryDto();
                int id = res.getInt("ID");
                String name = res.getString("Name");
                String description = res.getString("Descricao");
                subCategoryDto.setID(id);
                subCategoryDto.setName(name);
                subCategoryDto.setDescription(description);
                lista.add(subCategoryDto);
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public CategoryDto findByID(int id) {
        try {
            st = createConenctionMySql();
            res = st.executeQuery("SELECT * FROM Category where ID = " + id);
            while (res.next()) {
                categoryDto = new CategoryDto();
                int identificador = res.getInt("ID");
                String name = res.getString("Name");
                String description = res.getString("Descricao");
                categoryDto.setID(identificador);
                categoryDto.setName(name);
                categoryDto.setDescription(description);
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryDto;
    }

    public void create(CategoryDto dto) {
        try {
            st = createConenctionMySql();
            String queryTo = "INSERT INTO Category (Name,Descricao) VALUES ("
                    + "'"
                    + dto.getName()
                    + "'"
                    + ","
                    + "'"
                    + dto.getDescription() + "'" + ")";
            st.executeUpdate(queryTo);
            closeConnections();
        } catch (Exception e) {
            System.err.println("Execpcao no create category");
            e.printStackTrace();
        }

    }

    public void remove(CategoryDto dto) {
        try {
            st = createConenctionMySql();
            String queryTo = "DELETE FROM Category WHERE ID =" + dto.getID();
            st.executeUpdate(queryTo);
            closeConnections();
        } catch (Exception e) {
            System.err.println("Execpcao no remove category");
            e.printStackTrace();
        }
    }

    public void edit(CategoryDto dto) {
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("UPDATE Category SET Name = ? , Descricao = ? WHERE ID = ?");
            query.setString(1, dto.getName());
            query.setString(2, dto.getDescription());
            query.setInt(3, dto.getID());
            query.executeUpdate();
            closeConnections();
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
            query = conn
                    .prepareStatement("SELECT * FROM Category WHERE Name LIKE ?");
            query.setString(1, "%" + nameEnclosed + "%");
            res = query.executeQuery();
            identificador = res.getInt("ID");
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return identificador;
    }

    public List<String> findAllNames() {
        List<String> lista = new ArrayList<String>();
        try {
            st = createConenctionMySql();
            res = st.executeQuery("SELECT Name FROM Category");
            while (res.next()) {
                String name = res.getString("Name");
                lista.add(name);
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Integer> findYears() {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        try {
            st = createConenctionMySql();
            res = st.executeQuery("SELECT DISTINCT(YEAR(DateOfPurchase)) AS ano FROM Purchase");
            while (res.next()) {
                int valor = res.getInt("ano");
                lista.add(valor);
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public double findCategoryTotalByYear(int ano, int categoria) {
        double total = 0.0;
        try {
            st = createConenctionMySql();
            res = st.executeQuery("SELECT SUM(Price) AS Sumatorio FROM Purchase WHERE CategoryID = " + categoria + " AND Year(DateOfPurchase) = " + ano);
            while (res.next()) {
                total = res.getDouble("Sumatorio");
            }
            closeConnections();
        } catch (Exception e) {
            total = 0.0;
            e.printStackTrace();
        }
        return total;
    }

}
