/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.tiago.contasdespesas.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author NB20708
 */
public class PersonDto implements Serializable {
    private int ID;
    private String Name;
    private String Surname;
    private List<PurchaseDto> purchases;

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String Surname) {
        this.Surname = Surname;
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
