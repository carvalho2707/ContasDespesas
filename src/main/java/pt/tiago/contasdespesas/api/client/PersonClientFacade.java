package pt.tiago.contasdespesas.api.client;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.dto.PersonDto;

/**
 *
 * @author Tiago Carvalho
 */
@SuppressWarnings("CallToPrintStackTrace")
@Component
public class PersonClientFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    private Connection conn;
    private static final String urlDbName = "jdbc:mysql://localhost:3306/ContasDespesas";
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String userName = "root";
    private static final String password = "tiago";
    private PersonDto personDto = null;
    private ResultSet res;
    private PreparedStatement query;

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

    public List<PersonDto> findByName(String name, String surname) {
        List<PersonDto> lista = new ArrayList<PersonDto>();
        try {
            createConenctionMySql();
            if (!name.isEmpty() && surname.isEmpty()) {
                query = conn.prepareStatement("SELECT * FROM Person WHERE Name LIKE ?");
                query.setString(1, "%" + name + "%");
            } else if (name.isEmpty() && !surname.isEmpty()) {
                query = conn.prepareStatement("SELECT * FROM Person WHERE Surname LIKE ?");
                query.setString(1, "%" + surname + "%");
            } else if (!name.isEmpty() && !surname.isEmpty()) {
                query = conn.prepareStatement("SELECT * FROM Person WHERE Name LIKE ? AND Surname LIKE ?");
                query.setString(1, "%" + name + "%");
                query.setString(2, "%" + surname + "%");
            }
            res = query.executeQuery();
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
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<PersonDto> findAll() {
        List<PersonDto> lista = new ArrayList<PersonDto>();
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT * FROM Person");
            res = query.executeQuery();
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
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public PersonDto findByID(int id) {
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT * FROM Person where ID = ? ");
            query.setInt(1, id);
            res = query.executeQuery();
            while (res.next()) {
                personDto = new PersonDto();
                int identificador = res.getInt("ID");
                String name = res.getString("Name");
                String surname = res.getString("Surname");
                personDto.setID(identificador);
                personDto.setName(name);
                personDto.setSurname(surname);
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personDto;
    }

    public void create(PersonDto dto) {
        try {
            createConenctionMySql();
            String insertTableSQL = "INSERT INTO Person (Name,Surname) VALUES " + "(?,?)";
            query = conn
                    .prepareStatement(insertTableSQL);
            query.setString(1, dto.getName());
            query.setString(2, dto.getSurname());
            res = query.executeQuery();
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(PersonDto dto) {
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("DELETE FROM Person WHERE ID = ?");
            query.setInt(1, dto.getID());
            res = query.executeQuery();

            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void edit(PersonDto dto) {
        try {
            createConenctionMySql();
            query = conn.prepareStatement("UPDATE Person SET Name = ? , Surname = ? WHERE ID = ?");
            query.setString(1, dto.getName());
            query.setString(2, dto.getSurname());
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
                    .prepareStatement("SELECT * FROM Person WHERE Name LIKE ?");
            query.setString(1, "%" + nameEnclosed + "%");
            res = query.executeQuery();
            while (res.next()) {
                identificador = res.getInt("ID");
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (identificador > 0) ? identificador : 0;
    }

    public ArrayList<Integer> findYears() {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        try {
            createConenctionMySql();
            query = conn.prepareStatement("SELECT DISTINCT(YEAR(DateOfPurchase)) AS ano FROM Purchase");
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

    public double findPersonTotalByYear(int ano, int pessoa) {
        double total = 0.0;
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT SUM(Price) AS Sumatorio FROM Purchase WHERE PersonID = ? AND Year(DateOfPurchase) = ?");
            query.setInt(1, pessoa);
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
