/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.tiago.contasdespesas.controller;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.api.client.CategoryClientFacade;
import pt.tiago.contasdespesas.api.client.PersonClientFacade;
import pt.tiago.contasdespesas.api.client.PurchaseClientFacade;
import pt.tiago.contasdespesas.dto.PurchaseDto;
import pt.tiago.contasdespesas.util.JsfUtil;
import pt.tiago.contasdespesas.util.JsfUtil.PersistAction;

/**
 *
 * @author NB20708
 */
@Component("purchaseController")
@Scope("session")
public class PurchaseController implements Serializable {

    @Autowired
    private PurchaseClientFacade ejbFacade;
    @Autowired
    private CategoryClientFacade facadeCategory;
    @Autowired
    private PersonClientFacade facadePerson;
    private List<PurchaseDto> items = null;
    private PurchaseDto selected;
    private String name = "";
    private String category = "";
    private String person = "";
    private Boolean entry = false;

    public PurchaseController() {

    }

    public CategoryClientFacade getFacadeCategory() {
        return facadeCategory;
    }

    public void setFacadeCategory(CategoryClientFacade facadeCategory) {
        this.facadeCategory = facadeCategory;
    }

    public PersonClientFacade getFacadePerson() {
        return facadePerson;
    }

    public void setFacadePerson(PersonClientFacade facadePerson) {
        this.facadePerson = facadePerson;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Boolean getEntry() {
        return entry;
    }

    public void setEntry(Boolean entry) {
        this.entry = entry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Clear the filter
     *
     */
    public void clear() {
        name = "";
        category = "";
        person = "";
        entry = false;
        items = null;
        filteredItems();
    }

    public List<PurchaseDto> getItems() {
        if (items == null && entry == false) {
            filteredItems();
        }
        return items;
    }

    public void filteredItems() {
        if (!name.isEmpty() || !person.isEmpty() || !category.isEmpty()) {
            //int categoryID = facadeCategory.findIDByName(category);
            //int personID = facadePerson.findIDByName(person);
            items = getFacade().findByName(name, 0, 0);
        } else {
            items = getFacade().findAll();
        }
        entry = false;
    }

    public void setItems(List<PurchaseDto> items) {
        this.items = items;
    }

    public PurchaseClientFacade getFacade() {
        return ejbFacade;
    }

    public void setFacade(PurchaseClientFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public PurchaseDto getSelected() {
        return selected;
    }

    public void setSelected(PurchaseDto selected) {
        this.selected = selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PurchaseCreated"));
        if (!JsfUtil.isValidationFailed()) {
            clear();
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PurchaseUpdated"));
        clear();
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PurchaseDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            clear();
        }
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    getFacade().create(selected);
                } else if (persistAction == PersistAction.DELETE) {
                    getFacade().remove(selected);
                } else if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public PurchaseDto prepareCreate() {
        selected = new PurchaseDto();
        initializeEmbeddableKey();
        return selected;
    }

    public PurchaseDto getPurchase(java.lang.Integer id) {
        return getFacade().findByID(id);
    }

    @FacesConverter(forClass = PurchaseDto.class)
    public static class CategoryControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PurchaseController controller = (PurchaseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personController");
            return controller.getPurchase(Integer.parseInt(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PurchaseDto) {
                PurchaseDto o = (PurchaseDto) object;
                return getStringKey(o.getID());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PurchaseDto.class.getName()});
                return null;
            }
        }

    }

}
