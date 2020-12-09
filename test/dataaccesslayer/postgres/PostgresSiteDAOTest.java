/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Site;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
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
public class PostgresSiteDAOTest {

    private static PostgresSiteDAO postgresSiteDAO;
    private static Connection connection;

    public PostgresSiteDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        postgresSiteDAO = new PostgresSiteDAO();
        connection = PostgresDAOFactory.createConnection();
        connection.setAutoCommit(false);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Before
    public void setUp() throws SQLException {
        PreparedStatement prepareStatement = connection.prepareStatement("delete from activity;");
        prepareStatement.execute();
        prepareStatement = connection.prepareStatement("delete from site;");
        prepareStatement.execute();
    }

    @After
    public void tearDown() throws SQLException {
        PreparedStatement prepareStatement = connection.prepareStatement("delete from site;");
        prepareStatement.execute();
    }

    private void insertSite(int id, String factory, String area) throws SQLException {
        PreparedStatement prepareStatement = connection.prepareStatement("insert into site values (?, ?, ?);");
        prepareStatement.setInt(1, id);
        prepareStatement.setString(2, factory);
        prepareStatement.setString(3, area);
        prepareStatement.execute();
    }

    private Site retrieveSite(int id) throws SQLException {
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(String.format("select * from site where id = %d;", id));
        Site site = null;
        while (rs.next()) {
            site = postgresSiteDAO.convertToEntity(rs);
        }
        return site;
    }

    /**
     * Test of insert method, of class PostgresSiteDAO.
     * <ul>
     * <li>Insert a site in the database and save the returned
     * <code>id</code></li>
     * <li>Retrieve from the database the site with the mentioned
     * <code>id</code></li>
     * <li>Compare the two sites</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsert() throws SQLException {
        Site localSite = new Site("test", "test");
        int id = postgresSiteDAO.insert(localSite);
        localSite.setId(id);
        Site dbSite = retrieveSite(id);
        assertEquals(localSite, dbSite);
    }

    /**
     * Test of update method, of class PostgresSiteDAO.
     * <ul>
     * <li>Insert a site in the database with <code>id = 1</code></li>
     * <li>Update the site in the database with <code>id = 1</code> and check if
     * <code>true</code> is returned</li>
     * <li>Check if the site in the database has actually been updated</li>
     * <li>Check if the update on a non-existing site returns true</li>
     * <li>Check if the site still doesn't exist</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdate() throws SQLException {
        PreparedStatement prepareStatement = connection.prepareStatement("insert into site values (1, 'test', 'test');");
        prepareStatement.execute();
        Site localSite = new Site(1, "updated", "updated");
        assertTrue(postgresSiteDAO.update(localSite));
        assertEquals(localSite, retrieveSite(1));
        localSite.setId(2);
        assertTrue(postgresSiteDAO.update(localSite));
        assertNull(retrieveSite(localSite.getId()));
    }

    /**
     * Test of delete method, of class PostgresSiteDAO.
     * <ul>
     * <li>Insert a site in the database with <code>id = 1</code></li>
     * <li>Delete the site in the database with <code>id = 1</code> and check if
     * <code>true</code> is returned</li>
     * <li>Check if the site in the database has actually been deleted</li>
     * <li>Check if the delete on a non-existing site returns true</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDelete() throws SQLException {
        insertSite(1, "test", "test");
        assertTrue(postgresSiteDAO.delete(1));
        assertNull(retrieveSite(1));
        assertTrue(postgresSiteDAO.delete(1));
    }

    /**
     * Test of get method, of class PostgresSiteDAO.
     * <ul>
     * <li>Insert a site in the database with <code>id = 1</code></li>
     * <li>Create a local site with same fields of the one inserted before</li>
     * <li>Check if the site retrieved from the database with
     * <code>id = 1</code> is equals to the local site</li>
     * <li>Check if the site retrieved from the database with
     * <code>id = 2</code> is null</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGet() throws SQLException {
        insertSite(1, "test", "test");
        Site localSite = new Site(1, "test", "test");
        assertEquals(postgresSiteDAO.get(1), localSite);
        assertNull(postgresSiteDAO.get(2));
    }

    /**
     * Test of getAll method, of class PostgresSiteDAO.
     * <ul>
     * <li>Insert three sites in the database with
     * <code>id = 1, 2, 3</code></li>
     * <li>Create a local set of sites adding the same sites inserted
     * before</li>
     * <li>Check if the set of sites retrieved from the database is equals to
     * the local set of sites</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAll() throws SQLException {
        insertSite(1, "test", "test");
        insertSite(2, "test2", "test2");
        insertSite(3, "test3", "test3");
        Set<Site> localSites = new HashSet<>();
        localSites.add(new Site(1, "test", "test"));
        localSites.add(new Site(2, "test2", "test2"));
        localSites.add(new Site(3, "test3", "test3"));
        assertEquals(localSites, postgresSiteDAO.getAll());
    }

}
