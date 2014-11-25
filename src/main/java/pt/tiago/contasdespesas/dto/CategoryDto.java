package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Tiago Carvalho
 */
public class CategoryDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private int ID;
    private String Name;
    private String Description;
    private float Total;
    private float[] TotalByMonth;

    public float[] getTotalByMonth() {
        return this.TotalByMonth;
    }

    public void setTotalByMonth(float[] TotalByMonth) {
        this.TotalByMonth = TotalByMonth;
    }

    public float getTotal() {
        return this.Total;
    }

    public void setTotal(float Total) {
        this.Total = Total;
    }

    public int getID() {
        return this.ID;
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
