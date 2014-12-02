package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import org.bson.types.ObjectId;

/**
 *
 * @author Tiago Carvalho
 */
public class CategorySumByYearDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ID;
    private ObjectId objID;
    private String Name;
    private double Total;
    private int year;

    public ObjectId getObjID() {
        return objID;
    }

    public void setObjID(ObjectId objID) {
        this.objID = objID;
    }
    
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
