/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guilayer;

import businesslogiclayer.CompetencyBO;
import datatransferobjects.Competency;
import java.awt.Component;
import java.awt.Cursor;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

/**
 * Form to manage the competencies.
 *
 * @author Alfonso
 */
public class CompetenciesManagmentFrame extends javax.swing.JFrame {

    private List<Competency> competencies;

    private final CompetencyBO competencyBO = new CompetencyBO();

    // columns headers
    private static final String ID_COLUMN_NAME = "ID";
    private static final String DESCRIPTION_COLUMN_NAME = "Description";

    private final String[] tableColumnNames = new String[]{
        ID_COLUMN_NAME, DESCRIPTION_COLUMN_NAME
    };

    /**
     * Creates new form CompetenciesManagmentFrame
     */
    public CompetenciesManagmentFrame() {
        initComponents();
        initCompetencyManagmentTabbedPane();
        setUpTable();
        getContentPane().setBackground(mainPanel.getBackground());
    }

    /**
     * Defines the change listener that allow to initialize a specific view on
     * the selected tab of competencyManagmentTabbedPane.
     */
    private void initCompetencyManagmentTabbedPane() {
        competenciesManagmentTabbedPane.addChangeListener((e) -> {
            Component currentPanel = competenciesManagmentTabbedPane.getSelectedComponent();

            if (currentPanel.equals(updateCompetencyPanel)) {
                int row = competenciesTable.getSelectedRow();
                if (row == -1) {
                    competenciesManagmentTabbedPane.setSelectedIndex(0);
                    unselectedTableRowError();
                } else {
                    Competency competency = competencies.get(row);
                    moveComponents(insertCompentencyPanel, updateCompetencyPanel);
                    setUpdateCompetencyPanel(competency);

                    SwingUtilities.invokeLater(() -> setIDCompetencyComponenstVisible(true));
                }

            } else if (currentPanel.equals(insertCompentencyPanel)) {
                moveComponents(updateCompetencyPanel, insertCompentencyPanel);

                SwingUtilities.invokeLater(() -> setIDCompetencyComponenstVisible(false));

                cleanPanelComponent();
            } else if (currentPanel.equals(viewCompetenciesPanel)) {
                setUpTable();
            }
        });

    }

    /**
     * Sets the visibility of the label and the text field about the
     * competencyID.
     *
     * @param visible true to make the components visible; false to make them
     * invisible
     */
    private void setIDCompetencyComponenstVisible(boolean visible) {
        idCompetencyLabel.setVisible(visible);
        idCompetencyTextField.setVisible(visible);
    }

    /**
     * Move all components from specified source panel to specified destination
     * panel.
     *
     * @param sourcePanel the source panel
     * @param destPanel the destination panel
     */
    private void moveComponents(JPanel sourcePanel, JPanel destPanel) {
        for (Component component : sourcePanel.getComponents()) {
            destPanel.add(component);
        }
    }

    /**
     * Loads all system's competencies and updates the table showing them.
     * Loading is performed in a new thread without blocking the Event Dispatch
     * Thread.
     */
    private void setUpTable() {
        // Cursor indicates to user the wait needed to load activities from
        // the database and to update the table.
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        Runnable loader = () -> {
            competencies = loadAllCompetencies();
            SwingUtilities.invokeLater(() -> {
                if (competencies == null) {
                    loadingCompetenciesError();
                } else {
                    updateTable();
                }
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            });

        };

        new Thread(loader).start();
    }

    /**
     * Updates the table with the loaded system's competencies.
     */
    private void updateTable() {
        Object[][] data = convertToObjectMatrix(competencies);

        TableModel model = new CustomTableModel(tableColumnNames, data);

        competenciesTable.setModel(model);

    }

    /**
     * Converts a list of competencies to a matrix of Object.
     *
     * @param competencies the list of competencies
     * @return the matrix of Object
     */
    private Object[][] convertToObjectMatrix(List<Competency> competencies) {
        int numRow = competencies.size();
        int numCol = tableColumnNames.length;
        Object[][] data = new Object[numRow][numCol];
        int i = 0;
        for (Competency competency : competencies) {
            List<String> header = Arrays.asList(tableColumnNames);
            data[i][header.indexOf(ID_COLUMN_NAME)] = competency.getId();
            data[i][header.indexOf(DESCRIPTION_COLUMN_NAME)] = competency.getDescription();
            i++;
        }

        return data;

    }

    /**
     * Loads the whole list of system's competencies.
     *
     * @return the list of system's competencies
     */
    private List<Competency> loadAllCompetencies() {
        return competencyBO.getAll();
    }

    /**
     * Cleans the text field of panel.
     */
    private void cleanPanelComponent() {
        descriptionTextArea.setText("");
        idCompetencyTextField.setText("");
    }

    /**
     * Retrieves the information of specified competency and updates the
     * updateCompetencyPanel according them.
     *
     * @param competency the competency
     */
    private void setUpdateCompetencyPanel(Competency competency) {
        descriptionTextArea.setText(competency.getDescription());
        idCompetencyTextField.setText(String.valueOf(competency.getId()));
    }

    /**
     * Checks that the insert operation ,according with specified result, was
     * successful and notified that.
     *
     * @param result the result of insert operation
     */
    private void checkInsertCompetency(int result) {
        if (result == -1) {
            competencyInsertError();
        } else {
            competencyInsertSuccess();
            cleanPanelComponent();
        }

    }

    /**
     * Checks that the update operation ,according with specified result, was
     * successful and notified that specifying the competency's id.
     *
     * @param result the result of update operation
     * @param competencyID the competency's id
     */
    private void checkUpdateCompetency(boolean success, int competencyID) {
        if (!success) {
            competencyUpdateError(competencyID);
        } else {
            competencyUpdateSuccess(competencyID);
            cleanPanelComponent();
        }

    }

    /*-----------------------------Users Messages-----------------------------*/
    private void competencyUpdateError(int competencyID) {
        String msg = "Error while updating competency with id: " + competencyID;
        String title = "Update error";
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    private void competencyUpdateSuccess(int competencyID) {
        String msg = "Succesfully updated the competency with id: " + competencyID;
        String title = "Success";
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void competencyInsertError() {
        String msg = "Error while saving competency";
        String title = "Update error";
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    private void competencyInsertSuccess() {
        String msg = "Succesfully save the competency";
        String title = "Success";
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadingCompetenciesError() {
        String msg = "Error while loading competencies";
        String title = "Loading error";
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    private void unselectedTableRowError() {
        String msg = "You must select a row of table to update a competency";
        String title = "Competency selection error";
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        systemAdministratorTabbedPane = new javax.swing.JTabbedPane();
        competenciesManagmentTabbedPane = new javax.swing.JTabbedPane();
        viewCompetenciesPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        competenciesTable = new javax.swing.JTable();
        insertCompentencyPanel = new javax.swing.JPanel();
        descriptionLabel = new javax.swing.JLabel();
        saveCompetencyButton = new javax.swing.JButton();
        idCompetencyLabel = new javax.swing.JLabel();
        idCompetencyTextField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        updateCompetencyPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 102, 51));

        competenciesManagmentTabbedPane.setBackground(new java.awt.Color(0, 0, 153));
        competenciesManagmentTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        competenciesManagmentTabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        viewCompetenciesPanel.setBackground(new java.awt.Color(255, 255, 255));

        competenciesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(competenciesTable);

        javax.swing.GroupLayout viewCompetenciesPanelLayout = new javax.swing.GroupLayout(viewCompetenciesPanel);
        viewCompetenciesPanel.setLayout(viewCompetenciesPanelLayout);
        viewCompetenciesPanelLayout.setHorizontalGroup(
            viewCompetenciesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 478, Short.MAX_VALUE)
            .addGroup(viewCompetenciesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
        );
        viewCompetenciesPanelLayout.setVerticalGroup(
            viewCompetenciesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 267, Short.MAX_VALUE)
            .addGroup(viewCompetenciesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
        );

        competenciesManagmentTabbedPane.addTab("Competencies", viewCompetenciesPanel);

        insertCompentencyPanel.setBackground(new java.awt.Color(255, 255, 255));

        descriptionLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        descriptionLabel.setText("Description");

        saveCompetencyButton.setText("Save");
        saveCompetencyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCompetencyButtonActionPerformed(evt);
            }
        });

        idCompetencyLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        idCompetencyLabel.setText("ID");

        idCompetencyTextField.setEditable(false);

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        jScrollPane3.setViewportView(descriptionTextArea);

        javax.swing.GroupLayout insertCompentencyPanelLayout = new javax.swing.GroupLayout(insertCompentencyPanel);
        insertCompentencyPanel.setLayout(insertCompentencyPanelLayout);
        insertCompentencyPanelLayout.setHorizontalGroup(
            insertCompentencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, insertCompentencyPanelLayout.createSequentialGroup()
                .addGroup(insertCompentencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(insertCompentencyPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saveCompetencyButton))
                    .addGroup(insertCompentencyPanelLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(insertCompentencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descriptionLabel)
                            .addComponent(idCompetencyLabel))
                        .addGap(80, 80, 80)
                        .addGroup(insertCompentencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idCompetencyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
        );
        insertCompentencyPanelLayout.setVerticalGroup(
            insertCompentencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(insertCompentencyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(insertCompentencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionLabel)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(insertCompentencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(idCompetencyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idCompetencyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(saveCompetencyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        competenciesManagmentTabbedPane.addTab("Insert Compentency", insertCompentencyPanel);

        updateCompetencyPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout updateCompetencyPanelLayout = new javax.swing.GroupLayout(updateCompetencyPanel);
        updateCompetencyPanel.setLayout(updateCompetencyPanelLayout);
        updateCompetencyPanelLayout.setHorizontalGroup(
            updateCompetencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 478, Short.MAX_VALUE)
        );
        updateCompetencyPanelLayout.setVerticalGroup(
            updateCompetencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 267, Short.MAX_VALUE)
        );

        competenciesManagmentTabbedPane.addTab("Update Competency", updateCompetencyPanel);

        systemAdministratorTabbedPane.addTab("Competencies managment", competenciesManagmentTabbedPane);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(systemAdministratorTabbedPane)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(systemAdministratorTabbedPane)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Saves the <code>Competency</code> object when the saveButton is pressed.
     *
     * @param evt the event to be processed
     */
    private void saveCompetencyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveCompetencyButtonActionPerformed
        Component currentPanel = competenciesManagmentTabbedPane.getSelectedComponent();

        String description = descriptionTextArea.getText();
        String idText = idCompetencyTextField.getText();
        //mentre non è pronta la factory fai così
        int id = idText.isEmpty() ? -1 : Integer.valueOf(idText);
        //create a factory
        Competency competency = new Competency(id, description);

        Runnable saver = () -> {
            if (currentPanel.equals(insertCompentencyPanel)) {
                int result = competencyBO.insert(competency);
                SwingUtilities.invokeLater(() -> checkInsertCompetency(result));

            } else if (currentPanel.equals(updateCompetencyPanel)) {
                boolean success = competencyBO.update(competency);
                SwingUtilities.invokeLater(() -> checkUpdateCompetency(success, competency.getId()));
            }

        };

        new Thread(saver).start();
    }//GEN-LAST:event_saveCompetencyButtonActionPerformed

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
            java.util.logging.Logger.getLogger(CompetenciesManagmentFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CompetenciesManagmentFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CompetenciesManagmentFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CompetenciesManagmentFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CompetenciesManagmentFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane competenciesManagmentTabbedPane;
    private javax.swing.JTable competenciesTable;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JLabel idCompetencyLabel;
    private javax.swing.JTextField idCompetencyTextField;
    private javax.swing.JPanel insertCompentencyPanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton saveCompetencyButton;
    private javax.swing.JTabbedPane systemAdministratorTabbedPane;
    private javax.swing.JPanel updateCompetencyPanel;
    private javax.swing.JPanel viewCompetenciesPanel;
    // End of variables declaration//GEN-END:variables
}
