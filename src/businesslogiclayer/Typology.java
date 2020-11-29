/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import java.util.Objects;

/**
 * Typology for maintenance activities. 
 * 
 * @author Alfonso
 */
public class Typology {
    private int id;
    private final String name;
    private static final int DEFAULT_ID = -1;

    /**
     * Constructs the typology with the specified id and typology name.
     * 
     * @param id     the id of this typology
     * @param name   the name of this typology
     */
    public Typology(int id, String name) {
        if (name == null){
            throw new IllegalArgumentException("name must not be null");
        }
        this.id = id;
        this.name = name;
    }

    
    /**
     * Constructs the typology with the specified name.
     * The id associated with this typology will not be significant.
     * 
     * @param name the name of this typology
     */
    public Typology(String name) {
        this(DEFAULT_ID,name);
    }

    
    /**
     * Returns the id associated with this typology.
     * 
     * @return the typology id
     */
    public int getId() {
        return id;
    }

    
    /**
     * Returns the name associated with this typology.
     * 
     * @return the typology name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the hash code for this typology
     * The hash code is computed based on the id and name.
     * 
     * @return a hash code value for this typology
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
        hash = 73 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * Compares this typology to the specified object.
     * The result is <code>true</code> if and only if the argument is not <code>null</code>
     * and is a <code>Typology</code> object that represents a typology with
     * the same id and name as this object.
     * 
     * @param obj the object to compare this <code>Typology</code> against
     * @return <code>true</code> if the given object represents a <code>Typology</code>
     *         equivalent to this typology, <code>false</code> otherwise 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Typology other = (Typology) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    /**
     * Returns the string rappresentation of Typology object.
     * @return a string rappresentation of the Typology.
     */
    @Override
    public String toString() {
        return getName() ;
    }
    
    /**
     * Set the id of this typology.
     * 
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }
    
}
