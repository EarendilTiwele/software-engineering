/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guilayer;

import datatransferobjects.Activity;
import businesslogiclayer.ActivityBO;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
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

    private ActivityBO activityBO = new ActivityBO();

    // columns headers (the last two element in tableColumnNames is used for button column)
    private static final String ID_COLUMN_NAME = "ID";
    private static final String AREA_COLUMN_NAME = "AREA";
    private static final String TYPE_COLUMN_NAME = "TYPE";
    private static final String TIME_COLUMN_NAME = "Estimated intervention time[min]";
    private static final String SELECT_COLUMN_NAME = "";
    private static final String DELETE_COLUMN_NAME = "";

    private final String[] tableColumnNames = new String[]{
        ID_COLUMN_NAME,
        AREA_COLUMN_NAME, TYPE_COLUMN_NAME,
        TIME_COLUMN_NAME, SELECT_COLUMN_NAME, DELETE_COLUMN_NAME
    };

    //text of the buttons
    private static final String SELECT_BUTTON_TEXT = "Select";
    private static final String DELETE_BUTTON_TEXT = "Delete";

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
        initScheduledActivitiesTableMouseListener();
    }

    /**
     * Adds the mouse listener to the ID column in order to update the activity
     * corresponding to selected ID.
     */
    private void initScheduledActivitiesTableMouseListener() {
        scheduledActivitiesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = scheduledActivitiesTable.rowAtPoint(evt.getPoint());
                int col = scheduledActivitiesTable.columnAtPoint(evt.getPoint());
                int idCol = 0;
                if (row >= 0 && col == idCol) {
                    Activity activity = activities.get(row);
                    JFrame activityEditorFrame = new ActivityEditorFrame(activity);
                    activityEditorFrame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent windowsEvent) {
                            setUpTable(activity.getWeek());
                        }
                    });

                    activityEditorFrame.setVisible(true);

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
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Runnable loader = () -> {
            // Load all the scheduled activities for the week
            activities = loadAllActivitiesOfWeek(week);
            SwingUtilities.invokeLater(() -> {
                if (activities == null) {
                    //Error while loading activities
                    loadingActivityError();

                } else {
                    updateTable();
                }
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
        weekComboBox.addActionListener(
                (e) -> setUpTable(
                        Integer.valueOf(
                                (String) weekComboBox.getSelectedItem()
                        )));
        weekComboBox.setSelectedIndex(week - 1);

    }

    /**
     * Convert a list of activities to a matrix of Object.
     *
     * @param activities list of activities to be converted
     * @param selectButtonText text displays on the select button
     * @param deleteButtonText text displays on the delete button
     * @return the matrix of Object
     */
    private Object[][] convertToObjectMatrix(List<Activity> activities,
            String selectButtonText, String deleteButtonText) {
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
            matrix[i][columnsAsList.indexOf(SELECT_COLUMN_NAME)]
                    = selectButtonText;
            matrix[i][columnsAsList.lastIndexOf(DELETE_COLUMN_NAME)]
                    = deleteButtonText;

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
        Object[][] data = convertToObjectMatrix(activities, SELECT_BUTTON_TEXT, DELETE_BUTTON_TEXT);

        TableModel model = new CustomTableModel(tableColumnNames, data) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                //this is a non-editable table
                return columnIndex >= getColumnCount() - 2;
            }
        };
        scheduledActivitiesTable.setModel(model);

        int selectedButtonColumn = model.getColumnCount() - 2;
        int deleteButtonColumn = model.getColumnCount() - 1;

        setColumnButton(selectedButtonColumn, e -> showVerificationScreen());
        setColumnButton(deleteButtonColumn, e -> deleteActivity());

    }

    /**
     * Show the verification screen of the activity selected in the table.
     */
    private void showVerificationScreen() {
        int row = scheduledActivitiesTable.getSelectedRow();
        Activity activityToAssign = activities.get(row);
        JFrame verificationScreenFrame = new VerificationScreenFrame(activityToAssign);
        verificationScreenFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowsEvent) {
                setUpTable(activityToAssign.getWeek());
            }
        });
        verificationScreenFrame.setVisible(true);
    }

    /**
     * Delete the activity chosen showing a confirm dialog
     */
    private void deleteActivity() {
        int row = scheduledActivitiesTable.getSelectedRow();
        Activity activityToDelete = activities.get(row);
        int week = activityToDelete.getWeek();
        int id = activityToDelete.getId();

        String msg = "Are you sure you wanto to delete the activity with id: "
                + id;
        String title = "Delete activity: " + id;
        int selection = JOptionPane.showConfirmDialog(this, msg, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (selection == JOptionPane.YES_OPTION) {
            //start thread to delete activity
            Runnable updater = () -> {
                boolean success = activityBO.delete(activityToDelete.getId());

                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        deleteActivitySuccess(id);
                        setUpTable(week);
                    } else {
                        deleteActivityError(id);
                    }
                });
            };
            new Thread(updater).start();
        }
    }

    /**
     * Adds a button to specified column of the scheduledActivitiesTable and
     * adds to it a specified action listener.
     *
     * @param column the column of the table
     * @param actionListener the action listener
     */
    private void setColumnButton(int column, ActionListener actionListener) {
        // Set the button renderer for the last column of the table
        scheduledActivitiesTable.getColumnModel()
                .getColumn(column)
                .setCellRenderer(new ButtonRenderer());
        // Set the button editor for the last column of the table to react to users click
        scheduledActivitiesTable.getColumnModel()
                .getColumn(column)
                .setCellEditor(new ButtonEditor(actionListener));
    }

    /**
     * Load the whole list of activities scheduled for specificied week.
     *
     * @param week the week of the scheduled activities needed to load
     * @return the list of scheduled activities
     */
    private List<Activity> loadAllActivitiesOfWeek(int week) {
        return activityBO.getAllOfWeek(week);
    }

    /*-------------------------------User Messages----------------------------*/
    private void deleteActivityError(int id) {
        String errorMessage = "Error deleting activity with id: " + id;
        String errorTitle = "Deletion error";
        JOptionPane.showMessageDialog(this, errorMessage, errorTitle,
                JOptionPane.ERROR_MESSAGE);
    }

    private void deleteActivitySuccess(int id) {
        String infoMessage = "Activity id " + id + " correctly deleted";
        String infoTitle = "Info message";
        JOptionPane.showMessageDialog(this, infoMessage, infoTitle,
                JOptionPane.INFORMATION_MESSAGE);

    }

    private void loadingActivityError() {
        String msg = "Error while loading activities";
        String title = "Loading error";
        JOptionPane.showMessageDialog(this, msg, title,
                JOptionPane.ERROR_MESSAGE);
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

            },
            new String [] {
                "ID", "Area", "Type", "Estimanted Intervention Time"
            }
        ));
        scheduledActivitiesTable.setCellSelectionEnabled(true);
        jScrollPane2.setViewportView(scheduledActivitiesTable);
        scheduledActivitiesTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

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
                        .addComponent(weekComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(weekLabel)
                        .addComponent(createButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(weekComboBox)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addGap(180, 180, 180))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        String week = (String) weekComboBox.getSelectedItem();
        int currentWeek = Integer.valueOf(week);
        JFrame activityEditorFrame = new ActivityEditorFrame(null);
        activityEditorFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowsEvent) {
                setUpTable(currentWeek);
            }
        });
        activityEditorFrame.setVisible(true);
        //new ActivityEditorFrame(null).setVisible(true);
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
