/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Typology;
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
public class TypologyDALDatabaseTest {
    
    private TypologyDALDatabase typologyDAL;
    private Connection conn;
    
    public TypologyDALDatabaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws SQLException {
        typologyDAL = new TypologyDALDatabase();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
    }
    
    @After
    public void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }

    /**
     * Create a local typology
     * Insert it into db and retrieve the db version
     * Compare the two typologies
     */
    @Test
    public void testInsert () {
        Typology localTypology = new Typology("prova");
        Typology dbTypology = typologyDAL.insert(localTypology);
        localTypology.setId(dbTypology.getId());
        assertEquals(localTypology, dbTypology);
    }
    
    /**
     * Create a local typology
     * Insert it into db and retrieve the db version
     * Update the local typology
     * Update it into db and retrieve the db version
     * Compare the two typologies
     */
    @Test
    public void testUpdate () {
        Typology localTypology = new Typology("test");
        Typology dbTypology = typologyDAL.insert(localTypology);
        localTypology.setId(dbTypology.getId());
        assertEquals(localTypology, dbTypology);
        localTypology = new Typology(dbTypology.getId(), "updated");
        dbTypology = typologyDAL.update(localTypology);
        assertEquals(localTypology, dbTypology);
    }
    
    /**
     * Create a local typology
     * Insert it into db and retrieve the db version
     * Delete it from db and retrieve the db version
     * Compare the two version
     * Delete a non existing typology from db and check if return null
     */
    @Test
    public void testDelete () {
        Typology localTypology = new Typology("test");
        Typology dbTypology = typologyDAL.insert(localTypology);
        localTypology.setId(dbTypology.getId());
        assertEquals(localTypology, dbTypology);
        dbTypology = typologyDAL.delete(dbTypology.getId());
        assertEquals(localTypology, dbTypology);
        assertNull(typologyDAL.delete(dbTypology.getId()));
    }
    
    /**
     * Insert two typologies in db
     * Verify if the number of deleted typologies is equals to the size of the table
     * Verify if the size of the table is zero after the deletion
     * Verify if the deletion on an empty table returns zero
     */
    @Test
    public void testDeleteAll () {
        new ActivityDALDatabase().deleteAll();
        typologyDAL.insert(new Typology("test"));
        typologyDAL.insert(new Typology("test2"));
        int tableSize = typologyDAL.getAll().size();
        assertEquals(tableSize, typologyDAL.deleteAll());
        assertEquals(0, typologyDAL.getAll().size());
        assertEquals(0, typologyDAL.deleteAll());
    }
    
    /**
     * Create a local typology
     * Insert it into db and retrieve the db version
     * Get the typology from db and compare it with the local typology
     */
    @Test
    public void testGet () {
        Typology localTypology = new Typology("test");
        Typology dbTypology = typologyDAL.insert(localTypology);
        localTypology.setId(dbTypology.getId());
        assertEquals(localTypology, dbTypology);
        dbTypology = typologyDAL.get(dbTypology.getId());
        assertEquals(localTypology, dbTypology);
    }
    
    /**
     * Create a list of local typologies
     * Insert one by one typologies both in the local list and in db
     * Get all the typologies from db and compare them with the local list
     */
    @Test
    public void testGetAll () {
        List<Typology> localTypologies = new ArrayList<>();
        Typology typology1 = new Typology("typo1");
        typology1 = typologyDAL.insert(typology1);
        localTypologies.add(typology1);
        Typology typology2 = new Typology("typo2");
        typology2 = typologyDAL.insert(typology2);
        localTypologies.add(typology2);
        Typology typology3 = new Typology("typo3");
        typology3 = typologyDAL.insert(typology3);
        localTypologies.add(typology3);
        List<Typology> dbTypologies = typologyDAL.getAll();
        assertTrue(dbTypologies.containsAll(localTypologies));
    }
}
