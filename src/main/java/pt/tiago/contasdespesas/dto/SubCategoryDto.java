package pt.tiago.contasdespesas.dto;

import java.io.Serializable;

/**
 *
 * @author Tiago Carvalho
 */
public class SubCategoryDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private int ID;
    private String Name;
    private String Description;
    private float Total;
    private int categoryID;
    private String categoryName;
    private String categoryDescription;

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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public float getTotal() {
        return Total;
    }

    public void setTotal(float Total) {
        this.Total = Total;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    @Override
    public String toString() {
        return "SubCategory{" + "ID=" + ID + ", Name=" + Name + ", Description=" + Description + ", Total=" + Total + ", categoryID=" + categoryID + '}';
    }

}
