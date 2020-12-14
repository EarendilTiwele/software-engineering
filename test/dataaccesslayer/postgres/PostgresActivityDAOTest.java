/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Activity;
import datatransferobjects.PlannedActivity;
import datatransferobjects.Procedure;
import datatransferobjects.Site;
import datatransferobjects.Typology;
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
public class PostgresActivityDAOTest {

    private static PostgresActivityDAO postgresActivityDAO;
    private static Connection conn;

    public PostgresActivityDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        postgresActivityDAO = new PostgresActivityDAO();
        conn = PostgresDAOFactory.createConnection();
        conn.setAutoCommit(false);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.close();
    }

    @Before
    public void setUp() throws SQLException {
        Statement stm = conn.createStatement();
        stm.executeUpdate("delete from activity where true");
        stm.executeUpdate("delete from procedure where true");
        stm.executeUpdate("delete from typology where true");
        stm.executeUpdate("delete from site where true");
    }

    @After
    public void tearDown() throws SQLException {
        conn.rollback();
    }

    public List<Activity> sampleListActivity() throws SQLException {
        List<Activity> activityList = new ArrayList<>();
        Statement stm = conn.createStatement();

        Site site = new Site(189, "Ferrari", "Maranello");
        stm.executeUpdate(String.format("insert into Site values (%d,'%s','%s');",
                site.getId(), site.getFactory(), site.getArea()));

        Typology typology = new Typology(3, "Hydraulic");
        stm.executeUpdate(String.format("insert into Typology values (%d,'%s');",
                typology.getId(), typology.getName()));

        Procedure procedure = new Procedure(1, "hydraulicProcedure", "SMP1");
        stm.executeUpdate(String.format("insert into Procedure values (%d,'%s','%s');",
                procedure.getId(), procedure.getName(), procedure.getSmp()));

        String description = "hydraulic maintenance activity";
        int intervationTime = 12;
        boolean interruptible = false;
        int week = 2;
        String workspaceNotes = "Note to add";
        activityList.add(new PlannedActivity(0, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));
        /*------------------------------------------------------------------------------------*/

        site = new Site(0, "Lamborghini", "Sant Agata bolognese");
        stm.executeUpdate(String.format("insert into Site values (%d,'%s','%s');",
                site.getId(), site.getFactory(), site.getArea()));

        typology = new Typology(0, "Mechanical");
        stm.executeUpdate(String.format("insert into Typology values (%d,'%s');",
                typology.getId(), typology.getName()));

        procedure = new Procedure(0, "mechanicalProcedure", "SMP2");
        stm.executeUpdate(String.format("insert into Procedure values (%d,'%s','%s');",
                procedure.getId(), procedure.getName(), procedure.getSmp()));

        description = "mechanical maintenance activity";
        intervationTime = 15;
        interruptible = true;
        week = 9;
        workspaceNotes = "Note to add lamborghini";
        activityList.add(new PlannedActivity(1, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));
        /*------------------------------------------------------------------------------------*/
        site = new Site(100, "Fiat", "Torino");
        stm.executeUpdate(String.format("insert into Site values (%d,'%s','%s');",
                site.getId(), site.getFactory(), site.getArea()));

        typology = new Typology(2, "Eletrical");
        stm.executeUpdate(String.format("insert into Typology values (%d,'%s');",
                typology.getId(), typology.getName()));

        procedure = new Procedure(3, "eletricalProcedure", "SMP3");
        stm.executeUpdate(String.format("insert into Procedure values (%d,'%s','%s');",
                procedure.getId(), procedure.getName(), procedure.getSmp()));

        description = "eletrical maintenance activity";
        intervationTime = 19;
        interruptible = true;
        week = 9;
        workspaceNotes = "Note to add Fiat";
        activityList.add(new PlannedActivity(2, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));

        return activityList;
    }

    private int insertActivity(Activity activity) throws SQLException {
        String query = String.format("insert into Activity "
                + "(id, site, type, description, interventiontime, interruptible, week, "
                + "workspacenotes, procedure) "
                + "VALUES (%d,%d,%d,'%s',%d,%b,%d,'%s',%d) returning *;",
                activity.getId(), activity.getSite().getId(), activity.getTipology().getId(),
                activity.getDescription(), activity.getInterventionTime(), activity.isInterruptible(),
                activity.getWeek(), activity.getWorkspaceNotes(), activity.getProcedure().getId());
        PreparedStatement prepareStatement = conn.prepareStatement(query);
        ResultSet rs = prepareStatement.executeQuery();
        int idActivity = -1;
        while (rs.next()) {
            idActivity = rs.getInt("id");
        }
        return idActivity;
    }

    private Activity retrieveActivity(int id) throws SQLException {
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(String.format("select * from activity where id=%d", id));
        Activity activity = null;
        while (rs.next()) {
            activity = postgresActivityDAO.convertToEntity(rs);
        }
        return activity;
    }

    /**
     * Test of insert method, of class PostgresActivityDAO.
     * <ul>
     * <li>Insert a activity in the database and save the returned
     * <code>id</code></li>
     * <li>Retrieve from the database the activity with the mentioned
     * <code>id</code></li>
     * <li>Compare the two activities</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsert() throws SQLException {
        Activity activity = sampleListActivity().get(0);
        int idActivity = insertActivity(activity);
        Activity dbActivity = retrieveActivity(idActivity);
        assertNotNull(dbActivity);
        assertEquals(activity, dbActivity);
    }

    /**
     * Test of update method, of class PostgresActivityDAO. Test case: update of
     * an existing activity should return <code>true</code> and actually update
     * the activity on the database.
     * <ul>
     * <li>Insert a activity in the database
     * <li>Update the activity in the database with the <code>id</code>
     * retrieved by the insert operation and check if <code>true</code> is
     * returned</li>
     * <li>Check if the activity in the database has actually been updated</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateExisting() throws SQLException {
        List<Activity> activityList = sampleListActivity();
        Activity activity = activityList.get(0);
        int idActivity = insertActivity(activity);
        activity = retrieveActivity(idActivity);
        assertNotNull(activity);
        Site site2 = activityList.get(1).getSite();

        Typology typology2 = activityList.get(1).getTipology();
        Procedure procedure2 = activityList.get(1).getProcedure();
        String description = "mechanical maintenance activity";
        int intervationTime = 15;
        boolean interruptible = true;
        int week = 3;
        String workspaceNotes = "Note to add lamborghini";
        activity = new PlannedActivity(activity.getId(), site2, typology2, description, intervationTime,
                interruptible, week, procedure2, workspaceNotes);

        /*--------------------------------------------------------------------------*/
        boolean result = postgresActivityDAO.update(activity);
        assertTrue(result);
        Activity dbActivity = retrieveActivity(activity.getId());
        assertNotNull(dbActivity);
        assertEquals(activity, dbActivity);
    }

    /**
     * Test of update method, of class PostgresActivityDAO. Test case: update of
     * a non-existing activity should return <code>true</code>.
     * <ul>
     * <li>Update a non-existing activity in the database and check if
     * <code>true</code> is returned</li>
     * <li>Check if the activity still doesn't exist</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateNonExisting() throws SQLException {
        int id = 1;
        Activity activity = sampleListActivity().get(0);
        assertTrue(postgresActivityDAO.update(activity));
        assertNull(retrieveActivity(id));
    }

    /**
     * Test of delete method, of class PostgresActivityDAO. Test case: delete of
     * an existing activity should return <code>true</code> and actually delete
     * the activity from the database.
     * <ul>
     * <li>Insert a activity in the database</li>
     * <li>Delete the site in the database with the <code>id</code> retrieved by
     * the insert operation and check if <code>true</code> is returned</li>
     * <li>Check if the activity in the database has actually been deleted</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteExisting() throws SQLException {
        Activity activity = sampleListActivity().get(0);
        int idActivity = insertActivity(activity);
        activity = retrieveActivity(idActivity);
        assertNotNull(activity);
        boolean result = postgresActivityDAO.delete(activity.getId());
        assertTrue(result);
        assertNull(retrieveActivity(idActivity));
    }

    /**
     * Test of delete method, of class PostgresActivityDAO. Test case: delete of
     * a non-existing activity should return <code>true</code>.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteNonExisting() throws SQLException {
        int id = 1;
        assertNull(retrieveActivity(id));
        assertTrue(postgresActivityDAO.delete(id));
    }

    /**
     * <ul>
     * <li> Test of getAll method, of class PostgresActivityDAO. </li>
     * <li> Create a sample list of activities Insert each activity in the
     * database and retrieve all list. </li>
     * <li> Check that the both lists are equals </li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAll() throws SQLException {
        List<Activity> activityList = sampleListActivity();
        Set<Activity> resultActivitySet = new HashSet<>();
        Activity resultActivity;
        for (Activity activity : activityList) {
            int idActivity = insertActivity(activity);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(String.format("select * from activity where id=%d", idActivity));
            resultActivity = null;
            while (rs.next()) {
                resultActivity = postgresActivityDAO.convertToEntity(rs);
            }
            assertNotNull(resultActivity);
            resultActivitySet.add(resultActivity);
        }
        Set<Activity> activityList2 = postgresActivityDAO.getAll();
        assertTrue(resultActivitySet.equals(activityList2));
    }

    /**
     * Test of get method, of class PostgresActivityDAO. Test case: get of an
     * existing activity.
     * <ul>
     * <li>Insert an activity in the database</li>
     * <li> Save the id of the inserted activity returned by the insert
     * operation</li>
     * <li>Create a local activity with same fields of the one inserted
     * before</li>
     * <li>Check if the activity retrieved from the database with with the
     * specified id is equals to the local activity</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetExisting() throws SQLException {
        Activity activity = sampleListActivity().get(0);
        int idActivity = insertActivity(activity);
        activity = retrieveActivity(idActivity);
        assertNotNull(activity);
        Activity activity2 = postgresActivityDAO.get(activity.getId());
        assertNotNull(activity2);
        assertEquals(activity, activity2);
    }

    /**
     * Test of get method, of class PostgresActivityDAO. Test case: get of a
     * non-existing activity.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetNonExisting() throws SQLException {
        int id = 1;
        assertNull(postgresActivityDAO.get(id));
    }

    /**
     * Test of getAll method, of class PostgresActivityDAO. Test case: no
     * activities in the database.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllEmpty() throws SQLException {
        assertTrue(postgresActivityDAO.getAll().isEmpty());
    }

    /**
     * Test of getAllOfWeek method, of class PostgresActivityDAO.
     * <li> Create a sample list of activities </li>
     * <li>Insert each activity in the database and retrieve all list </li>
     * <li> Insert in the resulting list only the activity for the specified
     * week </li>
     * <li> Select from the db the activities for the specified week </li>
     * <li> Create a new list of activities </li>
     * <li> Check that the both lists are equals </li>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllOfWeek() throws SQLException {
        List<Activity> activityList = sampleListActivity();
        Set<Activity> resultActivitySet = new HashSet<>();
        int week = 9;
        Activity resultActivity = null;
        for (Activity activity : activityList) {
            int idActivity = insertActivity(activity);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(String.format("select * from activity where id=%d", idActivity));
            while (rs.next()) {
                resultActivity = postgresActivityDAO.convertToEntity(rs);
            }
            assertNotNull(resultActivity);
            if (resultActivity.getWeek() == week) {
                resultActivitySet.add(resultActivity);
            }
        }
        Set<Activity> activitySet2 = postgresActivityDAO.getAllOfWeek(week);
        assertTrue(resultActivitySet.equals(activitySet2));
    }

    /**
     * Test of getAllPlannedOfWeek method, of class PostgresActivityDAO.
     * <li> Create a sample list of activities </li>
     * <li> Insert each activity in the database and retrieve all list </li>
     * <li> Insert in the resulting list only the activity for the specified
     * week </li>
     * <li> Select from the db the activities for the specified week </li>
     * <li> Create a new list of planned activities Check that the both lists
     * are equals</li>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllPlannedOfWeek() throws SQLException {
        List<Activity> activityList = sampleListActivity();
        Set<Activity> resultActivitySet = new HashSet<>();
        int week = 9;
        Activity resultActivity = null;
        for (Activity activity : activityList) {
            int idActivity = insertActivity(activity);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(String.format("select * from activity where id=%d", idActivity));
            while (rs.next()) {
                resultActivity = postgresActivityDAO.convertToEntity(rs);
            }
            assertNotNull(resultActivity);
            if (resultActivity.getWeek() == week) {
                resultActivitySet.add(resultActivity);
            }
        }
        Set<Activity> activitySet2 = postgresActivityDAO.getAllPlannedOfWeek(week);
        assertTrue(resultActivitySet.equals(activitySet2));
    }
}
