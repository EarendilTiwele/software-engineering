/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Competency;
import businesslogiclayer.Procedure;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    private ProcedureDAL procedureDALDatabase;
    private Connection conn;

    @Before
    public void setUp() throws SQLException {
        procedureDALDatabase = new ProcedureDALDatabase();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        ActivityDAL activityDAL = new ActivityDALDatabase();
        activityDAL.deleteAll();
        PreparedStatement prepareStatement = conn.prepareStatement("delete from  procedurehascompetencies");
        prepareStatement.execute();
        prepareStatement = conn.prepareStatement("delete from competency ");
        prepareStatement.execute();
        procedureDALDatabase.deleteAll();
    }

    @After
    public void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
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
     */
    @Test
    public void testInsert() {
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
     */
    @Test
    public void testUpdate() {
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
     */
    @Test
    public void testDelete() {
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
     */
    @Test
    public void testGetAll() {
        List<Procedure> procedureList = new ArrayList<>();
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        Procedure procedure2 = new Procedure(procedure.getId(), "moto maintenance", "./moto.pdf");
        Procedure procedure3 = new Procedure(procedure.getId(), "tractor maintenance", "./tractor.pdf");
        procedureList.add(procedure);
        procedureList.add(procedure2);
        procedureList.add(procedure3);
        List<Procedure> procedureResultList1 = new ArrayList<>();
        List<Procedure> procedureResultList2;
        for (Procedure p : procedureList) {
            procedureResultList1.add(procedureDALDatabase.insert(p));
        }
        procedureResultList2 = procedureDALDatabase.getAll();
        for (Procedure p : procedureResultList1) {
            assertTrue(procedureResultList2.contains(p));
        }
    }

    /**
     * Create a local procedure Insert it into db and retrieve the db version
     * Create a new Competency and assign it to the procedure
     * Insert the procedure in the database
     * Get the procedure from db and compare it with the local procedure
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
     */
    @Test
    public void testDeleteAll() {
        List<Procedure> procedureList = new ArrayList<>();
        procedureDALDatabase.insert(new Procedure("procedure1", "/procedure1.pdf"));
        procedureDALDatabase.insert(new Procedure("procedure2", "/procedure2.pdf"));
        int tableSize = procedureDALDatabase.getAll().size();
        assertEquals(tableSize, procedureDALDatabase.deleteAll().size());
        assertEquals(0, procedureDALDatabase.getAll().size());
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
