/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatransferobjects;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author carbo
 */
public class Maintainer extends User{

    private Set<Competency> competencies = new HashSet<>();
    
    /**
     * Construct a maintainer specifying his id, username and password.
     * 
     * @param id            the id of this maintainer
     * @param username      the username of this maintainer
     * @param password      the password of this maintainer
     */
    public Maintainer(int id, String username, String password) {
        super(id, username, password);
    }

    /**
     * Construct a maintainer specifying his username and password.
     * The id associated with this maintainer will not be significant.
     * 
     * @param username  the username of this maintainer
     * @param password  the password of this maintainer
     */
    public Maintainer(String username, String password) {
        super(username, password);
    }
    
    /**
     * Returns the role of this maintainer.
     * The role is <code>User.Role.Maintainer</code>.
     * 
     * @return the role of this maintainer
     */
    @Override
    public Role getRole() {
        return User.Role.MAINTAINER;
    }

    /**
     * Returns the competencies of this maintainer.
     * 
     * @return the competencies of this maintainer
     */
    public Set<Competency> getCompetencies() {
        return competencies;
    }
    
    /**
     * Adds a competency to this maintainer.
     *
     * @param competency the competency to be added
     */
    public void addCompetency(Competency competency){
        competencies.add(competency);
    }
    
    /**
     * Remove a competency from this maintainer.
     *
     * @param competency the competency to be removed
     */
    public void removeCompetency(Competency competency){
        competencies.remove(competency);
    }

    /**
     * Returns the hash code for this maintainer.
     * The hash code is computed based on the id, username password and
     * competencies.
     * 
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = hash + super.hashCode();
        hash = 71 * hash + Objects.hashCode(this.competencies);
        return hash;
    }

    /**
     * Compares this maintainer to the specified object.
     * The result is <code>true</code> if and only if the argument is not 
     * <code>null</code> and is a <code>Maintainer</code> object that 
     * represents a maintainer with the same id, username, password,
     * role and competencies as this object.
     * 
     * @param obj the object to compare this <code>Maintainer</code> against
     * @return <code>true</code> if the given object represents a
     *         <code>Maintainer</code> equivalent to this maintainer,
     *         <code>false</code> otherwise
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
        if (!super.equals(obj)){
            return false;
        }
        final Maintainer other = (Maintainer) obj;
        if (!Objects.equals(this.competencies, other.competencies)) {
            return false;
        }
        return true;
    }
    
    
}
