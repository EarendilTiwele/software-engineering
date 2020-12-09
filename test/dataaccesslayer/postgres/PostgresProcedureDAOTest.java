/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Competency;
import datatransferobjects.Procedure;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class PostgresProcedureDAOTest {

    private static PostgresProcedureDAO postgresProcedureDAO;
    private static Connection conn;

    public PostgresProcedureDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        postgresProcedureDAO = new PostgresProcedureDAO();
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

    @After
    public void tearDown() {
    }

    private Procedure retrieveProcedure(int idProcedure) throws SQLException {
        Procedure procedure = null;
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(String.format("select * from procedure where id=%d", idProcedure));
        while (rs.next()) {
            procedure = postgresProcedureDAO.convertToEntity(rs);
        }
        return procedure;
    }

    /**
     * Test of insert method, of class PostgresProcedureDAO. Create a local
     * procedure Insert it into db and retrieve the db version with the id
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsert() throws SQLException {
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        int idProcedure = postgresProcedureDAO.insert(procedure);
        procedure = retrieveProcedure(idProcedure);
        assertNotNull(procedure);
    }

    /**
     * Test of update method, of class PostgresProcedureDAO. Create a local
     * procedure Insert it into db and retrieve the db version. Update the local
     * procedure. Update it into db and retrieve the db version Compare the two
     * procedures
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdate() throws SQLException {
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        int idProcedure = postgresProcedureDAO.insert(procedure);
        procedure = retrieveProcedure(idProcedure);
        assertNotNull(procedure);
        procedure = new Procedure(procedure.getId(), "moto maintenance", "./moto.pdf");
        assertTrue(postgresProcedureDAO.update(procedure));
        Procedure procedure2 = retrieveProcedure(idProcedure);
        assertNotNull(procedure2);
        assertEquals(procedure, procedure2);
    }

    /**
     * Test of delete method, of class PostgresProcedureDAO. Create a local
     * procedure Insert it into db and retrieve the db version Delete it form db
     * Delete a non existing procedure from db and check if return null
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDelete() throws SQLException {
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        int idProcedure = postgresProcedureDAO.insert(procedure);
        procedure = retrieveProcedure(idProcedure);
        assertNotNull(procedure);
        assertTrue(postgresProcedureDAO.delete(procedure.getId()));
        assertNull(retrieveProcedure(idProcedure));
    }

    /**
     * Test of getAll method, of class PostgresProcedureDAO. Create a list of
     * local procedures. Insert each procedure in a local list. Update the local
     * list with the db version of the procedures. Get all the procedures from
     * db and check that the procedures in the local list are present in the db
     * version list.
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
            int idProcedure = postgresProcedureDAO.insert(p);
            procedureResultSet1.add(retrieveProcedure(idProcedure));
        }
        procedureResultSet2 = postgresProcedureDAO.getAll();
        procedureResultSet1.forEach((p) -> {
            assertTrue(procedureResultSet2.contains(p));
        });
    }

    /**
     * Test of get method, of class PostgresProcedureDAO. Create a local
     * procedure. Insert it into db and retrieve the db version. Create a new
     * Competency and assign it to the procedure. Insert the procedure in the
     * database. Get the procedure from db and compare it with the local
     * procedure
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
        int idProcedure = postgresProcedureDAO.insert(procedure);
        procedure = retrieveProcedure(idProcedure);
        assertNotNull(procedure);
        Procedure procedure2 = postgresProcedureDAO.get(procedure.getId());
        assertEquals(procedure, procedure2);
    }

}
