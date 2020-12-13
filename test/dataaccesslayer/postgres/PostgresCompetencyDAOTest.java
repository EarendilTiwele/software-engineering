/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Competency;
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
public class PostgresCompetencyDAOTest {

    private static PostgresCompetencyDAO postgresCompetencyDAO;
    private static Connection connection;

    public PostgresCompetencyDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        postgresCompetencyDAO = new PostgresCompetencyDAO();
        connection = PostgresDAOFactory.createConnection();
        connection.setAutoCommit(false);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        connection.close();
    }

    @Before
    public void setUp() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from competency;");
        preparedStatement.execute();
    }

    @After
    public void tearDown() throws SQLException {
        connection.rollback();
    }

    private void insertCompetency(int id, String description) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into competency values (?, ?);");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, description);
        preparedStatement.execute();
    }

    private Competency retrieveCompetency(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from competency where id = ?;");
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        Competency competency = null;
        while (rs.next()) {
            competency = postgresCompetencyDAO.convertToEntity(rs);
        }
        return competency;
    }

    /**
     * Test of insert method, of class PostgresCompetencyDAO.
     * <ul>
     * <li>Insert a competency in the database and save the returned
     * <code>id</code></li>
     * <li>Retrieve from the database the competency with the mentioned
     * <code>id</code></li>
     * <li>Compare the two competencies</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsert() throws SQLException {
        Competency localCompetency = new Competency("test");
        int id = postgresCompetencyDAO.insert(localCompetency);
        localCompetency.setId(id);
        Competency dbCompetency = retrieveCompetency(id);
        assertEquals(localCompetency, dbCompetency);
    }

    /**
     * Test of update method, of class PostgresCompetencyDAO. Test case: update
     * of an existing competency should return <code>true</code> and actually
     * update the competency on the database.
     * <ul>
     * <li>Insert a competency in the database with <code>id = 1</code></li>
     * <li>Update the competency in the database with <code>id = 1</code> and
     * check if <code>true</code> is returned</li>
     * <li>Check if the competency in the database has actually been
     * updated</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateExisting() throws SQLException {
        int id = 1;
        insertCompetency(id, "test");
        Competency localCompetency = new Competency(id, "updated");
        assertTrue(postgresCompetencyDAO.update(localCompetency));
        assertEquals(localCompetency, retrieveCompetency(id));
    }

    /**
     * Test of update method, of class PostgresCompetencyDAO. Test case: update
     * of a non-existing competency should return <code>true</code>.
     * <ul>
     * <li>Update a non-existing competency in the database and check if
     * <code>true</code> is returned</li>
     * <li>Check if the competency still doesn't exist in the database</li>
     * </ul>
     *
     * @throws SQLException
     */
    @Test
    public void testUpdateNonExisting() throws SQLException {
        int id = 1;
        Competency localCompetency = new Competency(id, "updated");
        assertTrue(postgresCompetencyDAO.update(localCompetency));
        assertNull(retrieveCompetency(id));
    }

    /**
     * Test of get method, of class PostgresCompetencyDAO. Test case: get of an
     * existing competency.
     * <ul>
     * <li>Insert a competency in the database with <code>id = 1</code></li>
     * <li>Create a local competency with same fields of the one inserted
     * before</li>
     * <li>Check if the competency retrieved from the database with
     * <code>id = 1</code> is equals to the local competency</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetExisting() throws SQLException {
        int id = 1;
        insertCompetency(id, "test");
        Competency localCompetency = new Competency(id, "test");
        assertEquals(postgresCompetencyDAO.get(id), localCompetency);
    }

    /**
     * Test of get method, of class PostgresCompetencyDAO. Test case: get of a
     * non-existing competency should return <code>null</code>.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetNonExisting() throws SQLException {
        int id = 1;
        assertNull(postgresCompetencyDAO.get(id));
    }

    /**
     * Test of getAll method, of class PostgresCompetencyDAO. Test case: no
     * competencies in the database.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllEmpty() throws SQLException {
        assertTrue(postgresCompetencyDAO.getAll().isEmpty());
    }

    /**
     * Test of getAll method, of class PostgresCompetencyDAO. Test case: only
     * one competency in the database.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllOneCompetency() throws SQLException {
        insertCompetency(1, "test");
        Set<Competency> localCompetencies = new HashSet<>();
        localCompetencies.add(new Competency(1, "test"));
        assertEquals(localCompetencies, postgresCompetencyDAO.getAll());
    }

    /**
     * Test of getAll method, of class PostgresCompetencyDAO. Test case: more
     * than one (three) competency in the database.
     * <ul>
     * <li>Insert three competencies in the database with
     * <code>id = 1, 2, 3</code></li>
     * <li>Create a local set of competencies adding the same competencies
     * inserted before</li>
     * <li>Check if the set of competencies retrieved from the database is
     * equals to the local set of competencies</li>
     * </ul>
     *
     * @throws SQLException
     */
    @Test
    public void testGetAll() throws SQLException {
        insertCompetency(1, "test");
        insertCompetency(2, "test2");
        insertCompetency(3, "test3");
        Set<Competency> localCompetencies = new HashSet<>();
        localCompetencies.add(new Competency(1, "test"));
        localCompetencies.add(new Competency(2, "test2"));
        localCompetencies.add(new Competency(3, "test3"));
        assertEquals(localCompetencies, postgresCompetencyDAO.getAll());
    }

}
