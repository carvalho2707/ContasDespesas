package pt.tiago.contasdespesas.controller;

import java.io.Serializable;
import java.util.Calendar;
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
import pt.tiago.contasdespesas.api.client.ReportClientFacade;
import pt.tiago.contasdespesas.dto.PurchaseDto;
import pt.tiago.contasdespesas.util.JsfUtil;
import pt.tiago.contasdespesas.util.JsfUtil.PersistAction;

/**
 *
 * @author Tiago Carvalho
 */
@Component("purchaseController")
@Scope("session")
public class PurchaseController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Autowired
    private PurchaseClientFacade purchaseFacade;
    @Autowired
    private CategoryClientFacade categoryFacade;
    @Autowired
    private PersonClientFacade personFacade;
    @Autowired
    private ReportClientFacade reportFacade;
    private List<PurchaseDto> items = null;
    private PurchaseDto selected;
    private String name = "";
    private String category = "";
    private String person = "";
    private Boolean entry = false;
    private List<Integer> anos;
    private String anoEscolhido = "";

    public PurchaseController() {

    }
    
    public List<Integer> getAnos() {
        anos = purchaseFacade.findYears();
        return anos;
    }

    public void setAnos(List<Integer> anos) {
        this.anos = anos;
    }
    
    public String getAnoEscolhido() {
        if (anoEscolhido.isEmpty()) {
            Calendar now = Calendar.getInstance();   // Gets the current date and time.
            anoEscolhido = Integer.toString(now.get(Calendar.YEAR));
        }
        return anoEscolhido;
    }

    public void setAnoEscolhido(String anoEscolhido) {
        this.anoEscolhido = anoEscolhido;
    }

    public CategoryClientFacade getCategoryFacade() {
        return categoryFacade;
    }

    public void setCategoryFacade(CategoryClientFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    public PersonClientFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonClientFacade personFacade) {
        this.personFacade = personFacade;
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

    public void clear() {
        name = "";
        category = "";
        person = "";
        entry = false;
        items = null;
    }

    public List<PurchaseDto> getItems() {
        if (items == null && entry == false) {
            filteredItems();
        }
        return items;
    }

    public void filteredItems() {
        if (!name.isEmpty() || !person.isEmpty() || !category.isEmpty()) {
            String categoryID = "";
            String personID = "";
            if (!category.isEmpty()) {
                categoryID = categoryFacade.findIDByName(category);
            }
            if (!person.isEmpty()) {
                personID = personFacade.findIDByName(person);
            }
            items = getFacade().findByName(name, personID, categoryID, Integer.parseInt(anoEscolhido));
        } else {
            items = getFacade().findAll(Integer.parseInt(anoEscolhido));
        }
        entry = true;
    }

    public void setItems(List<PurchaseDto> items) {
        this.items = items;
    }

    public PurchaseClientFacade getFacade() {
        return purchaseFacade;
    }

    public void setFacade(PurchaseClientFacade purchaseFacade) {
        this.purchaseFacade = purchaseFacade;
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

    public PurchaseDto getPurchase(java.lang.String id) {
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
            return controller.getPurchase(value);
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
                return o.getID();
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PurchaseDto.class.getName()});
                return null;
            }
        }

    }

}
