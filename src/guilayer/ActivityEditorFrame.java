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
import businesslogiclayer.ProcedureBO;
import datatransferobjects.Site;
import businesslogiclayer.SiteBO;
import datatransferobjects.Typology;
import businesslogiclayer.TypologyBO;
import java.awt.Cursor;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Form to create a planned activity.
 *
 * @author Alfonso
 */
public class ActivityEditorFrame extends javax.swing.JFrame {

    private Activity activity;
    private final ActivityBO activityBO = new ActivityBO();

    /**
     * Creates new form ActivityEditorFrame
     *
     * @param activity
     */
    public ActivityEditorFrame(Activity activity) {
        initComponents();
        this.activity = activity;
        if (activity == null) {
            this.setTitle("Create activity");
            initCreateGUI();
            okButton.addActionListener((event) -> {
                createActivity();
                cleanFrame();

            });
        } else {
            this.setTitle("Update activity: " + activity.getId());
            initUpdateGUI();
            okButton.addActionListener((event) -> {
                Runnable saver = (() -> {
                    boolean success = updateActivity(this.activity);
                    SwingUtilities.invokeLater(() -> {
                        if (success) {
                            updateActivitySuccess();
                            dispose();
                        } else {
                            updateActivityError();
                        }
                    });
                });
                new Thread(saver).start();
            });
        }

    }

    /**
     * Adds a list of T object in a combobox.
     *
     * @param <T> the type of object
     * @param list the list of T object
     * @param comboBox the comboBox in which insert the list in input
     */
    private <T> void addListToComboBox(List<T> list, JComboBox<T> comboBox) {
        for (T object : list) {
            comboBox.addItem(object);
        }
    }

    /**
     * Loads all Typologies, Procedures and Sites and updates the respectives
     * comboBox. Loading is performed in a new thread without blocking the Event
     * Dispatch Thread.
     */
    private void initCreateGUI() {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        // Removed try-catch
        Runnable loader = (() -> {
            SiteBO siteBO = new SiteBO();
            List<Site> listSites = siteBO.getAll();
            TypologyBO typologyBO = new TypologyBO();
            List<Typology> listTypologies = typologyBO.getAll();
            ProcedureBO procedureBO = new ProcedureBO();
            List<Procedure> listProcedure = procedureBO.getAll();
            SwingUtilities.invokeLater(() -> {
                if (listSites == null || listProcedure == null
                        || listTypologies == null) {
                    dataLoadingError();
                }
                addListToComboBox(listSites, siteComboBox);
                addListToComboBox(listTypologies, typologyComboBox);
                addListToComboBox(listProcedure, procedureComboBox);
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            });
        });

        new Thread(loader).start();
    }

    /**
     * Store a new plannedActivity.
     */
    private void createActivity() {
        try {

            Site site = (Site) siteComboBox.getSelectedItem();
            Typology typology = (Typology) typologyComboBox.getSelectedItem();
            String description = descriptionTextField.getText();
            int interventionTime = new Integer(interventionTimeTextField.getText());
            boolean interruptible = interruptibleCheckBox.isSelected();
            int week = new Integer(weekTextField.getText());
            Procedure procedure = (Procedure) procedureComboBox.getSelectedItem();
            String workspaceNotes = workspaceNotesArea.getText();

            Activity newPlannedActivity = new PlannedActivity(site, typology,
                    description, interventionTime, interruptible, week,
                    procedure, workspaceNotes);

            Runnable saver = () -> {
                int newID = activityBO.insert(newPlannedActivity);
                SwingUtilities.invokeLater(() -> {
                    if (newID == -1) {
                        insertActivityError();
                    } else {
                        insertActivitySuccess(newID);
                    }
                });

            };
            new Thread(saver).start();

        } catch (NumberFormatException e) {
            showErrorMessage("Error in specified parameters");
        } catch (IllegalArgumentException i) {
            showErrorMessage("Error in week parameters"); // può accadere solo per week in questo caso
        }

    }

    private boolean updateActivity(Activity activity) {
        activity.setWorkspaceNotes(workspaceNotesArea.getText());
        return activityBO.update(activity);
    }

    /**
     * Pop up error message.
     *
     * @param msg the error message
     */
    private void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Clean frame's component.
     */
    private void cleanFrame() {
        String cleanString = "";
        boolean resetCheckBox = false;

        descriptionTextField.setText(cleanString);
        weekTextField.setText(cleanString);
        interruptibleCheckBox.setSelected(resetCheckBox);
        interventionTimeTextField.setText(cleanString);
        workspaceNotesArea.setText(cleanString);

    }

    private void initUpdateGUI() {

        siteComboBox.addItem(activity.getSite());
        typologyComboBox.addItem(activity.getTipology());
        descriptionTextField.setText(activity.getDescription());
        interventionTimeTextField.setText(activity.getInterventionTime() + "");
        interruptibleCheckBox.setSelected(activity.isInterruptible());
        weekTextField.setText(activity.getWeek() + "");
        //da aggiungere materiali
        procedureComboBox.addItem(activity.getProcedure());
        workspaceNotesArea.setText(activity.getWorkspaceNotes());
        JComponent[] components = new JComponent[]{siteComboBox,
            typologyComboBox, descriptionTextField, interventionTimeTextField,
            interruptibleCheckBox, weekTextField, procedureComboBox, materialsTable};
        disableComponents(components);

    }

    private void disableComponents(JComponent[] components) {
        for (JComponent component : components) {
            component.setEnabled(false);
        }
    }

    /*------------------------------User Messages-----------------------------*/
    private void dataLoadingError() {
        String msg = "Error while loading required data ";
        String title = "Loading error";
        JOptionPane.showMessageDialog(this, msg, title,
                JOptionPane.ERROR_MESSAGE);
    }

    private void insertActivityError() {
        String msg = "Error while creating the new activity ";
        String title = "Creation error";
        JOptionPane.showMessageDialog(this, msg, title,
                JOptionPane.ERROR_MESSAGE);
    }

    private void insertActivitySuccess(int id) {
        String msg = "Activity correctily created";
        String title = "Success";
        JOptionPane.showMessageDialog(this, msg, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateActivitySuccess() {
        String msg = "Activity correctily updated";
        String title = "Success";
        JOptionPane.showMessageDialog(this, msg, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateActivityError() {
        String msg = "Error while updating the new activity ";
        String title = "Update error";
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

        siteLabel = new javax.swing.JLabel();
        typologyLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        interventionTimeLabel = new javax.swing.JLabel();
        interruptibleLabel = new javax.swing.JLabel();
        materialsLabel = new javax.swing.JLabel();
        weekLabel = new javax.swing.JLabel();
        workspaceNotesLabel = new javax.swing.JLabel();
        siteComboBox = new javax.swing.JComboBox<>();
        typologyComboBox = new javax.swing.JComboBox<>();
        descriptionTextField = new javax.swing.JTextField();
        interventionTimeTextField = new javax.swing.JTextField();
        interruptibleCheckBox = new javax.swing.JCheckBox();
        weekTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        materialsTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        workspaceNotesArea = new javax.swing.JTextArea();
        okButton = new javax.swing.JButton();
        procedureLabel = new javax.swing.JLabel();
        procedureComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        siteLabel.setText("Site");

        typologyLabel.setText("Typology");

        descriptionLabel.setText("Description");

        interventionTimeLabel.setText("Intervention time");

        interruptibleLabel.setText("Interruptible");

        materialsLabel.setText("Materials");

        weekLabel.setText("Week");

        workspaceNotesLabel.setText("Workspace notes");

        siteComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new Site[] { }));

        typologyComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new Typology[] { }));

        descriptionTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descriptionTextFieldActionPerformed(evt);
            }
        });

        materialsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Material", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(materialsTable);
        if (materialsTable.getColumnModel().getColumnCount() > 0) {
            materialsTable.getColumnModel().getColumn(0).setResizable(false);
            materialsTable.getColumnModel().getColumn(1).setResizable(false);
        }

        workspaceNotesArea.setColumns(20);
        workspaceNotesArea.setRows(5);
        jScrollPane2.setViewportView(workspaceNotesArea);

        okButton.setText("OK");

        procedureLabel.setText("Procedure");

        procedureComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new Procedure[] { }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(okButton)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(procedureLabel)
                                .addGap(371, 371, 371))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(siteLabel)
                                    .addComponent(typologyLabel)
                                    .addComponent(descriptionLabel)
                                    .addComponent(interventionTimeLabel)
                                    .addComponent(interruptibleLabel)
                                    .addComponent(weekLabel))
                                .addGap(82, 82, 82)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(procedureComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(weekTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(interruptibleCheckBox)
                                            .addComponent(typologyComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(descriptionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                                            .addComponent(interventionTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(siteComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(50, 50, 50)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(workspaceNotesLabel)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(materialsLabel)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(materialsLabel)
                                .addGap(187, 187, 187))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addComponent(workspaceNotesLabel)
                        .addGap(11, 11, 11)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(okButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(siteLabel)
                            .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(typologyLabel)
                            .addComponent(typologyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(descriptionLabel)
                            .addComponent(descriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(interventionTimeLabel)
                            .addComponent(interventionTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(interruptibleLabel)
                            .addComponent(interruptibleCheckBox))
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(weekLabel)
                            .addComponent(weekTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(procedureLabel)
                            .addComponent(procedureComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void descriptionTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descriptionTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_descriptionTextFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextField descriptionTextField;
    private javax.swing.JCheckBox interruptibleCheckBox;
    private javax.swing.JLabel interruptibleLabel;
    private javax.swing.JLabel interventionTimeLabel;
    private javax.swing.JTextField interventionTimeTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel materialsLabel;
    private javax.swing.JTable materialsTable;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox<Procedure> procedureComboBox;
    private javax.swing.JLabel procedureLabel;
    private javax.swing.JComboBox<Site> siteComboBox;
    private javax.swing.JLabel siteLabel;
    private javax.swing.JComboBox<Typology> typologyComboBox;
    private javax.swing.JLabel typologyLabel;
    private javax.swing.JLabel weekLabel;
    private javax.swing.JTextField weekTextField;
    private javax.swing.JTextArea workspaceNotesArea;
    private javax.swing.JLabel workspaceNotesLabel;
    // End of variables declaration//GEN-END:variables
}
