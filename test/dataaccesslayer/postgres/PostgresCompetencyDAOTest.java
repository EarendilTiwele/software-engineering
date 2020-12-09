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
        connection.rollback();
        connection.close();
    }

    @Before
    public void setUp() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from competency;");
        preparedStatement.execute();
    }

    @After
    public void tearDown() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from competency;");
        preparedStatement.execute();
    }

    private void insertCompetency(int id, String description) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into competency values (?, ?);");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, description);
        preparedStatement.execute();
    }

    /**
     * Test of get method, of class PostgresCompetencyDAO.
     * <ul>
     * <li>Insert a competency in the database with <code>id = 1</code></li>
     * <li>Create a local competency with same fields of the one inserted
     * before</li>
     * <li>Check if the competency retrieved from the database with
     * <code>id = 1</code> is equals to the local competency</li>
     * <li>Check if the competency retrieved from the database with
     * <code>id = 2</code> is null</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGet() throws SQLException {
        insertCompetency(1, "test");
        Competency localCompetency = new Competency(1, "test");
        assertEquals(postgresCompetencyDAO.get(1), localCompetency);
        assertNull(postgresCompetencyDAO.get(2));
    }

}
