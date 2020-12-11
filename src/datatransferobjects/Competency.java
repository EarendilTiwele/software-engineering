/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatransferobjects;

import java.util.Objects;

/**
 * Competency related to a specific task required to perform the maintenance
 * activity.
 *
 * @author carbo
 */
public class Competency {

    private int id;
    private final String description;
    private static final int DEFAULT_ID = -1;

    /**
     * Constructs the competency with the specified id and description.
     *
     * @param id the id of this competency
     * @param description the description of this competency
     */
    public Competency(int id, String description) {
        if (description == null) {
            throw new NullPointerException("Competency description must not be null");
        }
        this.id = id;
        this.description = description;
    }

    /**
     * Constructs the competency with the specified description. The id
     * associated with this competency will not be significant.
     *
     * @param description the description of this competency
     */
    public Competency(String description) {
        this(DEFAULT_ID, description);
    }

    /**
     * Returns the hash code for this competency. The hash code is computed
     * based on the description only.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.description);
        return hash;
    }

    /**
     * Compares this competency to the specified object. The result is
     * <code>true</code> if and only if the argument is not <code>null</code>
     * and is a <code>Competency</code> object that represents a competency with
     * the same description as this object.
     *
     * @param obj the object to compare this <code>Competency</code> against
     * @return <code>true</code> if the given object represents a
     * <code>Competency</code> equivalent to this competency, <code>false</code>
     * otherwise
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
        final Competency other = (Competency) obj;
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }

    /**
     * Returns the id associated with this competency.
     *
     * @return the competency id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the description associated with this competency.
     *
     * @return the procedure description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a string representation of this competency. The string
     * representation consists of the description.
     *
     * @return a string representation of this competency
     */
    @Override
    public String toString() {
        return getDescription().toString();
    }

    /**
     * Set the id of this competency.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }
}
