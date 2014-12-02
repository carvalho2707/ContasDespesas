package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import org.bson.types.ObjectId;

/**
 *
 * @author Tiago Carvalho
 */
public class SubCategoryDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ID;
    private ObjectId objID;
    private String Name;
    private String Description;
    private double Total;
    private String categoryID;
    private ObjectId categoryObjID;
    private String categoryName;
    private String categoryDescription;
    
    public ObjectId getCategoryObjID() {
        return categoryObjID;
    }

    public void setCategoryObjID(ObjectId categoryObjID) {
        this.categoryObjID = categoryObjID;
    }
    
    public ObjectId getObjID() {
        return objID;
    }

    public void setObjID(ObjectId objID) {
        this.objID = objID;
    }
    
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double Total) {
        this.Total = Total;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    @Override
    public String toString() {
        return "SubCategory{" + "ID=" + ID + ", Name=" + Name + ", Description=" + Description + ", Total=" + Total + ", categoryID=" + categoryID + '}';
    }

}
