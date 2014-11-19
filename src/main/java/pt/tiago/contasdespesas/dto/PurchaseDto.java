package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Tiago Carvalho
 */
public class PurchaseDto implements Serializable{

    private static final long serialVersionUID = 1L;
    private int ID;
    private String ItemName;
    private double Price;
    private PersonDto person;
    private CategoryDto category;
    private Date DateOfPurchase;
    private int categoryID;
    private int subCategoryID;
    private int personID;

    public int getSubCategoryID() {
        return this.subCategoryID;
    }

    public void setSubCategoryID(int subCategoryID) {
        this.subCategoryID = subCategoryID;
    }

    public int getCategoryID() {
        return this.categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getPersonID() {
        return this.personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public Date getDateOfPurchase() {
        return this.DateOfPurchase;
    }

    public void setDateOfPurchase(Date DateOfPurchase) {
        this.DateOfPurchase = DateOfPurchase;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getItemName() {
        return this.ItemName;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    public double getPrice() {
        return this.Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public PersonDto getPerson() {
        return this.person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public CategoryDto getCategory() {
        return this.category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "PurchaseDto{" + "ID=" + ID + ", ItemName=" + ItemName + ", Price=" + Price + ", person=" + person + ", category=" + category + ", DateOfPurchase=" + DateOfPurchase + ", categoryID=" + categoryID + ", subCategoryID=" + subCategoryID + ", personID=" + personID + '}';
    }
}
