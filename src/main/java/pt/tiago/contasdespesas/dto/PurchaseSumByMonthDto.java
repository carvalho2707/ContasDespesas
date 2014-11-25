package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Tiago Carvalho
 */
public class PurchaseSumByMonthDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private int ID;
    private String Name;
    private float Total;
    private int month;

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

    public float getTotal() {
        return Total;
    }

    public void setTotal(float Total) {
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
