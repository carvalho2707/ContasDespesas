package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Tiago Carvalho
 */
public class PurchaseDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ID;
    private String ItemName;
    private double Price;
    private PersonDto person;
    private CategoryDto category;
    private SubCategoryDto subCategory;
    private Date DateOfPurchase;
    private String categoryID;
    private String categoryName;
    private String subCategoryID;
    private String subCategoryName;
    private String personID;
    private String personName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    public SubCategoryDto getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategoryDto subCategory) {
        this.subCategory = subCategory;
    }

    public String getSubCategoryID() {
        return this.subCategoryID;
    }

    public void setSubCategoryID(String subCategoryID) {
        this.subCategoryID = subCategoryID;
    }

    public String getCategoryID() {
        return this.categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getPersonID() {
        return this.personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public Date getDateOfPurchase() {
        return this.DateOfPurchase;
    }

    public void setDateOfPurchase(Date DateOfPurchase) {
        this.DateOfPurchase = DateOfPurchase;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
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
        return "PurchaseDto{" + "ID=" + ID + ", ItemName=" + ItemName + ", Price=" + Price + ", person=" + person + ", category=" + category + ", subCategory=" + subCategory + ", DateOfPurchase=" + DateOfPurchase + ", categoryID=" + categoryID + ", categoryName=" + categoryName + ", subCategoryID=" + subCategoryID + ", subCategoryName=" + subCategoryName + ", personID=" + personID + ", personName=" + personName + '}';
    }

}
