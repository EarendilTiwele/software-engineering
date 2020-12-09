/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class PostgresMaintainerSkillsDAOTest {

    private static PostgresMaintainerSkillsDAO postgresMaintainerSkillsDAO;
    private static Connection connection;

    public PostgresMaintainerSkillsDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        postgresMaintainerSkillsDAO = new PostgresMaintainerSkillsDAO();
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
        PreparedStatement preparedStatement = connection.prepareStatement("delete from users;");
        preparedStatement.execute();
        preparedStatement = connection.prepareStatement("delete from competency;");
        preparedStatement.execute();
        preparedStatement = connection.prepareStatement("delete from maintainerhascompetencies;");
        preparedStatement.execute();
    }

    @After
    public void tearDown() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from maintainerhascompetencies;");
        preparedStatement.execute();
    }

    private void insertMaintainerSkills(int maintainerId, int competencyId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into maintainerhascompetencies values (?, ?);");
        preparedStatement.setInt(1, maintainerId);
        preparedStatement.setInt(2, competencyId);
        preparedStatement.execute();
    }

    private void insertCompetency(int id, String description) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into competency values (?, ?);");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, description);
        preparedStatement.execute();
    }

    /**
     * Test of getAllCompetencies method, of class PostgresMaintainerSkillsDAO.
     * <ul>
     * <li>Insert three competencies in the database with
     * <code>id = 1, 2, 3</code></li>
     * <li>Insert three maintainer skills in the database with
     * <code>maintainerId = 1</code> and
     * <code>competencyId = 1, 2, 3</code></li>
     * <li>Create a local set of competencies adding the same competencies
     * inserted before</li>
     * <li>Create a maintainer with the same id inserted before</li>
     * <li>Check if the set of competencies retrieved from the database is
     * equals to the local set of competencies</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllCompetencies() throws SQLException {
        insertCompetency(1, "test");
        insertCompetency(2, "test2");
        insertCompetency(3, "test3");
        insertMaintainerSkills(1, 1);
        insertMaintainerSkills(1, 2);
        insertMaintainerSkills(1, 3);
        Set<Competency> localCompetencies = new HashSet<>();
        localCompetencies.add(new Competency(1, "test"));
        localCompetencies.add(new Competency(2, "test2"));
        localCompetencies.add(new Competency(3, "test3"));
        Maintainer maintainer = new Maintainer(1, "test", "test");
        assertEquals(localCompetencies, postgresMaintainerSkillsDAO.getAllCompetencies(maintainer));
    }

}
