package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import org.bson.types.ObjectId;

/**
 *
 * @author Tiago Carvalho
 */
public class PurchaseSumByMonthDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ID;
    private String Name;
    private double Total;
    private int month;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "PurchaseSumByMonthDto{" + "ID=" + ID + ", Name=" + Name + ", Total=" + Total + ", month=" + month + '}';
    }

}
