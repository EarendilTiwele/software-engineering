/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guilayer;

import businesslogiclayer.Activity;
import businesslogiclayer.ActivityBLL;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
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
        initializeWeekComboBox(FIRST_WEEK);
        scheduledActivitiesTable.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        int row = scheduledActivitiesTable.rowAtPoint(evt.getPoint());
        int col = scheduledActivitiesTable.columnAtPoint(evt.getPoint());
        int idCol = 0;
        if (row >= 0 && col == idCol) {
            Activity activity = activities.get(row);
            new ActivityEditorFrame(activity).setVisible(true);
            
        }
    }
});
    }

    /**
     * Sets an appropriate name to the main components (for test purpose only).
     */
    private void setComponentsNames() {
        scheduledActivitiesTable.setName(SCHEDULED_TABLE_NAME);
        weekComboBox.setName(WEEK_COMBO_NAME);
    }

    /**
     * Load the whole scheduled activities of the week and update the table 
     * showing the activities. Loading is performed in a new thread without 
     * blocking the Event Dispatch Thread.
     * 
     * @param week the week of interest
     */
    private void setUpTable(int week) {
        // Cursor indicates to user the wait needed to load activities from 
        // the database and to update the table.
        this.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        Runnable loader = () -> {
            // Load all the scheduled activities for the week
            activities = loadAllActivitiesOfWeek(week);
            
            SwingUtilities.invokeLater(() -> {
                updateTable();
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
        
        // Specify the listener to update the content of the table when a
        // different week is selected from the weekComboBox
        /*
        weekComboBox.addActionListener(
                event -> updateTable(
                        Integer.valueOf(
                                (String) weekComboBox.getSelectedItem()
                        )));
        */
        
        weekComboBox.addActionListener(
                                        (e) -> setUpTable(
                                               Integer.valueOf(
                                              (String)weekComboBox.getSelectedItem()
                                               )));
        weekComboBox.setSelectedIndex(week - 1);
        
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
    private void updateTable() {
        // Convert the filterderActivities to a matrix suitable for CustomTableModel
        Object[][] data = convertToObjectMatrix(activities, BUTTON_TEXT);

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

    /**
     * Load the whole list of activities scheduled for specificied week.
     * 
     * @param week the week of the scheduled activities needed to load
     * @return 
     */
    private List<Activity> loadAllActivitiesOfWeek(int week){
        ActivityBLL activityBLL = new ActivityBLL();
        return activityBLL.getAllOfWeek(week);
        
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
        createButton = new javax.swing.JButton();

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

        createButton.setText("+");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
                        .addComponent(createButton))
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
                    .addComponent(weekComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addGap(180, 180, 180))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        new ActivityEditorFrame(null).setVisible(true);
    }//GEN-LAST:event_createButtonActionPerformed

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
    private javax.swing.JButton createButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable scheduledActivitiesTable;
    private javax.swing.JComboBox<String> weekComboBox;
    private javax.swing.JLabel weekLabel;
    // End of variables declaration//GEN-END:variables
}
