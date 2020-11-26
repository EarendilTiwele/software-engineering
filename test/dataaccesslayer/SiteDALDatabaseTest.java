/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Site;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alexd
 */
public class SiteDALDatabaseTest {
    
    private SiteDALDatabase siteDAL;
    private Connection conn;
    
    public SiteDALDatabaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws SQLException {
        siteDAL = new SiteDALDatabase();
        conn = siteDAL.getConnectionObj();
        conn.setAutoCommit(false);
    }
    
    @After
    public void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }
    
    /**
     * Create a local site
     * Insert it into db and retrieve the db version
     * Compare the two sites
     */
    @Test
    public void testInsert () {
        Site localSite = new Site("test", "prova");
        Site dbSite = siteDAL.insert(localSite);
        assertEquals(localSite, dbSite);
    }
    
    /**
     * Create a local site
     * Insert it into db and retrieve the db version
     * Update the local site
     * Update it into db and retrieve the db version
     * Compare the two sites
     */
    @Test
    public void testUpdate () {
        Site localSite = new Site("test", "prova");
        Site dbSite = siteDAL.insert(localSite);
        assertEquals(localSite, dbSite);
        localSite = new Site("updated", "prova");
        dbSite = siteDAL.update(localSite);
        assertEquals(localSite, dbSite);
    }
    
    /**
     * Create a local site
     * Insert it into db and retrieve the db version
     * Delete it from db and retrieve the db version
     * Compare the two version
     * Delete a non existing site from db and check if return null
     */
    @Test
    public void testDelete () {
        Site localSite = new Site("test", "prova");
        Site dbSite = siteDAL.insert(localSite);
        assertEquals(localSite, dbSite);
        dbSite = siteDAL.delete(dbSite.getId());
        assertEquals(localSite, dbSite);
        assertNull(siteDAL.delete(dbSite.getId()));
    }
    
    /**
     * Create a local site
     * Insert it into db and retrieve the db version
     * Get the site from db and compare it with the local site
     */
    @Test
    public void testGet () {
        Site localSite = new Site("test", "prova");
        Site dbSite = siteDAL.insert(localSite);
        assertEquals(localSite, dbSite);
        dbSite = siteDAL.get(dbSite.getId());
        assertEquals(localSite, dbSite);
    }
    
    /**
     * Create a list of local sites
     * Insert one by one sites both in the local list and in db
     * Get all the sites from db and compare them with the local list
     */
    @Test
    public void testGetAll () {
        List<Site> localSites = new ArrayList<>();
        Site site1 = new Site("factory1", "area1");
        localSites.add(site1);
        siteDAL.insert(site1);
        Site site2 = new Site("factory2", "area2");
        localSites.add(site2);
        siteDAL.insert(site2);
        Site site3 = new Site("factory3", "area3");
        localSites.add(site3);
        siteDAL.insert(site3);
        List<Site> dbSites = siteDAL.getAll();
        assertTrue(localSites.equals(dbSites));
    }
    
}