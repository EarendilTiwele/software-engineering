/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import dataaccesslayer.postgres.PostgresTypologyDAO;
import dataaccesslayer.postgres.PostgresProcedureDAO;
import dataaccesslayer.postgres.PostgresActivityDAO;
import dataaccesslayer.postgres.PostgresAssignmentDAO;
import dataaccesslayer.postgres.PostgresSiteDAO;
import datatransferobjects.Activity;
import datatransferobjects.Assignment;
import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import datatransferobjects.PlannedActivity;
import datatransferobjects.Procedure;
import datatransferobjects.Site;
import datatransferobjects.Typology;
import datatransferobjects.User;
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

    private static AssignmentDAO assignmentDAL;
    private static Connection conn;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        assignmentDAL = new PostgresAssignmentDAO();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.rollback();
        conn.close();
    }
    
    @Before
    public void setUp() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("delete from assignment");
        preparedStatement.execute();
        preparedStatement = conn.prepareStatement("delete from  activity");
        preparedStatement.execute();
        preparedStatement = conn.prepareStatement("delete from  procedure");
        preparedStatement.execute();
        preparedStatement = conn.prepareStatement("delete from  typology");
        preparedStatement.execute();
        preparedStatement = conn.prepareStatement("delete from  site");
        preparedStatement.execute();
        preparedStatement = conn.prepareStatement("delete from  users");
        preparedStatement.execute();
        preparedStatement = conn.prepareStatement("delete from  maintainerhascompetencies");
        preparedStatement.execute();
        preparedStatement = conn.prepareStatement("delete from  competency");
        preparedStatement.execute();
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
     * Create assignments samples Insert them in the database Get the
     * assignments with a specific week Check that the local copy and the
     * database version of the assignments are equal
     *
     * @throws SQLException
     */
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

    public List<Activity> sampleListActivity() throws SQLException {
        List<Activity> activityList = new ArrayList<>();

        Site site = new Site(189, "Ferrari", "Maranello");
        SiteDAO siteDAL = new PostgresSiteDAO();
        site = siteDAL.insert(site);

        Typology typology = new Typology(3, "Hydraulic");
        TypologyDAO typologyDAL = new PostgresTypologyDAO();
        typology = typologyDAL.insert(typology);

        Procedure procedure = new Procedure(1, "hydraulicProcedure", "SMP1");
        ProcedureDAO procedureDAL = new PostgresProcedureDAO();
        procedure = procedureDAL.insert(procedure);

        String description = "hydraulic maintenance activity";
        int intervationTime = 12;
        boolean interruptible = false;
        int week = 2;
        String workspaceNotes = "Note to add";
        activityList.add(new PlannedActivity(0, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));

        /*------------------------------------------------------------------------------------*/
        site = new Site(0, "Lamborghini", "SantAgata bolognese");
        siteDAL = new PostgresSiteDAO();
        site = siteDAL.insert(site);

        typology = new Typology(0, "Mechanical");
        typologyDAL = new PostgresTypologyDAO();
        typology = typologyDAL.insert(typology);

        procedure = new Procedure(0, "mechanicalProcedure", "SMP2");
        procedureDAL = new PostgresProcedureDAO();
        procedure = procedureDAL.insert(procedure);

        description = "mechanical maintenance activity";
        intervationTime = 15;
        interruptible = true;
        week = 2;
        workspaceNotes = "Note to add lamborghini";
        activityList.add(new PlannedActivity(1, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));
        /*------------------------------------------------------------------------------------*/
        site = new Site(50, "Fiat", "Torino");
        siteDAL = new PostgresSiteDAO();
        site = siteDAL.insert(site);

        typology = new Typology(2, "Eletrical");
        typologyDAL = new PostgresTypologyDAO();
        typology = typologyDAL.insert(typology);

        procedure = new Procedure(3, "eletricalProcedure", "SMP3");
        procedureDAL = new PostgresProcedureDAO();
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
        Activity activity = activityList.get(0);
        Activity activity2 = activityList.get(1);
        ActivityDAO activityDAL = new PostgresActivityDAO();
        /*Users DAL insert user*/
        PreparedStatement preparedStatement = conn.prepareStatement("insert into users values(1,'Alfioc',"
                + "'Alfioc99', 'maintainer') returning *;");
        preparedStatement.executeQuery();
        activity = activityDAL.insert(activity);
        Maintainer user = new Maintainer(1, "Alfioc", "Alfioc99");
        /*---------------------*/
        preparedStatement = conn.prepareStatement("insert into competency values (1, 'competency1');");
        preparedStatement.execute();
        Competency c1 = new Competency(1, "competency1");
        /*---------------------*/

        preparedStatement = conn.prepareStatement("insert into competency values (2, 'competency2');");
        preparedStatement.execute();
        Competency c2 = new Competency(2, "competency2");
        /*---------------------*/

        user.addCompetency(c1);
        user.addCompetency(c2);
        
        preparedStatement = conn.prepareStatement("insert into maintainerhascompetencies values (" + user.getId() + "," + c1.getId() + ");");
        preparedStatement.execute();
        preparedStatement = conn.prepareStatement("insert into maintainerhascompetencies values (" + user.getId() + "," + c2.getId() + ");");
        preparedStatement.execute();

        Assignment assignment = new Assignment(user, activity, "Mon", 12);
        assignmentSet.add(assignment);

        /*Users DAL insert user*/
        preparedStatement = conn.prepareStatement("insert into users values(2,'francescoCarbone',"
                + "'francescoCarbone98', 'maintainer') returning *;");
        preparedStatement.executeQuery();
        activity2 = activityDAL.insert(activity2);
        Maintainer user2 = new Maintainer(2, "francescoCarbone", "francescoCarbone98");
        assignment = new Assignment(user2, activity2, "Mon", 12);
        assignmentSet.add(assignment);
        return assignmentSet;
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
