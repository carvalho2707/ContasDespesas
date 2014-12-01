package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Tiago Carvalho
 */
public class CategoryDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ID;
    private String Name;
    private String Description;
    private double Total;
    private double[] TotalByMonth;

    public double[] getTotalByMonth() {
        return this.TotalByMonth;
    }

    public void setTotalByMonth(double[] TotalByMonth) {
        this.TotalByMonth = TotalByMonth;
    }

    public double getTotal() {
        return this.Total;
    }

    public void setTotal(double Total) {
        this.Total = Total;
    }

    public String getID() {
        return this.ID;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    @Override
    public String toString() {
        return "CategoryDto{" + "ID=" + ID + ", Name=" + Name + ", Description=" + Description + ", Total=" + Total + ", TotalByMonth=" + Arrays.toString(TotalByMonth) + '}';
    }

}
