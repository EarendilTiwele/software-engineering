/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guilayer;

import datatransferobjects.Activity;
import businesslogiclayer.ActivityBO;
import datatransferobjects.PlannedActivity;
import datatransferobjects.Procedure;
import datatransferobjects.Site;
import datatransferobjects.Typology;
import java.awt.Component;
import java.awt.Container;
import java.sql.SQLException;
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
    
    // Activity BLL to retrieve scheduled activities
    private ActivityBO activityBLL;

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
        activityBLL = new ActivityBO();
        
    }

    @After
    public void tearDown() {
    }


    //Load the whole list of activities scheduled for specificied week
    private List<Activity> loadAllActivitiesOfWeek(int week) throws SQLException {
        return activityBLL.getAllOfWeek(week);
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
            
            //System.out.println((String) weekComboBox.getSelectedItem()+week);
            List<Activity> filteredActivities = loadAllActivitiesOfWeek(week);
            
            // Time required to wait to be able to perform a query
            Thread.sleep(1000);
            
            //change the current week (simulate user's actions)
            SwingUtilities.invokeAndWait(()
                    -> weekComboBox.setSelectedIndex(week-1)
            );
            
            // Time need to update the GUI
            Thread.sleep(500);
           
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
