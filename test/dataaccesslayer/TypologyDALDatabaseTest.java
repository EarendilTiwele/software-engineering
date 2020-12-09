/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import dataaccesslayer.postgres.PostgresTypologyDAO;
import dataaccesslayer.postgres.PostgresActivityDAO;
import dataaccesslayer.postgres.PostgresDAOFactory;
import datatransferobjects.Typology;
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
public class TypologyDALDatabaseTest {
    
    private static PostgresTypologyDAO typologyDAL;
    private static Connection conn;
    
    public TypologyDALDatabaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException {
        typologyDAL = new PostgresTypologyDAO();
        conn = PostgresDAOFactory.createConnection();
        conn.setAutoCommit(false);
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.rollback();
        conn.close();
    }

    /**
     * Create a local typology
     * Insert it into db and retrieve the db version
     * Compare the two typologies
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsert () throws SQLException {
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
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdate () throws SQLException {
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
     * @throws java.sql.SQLException
     */
    @Test
    public void testDelete () throws SQLException {
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
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteAll () throws SQLException {
        new PostgresActivityDAO().deleteAll();
        typologyDAL.insert(new Typology("test1"));
        typologyDAL.insert(new Typology("test2"));
        int tableSize = typologyDAL.getAll().size();
        assertEquals(tableSize, typologyDAL.deleteAll().size());
        assertEquals(0, typologyDAL.getAll().size());
        assertEquals(0, typologyDAL.deleteAll().size());
    }
    
    /**
     * Create a local typology
     * Insert it into db and retrieve the db version
     * Get the typology from db and compare it with the local typology
     * @throws java.sql.SQLException
     */
    @Test
    public void testGet () throws SQLException {
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
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAll () throws SQLException {
        Set<Typology> localTypologies = new HashSet<>();
        Typology typology1 = new Typology("typo1");
        typology1 = typologyDAL.insert(typology1);
        localTypologies.add(typology1);
        Typology typology2 = new Typology("typo2");
        typology2 = typologyDAL.insert(typology2);
        localTypologies.add(typology2);
        Typology typology3 = new Typology("typo3");
        typology3 = typologyDAL.insert(typology3);
        localTypologies.add(typology3);
        Set<Typology> dbTypologies = typologyDAL.getAll();
        assertTrue(dbTypologies.containsAll(localTypologies));
    }
}
