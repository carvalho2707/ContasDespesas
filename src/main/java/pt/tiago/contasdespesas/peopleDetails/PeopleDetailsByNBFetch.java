package pt.tiago.contasdespesas.peopleDetails;

import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
public class PeopleDetailsByNBFetch implements Serializable {

    private static final long serialVersionUID = 1L;

    private User userDetail;

    public PeopleDetailsByNBFetch() {
    }

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetails(String nbName, String password) {
        if (userDetail == null) {
            userDetail = new User();
            userDetail.setName(nbName);
        }
        this.userDetail = userDetail;
    }

}
