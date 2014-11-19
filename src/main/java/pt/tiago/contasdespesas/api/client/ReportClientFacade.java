package pt.tiago.contasdespesas.api.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.dto.CategorySumByYearDto;
import pt.tiago.contasdespesas.dto.PurchaseSumByMonthDto;
import pt.tiago.contasdespesas.dto.PersonDto;
import pt.tiago.contasdespesas.dto.PurchaseSumByYearDto;

/**
 *
 * @author Tiago Carvalho
 */
@SuppressWarnings("CallToPrintStackTrace")
@Component
public class ReportClientFacade {

    private PurchaseSumByMonthDto categoryByMonth = null;
    private Month[] mes = Month.values();
    private Connection conn;
    private static final String urlDbName = "jdbc:mysql://localhost:3306/ContasDespesas";
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String userName = "root";
    private static final String password = "tiago";
    private ResultSet res = null;
    private PreparedStatement query = null;

    public Month[] getMes() {
        return this.mes;
    }

    public void setMes(Month[] mes) {
        this.mes = mes;
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

    public List<PurchaseSumByMonthDto> findAllByMonth() {
        List<PurchaseSumByMonthDto> lista = new ArrayList<PurchaseSumByMonthDto>();
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("Select sum(Price) as Sumatorio, MONTH(DateOfPurchase) as Mes, CategoryID from Purchase group by CategoryID ,MONTH(DateOfPurchase)");
            while (res.next()) {
                categoryByMonth = new PurchaseSumByMonthDto();
                int id = res.getInt("CategoryID");
                Month data = mes[res.getInt("Mes")];
                double total = res.getDouble("Sumatorio");
                categoryByMonth.setID(id);
                categoryByMonth.setMonth(data);
                categoryByMonth.setTotal(total);
                lista.add(categoryByMonth);
            }
            if (!lista.isEmpty()) {
                for (PurchaseSumByMonthDto categoria : lista) {
                    createConenctionMySql();
                    query = conn
                            .prepareStatement("SELECT Name FROM Category where ID = ? ");
                    query.setInt(1, categoria.getID());
                    while (res.next()) {
                        String nome = res.getString("Name");
                        categoria.setName(nome);
                    }
                }
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public PurchaseSumByMonthDto[] findTotalPersonByNameByMonth(int identificador, int categoryID) {
        PurchaseSumByMonthDto[] purchase = new PurchaseSumByMonthDto[12];
        PurchaseSumByMonthDto temp;
        try {
            createConenctionMySql();
            if (categoryID == -1) {
                query = conn
                        .prepareStatement("SELECT SUM(Price) AS Sumatorio, MONTH(DateOfPurchase) AS Mes FROM Purchase WHERE PersonID = ? GROUP BY MONTH(DateOfPurchase) ");
                query.setInt(1, identificador);
            } else {
                query = conn
                        .prepareStatement("SELECT SUM(Price) AS Sumatorio, MONTH(DateOfPurchase) AS Mes FROM Purchase WHERE PersonID = ? AND CategoryID = ? GROUP BY MONTH(DateOfPurchase)");
                query.setInt(1, identificador);
                query.setInt(2, categoryID);
            }
            res = query.executeQuery();
            while (res.next()) {
                temp = new PurchaseSumByMonthDto();
                int pos = res.getInt("Mes") - 1;
                Month data = mes[pos];
                double total = res.getDouble("Sumatorio");
                temp.setMonth(data);
                temp.setTotal(total);
                purchase[pos] = temp;
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return purchase;
    }

    public List<PersonDto> findAllPersons() {
        List<PersonDto> lista = new ArrayList<PersonDto>();
        PersonDto personDto;
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

    public PurchaseSumByYearDto[] findTotalPersonByNameByYear(int identificador, int dataInicio, int dataFinal) {
        int tamanho = dataFinal - dataInicio;
        PurchaseSumByYearDto[] purchase = new PurchaseSumByYearDto[tamanho + 1];
        PurchaseSumByYearDto temp;
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT SUM(Price) AS Sumatorio, Year(DateOfPurchase) AS Ano FROM Purchase WHERE PersonID = ? GROUP BY Year(DateOfPurchase) ");
            query.setInt(1, identificador);
            res = query.executeQuery();
            while (res.next()) {
                temp = new PurchaseSumByYearDto();
                int ano = res.getInt("Ano");
                double total = res.getDouble("Sumatorio");
                temp.setYear(ano);
                temp.setTotal(total);
                int pos = ano - dataInicio;
                purchase[pos] = temp;
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return purchase;
    }

    public int findMinYear() {
        int pos = 0;
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT MIN(YEAR(DateOfPurchase)) AS inicial FROM Purchase");
            res = query.executeQuery();
            while (res.next()) {
                pos = res.getInt("inicial");
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pos;
    }

    public List<PurchaseSumByMonthDto> findTotalPersonByNameByMonth(String monthSelected) {
        List<PurchaseSumByMonthDto> purchase = new ArrayList<PurchaseSumByMonthDto>();
        PurchaseSumByMonthDto temp;
        try {
            Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthSelected);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int monthInt = cal.get(Calendar.MONTH) + 1;
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT SUM(Price) AS Sumatorio, PersonID AS pessoa FROM Purchase WHERE MONTH(DateOfPurchase) = ? GROUP BY PersonID ");
            query.setInt(1, monthInt);
            res = query.executeQuery();
            while (res.next()) {
                temp = new PurchaseSumByMonthDto();
                double total = res.getDouble("Sumatorio");
                int idP = res.getInt("pessoa");
                temp.setTotal(total);
                temp.setID(idP);
                purchase.add(temp);
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchase;
    }

    public List<PurchaseSumByYearDto> findTotalPersonByNameByYear(int ano) {
        List<PurchaseSumByYearDto> purchase = new ArrayList<PurchaseSumByYearDto>();
        PurchaseSumByYearDto temp;
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT SUM(Price) AS Sumatorio, PersonID AS pessoa FROM Purchase WHERE YEAR(DateOfPurchase) = ? GROUP BY PersonID ");
            query.setInt(1, ano);
            res = query.executeQuery();
            while (res.next()) {
                temp = new PurchaseSumByYearDto();
                double total = res.getDouble("Sumatorio");
                int idP = res.getInt("pessoa");
                temp.setTotal(total);
                temp.setID(idP);
                purchase.add(temp);
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchase;
    }

    public ArrayList<String> findYears() {
        ArrayList<String> lista = new ArrayList<String>();
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT DISTINCT(YEAR(DateOfPurchase)) AS ano FROM Purchase ");
            res = query.executeQuery();
            while (res.next()) {
                int valor = res.getInt("ano");
                lista.add(Integer.toString(valor));
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<CategorySumByYearDto> findTotalCategorySumByYear(int ano) {
        List<CategorySumByYearDto> purchase = new ArrayList<CategorySumByYearDto>();
        CategorySumByYearDto temp;
        try {
            createConenctionMySql();
            query = conn
                    .prepareStatement("SELECT SUM(Price) AS Sumatorio, CategoryID AS categoria FROM Purchase WHERE YEAR(DateOfPurchase) = ? GROUP BY CategoryID");
            query.setInt(1, ano);
            res = query.executeQuery();
            while (res.next()) {
                temp = new CategorySumByYearDto();
                double total = res.getDouble("Sumatorio");
                int idP = res.getInt("categoria");
                temp.setTotal(total);
                temp.setID(idP);
                purchase.add(temp);
            }
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchase;
    }

}
