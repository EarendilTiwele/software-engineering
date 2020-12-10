/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Activity;
import datatransferobjects.Assignment;
import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import datatransferobjects.PlannedActivity;
import datatransferobjects.Procedure;
import datatransferobjects.Site;
import datatransferobjects.Typology;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class PostgresAssignmentDAOTest {

    private static PostgresAssignmentDAO postgresAssignmentDAO;
    private static Connection conn;

    public PostgresAssignmentDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        postgresAssignmentDAO = new PostgresAssignmentDAO();
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

    @After
    public void tearDown() {
    }

    @Test
    public void testInsert() throws SQLException {
        List<Assignment> assignmentSet = new ArrayList<>(sampleSetAssignment());
        Assignment assignment = assignmentSet.get(0);
        boolean result = postgresAssignmentDAO.insert(assignment);
        assertTrue(result);
        PreparedStatement preparedStatement = conn.prepareStatement(String.format("Select * "
                + "from assignment where idmaintainer = %d and idactivity=%d",
                assignment.getMaintainer().getId(), assignment.getActivity().getId()));
        ResultSet rs = preparedStatement.executeQuery();
        Assignment assignment2 = null;
        while (rs.next()) {
            assignment2 = postgresAssignmentDAO.convertToEntity(rs);
        }
        assertEquals(assignment, assignment2);
    }

    /**
     * Test of getAllForWeek method, of class PostgresAssignmentDAO. Create
     * assignments samples. Insert them in the database. Get the assignments
     * with a specific week. Check that the local copy and the database version
     * of the assignments are equal
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllForWeek() throws SQLException {
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
        Set<Assignment> assignmentSet2 = postgresAssignmentDAO.getAllForWeek(2);
        assertEquals(assignmentSet, assignmentSet2);
    }

    public List<Activity> sampleListActivity() throws SQLException {
        List<Activity> activityList = new ArrayList<>();

        Site site = new Site(189, "Ferrari", "Maranello");
        site = insertAndRetrieveSite(site, true);

        Typology typology = new Typology(3, "Hydraulic");
        typology = insertAndRetrieveTypology(typology, true);

        Procedure procedure = new Procedure(1, "hydraulicProcedure", "SMP1");
        procedure = insertAndRetrieveProcedure(procedure, true);

        String description = "hydraulic maintenance activity";
        int intervationTime = 12;
        boolean interruptible = false;
        int week = 2;
        String workspaceNotes = "Note to add";
        activityList.add(new PlannedActivity(0, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));

        /*------------------------------------------------------------------------------------*/
        site = new Site(0, "Lamborghini", "SantAgata bolognese");
        site = insertAndRetrieveSite(site, true);

        typology = new Typology(0, "Mechanical");
        typology = insertAndRetrieveTypology(typology, true);

        procedure = new Procedure(0, "mechanicalProcedure", "SMP2");
        procedure = insertAndRetrieveProcedure(procedure, true);

        description = "mechanical maintenance activity";
        intervationTime = 15;
        interruptible = true;
        week = 2;
        workspaceNotes = "Note to add lamborghini";
        activityList.add(new PlannedActivity(1, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));
        /*------------------------------------------------------------------------------------*/
        site = new Site(50, "Fiat", "Torino");
        site = insertAndRetrieveSite(site, true);

        typology = new Typology(2, "Eletrical");
        typology = insertAndRetrieveTypology(typology, true);

        procedure = new Procedure(3, "eletricalProcedure", "SMP3");
        procedure = insertAndRetrieveProcedure(procedure, true);

        description = "eletrical maintenance activity";
        intervationTime = 19;
        interruptible = true;
        week = 9;
        workspaceNotes = "Note to add Fiat";
        activityList.add(new PlannedActivity(2, site, typology, description, intervationTime,
                interruptible, week, procedure, workspaceNotes));

        return activityList;
    }

    private Site insertAndRetrieveSite(Site site, boolean ins) throws SQLException {
        PreparedStatement preparedStatement;
        if (ins) {
            preparedStatement = conn.prepareStatement(String.format("insert into site values"
                    + " (%d,'%s','%s');", site.getId(), site.getFactory(), site.getArea()));
            preparedStatement.execute();
        }
        preparedStatement = conn.prepareStatement(String.format("select * from site where id=%d;",
                site.getId()));
        ResultSet rs = preparedStatement.executeQuery();
        site = null;
        while (rs.next()) {
            site = new Site(rs.getInt("id"), rs.getString("factory"), rs.getString("area"));
        }
        return site;
    }

    private Typology insertAndRetrieveTypology(Typology typology, boolean ins) throws SQLException {
        PreparedStatement preparedStatement;
        if (ins) {
            preparedStatement = conn.prepareStatement(String.format("insert into typology values"
                    + " (%d,'%s');", typology.getId(), typology.getName()));
            preparedStatement.execute();
        }
        preparedStatement = conn.prepareStatement(String.format("select * from typology where id=%d;",
                typology.getId()));
        ResultSet rs = preparedStatement.executeQuery();
        typology = null;
        while (rs.next()) {
            typology = new Typology(rs.getInt("id"), rs.getString("typo"));
        }
        return typology;
    }

    private Procedure insertAndRetrieveProcedure(Procedure procedure, boolean ins) throws SQLException {
        PreparedStatement preparedStatement;
        if (ins) {
            preparedStatement = conn.prepareStatement(String.format("insert into procedure values"
                    + " (%d,'%s','%s');", procedure.getId(), procedure.getName(), procedure.getSmp()));
            preparedStatement.execute();
        }
        preparedStatement = conn.prepareStatement(String.format("select * from procedure where id=%d;",
                procedure.getId()));
        ResultSet rs = preparedStatement.executeQuery();
        procedure = null;
        while (rs.next()) {
            procedure = new Procedure(rs.getInt("id"), rs.getString("name"), rs.getString("smp"));
        }
        return procedure;
    }

    private Activity insertAndRetrieveActivity(Activity activity) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(String.format("insert into activity values"
                + " (%d, %d, %d, '%s', %d, %b, %d, '%s', %d);", activity.getId(),
                activity.getSite().getId(), activity.getTipology().getId(), activity.getDescription(),
                activity.getInterventionTime(), activity.isInterruptible(), activity.getWeek(),
                activity.getWorkspaceNotes(), activity.getProcedure().getId()));
        preparedStatement.execute();
        preparedStatement = conn.prepareStatement(String.format("select * from activity where id=%d;",
                activity.getId()));
        ResultSet rs = preparedStatement.executeQuery();
        activity = null;
        while (rs.next()) {
            Site site = insertAndRetrieveSite(new Site(rs.getInt("site"), "", ""), false);
            Typology typology = insertAndRetrieveTypology(new Typology(rs.getInt("type"), ""), false);
            Procedure procedure = insertAndRetrieveProcedure(new Procedure(rs.getInt("procedure"), "", ""), false);
            activity = new PlannedActivity(rs.getInt("id"), site, typology,
                    rs.getString("description"), rs.getInt("interventiontime"),
                    rs.getBoolean("interruptible"), rs.getInt("week"), procedure, rs.getString("workspacenotes"));
        }
        return activity;
    }

    public Set<Assignment> sampleSetAssignment() throws SQLException {
        Set<Assignment> assignmentSet = new HashSet<>();
        List<Activity> activityList = sampleListActivity();
        Activity activity = activityList.get(0);
        Activity activity2 = activityList.get(1);
        PreparedStatement preparedStatement = conn.prepareStatement("insert into users values(1,'Alfioc',"
                + "'Alfioc99', 'maintainer') returning *;");
        preparedStatement.executeQuery();
        activity = insertAndRetrieveActivity(activity);
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
        activity2 = insertAndRetrieveActivity(activity2);
        Maintainer user2 = new Maintainer(2, "francescoCarbone", "francescoCarbone98");
        assignment = new Assignment(user2, activity2, "Mon", 12);
        assignmentSet.add(assignment);
        return assignmentSet;
    }

}
