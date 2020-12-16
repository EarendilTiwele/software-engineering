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
        connection.rollback();
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
     * Test of insert method, of class PostgresTypologyDAO. Test case: insert of
     * a typology with a <strong>unique</strong> <code>name</code> should return
     * <code>id != -1</code>.
     * <ul>
     * <li>Insert a typology in the database and save the returned
     * <code>id</code></li>
     * <li>Check if the returned <code>id</code> is not equals to
     * <code>-1</code></li>
     * <li>Retrieve from the database the typology with the mentioned
     * <code>id</code></li>
     * <li>Compare the two typologies</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsertSuccess() throws SQLException {
        Typology localTypology = new Typology("test");
        int id = postgresTypologyDAO.insert(localTypology);
        assertNotEquals(-1, id);
        localTypology.setId(id);
        Typology dbTypology = retrieveTypology(id);
        assertEquals(localTypology, dbTypology);
    }

    /**
     * Test of insert method, of class PostgresTypologyDAO. Test case: insert of
     * a typology with a <strong>non-unique</strong> <code>name</code> should
     * return <code>id == -1</code>.
     * <ul>
     * <li>Insert a typology in the database and save the returned
     * <code>id</code></li>
     * <li>Check if the returned <code>id</code> is not equals to
     * <code>-1</code></li>
     * <li>Insert another typology with the same <code>name</code> in the
     * database and save the returned <code>id</code></li>
     * <li>Check if the returned <code>id</code> is equals to
     * <code>-1</code></li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsertFailure() throws SQLException {
        Typology typology = new Typology("test");
        int id = postgresTypologyDAO.insert(typology);
        assertNotEquals(-1, id);
        id = postgresTypologyDAO.insert(typology);
        assertEquals(-1, id);
    }

    /**
     * Test of update method, of class PostgresTypologyDAO. Test case: update of
     * an existing typology with the new <code>name</code> being
     * <strong>unique</strong> should return <code>true</code> and actually
     * update the typology on the database.
     * <ul>
     * <li>Insert a typology in the database with <code>id = 1</code></li>
     * <li>Update the typology in the database with <code>id = 1</code> and
     * check if <code>true</code> is returned</li>
     * <li>Check if the typology in the database has actually been updated</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateExistingSuccess() throws SQLException {
        int id = 1;
        insertTypology(id, "test");
        Typology localTypology = new Typology(id, "updated");
        assertTrue(postgresTypologyDAO.update(localTypology));
        assertEquals(localTypology, retrieveTypology(id));
    }

    /**
     * Test of update method, of class PostgresTypologyDAO. Test case: update of
     * an existing typology with the new <code>name</code> being
     * <strong>non-unique</strong> should return <code>false</code>.
     * <ul>
     * <li>Insert two typologies in the database with
     * <code>id = 1, 2</code></li>
     * <li>Update the typology in the database with <code>id = 1</code> giving
     * it the same <code>name</code> of the already existing typology with
     * <code>id = 2</code> and check if the operation returns
     * <code>false</code></li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateExistingFailure() throws SQLException {
        int id = 1;
        insertTypology(id, "test");
        insertTypology(id + 1, "update");
        Typology updatedTypology = new Typology(id, "update");
        assertFalse(postgresTypologyDAO.update(updatedTypology));
    }

    /**
     * Test of update method, of class PostgresTypologyDAO. Test case: update of
     * a non-existing typology should return <code>true</code>.
     * <ul>
     * <li>Update a non-existing typology in the database and check if
     * <code>true</code> is returned</li>
     * <li>Check if the typology still doesn't exist in the database</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateNonExisting() throws SQLException {
        int id = 1;
        Typology localTypology = new Typology(id, "updated");
        assertTrue(postgresTypologyDAO.update(localTypology));
        assertNull(retrieveTypology(id));
    }

    /**
     * Test of delete method, of class PostgresTypologyDAO. Test case: delete of
     * an existing typology should return <code>true</code> and actually delete
     * the typology from the database.
     * <ul>
     * <li>Insert a typology in the database with <code>id = 1</code></li>
     * <li>Delete the typology in the database with <code>id = 1</code> and
     * check if <code>true</code> is returned</li>
     * <li>Check if the typology in the database has actually been deleted</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteExisting() throws SQLException {
        int id = 1;
        insertTypology(id, "test");
        assertTrue(postgresTypologyDAO.delete(id));
        assertNull(retrieveTypology(id));
    }

    /**
     * Test of delete method, of class PostgresTypologyDAO. Test case: delete of
     * a non-existing typology should return <code>true</code>.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteNonExisting() throws SQLException {
        int id = 1;
        assertTrue(postgresTypologyDAO.delete(id));
    }

    /**
     * Test of get method, of class PostgresTypologyDAO. Test case: get of an
     * existing typology.
     * <ul>
     * <li>Insert a typology in the database with <code>id = 1</code></li>
     * <li>Create a local typology with same fields of the one inserted
     * before</li>
     * <li>Check if the typology retrieved from the database with
     * <code>id = 1</code> is equals to the local typology</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetExisting() throws SQLException {
        int id = 1;
        insertTypology(id, "test");
        Typology localTypology = new Typology(id, "test");
        assertEquals(postgresTypologyDAO.get(id), localTypology);
    }

    /**
     * Test of get method, of class PostgresTypologyDAO. Test case: get of a
     * non-existing typology should return <code>null</code>.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetNonExisting() throws SQLException {
        int id = 1;
        assertNull(postgresTypologyDAO.get(id));
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
        Set<Typology> localTypologies = new HashSet<>();
        localTypologies.add(new Typology(1, "test"));
        assertEquals(localTypologies, postgresTypologyDAO.getAll());
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
        Set<Typology> localTypologies = new HashSet<>();
        localTypologies.add(new Typology(1, "test"));
        localTypologies.add(new Typology(2, "test2"));
        localTypologies.add(new Typology(3, "test3"));
        assertEquals(localTypologies, postgresTypologyDAO.getAll());
    }

}
