/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Site;
import dataaccesslayer.SiteDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class PostgresSiteDAO extends PostgresAbstractDAO<Site> implements SiteDAO {
    
    @Override
    public Site convertToEntity(ResultSet rs) throws SQLException {
        Site dbSite = new Site(rs.getInt("id"), rs.getString("factory"),
                               rs.getString("area"));
        return dbSite;
    }
    
    /**
     * Insert a site in a database
     * @param site the site to insert
     * @return the inserted site
     * @throws java.sql.SQLException
     */
    @Override
    public Site insert(Site site) throws SQLException {
        String query = String.format("insert into site "
                                   + "(factory, area)"
                                   + "values ('%s', '%s') returning *;"
                                   , site.getFactory(), site.getArea());
        return executeQuery(query);
    }
    
    /**
     * Update a site in a database
     * @param site the site to update
     * @return the updated site
     * @throws java.sql.SQLException
     */
    @Override
    public Site update(Site site) throws SQLException {
        String query = String.format("update site "
                                   + "set factory = '%s', area = '%s' "
                                   + "where id = %d returning *;"
                                   , site.getFactory(), site.getArea(), site.getId());
        return executeQuery(query);
    }

    /**
     * Delete a site with given id from a database
     * @param id the id which identifies the site
     * @return the deleted site
     * @throws java.sql.SQLException
     */
    @Override
    public Site delete(int id) throws SQLException {
        String query = String.format("delete from site "
                                   + "where id = %d returning *;", id);
        return executeQuery(query);
    }
    
    /**
     * Delete all sites from a database
     * @return the set of deleted sites
     * @throws java.sql.SQLException
     */
    @Override
    public Set<Site> deleteAll() throws SQLException {
        String query = "delete from site returning *;";
        return executeSetQuery(query);
    }

    /** 
     * Retrieve a site with given id from a database
     * @param id the id which identifies the site
     * @return the site retrieved
     * @throws java.sql.SQLException
     */
    @Override
    public Site get(int id) throws SQLException {
        String query = String.format("select * from site "
                                   + "where id = %d;", id);
        return executeQuery(query);
    }
    
    /**
     * Retrieve all the sites from a database
     * @return the set of the sites retrieved
     * @throws java.sql.SQLException
     */
    @Override
    public Set<Site> getAll() throws SQLException {
        String query = "select * from site;";
        return executeSetQuery(query);
    }
    
}
