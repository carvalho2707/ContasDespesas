package pt.tiago.contasdespesas.peopleDetails;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof User) {
            return getName().equals(((User) rhs).getName());
        }
        return false;
    }

    /**
     * Returns the hashcode of the {@code username}.
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

}
