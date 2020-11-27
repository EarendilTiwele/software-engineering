/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Maintenance procedure to perform a maintenance activity.
 *
 * @author avall
 */
public class Procedure {

    private int id;
    private String name;
    private String smp;
    private final Set<Competency> competencies = new HashSet<>();
    private static final int DEFAULT_ID = -1;

    /**
     * Constructs a maintenance procedure with the specified id, name and SMP
     * path.
     *
     * @param id    the id of this procedure
     * @param name  the name of this procedure
     * @param smp   the path of the SMP pdf file associated with this procedure
     */
    public Procedure(int id, String name, String smp) {
        if (name == null || smp == null) {
            throw new NullPointerException("Procedure name and SMP path must not be null");
        }
        this.id = id;
        this.name = name;
        this.smp = smp;
    }

    /**
     * Constructs a maintenance procedure with the specified name and SMP path.
     * The id associated with this procedure will not be significant.
     *
     * @param name the name of this procedure
     * @param smp  the path of the SMP pdf file associated with this procedure
     */
    public Procedure(String name, String smp) {
        this(DEFAULT_ID, name, smp);
    }

    /**
     * Returns the id associated with this procedure.
     *
     * @return the procedure id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of this procedure.
     *
     * @return the procedure name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the SMP path of this procedure.
     *
     * @return the procedure SMP path
     */
    public String getSmp() {
        return smp;
    }

    /**
     * Returns the set of competencies associated with this procedure.
     *
     * @return the set of competencies
     */
    public Set<Competency> getCompetencies() {
        return competencies;
    }

    /**
     * Adds a competency to this procedure.
     *
     * @param competency the competency to be added
     */
    public void addCompetency(Competency competency) {
        competencies.add(competency);
    }

    /**
     * Returns the hash code for this procedure. The hash code is computed based
     * on name, SMP path, competencies.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.smp);
        hash = 67 * hash + Objects.hashCode(this.competencies);
        return hash;
    }

    /**
     * Compares this procedure to the specified object. The result is
     * <code>true</code> if and only if the argument is not <code>null</code>
     * and is a <code>Procedure</code> object that represents a procedure with
     * the same name, SMP path and competencies as this object.
     *
     * @param obj the object to compare this <code>Procedure</code> against
     * @return <code>true</code> if the given object represents a
     *         <code>Procedure</code> equivalent to this procedure, <code>false</code>
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
        final Procedure other = (Procedure) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.smp, other.smp)) {
            return false;
        }
        if (!Objects.equals(this.competencies, other.competencies)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of this procedure. The string
     * representation consists of name, SMP path and the list of competencies.
     *
     * @return a string representation of this procedure
     */
    @Override
    public String toString() {
        return getName() + "\n" + getSmp() + "\n" + getCompetencies().toString();
    }

}
