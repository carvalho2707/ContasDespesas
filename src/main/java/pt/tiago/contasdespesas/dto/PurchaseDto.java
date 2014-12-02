package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import java.util.Date;
import org.bson.types.ObjectId;

/**
 *
 * @author Tiago Carvalho
 */
public class PurchaseDto implements Serializable{

    private static final long serialVersionUID = 1L;
    private String ID;
    private ObjectId objID;
    private String ItemName;
    private double Price;
    private PersonDto person;
    private CategoryDto category;
    private SubCategoryDto subCategory;
    private Date DateOfPurchase;
    private String categoryID;
    private ObjectId categoryObjID;
    private String subCategoryID;
    private ObjectId subCategoryObjID;
    private String personID;
    private ObjectId personObjID;

    public ObjectId getObjID() {
        return objID;
    }

    public void setObjID(ObjectId objID) {
        this.objID = objID;
    }

    public ObjectId getCategoryObjID() {
        return categoryObjID;
    }

    public void setCategoryObjID(ObjectId categoryObjID) {
        this.categoryObjID = categoryObjID;
    }

    public ObjectId getSubCategoryObjID() {
        return subCategoryObjID;
    }

    public void setSubCategoryObjID(ObjectId subCategoryObjID) {
        this.subCategoryObjID = subCategoryObjID;
    }

    public ObjectId getPersonObjID() {
        return personObjID;
    }

    public void setPersonObjID(ObjectId personObjID) {
        this.personObjID = personObjID;
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
        return "PurchaseDto{" + "ID=" + ID + ", ItemName=" + ItemName + ", Price=" + Price + ", person=" + person + ", category=" + category + ", DateOfPurchase=" + DateOfPurchase + ", categoryID=" + categoryID + ", subCategoryID=" + subCategoryID + ", personID=" + personID + '}';
    }
}
