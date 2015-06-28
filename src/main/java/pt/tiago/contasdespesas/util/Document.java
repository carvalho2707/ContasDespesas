/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.tiago.contasdespesas.util;

import java.io.Serializable;
import pt.tiago.contasdespesas.dto.CategoryDto;
import pt.tiago.contasdespesas.dto.SubCategoryDto;

/**
 *
 * @author NB20708
 */
public class Document implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private CategoryDto categoryDto;
    private SubCategoryDto subCategoryDto;

    public Document(Object obj) {
        if(obj instanceof SubCategoryDto){
            this.subCategoryDto = (SubCategoryDto)obj;
        } else{
            this.categoryDto = (CategoryDto)obj;
        }
    }
    
    public CategoryDto getCategoryDto() {
        return categoryDto;
    }

    public void setCategoryDto(CategoryDto categoryDto) {
        this.categoryDto = categoryDto;
    }

    public SubCategoryDto getSubCategoryDto() {
        return subCategoryDto;
    }

    public void setSubCategoryDto(SubCategoryDto subCategoryDto) {
        this.subCategoryDto = subCategoryDto;
    }
}
