package pt.tiago.contasdespesas.api.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT * FROM Category");
            res = query.executeQuery();
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

    public List<SubCategoryDto> findAllSub() {
        List<SubCategoryDto> lista = new ArrayList<SubCategoryDto>();
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT * FROM SubCategory S INNER JOIN Category C ON S.CategoryID = C.ID");
            res = query.executeQuery();
            while (res.next()) {
                subCategoryDto = new SubCategoryDto();
                int id = res.getInt("S.ID");
                String name = res.getString("S.Name");
                String description = res.getString("S.Descricao");
                int categoryid = res.getInt("C.ID");
                String categoryName = res.getString("C.Name");
                String categoryDescription = res.getString("C.Descricao");
                subCategoryDto.setID(id);
                subCategoryDto.setName(name);
                subCategoryDto.setDescription(description);
                subCategoryDto.setCategoryID(categoryid);
                subCategoryDto.setCategoryName(categoryName);
                subCategoryDto.setCategoryDescription(categoryDescription);
                lista.add(subCategoryDto);
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<SubCategoryDto> findAllSubByCategoryID(int id) {
        List<SubCategoryDto> lista = new ArrayList<SubCategoryDto>();
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT * FROM SubCategory S WHERE CategoryID = ? ");
            query.setInt(1, id);
            res = query.executeQuery();
            while (res.next()) {
                subCategoryDto = new SubCategoryDto();
                int identification = res.getInt("S.ID");
                String name = res.getString("S.Name");
                String description = res.getString("S.Descricao");
                int categoryID = res.getInt("S.CategoryID");
                subCategoryDto.setID(identification);
                subCategoryDto.setName(name);
                subCategoryDto.setDescription(description);
                subCategoryDto.setCategoryID(categoryID);
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
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT * FROM Category where ID = ? ");
            query.setInt(1, id);
            res = query.executeQuery();
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
            createConenctionMySql();
            String insertTableSQL = "INSERT INTO Category (Name,Descricao) VALUES " + "(?,?)";
            query = conn
                    .prepareStatement(insertTableSQL);
            query.setString(1, dto.getName());
            query.setString(2, dto.getDescription());
            res = query.executeQuery();
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void createSub(SubCategoryDto dto) {
        try {
            createConenctionMySql();
            String insertTableSQL = "INSERT INTO SubCategory (Name,Descricao,CategoryID) VALUES " + "(?,?,?)";
            query = conn
                    .prepareStatement(insertTableSQL);
            query.setString(1, dto.getName());
            query.setString(2, dto.getDescription());
            query.setInt(2, dto.getCategoryID());
            res = query.executeQuery();
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(CategoryDto dto) {
        try {
            createConenctionMySql();
            String insertTableSQL = "DELETE FROM Category WHERE ID = " + "(?)";
            query = conn
                    .prepareStatement(insertTableSQL);
            query.setInt(1, dto.getID());
            res = query.executeQuery();
            closeConnections();
        } catch (Exception e) {
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
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT Name FROM Category");
            res = query.executeQuery();
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
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT DISTINCT(YEAR(DateOfPurchase)) AS ano FROM Purchase");
            res = query.executeQuery();
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
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT SUM(Price) AS Sumatorio FROM Purchase WHERE CategoryID = ?  AND Year(DateOfPurchase) = ?");
            query.setInt(1, categoria);
            query.setInt(2, ano);
            res = query.executeQuery();
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
