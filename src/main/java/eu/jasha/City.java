package eu.jasha;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.UUID;

public class City implements Serializable {

    private static final long serialVersionUID = -7041939576079911914L;
    private String id;
    private String name;
    private int foundedIn;
    private int population;

    public City() {
    }

    public City(String name, int foundedIn, int population) {
        this();
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.foundedIn = foundedIn;
        this.population = population;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFoundedIn() {
        return foundedIn;
    }

    public void setFoundedIn(int foundedIn) {
        this.foundedIn = foundedIn;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        City rhs = (City) obj;
        return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.name, rhs.name)
                .append(this.foundedIn, rhs.foundedIn)
                .append(this.population, rhs.population)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .toHashCode();
    }
}
