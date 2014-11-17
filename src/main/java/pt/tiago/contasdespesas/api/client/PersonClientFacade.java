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
import pt.tiago.contasdespesas.dto.PersonDto;

/**
 *
 * @author NB20708
 */
@Component
public class PersonClientFacade {

    private Connection conn = null;
    private final String url = "jdbc:mysql://localhost:3306/";
    private final String dbName = "ContasDespesas";
    private final String driver = "com.mysql.jdbc.Driver";
    private final String userName = "root";
    private final String password = "tiago";
    private PersonDto personDto = null;

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

    public List<PersonDto> findByName(String name, String surname) {
        List<PersonDto> lista = new ArrayList<PersonDto>();
        PreparedStatement query = null;
        try {
            createConenctionMySql();
            if (!name.isEmpty() && surname.isEmpty()) {
                query = conn.prepareStatement("SELECT * FROM Person WHERE Name LIKE ?");
                query.setString(1, "%" + name + "%");
            } else if (name.isEmpty() && !surname.isEmpty()) {
                query = conn.prepareStatement("SELECT * FROM Person WHERE Surname LIKE ?");
                query.setString(2, "%" + surname + "%");
            } else if (!name.isEmpty() && !surname.isEmpty()) {
                query = conn.prepareStatement("SELECT * FROM Person WHERE Name LIKE ? AND Surname LIKE ?");
                query.setString(1, "%" + name + "%");
                query.setString(2, "%" + surname + "%");
            }
            ResultSet res = query.executeQuery();
            while (res.next()) {
                personDto = new PersonDto();
                int id = res.getInt("ID");
                String nome = res.getString("Name");
                String surnameQ = res.getString("Surname");
                personDto.setID(id);
                personDto.setName(nome);
                personDto.setSurname(surnameQ);
                lista.add(personDto);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<PersonDto> findAll() {
        List<PersonDto> lista = new ArrayList<PersonDto>();
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT * FROM Person");
            while (res.next()) {
                personDto = new PersonDto();
                int id = res.getInt("ID");
                String name = res.getString("Name");
                String surname = res.getString("Surname");
                personDto.setID(id);
                personDto.setName(name);
                personDto.setSurname(surname);
                lista.add(personDto);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public PersonDto findByID(int id) {
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT * FROM Person where ID = " + id);
            while (res.next()) {
                personDto = new PersonDto();
                int identificador = res.getInt("ID");
                String name = res.getString("Name");
                String surname = res.getString("Surname");
                personDto.setID(id);
                personDto.setName(name);
                personDto.setSurname(surname);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personDto;
    }

    public void create(PersonDto dto) {
        try {
            Statement st = createConenctionMySql();
            String query = "INSERT INTO Person (Name,Surname) VALUES (" + "'" + dto.getName() + "'" + "," + "'" + dto.getSurname() + "'" + ")";
            System.out.println("Query to execute = " + query);
            int output = st.executeUpdate(query);
            System.out.println("Result query create person = " + output);
            conn.close();
        } catch (Exception e) {
            System.err.println("Execpcao no create person");
            e.printStackTrace();
        }

    }

    public void remove(PersonDto dto) {
        try {
            Statement st = createConenctionMySql();
            String query = "DELETE FROM Person WHERE ID =" + dto.getID();
            System.out.println("Query to execute = " + query);
            int output = st.executeUpdate(query);
            System.out.println("Result query person = " + output);
            conn.close();
        } catch (Exception e) {
            System.err.println("Execpcao no remove person");
            e.printStackTrace();
        }
    }

    public void edit(PersonDto dto) {
        try {
            Statement st = createConenctionMySql();
            PreparedStatement query = conn.prepareStatement("UPDATE Person SET Name = ? , Surname = ? WHERE ID = ?");
            query.setString(1, dto.getName());
            query.setString(2, dto.getSurname());
            query.setInt(3, dto.getID());
            int output = query.executeUpdate();
            System.out.println("Result query edit person = " + output);
            conn.close();
        } catch (Exception e) {
            System.err.println("Execpcao no edit person");
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
                    .prepareStatement("SELECT * FROM Person WHERE Name LIKE ?");
            query.setString(1, "%" + nameEnclosed + "%");
            ResultSet res = query.executeQuery();
            identificador = res.getInt("ID");
            System.out.println("RESULTADO ->    " + identificador);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return identificador;
    }

}
