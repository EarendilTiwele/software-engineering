/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Site;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexd
 */
public class SiteDALDatabase implements SiteDAL {
    
    private Connection conn;
    
    /**
     * Insert a site in a database
     * @param site the site to insert
     * @return the inserted site
     */
    @Override
    public Site insert(Site site) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("insert into site "
                    + "(factory, area)"
                    + "values (?,?) returning *;");
            prepareStatement.setString(1, site.getFactory());
            prepareStatement.setString(2, site.getArea());
            ResultSet rs = prepareStatement.executeQuery();
            Site dbSite = null;
            while (rs.next()) {
                dbSite = new Site(rs.getInt("id"), rs.getString("factory"),
                                  rs.getString("area"));
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbSite;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Update a site in a database
     * @param site the site to update
     * @return the updated site
     */
    @Override
    public Site update(Site site) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("update site "
                    + "set factory = ?, area = ? "
                    + "where id = ? returning *;");
            prepareStatement.setString(1, site.getFactory());
            prepareStatement.setString(2, site.getArea());
            prepareStatement.setInt(3, site.getId());
            ResultSet rs = prepareStatement.executeQuery();
            Site dbSite = null;
            while (rs.next()) {
                dbSite = new Site(rs.getInt("id"), rs.getString("factory"),
                                  rs.getString("area"));
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbSite;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Delete a site with given id from a database
     * @param id the id which identifies the site
     * @return the deleted site
     */
    @Override
    public Site delete(int id) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("delete from site "
                    + "where id = ? returning *;");
            prepareStatement.setInt(1, id);
            ResultSet rs = prepareStatement.executeQuery();
            Site dbSite = null;
            while (rs.next()) {
                dbSite = new Site(rs.getInt("id"), rs.getString("factory"),
                                  rs.getString("area"));
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbSite;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Delete all sites from a database
     * @return the number of deleted sites
     */
    @Override
    public int deleteAll() {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("delete from site "
                    + "where true;");
            int deletedRows = prepareStatement.executeUpdate();
            if (connectionWasClosed) {
                conn.close();
            }
            return deletedRows;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /** 
     * Retrieve a site with given id from a database
     * @param id the id which identifies the site
     * @return the site retrieved
     */
    @Override
    public Site get(int id) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("select * from site "
                    + "where id = ?;");
            prepareStatement.setInt(1, id);
            ResultSet rs = prepareStatement.executeQuery();
            Site dbSite = null;
            while (rs.next()) {
                dbSite = new Site(rs.getInt("id"), rs.getString("factory"),
                                  rs.getString("area"));
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbSite;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Retrieve all the sites from a database
     * @return the list of the sites retrieved
     */
    @Override
    public List<Site> getAll() {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        List<Site> sites = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("select * from site;");
            ResultSet rs = prepareStatement.executeQuery();
            Site dbSite;
            while (rs.next()) {
                dbSite = new Site(rs.getInt("id"), rs.getString("factory"),
                                  rs.getString("area"));
                sites.add(dbSite);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return sites;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
