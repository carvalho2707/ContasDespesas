package pt.tiago.contasdespesas.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.api.client.CategoryClientFacade;
import pt.tiago.contasdespesas.api.client.PurchaseClientFacade;
import pt.tiago.contasdespesas.dto.CategoryDto;
import pt.tiago.contasdespesas.util.JsfUtil;
import pt.tiago.contasdespesas.util.JsfUtil.PersistAction;

/**
 *
 * @author Tiago Carvalho
 */
@Component("categoryController")
@Scope("session")
public class CategoryController implements Serializable {

    @Autowired
    private CategoryClientFacade ejbFacade;
    @Autowired
    private PurchaseClientFacade ejbFacadePurchase;
    private List<CategoryDto> items = null;
    private CartesianChartModel lineTotalYearModel;
    private CategoryDto selected;
    private String name = "";
    private Boolean entry = false;

    public CategoryController() {

    }

    public CartesianChartModel getLineTotalYearModel() {
        if (lineTotalYearModel == null) {
            createLineTotalMonthModel();
        }
        return lineTotalYearModel;
    }

    public void setLineTotalYearModel(CartesianChartModel lineTotalYearModel) {
        this.lineTotalYearModel = lineTotalYearModel;
    }

    private void createLineTotalMonthModel() {
        lineTotalYearModel = new CartesianChartModel();
        ChartSeries chartSeries = new ChartSeries();
        chartSeries.setLabel(selected.getName());
        List<Integer> anos = ejbFacade.findYears();
        Axis yAxis = lineTotalYearModel.getAxis(AxisType.Y);
        int idCategoria = selected.getID();
        double max = 20;
        Collections.sort(anos);
        for (Integer ano : anos) {
            double valor = ejbFacade.findCategoryTotalByYear(ano, idCategoria);
            chartSeries.set(ano.toString(), valor);
        }
        lineTotalYearModel.addSeries(chartSeries);
        yAxis.setMax(max);
    }

    public PurchaseClientFacade getEjbFacadePurchase() {
        return ejbFacadePurchase;
    }

    public void setEjbFacadePurchase(PurchaseClientFacade ejbFacadePurchase) {
        this.ejbFacadePurchase = ejbFacadePurchase;
    }

    public Boolean getEntry() {
        return entry;
    }

    public void setEntry(Boolean entry) {
        this.entry = entry;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public List<CategoryDto> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CategoryDto> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public CategoryClientFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(CategoryClientFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public CategoryClientFacade getFacade() {
        return ejbFacade;
    }

    public void setFacade(CategoryClientFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public List<CategoryDto> getItems() {
        if (items == null && entry == false) {
            filteredItems();
        }
        return items;
    }

    public void setItems(List<CategoryDto> items) {
        this.items = items;
    }

    public CategoryDto getSelected() {
        return selected;
    }

    public void setSelected(CategoryDto selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void filteredItems() {
        if (!name.isEmpty()) {
            items = getFacade().findByName(name);
        } else {
            items = getFacade().findAll();
        }
        int ano = Calendar.getInstance().get(Calendar.YEAR);
        for (CategoryDto cate : items) {
            cate.setTotal(ejbFacadePurchase.findTotalYear(ano, cate.getID()));
        }
        entry = true;
    }

    public void clear() {
        name = "";
        entry = false;
        selected = null;
        lineTotalYearModel = null;
        items = null;
        filteredItems();
    }

    public void populateCollections() {
        lineTotalYearModel = null;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CategoryCreated"));
        if (!JsfUtil.isValidationFailed()) {
            clear();
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CategoryUpdated"));
        clear();
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CategoryDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            clear();
        }
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

    public CategoryDto prepareCreate() {
        selected = new CategoryDto();
        initializeEmbeddableKey();
        return selected;
    }

    public CategoryDto getCategory(java.lang.Integer id) {
        return getFacade().findByID(id);
    }

    @FacesConverter(forClass = CategoryDto.class)
    public static class CategoryControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CategoryController controller = (CategoryController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "categoryController");
            return controller.getCategory(Integer.parseInt(value));
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
            if (object instanceof CategoryDto) {
                CategoryDto o = (CategoryDto) object;
                return getStringKey(o.getID());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CategoryDto.class.getName()});
                return null;
            }
        }

    }

}
