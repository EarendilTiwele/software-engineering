/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import businesslogiclayer.Activity;
import businesslogiclayer.Site;
import dataaccesslayer.ActivityDALDatabase;
import java.beans.Transient;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Avallone
 */
public class ActivityDALDatabaseTest {

    private ActivityDALDatabase activityDALDatabase;
    private Connection conn;

    @Before
    public void setUp() {
        activityDALDatabase = new ActivityDALDatabase();
        conn = activityDALDatabase.getConnectionObj();
    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
    }

    /**
     * Test the connection with the database
     */
    @Test
    public void testConnection() throws SQLException {
        assertTrue(conn.isValid(0));
        assertNotNull(conn);
    }

    /**
     * Test the insert query in the database
     */
   public void testInsert() throws SQLException {
        conn.setAutoCommit(false);    //begin transaction
        try {
            Activity activity = new Activity();
            Site site = new Site(0, "Ferrari", "Maranello");
            Typology typology = new Typology(0, "Hydraulic");
            Procedure procedure = new Procedure(0, "hydraulicProcedure", "SMP1");
            activity.setSite(site);
            activity.setTipology(typology);
            activity.setDescription("hydraulic maintenance activity");
            activity.setInterventionTime(12);
            activity.setInterruptible(false);
            activity.setWeek(2);
            activity.setWorkspaceNotes("Note to add");
            activity.setProcedure(procedure);
            assertTrue(activityDALDatabase.insert(activity));
            Activity activity2 = activityDALDatabase.get(activity.getId());
            assertEquals(activity, activity2);
        } finally {
            conn.rollback();
            conn.close();
        }

    }

    public void testuUpdate(Activity activity) throws SQLException {

        conn.setAutoCommit(false);    //begin transaction
        try {
            Activity activity = new Activity();
            Site site = new Site(0, "Ferrari", "Maranello");
            Typology typology = new Typology(0, "Hydraulic");
            Procedure procedure = new Procedure(0, "hydraulicProcedure", "SMP1");
            activity.setSite(site);
            activity.setTipology(typology);
            activity.setDescription("hydraulic maintenance activity");
            activity.setInterventionTime(12);
            activity.setInterruptible(false);
            activity.setWeek(2);
            activity.setWorkspaceNotes("Note to add");
            activity.setProcedure(procedure);
            assertTrue(activityDALDatabase.insert(activity));
            /* create new activity with the same id of the previous, run update query
            and check if both are equal*/
            assertTrue(activityDALDatabase.update(activity));
            Activity activity2 = activityDALDatabase.get(activity.getId());
            assertEquals(activity, activity2);
        } finally {
            conn.rollback();
            conn.close();
        }
    }

    public void testDelete(int id) throws SQLException {
        conn.setAutoCommit(false);    //begin transaction
        try {
            Site site = new Site(0, "Ferrari", "Maranello");
            Typology typology = new Typology(0, "Hydraulic");
            Procedure procedure = new Procedure(0, "hydraulicProcedure", "SMP1");
            Activity activity = new Activity();
            activity.setSite(site);
            activity.setTipology(typology);
            activity.setDescription("hydraulic maintenance activity");
            activity.setInterventionTime(12);
            activity.setInterruptible(false);
            activity.setWeek(2);
            activity.setWorkspaceNotes("Note to add");
            activity.setProcedure(procedure);
            assertTrue(activityDALDatabase.insert(activity));
            /* create new activity with the same id of the previous, run update query
            and check if both are equal*/
            assertTrue(activityDALDatabase.update(activity));
            Activity activity2 = activityDALDatabase.get(activity.getId());
            assertEquals(activity, activity2);
        } finally {
            conn.rollback();
            conn.close();
        }
    }

    public void testGetAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void testGet(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void testGetAllOfWeek(int week) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void testGetAllPlannedOfWeek(int week) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
