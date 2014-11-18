package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import java.time.Month;

/**
 *
 * @author Tiago Carvalho
 */
public class PurchaseSumByMonthDto implements Serializable {

    private int ID;
    private String Name;
    private double Total;
    private Month month;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double Total) {
        this.Total = Total;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

}
