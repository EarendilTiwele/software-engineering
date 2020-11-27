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
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

/**
 * Form to view the whole scheduled activities ordered by week.
 *
 * @author Alfonso and carbo
 */
public class ScheduledActivitiesFrame extends javax.swing.JFrame {

    //list of scheduled activities
    private List<Activity> activities = null;

    // columns headers (the last element in tableColumnNames is used for button column)
    private static final String ID_COLUMN_NAME = "ID";
    private static final String AREA_COLUMN_NAME = "AREA";
    private static final String TYPE_COLUMN_NAME = "TYPE";
    private static final String TIME_COLUMN_NAME = "Estimated intervention time[min]";
    private static final String BUTTON_COLUMN_NAME = "";

    private final String[] tableColumnNames = new String[]{
        ID_COLUMN_NAME,
        AREA_COLUMN_NAME, TYPE_COLUMN_NAME,
        TIME_COLUMN_NAME, BUTTON_COLUMN_NAME
    };

    //text of the buttons
    private static final String BUTTON_TEXT = "Select";

    // First week that the user view in this form
    private static final int FIRST_WEEK = 1;

    //names of main components (for test purpose)
    private static final String SCHEDULED_TABLE_NAME = "Activities table";
    private static final String WEEK_COMBO_NAME = "Week combo box";

    /**
     * Creates new form ScheduledActivitiesFrame
     */
    public ScheduledActivitiesFrame() {
        initComponents();
        setComponentsNames();
        this.setTitle("Scheduled activities");
        setUp();

    }

    /**
     * Sets an appropriate name to the main components (for test purpose only).
     */
    private void setComponentsNames() {
        scheduledActivitiesTable.setName(SCHEDULED_TABLE_NAME);
        weekComboBox.setName(WEEK_COMBO_NAME);
    }

    /**
     * Load all scheduled activities and update the table showing the activities
     * for the first week. Loading is performed in a new thread without blocking
     * the Event Dispatch Thread.
     *
     */
    private void setUp() {
        Runnable loader = () -> {
            // Load all the scheduled activities
            activities = loadAllActivities();
            // sort by ID in ascending order
            activities.sort((activity1, activity2)
                    -> activity1.getId() - activity2.getId());

            SwingUtilities.invokeLater(() -> {
                initializeWeekComboBox(FIRST_WEEK);
                updateTable(FIRST_WEEK);
            });
        };
        new Thread(loader).start();

    }

    /**
     * Initialize the weekComboBox to the specified week and set a Listener to
     * update the table.
     *
     * @param week the initial week of the combo box
     */
    private void initializeWeekComboBox(int week) {
        weekComboBox.setSelectedIndex(week - 1);
        // Specify the listener to update the content of the table when a
        // different week is selected from the weekComboBox
        weekComboBox.addActionListener(
                event -> updateTable(
                        Integer.valueOf(
                                (String) weekComboBox.getSelectedItem()
                        )));
    }

    /**
     * Convert a list of activities to a matrix of Object.
     *
     * @param activities list of activities to be converted
     * @param buttonText text displays on the button
     * @return the matrix of Object
     */
    private Object[][] convertToObjectMatrix(List<Activity> activities, String buttonText) {
        int numRow = activities.size();
        int numCol = tableColumnNames.length;
        List<String> columnsAsList = Arrays.asList(tableColumnNames);
        Object[][] matrix = new Object[numRow][numCol];

        //create the matrix
        int i = 0;
        for (Activity activity : activities) {
            matrix[i][columnsAsList.indexOf(ID_COLUMN_NAME)]
                    = activity.getId();
            matrix[i][columnsAsList.indexOf(AREA_COLUMN_NAME)]
                    = activity.getSite().toString();
            matrix[i][columnsAsList.indexOf(TYPE_COLUMN_NAME)]
                    = activity.getTipology().toString();
            matrix[i][columnsAsList.indexOf(TIME_COLUMN_NAME)]
                    = activity.getInterventionTime();
            matrix[i][columnsAsList.indexOf(BUTTON_COLUMN_NAME)]
                    = buttonText;

            i++;
        }

        return matrix;
    }

    /**
     * Updates the table with the activities scheduled for the specified week.
     *
     * @param week the week selected by the user
     */
    private void updateTable(int week) {
        // List of the activities scheduled for the specifed week
        List<Activity> filteredActivities = activities.stream()
                .filter(activity -> activity.getWeek() == week)
                .collect(Collectors.toList());

        // Convert the filterderActivities to a matrix suitable for CustomTableModel
        Object[][] data = convertToObjectMatrix(filteredActivities, BUTTON_TEXT);

        TableModel model = new CustomTableModel(tableColumnNames, data);
        scheduledActivitiesTable.setModel(model);

        // Set the button renderer for the last column of the table
        scheduledActivitiesTable.getColumnModel()
                .getColumn(model.getColumnCount() - 1)
                .setCellRenderer(new ButtonRenderer());

        // Simple ActionListener for testing the button
        ActionListener popupActionListener = e -> {
            String msg = "clicked";
            JOptionPane.showMessageDialog(rootPane, msg,
                    "title", JOptionPane.INFORMATION_MESSAGE);
        };

        // Set the button editor for the last column of the table to react to users click
        scheduledActivitiesTable.getColumnModel()
                .getColumn(model.getColumnCount() - 1)
                .setCellEditor(new ButtonEditor(popupActionListener));
    }

    //Load all the scheduled activities.
    private List<Activity> loadAllActivities() {
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        weekLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        scheduledActivitiesTable = new javax.swing.JTable();
        weekComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        weekLabel.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        weekLabel.setText("Week n°");

        scheduledActivitiesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scheduledActivitiesTable.setCellSelectionEnabled(true);
        jScrollPane2.setViewportView(scheduledActivitiesTable);

        String[] weeks = new String[52];
        for (int i=0; i<weeks.length;i++){
            weeks[i]=(i+1)+"";
        }
        weekComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(weeks));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(weekLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weekComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 249, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weekLabel)
                    .addComponent(weekComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addGap(180, 180, 180))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScheduledActivitiesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScheduledActivitiesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScheduledActivitiesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScheduledActivitiesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScheduledActivitiesFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable scheduledActivitiesTable;
    private javax.swing.JComboBox<String> weekComboBox;
    private javax.swing.JLabel weekLabel;
    // End of variables declaration//GEN-END:variables
}
