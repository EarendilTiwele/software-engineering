/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

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

}
