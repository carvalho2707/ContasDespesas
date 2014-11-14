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
import javax.ejb.EJBException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.api.client.PersonClientFacade;
import pt.tiago.contasdespesas.dto.PersonDto;
import pt.tiago.contasdespesas.util.JsfUtil;
import pt.tiago.contasdespesas.util.JsfUtil.PersistAction;

/**
 *
 * @author NB20708
 */
@Component("personController")
@Scope("session")
public class PersonController implements Serializable {
    //TODO Descobrir o que faz SessionScoped

    @Autowired
    private pt.tiago.contasdespesas.api.client.PersonClientFacade ejbFacade;
    private List<PersonDto> items = null;
    private PersonDto selected;
    private String name = "";
    private String surname = "";
    private Boolean entry = false;

    public PersonController() {

    }

    public Boolean getEntry() {
        return entry;
    }

    public void setEntry(Boolean entry) {
        this.entry = entry;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PersonDto> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PersonDto> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    /**
     * Clear the filter
     *
     */
    public void clear() {
        name = "";
        surname = "";
        selected = null;
        entry = false;
        items = null;
        filteredItems();
    }

    public List<PersonDto> getItems() {
        if (items == null && entry == false) {
            filteredItems();
        }
        return items;
    }

    public void filteredItems() {
        if (!name.isEmpty() || !surname.isEmpty()) {
            items = getFacade().findByName(name, surname);
        } else {
            items = getFacade().findAll();
        }
        entry = true;
    }

    public void setItems(List<PersonDto> items) {
        this.items = items;
    }

    public PersonClientFacade getFacade() {
        return ejbFacade;
    }

    public void setFacade(PersonClientFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public PersonDto getSelected() {
        return selected;
    }

    public void setSelected(PersonDto selected) {
        this.selected = selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PersonCreated"));
        if (!JsfUtil.isValidationFailed()) {
            clear();
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PersonUpdated"));
        clear();
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PersonDeleted"));
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

    public PersonDto prepareCreate() {
        selected = new PersonDto();
        initializeEmbeddableKey();
        return selected;
    }

    public PersonDto getPerson(java.lang.Integer id) {
        return getFacade().findByID(id);
    }

    @FacesConverter(forClass = PersonDto.class)
    public static class CategoryControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonController controller = (PersonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personController");
            return controller.getPerson(Integer.parseInt(value));
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
            if (object instanceof PersonDto) {
                PersonDto o = (PersonDto) object;
                return getStringKey(o.getID());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PersonDto.class.getName()});
                return null;
            }
        }

    }
}
