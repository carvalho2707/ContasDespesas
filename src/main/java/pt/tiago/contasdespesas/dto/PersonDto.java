package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Tiago Carvalho
 */
public class PersonDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ID;
    private String Name;
    private String Surname;
    private List<PurchaseDto> purchases;

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String Surname) {
        this.Surname = Surname;
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

    public List<PurchaseDto> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseDto> purchases) {
        this.purchases = purchases;
    }

    @Override
    public String toString() {
        return "PersonDto{" + "PersonID=" + ID + ", Name=" + Name + ", Surname=" + Surname + ", purchases=" + purchases + '}';
    }
}
