/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import dataaccesslayer.postgres.PostgresDAOFactory;
import dataaccesslayer.postgres.PostgresProcedureSkillsDAO;
import datatransferobjects.Competency;
import datatransferobjects.Procedure;
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
 * @author avall
 */
public class ProcedureHasCompetenciesDALTest {

    private static ProcedureSkillsDAO procedureHasCompetenciesDAL;
    private static Connection conn;

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
        /*also deleteAll of the competencies*/
    }

    /**
     * Test the connection with the database checking that the connection is
     * valid
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testConnection() throws SQLException {
        assertNotNull(conn);
        assertTrue(conn.isValid(0));
    }

    /**
     * Create new Procedure with 3 competencies required Insert the Procedure in
     * the database Insert the 3 competencies in the database Insert the
     * associations in the ProcedureHasCompetencies table Get the procedure from
     * the database Check that the local copy and the database version of the
     * procedure are equal.
     *
     * @throws SQLException
     */
    @Test
    public void testgetAllCompetencies() throws SQLException {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
