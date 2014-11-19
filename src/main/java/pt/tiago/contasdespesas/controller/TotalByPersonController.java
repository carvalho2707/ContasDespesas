package pt.tiago.contasdespesas.controller;

import org.primefaces.model.chart.CartesianChartModel;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.time.Month;
import java.time.Year;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.api.client.CategoryClientFacade;
import pt.tiago.contasdespesas.api.client.PersonClientFacade;
import pt.tiago.contasdespesas.api.client.ReportClientFacade;
import pt.tiago.contasdespesas.dto.CategoryDto;
import pt.tiago.contasdespesas.dto.CategorySumByYearDto;
import pt.tiago.contasdespesas.dto.PurchaseSumByMonthDto;
import pt.tiago.contasdespesas.dto.PersonDto;
import pt.tiago.contasdespesas.dto.PurchaseSumByYearDto;

/**
 *
 * @author Tiago Carvalho
 */
@Component("totalByPersonController")
@Scope("session")
public class TotalByPersonController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Autowired
    private ReportClientFacade reportFacade;
    @Autowired
    private CategoryClientFacade categoryFacade;
    @Autowired
    private PersonClientFacade personFacade;
    private CartesianChartModel lineTotalMonthModel;
    private PieChartModel pieModelYear = null;
    private PieChartModel pieModelYearCategory = null;
    private List<String> anos;
    private String escolhido = "";
    private String anoEscolhido = "";
    private String categoriaEscolhida = "";
    private List<String> categories;

    public PersonClientFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonClientFacade personFacade) {
        this.personFacade = personFacade;
    }

    public PieChartModel getPieModelYearCategory() {
        filteredItemsYearCategory();
        return pieModelYearCategory;
    }

    public void setPieModelYearCategory(PieChartModel pieModelYearCategory) {
        this.pieModelYearCategory = pieModelYearCategory;
    }

    public CategoryClientFacade getCategoryFacade() {
        return categoryFacade;
    }

    public void setCategoryFacade(CategoryClientFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    public List<String> getCategories() {
        categories = categoryFacade.findAllNames();
        categories.add(0, "Todas");
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getCategoriaEscolhida() {
        if (categoriaEscolhida.isEmpty()) {
            categoriaEscolhida = "Todas";
        }
        return categoriaEscolhida;
    }

    public void setCategoriaEscolhida(String categoriaEscolhida) {
        this.categoriaEscolhida = categoriaEscolhida;
    }

    public String getAnoEscolhido() {
        if (anoEscolhido.isEmpty()) {
            Calendar now = Calendar.getInstance();   // Gets the current date and time.
            anoEscolhido = Year.of(now.get(Calendar.YEAR)).toString();
        }
        return anoEscolhido;
    }

    public void setAnoEscolhido(String anoEscolhido) {
        this.anoEscolhido = anoEscolhido;
    }

    public PieChartModel getPieModelYear() {
        filteredItemsYear();
        return pieModelYear;
    }

    public void setPieModelYear(PieChartModel pieModelYear) {
        this.pieModelYear = pieModelYear;
    }

    public List<String> getAnos() {
        anos = getFacade().findYears();
        return anos;
    }

    public void setAnos(List<String> anos) {
        this.anos = anos;
    }

    public String getEscolhido() {
        if (escolhido.isEmpty()) {
            Calendar now = Calendar.getInstance();   // Gets the current date and time.
            escolhido = Month.of(now.get(Calendar.MONTH)).toString();
        }
        return escolhido;
    }

    public void setEscolhido(String escolhido) {
        this.escolhido = escolhido;
    }

    public void filteredItemsLine() {
        if (!categoriaEscolhida.isEmpty()) {
            createLineTotalMonthModel();
        }
    }

    public void filteredItemsYear() {
        createPieModelYear();
    }

    public void filteredItemsYearCategory() {
        createPieModelYearCategory();
    }

    public void clear() {
        escolhido = "";
        categoriaEscolhida = "";
        lineTotalMonthModel = null;
        filteredItemsLine();
    }

    public CartesianChartModel getLineTotalMonthModel() {
        if (lineTotalMonthModel == null) {
            filteredItemsLine();
        }
        return lineTotalMonthModel;
    }

    public void setLineTotalMonthModel(CartesianChartModel lineTotalMonthModel) {
        this.lineTotalMonthModel = lineTotalMonthModel;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public ReportClientFacade getFacade() {
        return reportFacade;
    }

    public void setFacade(ReportClientFacade reportFacade) {
        this.reportFacade = reportFacade;
    }

    private void createLineTotalMonthModel() {
        lineTotalMonthModel = new CartesianChartModel();
        ChartSeries chartSeries;
        List<PersonDto> pessoas = getFacade().findAllPersons();
        Axis yAxis = lineTotalMonthModel.getAxis(AxisType.Y);
        int idCategoria = 0;
        if (categoriaEscolhida.equals("Todas")) {
            idCategoria = -1;
        } else {
            for (CategoryDto cate : categoryFacade.findAll()) {
                if (cate.getName().equals(categoriaEscolhida)) {
                    idCategoria = cate.getID();
                }
            }
        }

        double max = 20;
        for (PersonDto person : pessoas) {
            PurchaseSumByMonthDto[] lista = getFacade().findTotalPersonByNameByMonth(person.getID(), idCategoria);
            chartSeries = new ChartSeries();
            chartSeries.setLabel(person.getName());
            for (int i = 0; i < 12; i++) {
                if (lista[i] != null) {
                    double maxTemp = lista[i].getTotal();
                    if (maxTemp > max) {
                        max = maxTemp;

                    }
                    chartSeries.set(new DateFormatSymbols().getMonths()[i], maxTemp);

                } else {
                    chartSeries.set(new DateFormatSymbols().getMonths()[i], 0);
                }
            }
            lineTotalMonthModel.addSeries(chartSeries);
        }
        yAxis.setMax(max);
    }

    private void createPieModelYear() {
        pieModelYear = new PieChartModel();
        List<PurchaseSumByYearDto> lista = getFacade().findTotalPersonByNameByYear(Integer.parseInt(anoEscolhido));
        if (!lista.isEmpty()) {
            String name;
            for (PurchaseSumByYearDto dto : lista) {
                name = personFacade.findByID(dto.getID()).getName();
                pieModelYear.set(name + " - " + dto.getTotal() + " €", dto.getTotal());
            }
            pieModelYear.setTitle(ResourceBundle.getBundle("/Bundle").getString("TotalByYearByPersonPie") + " - " + anoEscolhido);
            pieModelYear.setLegendPosition("e");
            pieModelYear.setFill(false);
            pieModelYear.setShowDataLabels(true);
            pieModelYear.setDiameter(200);
        } else {
            pieModelYear.setTitle(ResourceBundle.getBundle("/Bundle").getString("TotalByYearByPersonPie") + " - " + anoEscolhido);
            pieModelYear.setLegendPosition("e");
            pieModelYear.setFill(false);
            pieModelYear.setShowDataLabels(true);
            pieModelYear.setDiameter(200);
        }
    }

    private void createPieModelYearCategory() {
        pieModelYearCategory = new PieChartModel();
        List<CategorySumByYearDto> lista = getFacade().findTotalCategorySumByYear(Integer.parseInt(anoEscolhido));
        if (!lista.isEmpty()) {
            String name;
            for (CategorySumByYearDto dto : lista) {
                name = categoryFacade.findByID(dto.getID()).getName();
                pieModelYearCategory.set(name + " - " + dto.getTotal() + " €", dto.getTotal());
            }
            pieModelYearCategory.setTitle(ResourceBundle.getBundle("/Bundle").getString("TotalByYearByCategoryPie") + " - " + anoEscolhido);
            pieModelYearCategory.setLegendPosition("e");
            pieModelYearCategory.setFill(false);
            pieModelYearCategory.setShowDataLabels(true);
            pieModelYearCategory.setDiameter(200);
        } else {
            pieModelYearCategory.setTitle(ResourceBundle.getBundle("/Bundle").getString("TotalByYearByCategoryPie") + " - " + anoEscolhido);
            pieModelYearCategory.setLegendPosition("e");
            pieModelYearCategory.setFill(false);
            pieModelYearCategory.setShowDataLabels(true);
            pieModelYearCategory.setDiameter(200);
        }
    }
}
