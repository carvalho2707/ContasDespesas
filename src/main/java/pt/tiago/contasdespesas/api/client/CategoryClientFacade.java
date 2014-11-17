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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import pt.tiago.contasdespesas.dto.CategoryDto;

/**
 *
 * @author NB20708
 */
@Component
public class CategoryClientFacade {

    private String dbURL = "jdbc:derby://localhost:1527/ContasDespesas;create=true;user=tiago;password=tiago";
    private Connection conn = null;
    private CategoryDto categoryDto = null;
    private Statement stmt = null;

    private final String url = "jdbc:mysql://localhost:3306/";
    private final String dbName = "ContasDespesas";
    private final String driver = "com.mysql.jdbc.Driver";
    private final String userName = "root";
    private final String password = "tiago";

    private Statement createConnectionJavaDb() {

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            conn = DriverManager.getConnection(dbURL);
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

    public List<CategoryDto> findByName(String name) {
        List<CategoryDto> lista = new ArrayList<CategoryDto>();
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st
                    .executeQuery("SELECT * FROM Category where Name LIKE '%'"
                            + name + "'%'");
            while (res.next()) {
                categoryDto = new CategoryDto();
                int id = res.getInt("ID");
                String nome = res.getString("Name");
                String description = res.getString("Descricao");
                categoryDto.setID(id);
                categoryDto.setName(nome);
                categoryDto.setDescription(description);
                lista.add(categoryDto);
                System.out
                        .println(id + "\t" + nome + "\t" + description + "\t");
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
                System.out
                        .println(id + "\t" + name + "\t" + description + "\t");
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
                categoryDto.setID(id);
                categoryDto.setName(name);
                categoryDto.setDescription(description);
                System.out.println(identificador + "\t" + name + "\t"
                        + description + "\t");
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
            System.out.println("Query to execute = " + query);
            int output = st.executeUpdate(query);
            System.out.println("Result query create category = " + output);
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
            System.out.println("Query to execute = " + query);
            int output = st.executeUpdate(query);
            System.out.println("Result query remove category = " + output);
            conn.close();
        } catch (Exception e) {
            System.err.println("Execpcao no remove category");
            e.printStackTrace();
        }
    }

    public void edit(CategoryDto dto) {
        try {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url + dbName,
                    userName, password);
            PreparedStatement query = conn
                    .prepareStatement("UPDATE Category SET Name = ? , Descricao = ? WHERE ID = ?");
            query.setString(1, dto.getName());
            query.setString(2, dto.getDescription());
            query.setInt(3, dto.getID());
            int output = query.executeUpdate();
            System.out.println("Result query edit category = " + output);
            conn.close();
        } catch (Exception e) {
            System.err.println("Execpcao no edit category");
            e.printStackTrace();
        }
    }

    public int findIDByName(String name) {
        Integer identificador = 0;
        try {
            System.out.println("NAME A PEDIR ->    " + name);
            Statement st = createConenctionMySql();
            String nameEnclosed = name.replaceAll("\\s+", "%20");
            PreparedStatement query = conn
                    .prepareStatement("SELECT * FROM Category WHERE Name LIKE ?");
            query.setString(1, "%" + nameEnclosed + "%");
            ResultSet res = query.executeQuery();
            String nome = res.getString("Name");
            String descri = res.getString("Descricao");
            System.out.println("RESULTADO ->    " + identificador);
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