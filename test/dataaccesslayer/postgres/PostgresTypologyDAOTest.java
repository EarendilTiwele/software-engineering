/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Typology;
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
public class PostgresTypologyDAOTest {

    private static PostgresTypologyDAO postgresTypologyDAO;
    private static Connection connection;

    public PostgresTypologyDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        postgresTypologyDAO = new PostgresTypologyDAO();
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
        PreparedStatement preparedStatement = connection.prepareStatement("delete from activity;");
        preparedStatement.execute();
        preparedStatement = connection.prepareStatement("delete from typology;");
        preparedStatement.execute();
    }

    @After
    public void tearDown() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from typology;");
        preparedStatement.execute();
    }

    private void insertTypology(int id, String typo) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into typology values (?, ?);");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, typo);
        preparedStatement.execute();
    }

    private Typology retrieveTypology(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from typology where id = ?;");
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        Typology typology = null;
        while (rs.next()) {
            typology = postgresTypologyDAO.convertToEntity(rs);
        }
        return typology;
    }

    /**
     * Test of insert method, of class PostgresTypologyDAO.
     * <ul>
     * <li>Insert a typology in the database and save the returned
     * <code>id</code></li>
     * <li>Retrieve from the database the typology with the mentioned
     * <code>id</code></li>
     * <li>Compare the two typologies</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsert() throws SQLException {
        Typology localTypology = new Typology("test");
        int id = postgresTypologyDAO.insert(localTypology);
        localTypology.setId(id);
        Typology dbTypology = retrieveTypology(id);
        assertEquals(localTypology, dbTypology);
    }

    /**
     * Test of update method, of class PostgresTypologyDAO.
     * <ul>
     * <li>Insert a typology in the database with <code>id = 1</code></li>
     * <li>Update the typology in the database with <code>id = 1</code> and
     * check if <code>true</code> is returned</li>
     * <li>Check if the typology in the database has actually been updated</li>
     * <li>Check if the update on a non-existing typology returns true</li>
     * <li>Check if the typology still doesn't exist</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdate() throws SQLException {
        insertTypology(1, "test");
        Typology localTypology = new Typology(1, "updated");
        assertTrue(postgresTypologyDAO.update(localTypology));
        assertEquals(localTypology, retrieveTypology(1));
        localTypology.setId(2);
        assertTrue(postgresTypologyDAO.update(localTypology));
        assertNull(retrieveTypology(localTypology.getId()));
    }

    /**
     * Test of delete method, of class PostgresTypologyDAO.
     * <ul>
     * <li>Insert a typology in the database with <code>id = 1</code></li>
     * <li>Delete the typology in the database with <code>id = 1</code> and
     * check if <code>true</code> is returned</li>
     * <li>Check if the typology in the database has actually been deleted</li>
     * <li>Check if the delete on a non-existing typology returns true</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDelete() throws SQLException {
        insertTypology(1, "test");
        assertTrue(postgresTypologyDAO.delete(1));
        assertNull(retrieveTypology(1));
        assertTrue(postgresTypologyDAO.delete(1));
    }

    /**
     * Test of get method, of class PostgresTypologyDAO.
     * <ul>
     * <li>Insert a typology in the database with <code>id = 1</code></li>
     * <li>Create a local typology with same fields of the one inserted
     * before</li>
     * <li>Check if the typology retrieved from the database with
     * <code>id = 1</code> is equals to the local typology</li>
     * <li>Check if the typology retrieved from the database with
     * <code>id = 2</code> is null</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGet() throws SQLException {
        insertTypology(1, "test");
        Typology localTypology = new Typology(1, "test");
        assertEquals(postgresTypologyDAO.get(1), localTypology);
        assertNull(postgresTypologyDAO.get(2));
    }

    /**
     * Test of getAll method, of class PostgresTypologyDAO. Test case: no
     * typologies in the database.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllEmpty() throws SQLException {
        assertTrue(postgresTypologyDAO.getAll().isEmpty());
    }

    /**
     * Test of getAll method, of class PostgresTypologyDAO. Test case: only one
     * typology in the database.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllOneTypology() throws SQLException {
        insertTypology(1, "test");
        Set<Typology> localCompetencies = new HashSet<>();
        localCompetencies.add(new Typology(1, "test"));
        assertEquals(localCompetencies, postgresTypologyDAO.getAll());
    }

    /**
     * Test of getAll method, of class PostgresTypologyDAO.
     * <ul>
     * <li>Insert three typologies in the database with
     * <code>id = 1, 2, 3</code></li>
     * <li>Create a local set of typologies adding the same typologies inserted
     * before</li>
     * <li>Check if the set of typologies retrieved from the database is equals
     * to the local set of typologies</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAll() throws SQLException {
        insertTypology(1, "test");
        insertTypology(2, "test2");
        insertTypology(3, "test3");
        Set<Typology> localTypologys = new HashSet<>();
        localTypologys.add(new Typology(1, "test"));
        localTypologys.add(new Typology(2, "test2"));
        localTypologys.add(new Typology(3, "test3"));
        assertEquals(localTypologys, postgresTypologyDAO.getAll());
    }

}
