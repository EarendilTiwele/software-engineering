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
