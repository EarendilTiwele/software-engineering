package dataaccesslayer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import datatransferobjects.Activity;
import datatransferobjects.PlannedActivity;
import datatransferobjects.Procedure;
import datatransferobjects.Site;
import datatransferobjects.Typology;
import dataaccesslayer.postgres.PostgresActivityDAO;
import dataaccesslayer.postgres.PostgresDAOFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Avallone
 */
public class ActivityDALDatabaseTest {

    private static ActivityDAO activityDALDatabase;
    private static Connection conn;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        activityDALDatabase = new PostgresActivityDAO();
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
        Statement stm = conn.createStatement();
        stm.executeUpdate("delete from activity where true");
        stm.executeUpdate("delete from procedure where true");
        stm.executeUpdate("delete from typology where true");
        stm.executeUpdate("delete from site where true");
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
     * Create a sample of activity Insert it in the database and retrieve it
     * back Compare the two activities
     *
     */
    @Test
    public void testInsert() throws SQLException {
        Activity activity = sampleListActivity().get(0);
        activity = activityDALDatabase.insert(activity);
        assertNotNull(activity);
        Activity activity2 = activityDALDatabase.get(activity.getId());
        assertNotNull(activity2);
        assertEquals(activity, activity2);
    }

    /**
     * Create a sample activity Insert it in the database, retrieve, modify and
     * reinsert it. Check that the record is updated.
     */
    @Test
    public void testuUpdate() throws SQLException {

        List<Activity> activityList = sampleListActivity();
        Activity activity = activityList.get(0);
        activity = activityDALDatabase.insert(activity);
        assertNotNull(activity);
        Site site2 = activityList.get(1).getSite();
        /* I didn't want to use the sampleListActivity because I need that
        the new activity must have the same id of the previous*/
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
        activity = activityDALDatabase.update(activity);
        assertNotNull(activity);
        Activity activity2 = activityDALDatabase.get(activity.getId());
        assertNotNull(activity2);
        assertEquals(activity, activity2);
    }

    /**
     * Create a sample activity Insert it in the database, retrieve and delete
     * it. Check that the record is deleted successfully.
     *
     */
    @Test
    public void testDelete() throws SQLException {
        Activity activity = sampleListActivity().get(0);
        activity = activityDALDatabase.insert(activity);
        assertNotNull(activity);
        activity = activityDALDatabase.delete(activity.getId());
        assertNotNull(activity);
        assertNull(activityDALDatabase.get(activity.getId()));
    }

    /**
     * Create a sample list of activities Insert each activity in the database
     * and retrieve all list. Check that the both lists are equals
     */
    @Test
    public void testGetAll() throws SQLException {

        List<Activity> activityList = sampleListActivity();
        Set<Activity> resultActivitySet = new HashSet<>();
        Activity resultActivity;
        for (Activity activity : activityList) {
            resultActivity = activityDALDatabase.insert(activity);
            assertNotNull(resultActivity);
            resultActivitySet.add(resultActivity);
        }
        Set<Activity> activityList2 = activityDALDatabase.getAll();
        assertTrue(resultActivitySet.equals(activityList2));

    }

    /**
     * Create a sample activity Insert it in the database and retrieve it.
     * Compare the local activity with the activity retrieve from the database
     */
    @Test
    public void testGet() throws SQLException {
        Activity activity = sampleListActivity().get(0);
        activity = activityDALDatabase.insert(activity);
        assertNotNull(activity);
        Activity activity2 = activityDALDatabase.get(activity.getId());
        assertEquals(activity, activity2);
    }

    /**
     * Create a sample list of activities Insert each activity in the database
     * and retrieve all list Insert in the resulting list only the activity for
     * the specified week Select from the db the activities for the specified
     * week Create a new list of activities Check that the both lists are equals
     */
    @Test
    public void testGetAllOfWeek() throws SQLException {
        List<Activity> activityList = sampleListActivity();
        Set<Activity> resultActivitySet = new HashSet<>();
        int week = 9;
        Activity resultActivity;
        for (Activity activity : activityList) {
            resultActivity = activityDALDatabase.insert(activity);
            assertNotNull(resultActivity);
            if (resultActivity.getWeek() == week) {
                resultActivitySet.add(resultActivity);
            }
        }
        Set<Activity> activitySet2 = activityDALDatabase.getAllOfWeek(week);
        assertTrue(resultActivitySet.equals(activitySet2));
    }

    /**
     * Create a sample list of activities Insert each activity in the database
     * and retrieve all list Insert in the resulting list only the activity for
     * the specified week Select from the db the activities for the specified
     * week Create a new list of planned activities Check that the both lists
     * are equals
     */
    @Test
    public void testGetAllPlannedOfWeek() throws SQLException {
        List<Activity> activityList = sampleListActivity();
        Set<Activity> resultActivitySet = new HashSet<>();
        int week = 9;
        Activity resultActivity;
        for (Activity activity : activityList) {
            resultActivity = activityDALDatabase.insert(activity);
            assertNotNull(resultActivity);
            if (resultActivity.getWeek() == week) {
                resultActivitySet.add(resultActivity);
            }
        }
        Set<Activity> activitySet2 = activityDALDatabase.getAllPlannedOfWeek(week);
        assertTrue(resultActivitySet.equals(activitySet2));
    }

    /**
     * Insert 2 activities Check the table size before the deleteAll Check the
     * table size after the deleAll that must be 0
     */
    @Test
    public void testDeleteAll() throws SQLException {
        List<Activity> activityList = sampleListActivity();
        HashSet<Activity> activitySet = new HashSet<>();
        activitySet.add(activityList.get(0));
        activitySet.add(activityList.get(1));
        activityDALDatabase.insert(activityList.get(0));
        activityDALDatabase.insert(activityList.get(1));
        int tableSize = activityDALDatabase.getAll().size();
        Set<Activity> activitySet2 = activityDALDatabase.deleteAll();
        assertEquals(activitySet, activitySet2);
        assertEquals(0, activityDALDatabase.getAll().size());
    }

}
