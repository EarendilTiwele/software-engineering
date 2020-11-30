/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Activity;
import businesslogiclayer.Assignment;
import businesslogiclayer.Maintainer;
import businesslogiclayer.PlannedActivity;
import businesslogiclayer.Procedure;
import businesslogiclayer.Site;
import businesslogiclayer.Typology;
import businesslogiclayer.User;
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
public class AssignmentDALDatabaseTest {

    private AssignmentDAL assignmentDAL;
    private Connection conn;

    @Before
    public void setUp() throws SQLException {
        AssignmentDAL assignmentDAL = new AssignmentDALDatabase();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        PreparedStatement preparedStatement = conn.prepareStatement("delete from assignment");
        preparedStatement.execute();
        ActivityDAL activityDAL = new ActivityDALDatabase();
        ProcedureDAL procedureDAL = new ProcedureDALDatabase();
        TypologyDAL typologyDAL = new TypologyDALDatabase();
        SiteDAL siteDAL = new SiteDALDatabase();
        activityDAL.deleteAll();
        preparedStatement = conn.prepareStatement("delete from  users");
        preparedStatement.execute();
        procedureDAL.deleteAll();
        typologyDAL.deleteAll();
        siteDAL.deleteAll();
    }

    @After
    public void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }

    @Test
    public void testConnection() throws SQLException {
        assertNotNull(conn);
        assertTrue(conn.isValid(0));
    }

    @Test
    public void testGetAllForWeek() throws SQLException {
        /*I have used an obj of ActivityDALDatabaseTest only to retrive
        a sample activity to insert in the database*/
        Set<Assignment> assignmentSet = sampleSetAssignment();
        for (Assignment assignment : assignmentSet) {
            PreparedStatement preparedStatement = conn.prepareStatement("insert into assignment values"
                    + " (?,?,?,?);");
            preparedStatement.setInt(1, assignment.getMaintainer().getId());
            preparedStatement.setInt(2, assignment.getActivity().getId());
            preparedStatement.setString(3, assignment.getDay());
            preparedStatement.setInt(4, assignment.getHour());
            preparedStatement.execute();
        }
        Set<Assignment> assignmentSet2 = assignmentDAL.getAllForWeek(2);
        assertEquals(assignmentSet, assignmentSet2);
    }

    public List<Activity> sampleListActivity() {
        List<Activity> activityList = new ArrayList<>();

        Site site = new Site(189, "Ferrari", "Maranello");
        SiteDAL siteDAL = new SiteDALDatabase();
        site = siteDAL.insert(site);

        Typology typology = new Typology(3, "Hydraulic");
        TypologyDAL typologyDAL = new TypologyDALDatabase();
        typology = typologyDAL.insert(typology);

        Procedure procedure = new Procedure(1, "hydraulicProcedure", "SMP1");
        ProcedureDAL procedureDAL = new ProcedureDALDatabase();
        procedure = procedureDAL.insert(procedure);

        String description = "hydraulic maintenance activity";
        int intervationTime = 12;
        boolean interruptible = false;
        int week = 2;
        String workspaceNotes = "Note to add";
        activityList.add(new PlannedActivity(0, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));
        /*------------------------------------------------------------------------------------*/

        site = new Site(0, "Lamborghini", "Sant'Agata bolognese");
        siteDAL = new SiteDALDatabase();
        site = siteDAL.insert(site);

        typology = new Typology(0, "Mechanical");
        typologyDAL = new TypologyDALDatabase();
        typology = typologyDAL.insert(typology);

        procedure = new Procedure(0, "mechanicalProcedure", "SMP2");
        procedureDAL = new ProcedureDALDatabase();
        procedure = procedureDAL.insert(procedure);

        description = "mechanical maintenance activity";
        intervationTime = 15;
        interruptible = true;
        week = 2;
        workspaceNotes = "Note to add lamborghini";
        activityList.add(new PlannedActivity(1, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));
        /*------------------------------------------------------------------------------------*/
        site = new Site(0, "Fiat", "Torino");
        siteDAL = new SiteDALDatabase();
        site = siteDAL.insert(site);

        typology = new Typology(2, "Eletrical");
        typologyDAL = new TypologyDALDatabase();
        typology = typologyDAL.insert(typology);

        procedure = new Procedure(3, "eletricalProcedure", "SMP3");
        procedureDAL = new ProcedureDALDatabase();
        procedure = procedureDAL.insert(procedure);

        description = "eletrical maintenance activity";
        intervationTime = 19;
        interruptible = true;
        week = 9;
        workspaceNotes = "Note to add Fiat";
        activityList.add(new PlannedActivity(2, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));

        return activityList;
    }

    public Set<Assignment> sampleSetAssignment() throws SQLException {
        Set<Assignment> assignmentSet = new HashSet<>();
        List<Activity> activityList = sampleListActivity();
        Activity activity = sampleListActivity().get(0);
        ActivityDAL activityDAL = new ActivityDALDatabase();
        activity = activityDAL.insert(activity);
        /*Users DAL insert user*/
        PreparedStatement preparedStatement = conn.prepareStatement("insert into users values(1,'Alfioc',"
                + "'Alfioc99', 'maintainer') returning *;");
        preparedStatement.executeQuery();
        Maintainer user = new Maintainer(1, "Alfioc", "Alfioc99");
        Assignment assignment = new Assignment(user, activity, "monday", 12);
        assignmentSet.add(assignment);

        activity = sampleListActivity().get(1);
        activity = activityDAL.insert(activity);
        /*Users DAL insert user*/
        preparedStatement = conn.prepareStatement("insert into users values(2,'francescoCarbone',"
                + "'francescoCarbone98', 'maintainer') returning *;");
        preparedStatement.executeQuery();
        user = new Maintainer(2, "francescoCarbone", "francescoCarbone98");
        assignment = new Assignment(user, activity, "monday", 12);
        assignmentSet.add(assignment);
        return assignmentSet;
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
