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
    public void tearDown() throws SQLException {
        conn.rollback();
    }

    private int insertProcedure(Procedure procedure) throws SQLException {
        String query = String.format("insert into Procedure (name, smp) values ('%s','%s') returning *;",
                procedure.getName(), procedure.getSmp());
        PreparedStatement prepareStatement = conn.prepareStatement(query);
        ResultSet rs = prepareStatement.executeQuery();
        int idProcedure = -1;
        while (rs.next()) {
            idProcedure = rs.getInt("id");
        }
        return idProcedure;
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
     * Test of insert method, of class PostgresProcedureDAO.
     * <ul>
     * <li>Insert a procedure in the database and save the returned
     * <code>id</code></li>
     * <li>Retrieve from the database the procedure with the mentioned
     * <code>id</code></li>
     * <li>Compare the two procedures</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsert() throws SQLException {
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        int idProcedure = insertProcedure(procedure);
        procedure = retrieveProcedure(idProcedure);
        assertNotNull(procedure);
    }

    /**
     * Test of update method, of class PostgresProcedureDAO. Test case: update
     * of an existing procedure should return <code>true</code> and actually
     * update the procedure on the database.
     * <ul>
     * <li>Insert a procedure in the database and retrieve the id from the
     * returning of the insert operation </li>
     * <li>Update the procedure in the database with the retrieved id and check
     * if <code>true</code> is returned</li>
     * <li>Check if the procedure in the database has actually been updated</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateExisting() throws SQLException {
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        int idProcedure = insertProcedure(procedure);
        procedure = retrieveProcedure(idProcedure);
        assertNotNull(procedure);
        procedure = new Procedure(procedure.getId(), "moto maintenance", "./moto.pdf");
        assertTrue(postgresProcedureDAO.update(procedure));
        Procedure procedure2 = retrieveProcedure(idProcedure);
        assertNotNull(procedure2);
        assertEquals(procedure, procedure2);
    }

    /**
     * Test of update method, of class PostgresProcedureDAO. Test case: update
     * of a non-existing procedure should return <code>true</code>.
     * <ul>
     * <li>Update a non-existing procedure in the database and check if
     * <code>true</code> is returned</li>
     * <li>Check if the procedure still doesn't exist</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateNonExisting() throws SQLException {
        Procedure procedure = new Procedure(1, "car maintenance", "./car.pdf");
        assertTrue(postgresProcedureDAO.update(procedure));
        Procedure procedure2 = retrieveProcedure(procedure.getId());
        assertNull(procedure2);
    }

    /**
     * Test of delete method, of class PostgresProcedureDAO. Test case: delete
     * of an existing procedure should return <code>true</code> and actually
     * delete the procedure from the database.
     * <ul>
     * <li>Insert a procedure in the database and retrieve the id from the
     * returning of the insert operation </li>
     * <li>Delete the procedure in the database with the retrieved id and check
     * if <code>true</code> is returned</li>
     * <li>Check if the procedure in the database has actually been deleted</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteExisting() throws SQLException {
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        int idProcedure = insertProcedure(procedure);
        procedure = retrieveProcedure(idProcedure);
        assertNotNull(procedure);
        assertTrue(postgresProcedureDAO.delete(procedure.getId()));
        assertNull(retrieveProcedure(idProcedure));
    }

    /**
     * Test of delete method, of class PostgresProcedureDAO. Test case: delete
     * of a non-existing procedure should return <code>true</code>.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteNonExisting() throws SQLException {
        int id = 1;
        assertNull(retrieveProcedure(id));
        assertTrue(postgresProcedureDAO.delete(id));
    }

    /**
     * Test of getAll method, of class PostgresProcedureDAO.
     * <ul>
     * <li> Create a list of local procedures. </li>
     * <li> Insert each procedure in a local list. </li>
     * <li> Update the local list with the db version of the procedures. </li>
     * <li> Get all the procedures from db and check that the procedures in the
     * local list are present in the db version list. </li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllExisting() throws SQLException {
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
            int idProcedure = insertProcedure(p);
            procedureResultSet1.add(retrieveProcedure(idProcedure));
        }
        procedureResultSet2 = postgresProcedureDAO.getAll();
        procedureResultSet1.forEach((p) -> {
            assertTrue(procedureResultSet2.contains(p));
        });
    }

    /**
     * Test of get method, of class PostgresProcedureDAO. Test case: get of an
     * existing procedure.
     * <ul>
     * <li>Insert a procedure in the database and retrieve the id from the
     * returning of the insert operation </li>
     * <li>Create a local procedure with same fields of the one inserted
     * before</li>
     * <li>Check if the procedure retrieved from the database with the retrieved
     * id is equals to the local procedure</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetExisting() throws SQLException {
        Procedure procedure = new Procedure("car maintenance", "./car.pdf");
        int idProcedure = insertProcedure(procedure);
        procedure = retrieveProcedure(idProcedure);
        assertNotNull(procedure);
        Procedure procedure2 = postgresProcedureDAO.get(procedure.getId());
        assertEquals(procedure, procedure2);
    }

    /**
     * Test of get method, of class PostgresProcedureDAO. Test case: get of a
     * non-existing procedure.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetNonExisting() throws SQLException {
        int id = 1;
        assertNull(postgresProcedureDAO.get(id));
    }

    /**
     * Test of getAll method, of class PostgresProcedureDAO. Test case: no
     * procedures in the database.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllEmpty() throws SQLException {
        assertTrue(postgresProcedureDAO.getAll().isEmpty());
    }
}
