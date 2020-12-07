/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import dataaccesslayer.postgres.PostgresActivityDAO;
import dataaccesslayer.postgres.PostgresSiteDAO;
import datatransferobjects.Site;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alexd
 */
public class SiteDALDatabaseTest {
    
    private static PostgresSiteDAO siteDAL;
    private static Connection conn;
    
    public SiteDALDatabaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException {
        siteDAL = new PostgresSiteDAO();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.rollback();
        conn.close();
    }
    
    /**
     * Create a local site
     * Insert it into db and retrieve the db version
     * Compare the two sites
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsert () throws SQLException {
        Site localSite = new Site("test", "prova");
        Site dbSite = siteDAL.insert(localSite);
        localSite.setId(dbSite.getId());
        assertEquals(localSite, dbSite);
    }
    
    /**
     * Create a local site
     * Insert it into db and retrieve the db version
     * Update the local site
     * Update it into db and retrieve the db version
     * Compare the two sites
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdate () throws SQLException {
        Site localSite = new Site("test", "prova");
        Site dbSite = siteDAL.insert(localSite);
        localSite.setId(dbSite.getId());
        assertEquals(localSite, dbSite);
        localSite = new Site(dbSite.getId(), "updated", "prova");
        dbSite = siteDAL.update(localSite);
        assertEquals(localSite, dbSite);
    }
    
    /**
     * Create a local site
     * Insert it into db and retrieve the db version
     * Delete it from db and retrieve the db version
     * Compare the two version
     * Delete a non existing site from db and check if return null
     * @throws java.sql.SQLException
     */
    @Test
    public void testDelete () throws SQLException {
        Site localSite = new Site("test", "prova");
        Site dbSite = siteDAL.insert(localSite);
        localSite.setId(dbSite.getId());
        assertEquals(localSite, dbSite);
        dbSite = siteDAL.delete(dbSite.getId());
        assertEquals(localSite, dbSite);
        assertNull(siteDAL.delete(dbSite.getId()));
    }
    
    /**
     * Insert two sites in db
     * Verify if the number of deleted sites is equals to the size of the table
     * Verify if the size of the table is zero after the deletion
     * Verify if the deletion on an empty table returns zero
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteAll () throws SQLException {
        new PostgresActivityDAO().deleteAll();
        siteDAL.insert(new Site("test", "prova"));
        siteDAL.insert(new Site("test2", "prova2"));
        int tableSize = siteDAL.getAll().size();
        assertEquals(tableSize, siteDAL.deleteAll().size());
        assertEquals(0, siteDAL.getAll().size());
        assertEquals(0, siteDAL.deleteAll().size());
    }
    
    /**
     * Create a local site
     * Insert it into db and retrieve the db version
     * Get the site from db and compare it with the local site
     * @throws java.sql.SQLException
     */
    @Test
    public void testGet () throws SQLException {
        Site localSite = new Site("test", "prova");
        Site dbSite = siteDAL.insert(localSite);
        localSite.setId(dbSite.getId());
        assertEquals(localSite, dbSite);
        dbSite = siteDAL.get(dbSite.getId());
        assertEquals(localSite, dbSite);
    }
    
    /**
     * Create a list of local sites
     * Insert one by one sites both in the local list and in db
     * Get all the sites from db and compare them with the local list
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAll () throws SQLException {
        Set<Site> localSites = new HashSet<>();
        Site site1 = new Site("factory1", "area1");
        site1 = siteDAL.insert(site1);
        localSites.add(site1);
        Site site2 = new Site("factory2", "area2");
        site2 = siteDAL.insert(site2);
        localSites.add(site2);
        Site site3 = new Site("factory3", "area3");
        site3 = siteDAL.insert(site3);
        localSites.add(site3);
        Set<Site> dbSites = siteDAL.getAll();
        assertTrue(dbSites.containsAll(localSites));
    }
    
}
