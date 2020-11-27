/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import java.util.Objects;

/**
 * Site representing factory and area in the establishment.
 *
 * @author avall
 */
public class Site {
    private int id;
    private String factory;
    private String area;
    private static final int DEFAULT_ID = -1;

    /**
     * Constructs the site with the specified id, factory and area.
     * 
     * @param id        the id of this site
     * @param factory   the factory of this site
     * @param area      the area of this site
     */
    public Site(int id, String factory, String area) {
        if (factory == null || area == null){
            throw new NullPointerException("Site factory and area must not be null");
        }
        this.id = id;
        this.factory = factory;
        this.area = area;
    }

    /**
     * Constructs the site with the specified factory and area.
     * The id associated with this site will not be significant.
     * 
     * @param factory   the factory of this site
     * @param area      the area of this site
     */
    public Site(String factory, String area) {
        this(DEFAULT_ID, factory, area);
    }
    
    /**
     * Returns the id associated with this site.
     *
     * @return the site id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the factory associated with this site.
     *
     * @return the site factory
     */
    public String getFactory() {
        return factory;
    }

    /**
     * Returns the area associated with this site.
     *
     * @return the site area
     */
    public String getArea() {
        return area;
    }

    /**
     * Returns the hash code for this site.
     * The hash code is computed based on factory and area.
     * 
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.factory);
        hash = 37 * hash + Objects.hashCode(this.area);
        return hash;
    }

    /**
     * Compares this site to the specified object.
     * The result is <code>true</code> if and only if the argument is not <code>null</code>
     * and is a <code>Site</code> object that represents a site with
     * the same factory and area as this object.
     * 
     * @param obj  the object to compare this <code>Site</code> against
     * @return     <code>true</code> if the given object represents a <code>Site</code>
     *             equivalent to this site, <code>false</code> otherwise
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
        final Site other = (Site) obj;
        if (!Objects.equals(this.factory, other.factory)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        return true;
    }
    
    /**
     * Returns a string representation of this site.
     * The string representation consists of factory and area
     * separated by a dash.
     * 
     * @return a string representation of this site
     */
    @Override
    public String toString(){
        return getFactory() + " - " + getArea();
    }

    /**
     * Set the id of this site.
     * 
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }
    

}
