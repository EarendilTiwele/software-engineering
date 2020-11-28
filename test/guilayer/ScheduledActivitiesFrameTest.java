/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guilayer;

import businesslogiclayer.Activity;
import businesslogiclayer.PlannedActivity;
import businesslogiclayer.Procedure;
import businesslogiclayer.Site;
import businesslogiclayer.Typology;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carbo
 */
public class ScheduledActivitiesFrameTest {

    //GUI components references
    private ScheduledActivitiesFrame frame = null;
    private JComboBox<String> weekComboBox = null;
    private JTable table = null;

    //scheduled activities
    private List<Activity> activities = null;

    //GUI table indices
    private static final int ID_COLUMN = 0;
    private static final int SITE_COLUMN = 1;
    private static final int TYPOLOGY_COLUMN = 2;
    private static final int INTERVENTION_TIME_COLUMN = 3;

    //GUI components names
    private static final String SCHEDULED_TABLE_NAME = "Activities table";
    private static final String WEEK_COMBO_NAME = "Week combo box";

    /**
     * Retrieves the component whose parent is <code>parent</code> named
     * <code>name</code>.
     *
     * @param parent the partent component
     * @param name the name of the component to search for
     * @return the component
     */
    private Component getChildNamed(Component parent, String name) {
        if (name.equals(parent.getName())) {
            return parent;
        }
        if (parent instanceof Container) {
            Component[] children = null;
            if (parent instanceof JMenu) {
                children = ((JMenu) parent).getMenuComponents();
            } else {
                children = ((Container) parent).getComponents();
            }

            for (int i = 0; i < children.length; i++) {
                Component child = getChildNamed(children[i], name);
                if (child != null) {
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * Create the main frame and load all the scheduled activities.
     */
    @Before
    public void setUp() throws Exception {
        java.awt.EventQueue.invokeAndWait(() -> {
            frame = new ScheduledActivitiesFrame();
            frame.setVisible(true);
        });
        //search for GUI components
        table = (JTable) getChildNamed(frame, SCHEDULED_TABLE_NAME);
        weekComboBox = (JComboBox<String>) getChildNamed(frame, WEEK_COMBO_NAME);

        /*
        activities.sort((activity1, activity2)
                -> activity1.getId() - activity2.getId()); // sort by ID in ascending order
         */
    }

    @After
    public void tearDown() {
    }

    /*
    //Load all the scheduled activities.
    private List<Activity> loadAllActivitiesOfWeek() {
        //to be changed to load data from database...
        List<Activity> activitiesList = new ArrayList<>();

        PlannedActivity plannedActivity1 = new PlannedActivity(1,
                new Site("factory1", "area1"), new Typology("typology1"), "description1",
                1001, false, 1, new Procedure("procedure1", "procedure1.pdf"));
        PlannedActivity plannedActivity2 = new PlannedActivity(2,
                new Site("factory2", "area2"), new Typology("typology2"), "description2",
                1002, false, 1, new Procedure("procedure2", "procedure2.pdf"));
        PlannedActivity plannedActivity3 = new PlannedActivity(3,
                new Site("factory3", "area3"), new Typology("typology3"), "description3",
                1003, true, 1, new Procedure("procedure3", "procedure3.pdf"), "workspaceNote3");

        activitiesList.add(plannedActivity1);
        activitiesList.add(plannedActivity2);
        activitiesList.add(plannedActivity3);

        return activitiesList;
    }
     */

    //Load the whole list of activities scheduled for specificied week
    private List<Activity> loadAllActivitiesOfWeek(int week) {
        List<Activity> activitiesList = new ArrayList<>();
        PlannedActivity plannedActivity = null;

        switch (week) {
            case 1:
                plannedActivity = new PlannedActivity(1,
                        new Site("factory1", "area1"),
                        new Typology("typology1"), "description1", 1001, false, 1,
                        new Procedure("procedure1", "procedure1.pdf"));
                break;
            case 2:
                plannedActivity = new PlannedActivity(2,
                        new Site("factory2", "area2"), new Typology("typology2"),
                        "description2", 1002, false, 2,
                        new Procedure("procedure2", "procedure2.pdf"));
                break;
            case 3:
                plannedActivity = new PlannedActivity(3,
                        new Site("factory3", "area3"), new Typology("typology3"),
                        "description3", 1003, true, 3,
                        new Procedure("procedure3", "procedure3.pdf"), "workspaceNote3");
                break;
        }

        if (plannedActivity != null) {
            activitiesList.add(plannedActivity);
        }

        return activitiesList;
    }

    /**
     * Test of table updates. For every week: - get the scheduled activities for
     * that week sorted by ID - update the week combo box - assert all values
     * are correctly inserted in the table
     */
    @Test
    public void testMain() throws Exception {

        int numWeeks = 52;
        for (int _week = 1; _week <= numWeeks; _week++) {
            //get activities scheduled for this week
            int week = _week;
            /*
            List<Activity> filteredActivities = activities.stream()
                    .filter(activity -> activity.getWeek() == week)
                    .collect(Collectors.toList());
             */
            //List<Activity> filteredActivities=activities;
            
            //System.out.println((String) weekComboBox.getSelectedItem()+week);
            List<Activity> filteredActivities = loadAllActivitiesOfWeek(week);
            
            //change the current week (simulate user's actions)
            SwingUtilities.invokeAndWait(()
                    -> weekComboBox.setSelectedIndex(week-1)
            );
            
            // Time need to update the GUI
            Thread.sleep(100);
           
            //retrieve the new model of the JTable
            TableModel tableModel = table.getModel();

            //assert that every value is correctly inserted in the table
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int actualId = (int) tableModel.getValueAt(i, ID_COLUMN);
                String actualSite
                        = (String) tableModel.getValueAt(i, SITE_COLUMN);
                String actualTypology
                        = (String) tableModel.getValueAt(i, TYPOLOGY_COLUMN);
                int actualInterventionTime
                        = (int) tableModel.getValueAt(i, INTERVENTION_TIME_COLUMN);

                Activity expectedActivity = filteredActivities.get(i);
                int expectedId = expectedActivity.getId();
                String expectedSite = expectedActivity.getSite().toString();
                String expectedTypology = expectedActivity.getTipology().toString();
                int expectedInterventionTime = expectedActivity.getInterventionTime();
                
                assertEquals(expectedId, actualId);
                assertEquals(expectedSite, actualSite);
                assertEquals(expectedTypology, actualTypology);
                assertEquals(expectedInterventionTime, actualInterventionTime);

            }
        }

    }

}
