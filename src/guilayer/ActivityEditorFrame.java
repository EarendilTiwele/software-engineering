/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guilayer;

import businesslogiclayer.Activity;
import businesslogiclayer.ActivityBLL;
import businesslogiclayer.PlannedActivity;
import businesslogiclayer.Procedure;
import businesslogiclayer.ProcedureBLL;
import businesslogiclayer.Site;
import businesslogiclayer.SiteBLL;
import businesslogiclayer.Typology;
import businesslogiclayer.TypologyBLL;
import java.awt.Cursor;
import java.util.List;
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
    private final ActivityBLL activityBLL = new ActivityBLL();

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
                    updateActivity(this.activity);
                    SwingUtilities.invokeLater(() -> dispose());
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
        Runnable loader = (() -> {
            SiteBLL siteBLL = new SiteBLL();
            List<Site> listSites = siteBLL.getAll();
            TypologyBLL typologyBLL = new TypologyBLL();
            List<Typology> listTypologies = typologyBLL.getAll();
            ProcedureBLL procedureBLL = new ProcedureBLL();
            List<Procedure> listProcedure = procedureBLL.getAll();

            SwingUtilities.invokeLater(() -> {
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
            int id = new Integer(idTextField.getText());
            Site site = (Site) siteComboBox.getSelectedItem();
            Typology typology = (Typology) typologyComboBox.getSelectedItem();
            String description = descriptionTextField.getText();
            int interventionTime = new Integer(interventionTimeTextField.getText());
            boolean interruptible = interruptibleCheckBox.isSelected();
            int week = new Integer(weekTextField.getText());
            Procedure procedure = (Procedure) procedureComboBox.getSelectedItem();
            String workspaceNotes = workspaceNotesArea.getText();

            Activity newPlannedActivity = new PlannedActivity(id, site, typology,
                    description, interventionTime, interruptible, week,
                    procedure, workspaceNotes);

            new Thread(() -> saveActivity(newPlannedActivity)).start();

        } catch (NumberFormatException e) {
            showErrorMessage("Error in specified parameters");
        } catch (IllegalArgumentException i) {
            showErrorMessage("Error in week parameters"); // può accadere solo per week in questo caso
        }

    }

    private void saveActivity(Activity activity) {
        // cath exception if necessary 
        activityBLL.insert(activity);

    }

    private void updateActivity(Activity activity) {
        activity.setWorkspaceNotes(workspaceNotesArea.getText());
        activityBLL.update(activity);
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
        idTextField.setText(cleanString);
        descriptionTextField.setText(cleanString);
        weekTextField.setText(cleanString);
        interruptibleCheckBox.setSelected(resetCheckBox);
        interventionTimeTextField.setText(cleanString);
        workspaceNotesArea.setText(cleanString);

    }

    private void initUpdateGUI() {
        idTextField.setText(activity.getId() + "");
        siteComboBox.addItem(activity.getSite());
        typologyComboBox.addItem(activity.getTipology());
        descriptionTextField.setText(activity.getDescription());
        interventionTimeTextField.setText(activity.getInterventionTime() + "");
        interruptibleCheckBox.setSelected(activity.isInterruptible());
        weekTextField.setText(activity.getWeek() + "");
        //da aggiungere materiali
        procedureComboBox.addItem(activity.getProcedure());
        workspaceNotesArea.setText(activity.getWorkspaceNotes());
        System.out.println("WorkspaceNotes-------------->"+activity.getWorkspaceNotes());
        JComponent[] components = new JComponent[]{idTextField, siteComboBox,
            typologyComboBox, descriptionTextField, interventionTimeTextField,
            interruptibleCheckBox, weekTextField, procedureComboBox, materialsTable};
        disableComponents(components);

    }

    private void disableComponents(JComponent[] components) {
        for (JComponent component : components) {
            component.setEnabled(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        idLabel = new javax.swing.JLabel();
        siteLabel = new javax.swing.JLabel();
        typologyLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        interventionTimeLabel = new javax.swing.JLabel();
        interruptibleLabel = new javax.swing.JLabel();
        materialsLabel = new javax.swing.JLabel();
        weekLabel = new javax.swing.JLabel();
        workspaceNotesLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
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

        idLabel.setText("ID");

        siteLabel.setText("Site");

        typologyLabel.setText("Typology");

        descriptionLabel.setText("Description");

        interventionTimeLabel.setText("Intervention time");

        interruptibleLabel.setText("Interruptible");

        materialsLabel.setText("Materials");

        weekLabel.setText("Week");

        workspaceNotesLabel.setText("Workspace notes");

        idTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idTextFieldActionPerformed(evt);
            }
        });

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(idLabel)
                                        .addGap(154, 154, 154)
                                        .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(siteLabel)
                                            .addComponent(typologyLabel)
                                            .addComponent(descriptionLabel)
                                            .addComponent(interventionTimeLabel)
                                            .addComponent(interruptibleLabel)
                                            .addComponent(weekLabel))
                                        .addGap(82, 82, 82)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(weekTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(interruptibleCheckBox)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(siteComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(typologyComboBox, 0, 205, Short.MAX_VALUE)
                                                .addComponent(descriptionTextField)
                                                .addComponent(interventionTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(procedureComboBox, 0, 205, Short.MAX_VALUE))))
                                .addGap(50, 50, 50))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(procedureLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(workspaceNotesLabel)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(materialsLabel)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(idLabel)
                            .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(materialsLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(siteLabel))
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
                            .addComponent(interruptibleCheckBox)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(workspaceNotesLabel)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(weekLabel)
                            .addComponent(weekTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(procedureLabel)
                            .addComponent(procedureComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(okButton)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void idTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idTextFieldActionPerformed

    private void descriptionTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descriptionTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_descriptionTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextField descriptionTextField;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
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
