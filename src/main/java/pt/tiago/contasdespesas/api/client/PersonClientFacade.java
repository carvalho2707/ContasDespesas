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
import javax.ejb.Stateless;
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

    public List<PersonDto> findByName(String name, String surname) {
        List<PersonDto> lista = new ArrayList<PersonDto>();
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT * FROM Person where Name LIKE '%'" + name + "'%'");
            while (res.next()) {
                personDto = new PersonDto();
                int id = res.getInt("ID");
                String nome = res.getString("Name");
                String surnameQ = res.getString("Surname");
                personDto.setID(id);
                personDto.setName(nome);
                personDto.setSurname(surnameQ);
                lista.add(personDto);
                System.out.println(id + "\t" + nome + "\t" + surnameQ + "\t");
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
                System.out.println(id + "\t" + name + "\t" + surname + "\t");
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
                System.out.println(identificador + "\t" + name + "\t" + surname + "\t");
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

}
