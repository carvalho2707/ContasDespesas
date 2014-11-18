package pt.tiago.contasdespesas.api.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    private Connection conn = null;
    private PurchaseSumByMonthDto categoryByMonth = null;
    private Month[] mes = Month.values();
    private final String url = "jdbc:mysql://localhost:3306/";
    private final String dbName = "ContasDespesas";
    private final String driver = "com.mysql.jdbc.Driver";
    private final String userName = "root";
    private final String password = "tiago";

    public Month[] getMes() {
        return mes;
    }

    public void setMes(Month[] mes) {
        this.mes = mes;
    }

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

    public List<PurchaseSumByMonthDto> findAllByMonth() {
        List<PurchaseSumByMonthDto> lista = new ArrayList<PurchaseSumByMonthDto>();
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("Select sum(Price) as Sumatorio, MONTH(DateOfPurchase) as Mes, CategoryID from Purchase group by CategoryID ,MONTH(DateOfPurchase)");
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
                    st = createConenctionMySql();
                    res = st.executeQuery("SELECT Name FROM Category where ID = " + categoria.getID());
                    while (res.next()) {
                        String nome = res.getString("Name");
                        categoria.setName(nome);
                    }
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public PurchaseSumByMonthDto[] findTotalPersonByNameByMonth(int identificador, int categoryID) {
        PurchaseSumByMonthDto[] purchase = new PurchaseSumByMonthDto[12];
        PurchaseSumByMonthDto temp;
        ResultSet res = null;
        try {
            Statement st = createConenctionMySql();
            if (categoryID == -1) {
                res = st.executeQuery("SELECT SUM(Price) AS Sumatorio, MONTH(DateOfPurchase) AS Mes FROM Purchase WHERE PersonID = " + identificador + " GROUP BY MONTH(DateOfPurchase)");
            } else {
                String pedido = "SELECT SUM(Price) AS Sumatorio, MONTH(DateOfPurchase) AS Mes FROM Purchase WHERE PersonID = " + identificador + " AND CategoryID = " + categoryID + " GROUP BY MONTH(DateOfPurchase)";
                res = st.executeQuery(pedido);
            }
            while (res.next()) {
                temp = new PurchaseSumByMonthDto();
                int pos = res.getInt("Mes") - 1;
                Month data = mes[pos];
                double total = res.getDouble("Sumatorio");
                temp.setMonth(data);
                temp.setTotal(total);
                purchase[pos] = temp;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return purchase;
    }

    public List<PersonDto> findAllPersons() {
        List<PersonDto> lista = new ArrayList<PersonDto>();
        PersonDto personDto;
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

    public PurchaseSumByYearDto[] findTotalPersonByNameByYear(int identificador, int dataInicio, int dataFinal) {
        int tamanho = dataFinal - dataInicio;
        PurchaseSumByYearDto[] purchase = new PurchaseSumByYearDto[tamanho + 1];
        PurchaseSumByYearDto temp;
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT SUM(Price) AS Sumatorio, Year(DateOfPurchase) AS Ano FROM Purchase WHERE PersonID = " + identificador + " GROUP BY Year(DateOfPurchase)");
            while (res.next()) {
                temp = new PurchaseSumByYearDto();
                int ano = res.getInt("Ano");
                double total = res.getDouble("Sumatorio");
                temp.setYear(ano);
                temp.setTotal(total);
                int pos = ano - dataInicio;
                purchase[pos] = temp;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return purchase;
    }

    public int findMinYear() {
        int pos = 0;
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT MIN(YEAR(DateOfPurchase)) AS inicial FROM Purchase");
            while (res.next()) {
                pos = res.getInt("inicial");
            }
            conn.close();
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
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT SUM(Price) AS Sumatorio, PersonID AS pessoa FROM Purchase WHERE MONTH(DateOfPurchase) = " + monthInt + " GROUP BY PersonID");
            while (res.next()) {
                temp = new PurchaseSumByMonthDto();
                double total = res.getDouble("Sumatorio");
                int idP = res.getInt("pessoa");
                temp.setTotal(total);
                temp.setID(idP);
                purchase.add(temp);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchase;
    }

    public List<PurchaseSumByYearDto> findTotalPersonByNameByYear(int ano) {
        List<PurchaseSumByYearDto> purchase = new ArrayList<PurchaseSumByYearDto>();
        PurchaseSumByYearDto temp;
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT SUM(Price) AS Sumatorio, PersonID AS pessoa FROM Purchase WHERE YEAR(DateOfPurchase) = " + ano + " GROUP BY PersonID");
            while (res.next()) {
                temp = new PurchaseSumByYearDto();
                double total = res.getDouble("Sumatorio");
                int idP = res.getInt("pessoa");
                temp.setTotal(total);
                temp.setID(idP);
                purchase.add(temp);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchase;
    }

    public ArrayList<String> findYears() {
        ArrayList<String> lista = new ArrayList<String>();
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT DISTINCT(YEAR(DateOfPurchase)) AS ano FROM Purchase");
            while (res.next()) {
                int valor = res.getInt("ano");
                lista.add(Integer.toString(valor));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<CategorySumByYearDto> findTotalCategorySumByYear(int ano) {
        List<CategorySumByYearDto> purchase = new ArrayList<CategorySumByYearDto>();
        CategorySumByYearDto temp;
        try {
            Statement st = createConenctionMySql();
            ResultSet res = st.executeQuery("SELECT SUM(Price) AS Sumatorio, CategoryID AS categoria FROM Purchase WHERE YEAR(DateOfPurchase) = " + ano + " GROUP BY CategoryID");
            while (res.next()) {
                temp = new CategorySumByYearDto();
                double total = res.getDouble("Sumatorio");
                int idP = res.getInt("categoria");
                temp.setTotal(total);
                temp.setID(idP);
                purchase.add(temp);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchase;
    }

}
