package dataaccesslayer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import businesslogiclayer.Activity;
import businesslogiclayer.PlannedActivity;
import businesslogiclayer.Procedure;
import businesslogiclayer.Site;
import businesslogiclayer.Typology;
import dataaccesslayer.ActivityDALDatabase;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Avallone
 */
public class ActivityDALDatabaseTest {

    private ActivityDALDatabase activityDALDatabase;
    private Connection conn;

    /*remember to use the other constructor for PlannedActivity*/
    public List<Activity> sampleListActivity() {
        List<Activity> activityList = new ArrayList<>();

        Site site = new Site(0, "Ferrari", "Maranello");
        Typology typology = new Typology(3, "Hydraulic");
        Procedure procedure = new Procedure(0, "hydraulicProcedure", "SMP1");
        String description = "hydraulic maintenance activity";
        int intervationTime = 12;
        boolean interruptible = false;
        int week = 2;
        String workspaceNotes = "Note to add";
        activityList.add(new PlannedActivity(0, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));
        /*------------------------------------------------------------------------------------*/
        site = new Site(0, "Lamborghini", "Sant'Agata bolognese");
        typology = new Typology(0, "Mechanical");
        procedure = new Procedure(0, "mechanicalProcedure", "SMP2");
        description = "mechanical maintenance activity";
        intervationTime = 15;
        interruptible = true;
        week = 9;
        workspaceNotes = "Note to add lamborghini";
        activityList.add(new PlannedActivity(1, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));
        /*------------------------------------------------------------------------------------*/
        site = new Site(0, "Fiat", "Torino");
        typology = new Typology(2, "Eletrical");
        procedure = new Procedure(3, "eletricalProcedure", "SMP3");
        description = "eletrical maintenance activity";
        intervationTime = 19;
        interruptible = true;
        week = 9;
        workspaceNotes = "Note to add Fiat";
        activityList.add(new PlannedActivity(2, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));

        return activityList;
    }

    @Before
    public void setUp() throws SQLException {
        activityDALDatabase = new ActivityDALDatabase();
        conn = activityDALDatabase.getConnectionObj();
        conn.setAutoCommit(false);
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
     * Create a sample of activity Insert it in the database and retrieve it
     * back Compare the two activities
     *
     */
    @Test
    public void testInsert() {
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
    public void testuUpdate() {

        Activity activity = sampleListActivity().get(0);
        activity = activityDALDatabase.insert(activity);
        assertNotNull(activity);

        /* I didn't want to use the sampleListActivity because I need that
        the new activity must have the same id of the previous*/
        Site site = new Site(0, "Lamborghini", "Sant'Agata bolognese");
        Typology typology = new Typology(0, "Mechanical");
        Procedure procedure = new Procedure(0, "mechanicalProcedure", "SMP2");
        String description = "mechanical maintenance activity";
        int intervationTime = 15;
        boolean interruptible = true;
        int week = 3;
        String workspaceNotes = "Note to add lamborghini";
        activity = new PlannedActivity(0, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes);

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
    public void testDelete() {
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
    public void testGetAll() {

        List<Activity> activityList = sampleListActivity();
        List<Activity> resultActivityList = new ArrayList<>();
        Activity resultActivity;
        for (Activity activity : activityList) {
            resultActivity = activityDALDatabase.insert(activity);
            assertNotNull(resultActivity);
            resultActivityList.add(resultActivity);
        }
        List<Activity> activityList2 = activityDALDatabase.getAll();
        assertTrue(resultActivityList.equals(activityList2));

    }

    /**
     * Create a sample activity Insert it in the database and retrieve it.
     * Compare the local activity with the activity retrieve from the database
     */
    @Test
    public void testGet() {
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
    public void testGetAllOfWeek() {
        List<Activity> activityList = sampleListActivity();
        List<Activity> resultActivityList = new ArrayList<>();
        int week = 9;
        Activity resultActivity;
        for (Activity activity : activityList) {
            resultActivity = activityDALDatabase.insert(activity);
            assertNotNull(resultActivity);
            if (resultActivity.getWeek() == week) {
                resultActivityList.add(resultActivity);
            }
        }
        List<Activity> activityList2 = activityDALDatabase.getAllOfWeek(week);
        assertTrue(resultActivityList.equals(activityList2));
    }

    public void testGetAllPlannedOfWeek() {
        List<Activity> activityList = sampleListActivity();
        List<Activity> resultActivityList = new ArrayList<>();
        int week = 9;
        Activity resultActivity;
        for (Activity activity : activityList) {
            resultActivity = activityDALDatabase.insert(activity);
            assertNotNull(resultActivity);
            if (resultActivity.getWeek() == week) {
                resultActivityList.add(resultActivity);
            }
        }
        List<Activity> activityList2 = activityDALDatabase.getAllPlannedOfWeek(week);
        assertTrue(resultActivityList.equals(activityList2));
    }

}
