package pt.tiago.contasdespesas.controller;

import java.io.Serializable;
import java.util.Locale;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.peopleDetails.PeopleDetailsByNBFetch;
import pt.tiago.contasdespesas.peopleDetails.User;


/**
 *
 * @author Tiago Carvalho
 */
@Component("mainPageController")
@Scope("session")
public class MainPageController implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userNB;
    @Autowired
    PeopleDetailsByNBFetch peopleDetailsByNBFetch;
    @Autowired
    CategoryController categoryController;
    @Autowired
    PersonController personController;
    @Autowired
    PurchaseController purchaseController;
    @Autowired
    TotalByPersonController totalByPersonController;
    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void changeLanguage(String local) {
        locale = new Locale(local);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public String getUserNB() {
        if (this.userNB == null || this.userNB.isEmpty()) {
            Authentication auth = SecurityContextHolder.getContext()
                    .getAuthentication();
            User user = peopleDetailsByNBFetch.getUserDetail();
            setUserNB(user.getName());
        }
        return this.userNB;
    }

    public void setUserNB(String userNB) {
        this.userNB = userNB;
    }
    
    public void clean(){
        categoryController.clear();
        personController.clear();
        purchaseController.clear();
        totalByPersonController.clear();
    }
}
