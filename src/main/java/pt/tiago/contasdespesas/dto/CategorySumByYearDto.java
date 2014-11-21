package pt.tiago.contasdespesas.dto;

import java.io.Serializable;

/**
 *
 * @author Tiago Carvalho
 */
public class CategorySumByYearDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private int ID;
    private String Name;
    private double Total;
    private int year;

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "CategorySumByYearDto{" + "ID=" + ID + ", Name=" + Name + ", Total=" + Total + ", year=" + year + '}';
    }
    
}
