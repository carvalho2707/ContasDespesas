/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.tiago.contasdespesas.dto;

import java.io.Serializable;

/**
 *
 * @author NB20708
 */
public class CategoryDto implements Serializable {
    private int ID;
    private String Name;
    private String Description;
    private double Total;
    private double[] TotalByMonth;

    
    
    
    
    public double[] getTotalByMonth() {
        return TotalByMonth;
    }

    public void setTotalByMonth(double[] TotalByMonth) {
        this.TotalByMonth = TotalByMonth;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double Total) {
        this.Total = Total;
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

    @Override
    public String toString() {
        return "CategoryDto{" + "CategoryID=" + ID + ", Name=" + Name + ", Description=" + Description + ", TotalSpent=" + Total + '}';
    }
    
    
    
}
