/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import dataaccesslayer.postgres.PostgresDAOFactory;
import dataaccesslayer.postgres.PostgresProcedureDAO;
import datatransferobjects.Competency;
import datatransferobjects.Procedure;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class ProcedureDALDatabaseTest {

    private static ProcedureDAO procedureDALDatabase;
    private static Connection conn;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        procedureDALDatabase = new PostgresProcedureDAO();
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
        PreparedStatement prepareStatement = conn.prepareStatement("delete from  activity");
        prepareStatement.execute();
        prepareStatement = conn.prepareStatement("delete from  procedurehascompetencies");
        prepareStatement.execute();
        prepareStatement = conn.prepareStatement("delete from competency ");
        prepareStatement.execute();
        prepareStatement = conn.prepareStatement("delete from procedure ");
        prepareStatement.execute();
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
     * Create a local procedure Insert it into db and retrieve the db version
     * Compare the two procedures
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsert() throws SQLException {
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        procedure = procedureDALDatabase.insert(procedure);
        assertNotNull(procedure);
        Procedure procedure2 = procedureDALDatabase.get(procedure.getId());
        assertNotNull(procedure2);
        assertEquals(procedure, procedure2);
    }

    /**
     * Create a local procedure Insert it into db and retrieve the db version
     * Update the local procedure Update it into db and retrieve the db version
     * Compare the two procedures
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdate() throws SQLException {
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        procedure = procedureDALDatabase.insert(procedure);
        assertNotNull(procedure);
        procedure = new Procedure(procedure.getId(), "moto maintenance", "./moto.pdf");
        procedure = procedureDALDatabase.update(procedure);
        Procedure procedure2 = procedureDALDatabase.get(procedure.getId());
        assertNotNull(procedure2);
        assertEquals(procedure, procedure2);
    }

    /**
     * Create a local procedure Insert it into db and retrieve the db version
     * Delete it form db Delete a non existing procedure from db and check if
     * return null
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDelete() throws SQLException {
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        procedure = procedureDALDatabase.insert(procedure);
        assertNotNull(procedure);
        procedure = procedureDALDatabase.delete(procedure.getId());
        assertNull(procedureDALDatabase.get(procedure.getId()));
    }

    /**
     * Create a list of local procedures Insert each procedure in a local list
     * Update the local list with the db version of the procedures Get all the
     * procedures from db and check that the procedures in the local list are
     * present in the db version list.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAll() throws SQLException {
        List<Procedure> procedureList = new ArrayList<>();
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        Procedure procedure2 = new Procedure(procedure.getId(), "moto maintenance", "./moto.pdf");
        Procedure procedure3 = new Procedure(procedure.getId(), "tractor maintenance", "./tractor.pdf");
        procedureList.add(procedure);
        procedureList.add(procedure2);
        procedureList.add(procedure3);
        Set<Procedure> procedureResultSet1 = new HashSet<>();
        Set<Procedure> procedureResultSet2;
        for (Procedure p : procedureList) {
            procedureResultSet1.add(procedureDALDatabase.insert(p));
        }
        procedureResultSet2 = procedureDALDatabase.getAll();
        for (Procedure p : procedureResultSet1) {
            assertTrue(procedureResultSet2.contains(p));
        }
    }

    /**
     * Create a local procedure Insert it into db and retrieve the db version
     * Create a new Competency and assign it to the procedure Insert the
     * procedure in the database Get the procedure from db and compare it with
     * the local procedure
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGet() throws SQLException {
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        Competency competency = new Competency(1, "mechanical competence");
        procedure.addCompetency(competency);
        PreparedStatement prepareStatement = conn.prepareStatement("insert into competency VALUES (1, 'mechanical competence')");
        prepareStatement.execute();
        prepareStatement = conn.prepareStatement("insert into procedurehascompetencies VALUES(?,1)");
        prepareStatement.setInt(1, procedure.getId());
        prepareStatement.execute();
        procedure = procedureDALDatabase.insert(procedure);
        assertNotNull(procedure);
        Procedure procedure2 = procedureDALDatabase.get(procedure.getId());
        assertEquals(procedure, procedure2);
    }

    /**
     * Insert 2 procedures Check the table size before the deleteAll Check the
     * table size after the deleAll that must be 0
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteAll() throws SQLException {
        Set<Procedure> procedureSet = new HashSet<>();
        procedureDALDatabase.insert(new Procedure("procedure1", "/procedure1.pdf"));
        procedureDALDatabase.insert(new Procedure("procedure2", "/procedure2.pdf"));
        procedureSet = procedureDALDatabase.getAll();
        assertEquals(procedureSet, procedureDALDatabase.deleteAll());
        assertEquals(0, procedureDALDatabase.getAll().size());
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
