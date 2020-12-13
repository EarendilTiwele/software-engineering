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
        connection.close();
    }

    @Before
    public void setUp() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from activity;");
        preparedStatement.execute();
        preparedStatement = connection.prepareStatement("delete from site;");
        preparedStatement.execute();
    }

    @After
    public void tearDown() throws SQLException {
        connection.rollback();
    }

    private void insertSite(int id, String factory, String area) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into site values (?, ?, ?);");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, factory);
        preparedStatement.setString(3, area);
        preparedStatement.execute();
    }

    private Site retrieveSite(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from site where id = ?;");
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
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
     * Test of update method, of class PostgresSiteDAO. Test case: update of an
     * existing site should return <code>true</code> and actually update the
     * site on the database.
     * <ul>
     * <li>Insert a site in the database with <code>id = 1</code></li>
     * <li>Update the site in the database with <code>id = 1</code> and check if
     * <code>true</code> is returned</li>
     * <li>Check if the site in the database has actually been updated</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateExisting() throws SQLException {
        int id = 1;
        insertSite(id, "test", "test");
        Site localSite = new Site(id, "updated", "updated");
        assertTrue(postgresSiteDAO.update(localSite));
        assertEquals(localSite, retrieveSite(id));
    }

    /**
     * Test of update method, of class PostgresSiteDAO. Test case: update of a
     * non-existing site should return <code>true</code>.
     * <ul>
     * <li>Update a non-existing site in the database and check if
     * <code>true</code> is returned</li>
     * <li>Check if the site still doesn't exist</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateNonExisting() throws SQLException {
        int id = 1;
        Site localSite = new Site(id, "updated", "updated");
        assertTrue(postgresSiteDAO.update(localSite));
        assertNull(retrieveSite(id));
    }

    /**
     * Test of delete method, of class PostgresSiteDAO. Test case: delete of an
     * existing site should return <code>true</code> and actually delete the
     * site from the database.
     * <ul>
     * <li>Insert a site in the database with <code>id = 1</code></li>
     * <li>Delete the site in the database with <code>id = 1</code> and check if
     * <code>true</code> is returned</li>
     * <li>Check if the site in the database has actually been deleted</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteExisting() throws SQLException {
        int id = 1;
        insertSite(id, "test", "test");
        assertTrue(postgresSiteDAO.delete(id));
        assertNull(retrieveSite(id));
    }

    /**
     * Test of delete method, of class PostgresSiteDAO. Test case: delete of a
     * non-existing site should return <code>true</code>.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteNonExisting() throws SQLException {
        int id = 1;
        assertNull(retrieveSite(id));
        assertTrue(postgresSiteDAO.delete(id));
    }

    /**
     * Test of get method, of class PostgresSiteDAO. Test case: get of an
     * existing site.
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
    public void testGetExisting() throws SQLException {
        int id = 1;
        insertSite(id, "test", "test");
        Site localSite = new Site(id, "test", "test");
        assertEquals(postgresSiteDAO.get(id), localSite);
    }

    /**
     * Test of get method, of class PostgresSiteDAO. Test case: get of a
     * non-existing site.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetNonExisting() throws SQLException {
        int id = 1;
        assertNull(postgresSiteDAO.get(id));
    }

    /**
     * Test of getAll method, of class PostgresSiteDAO. Test case: no sites in
     * the database.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllEmpty() throws SQLException {
        assertTrue(postgresSiteDAO.getAll().isEmpty());
    }

    /**
     * Test of getAll method, of class PostgresSiteDAO. Test case: only one site
     * in the database.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllOneSite() throws SQLException {
        insertSite(1, "test", "test");
        Set<Site> localCompetencies = new HashSet<>();
        localCompetencies.add(new Site(1, "test", "test"));
        assertEquals(localCompetencies, postgresSiteDAO.getAll());
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
