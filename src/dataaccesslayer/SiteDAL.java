/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Site;
import java.util.List;

/**
 *
 * @author alexd
 */
public interface SiteDAL {
    
    /**
     * Insert a site in a persistent storage
     * @param site the site to insert
     * @return the inserted site
     */
    public Site insert (Site site);
    
    /**
     * Update a site in a persistent storage
     * @param site the site to update
     * @return the updated site
     */
    public Site update (Site site);
    
    /**
     * Delete a site with given id from a persistent storage
     * @param id the id which identifies the site
     * @return the deleted site
     */
    public Site delete (int id);
    
    /**
     * Delete all sites in a persistent storage
     * @return the number of deleted sites
     */
    public int deleteAll ();
    
    /** 
     * Retrieve a site with given id from a persistent storage
     * @param id the id which identifies the site
     * @return the site retrieved
     */
    public Site get(int id);
    
    /**
     * Retrieve all the sites from a persistent storage
     * @return the list of the sites retrieved
     */
    public List<Site> getAll();
    
}
