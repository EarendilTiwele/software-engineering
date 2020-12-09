/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import dataaccesslayer.ProcedureSkillsDAO;
import datatransferobjects.Competency;
import datatransferobjects.Procedure;
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
 * @author avall
 */
public class PostgresProcedureSkillsDAOTest {

    private static ProcedureSkillsDAO procedureHasCompetenciesDAL;
    private static Connection conn;

    public PostgresProcedureSkillsDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        procedureHasCompetenciesDAL = new PostgresProcedureSkillsDAO();
        conn = PostgresDAOFactory.createConnection();
        conn.setAutoCommit(false);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.rollback();
        conn.close();
    }

    @Before
    public void setUp() throws SQLException {
        PreparedStatement prepareStatement = conn.prepareStatement("delete from  procedurehascompetencies");
        prepareStatement.execute();
        prepareStatement = conn.prepareStatement("delete from  activity");
        prepareStatement.execute();
        prepareStatement = conn.prepareStatement("delete from  procedure");
        prepareStatement.execute();
        prepareStatement = conn.prepareStatement("delete from competency ");
        prepareStatement.execute();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAllCompetencies method, of class PostgresProcedureSkillsDAO.
     * Create new Procedure with 3 competencies required. Insert the Procedure
     * in the database. Insert the 3 competencies in the database. Insert the
     * associations in the ProcedureHasCompetencies table. Get the procedure
     * from the database. Check that the local copy and the database version of
     * the procedure are equal.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllCompetencies() throws SQLException {
        Procedure procedure = new Procedure(1, "procedure1", "smp1");
        PreparedStatement prepareStatement = conn.prepareStatement(String.format("insert into procedure (id, name, smp)"
                + " VALUES (%d, '%s', '%s')", procedure.getId(), procedure.getName(), procedure.getSmp()));
        prepareStatement.execute();
        Set<Competency> competencySet = new HashSet<>();

        competencySet.add(new Competency(1, "mechanical competence"));
        prepareStatement = conn.prepareStatement("insert into competency VALUES (1, 'mechanical competence')");
        prepareStatement.execute();
        competencySet.add(new Competency(2, "hydraulic competence"));
        prepareStatement = conn.prepareStatement("insert into competency VALUES (2, 'hydraulic competence')");
        prepareStatement.execute();
        competencySet.add(new Competency(3, "eletrical competence"));
        prepareStatement = conn.prepareStatement("insert into competency VALUES (3, 'eletrical competence')");
        prepareStatement.execute();
        /*-------------------------------------------------------------------------*/
        prepareStatement = conn.prepareStatement("insert into procedurehascompetencies VALUES(?,1)");
        prepareStatement.setInt(1, procedure.getId());
        prepareStatement.execute();
        prepareStatement = conn.prepareStatement("insert into procedurehascompetencies VALUES(?,2)");
        prepareStatement.setInt(1, procedure.getId());
        prepareStatement.execute();
        prepareStatement = conn.prepareStatement("insert into procedurehascompetencies VALUES(?,3)");
        prepareStatement.setInt(1, procedure.getId());
        prepareStatement.execute();

        Set<Competency> competencySet2 = procedureHasCompetenciesDAL.getAllCompetencies(procedure);

        assertEquals(competencySet, competencySet2);
    }

    /**
     * Test of testGetNoCompetencies method, of class
     * PostgresProcedureSkillsDAO. Create new Procedure without competencies.
     * Insert the Procedure in the database. Get the procedure from the
     * database. Check that the set of the competencies is empty
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetNoCompetencies() throws SQLException {
        Procedure procedure = new Procedure(1, "procedure1", "smp1");
        PreparedStatement prepareStatement = conn.prepareStatement(String.format("insert into procedure (id, name, smp)"
                + " VALUES (%d, '%s', '%s')", procedure.getId(), procedure.getName(), procedure.getSmp()));
        prepareStatement.execute();

        Set<Competency> competencySet2 = procedureHasCompetenciesDAL.getAllCompetencies(procedure);

        assertTrue(competencySet2.isEmpty());
    }
}