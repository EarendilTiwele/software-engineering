/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Site;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public interface SiteDAO {
    
    /**
     * Insert a site in a persistent storage
     * @param site the site to insert
     * @return the inserted site
     * @throws java.sql.SQLException
     */
    public Site insert (Site site) throws SQLException;
    
    /**
     * Update a site in a persistent storage
     * @param site the site to update
     * @return the updated site
     * @throws java.sql.SQLException
     */
    public Site update (Site site) throws SQLException;
    
    /**
     * Delete a site with given id from a persistent storage
     * @param id the id which identifies the site
     * @return the deleted site
     * @throws java.sql.SQLException
     */
    public Site delete (int id) throws SQLException;
    
    /**
     * Delete all sites in a persistent storage
     * @return the set of deleted sites
     * @throws java.sql.SQLException
     */
    public Set<Site> deleteAll () throws SQLException;
    
    /** 
     * Retrieve a site with given id from a persistent storage
     * @param id the id which identifies the site
     * @return the site retrieved
     * @throws java.sql.SQLException
     */
    public Site get(int id) throws SQLException;
    
    /**
     * Retrieve all the sites from a persistent storage
     * @return the set of the sites retrieved
     * @throws java.sql.SQLException
     */
    public Set<Site> getAll() throws SQLException;
    
}
