/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guilayer;

import businesslogiclayer.UserBO;
import datatransferobjects.Maintainer;
import datatransferobjects.Planner;
import datatransferobjects.User;
import java.awt.Component;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Alfonso
 */
public class SystemAdministratorFrame extends javax.swing.JFrame {

    private List<User> users;

    private UserBO userBO = new UserBO();

    // columns headers
    private static final String ID_COLUMN_NAME = "ID";
    private static final String USERNAME_COLUMN_NAME = "Username";
    private static final String ROLE_COLUMN_NAME = "Role";

    private static final String MAINTAINER = "Maintainer";
    private static final String PLANNER = "Planner";

    private final String[] tableColumnNames = new String[]{
        ID_COLUMN_NAME,
        USERNAME_COLUMN_NAME, ROLE_COLUMN_NAME
    };

    /**
     * Creates new form SystemAdministratorFrame
     */
    public SystemAdministratorFrame() {
        initComponents();
        initSystemAdministratorTabbedPane();
        setUpTable();
        getContentPane().setBackground(mainPanel.getBackground());

    }

    private void initSystemAdministratorTabbedPane() {
        systemAdministratorTabbedPane.addChangeListener((e) -> {
            Component currentPanel = systemAdministratorTabbedPane.getSelectedComponent();

            if (currentPanel.equals(updateUserPanel)) {
                int row = usersTable.getSelectedRow();
                if (row == -1) {
                    systemAdministratorTabbedPane.setSelectedIndex(0);
                    unselectedTableRowError();
                } else {
                    User user = users.get(row);
                    idLabel.setVisible(true);
                    idTextField.setVisible(true);
                    moveComponents(insertUserPanel, updateUserPanel);
                    setUpdatePanel(user);

                }

            } else if (currentPanel.equals(insertUserPanel)) {
                idLabel.setVisible(false);
                idTextField.setVisible(false);
                moveComponents(updateUserPanel, insertUserPanel);
                cleanPanelComponent();
            } else if (currentPanel.equals(viewUsersPanel)) {
                setUpTable();
            }
        });

    }

    private void moveComponents(JPanel sourcePanel, JPanel destPanel) {
        for (Component component : sourcePanel.getComponents()) {
            destPanel.add(component);
        }
    }

    /**
     * Loads all system's user and updates the table showing them. Loading is
     * performed in a new thread without blocking the Event Dispatch Thread.
     */
    private void setUpTable() {
        // Cursor indicates to user the wait needed to load activities from
        // the database and to update the table.
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        Runnable loader = () -> {
            users = loadAllUsers();
            SwingUtilities.invokeLater(() -> {
                if (users == null) {
                    loadingUsersError();
                } else {
                    updateTable();
                }
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            });

        };

        new Thread(loader).start();
    }

    /**
     * Loads the whole list of system's user.
     *
     * @return the list of system's user
     */
    private List<User> loadAllUsers() {
        return userBO.getAll();
    }

    /**
     * Updates the table with the loaded system's user.
     */
    private void updateTable() {
        Object[][] data = convertToObjectMatrix(users);

        TableModel model = new CustomTableModel(tableColumnNames, data);

        usersTable.setModel(model);

    }

    /**
     * Converts a list of users to a matrix of Object.
     *
     * @param users the list of users
     * @return the matrix of Object
     */
    private Object[][] convertToObjectMatrix(List<User> users) {
        int numRow = users.size();
        int numCol = tableColumnNames.length;
        Object[][] data = new Object[numRow][numCol];
        int i = 0;
        for (User user : users) {
            List<String> header = Arrays.asList(tableColumnNames);
            data[i][header.indexOf(ID_COLUMN_NAME)] = user.getId();
            data[i][header.indexOf(USERNAME_COLUMN_NAME)] = user.getUsername();
            data[i][header.indexOf(ROLE_COLUMN_NAME)] = user.getRole();
            i++;
        }

        return data;

    }

    /**
     * Cleans the text field of panel.
     */
    private void cleanPanelComponent() {
        usernameTextField.setText("");
        passwordField.setText("");
        idTextField.setText("");
    }

    private void setUpdatePanel(User user) {
        usernameTextField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        roleComboBox.setSelectedItem(user.getRole().toString());
        idTextField.setText(String.valueOf(user.getId()));
    }

    private void checkInsert(int result) {
        SwingUtilities.invokeLater(() -> {
            if (result == -1) {
                userInsertError();
            } else {
                userInsertSuccess();
                cleanPanelComponent();
            }

        });

    }

    private void checkUpdate(boolean success, int userID) {
        SwingUtilities.invokeLater(() -> {
            if (!success) {
                userUpdateError(userID);
            } else {
                userUpdateSuccess(userID);
                cleanPanelComponent();
            }
        });

    }

    /*-----------------------------Users Messages-----------------------------*/
    private void userUpdateError(int userID) {
        String msg = "Error while updating user with id: " + userID;
        String title = "Update error";
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    private void userUpdateSuccess(int userID) {
        String msg = "Succesfully updated the user with id: " + userID;
        String title = "Success";
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void userInsertError() {
        String msg = "Error while saving user";
        String title = "Update error";
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    private void userInsertSuccess() {
        String msg = "Succesfully save the user";
        String title = "Success";
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadingUsersError() {
        String msg = "Error while loading users";
        String title = "Loading error";
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    private void unselectedTableRowError() {
        String msg = "You must select a row of table to update an user";
        String title = "User selection error";
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

        jDesktopPane1 = new javax.swing.JDesktopPane();
        mainPanel = new javax.swing.JPanel();
        systemAdministratorTabbedPane = new javax.swing.JTabbedPane();
        viewUsersPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        insertUserPanel = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        roleComboBox = new javax.swing.JComboBox<>();
        roleLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        usernameTextField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        updateUserPanel = new javax.swing.JPanel();

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        mainPanel.setBackground(new java.awt.Color(0, 0, 153));

        systemAdministratorTabbedPane.setBackground(new java.awt.Color(0, 0, 153));
        systemAdministratorTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        systemAdministratorTabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        viewUsersPanel.setBackground(new java.awt.Color(255, 255, 255));

        usersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Username", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(usersTable);

        javax.swing.GroupLayout viewUsersPanelLayout = new javax.swing.GroupLayout(viewUsersPanel);
        viewUsersPanel.setLayout(viewUsersPanelLayout);
        viewUsersPanelLayout.setHorizontalGroup(
            viewUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 436, Short.MAX_VALUE)
            .addGroup(viewUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE))
        );
        viewUsersPanelLayout.setVerticalGroup(
            viewUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 273, Short.MAX_VALUE)
            .addGroup(viewUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE))
        );

        systemAdministratorTabbedPane.addTab("Members", viewUsersPanel);

        insertUserPanel.setBackground(new java.awt.Color(255, 255, 255));

        usernameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        usernameLabel.setText("Username");

        passwordLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        passwordLabel.setText("Password");

        roleComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        roleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { MAINTAINER, PLANNER }));

        roleLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        roleLabel.setText("Role");

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        usernameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        usernameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameTextFieldActionPerformed(evt);
            }
        });

        idLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        idLabel.setText("ID");

        idTextField.setEditable(false);

        javax.swing.GroupLayout insertUserPanelLayout = new javax.swing.GroupLayout(insertUserPanel);
        insertUserPanel.setLayout(insertUserPanelLayout);
        insertUserPanelLayout.setHorizontalGroup(
            insertUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, insertUserPanelLayout.createSequentialGroup()
                .addGroup(insertUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(insertUserPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saveButton))
                    .addGroup(insertUserPanelLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(insertUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(roleLabel)
                            .addComponent(usernameLabel)
                            .addComponent(passwordLabel)
                            .addComponent(idLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(insertUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(roleComboBox, 0, 291, Short.MAX_VALUE)
                            .addComponent(usernameTextField)
                            .addComponent(passwordField)
                            .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18))
        );
        insertUserPanelLayout.setVerticalGroup(
            insertUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(insertUserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(insertUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(insertUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(insertUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(roleLabel)
                    .addComponent(roleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(insertUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idLabel)
                    .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        systemAdministratorTabbedPane.addTab("Insert User", insertUserPanel);

        updateUserPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout updateUserPanelLayout = new javax.swing.GroupLayout(updateUserPanel);
        updateUserPanel.setLayout(updateUserPanelLayout);
        updateUserPanelLayout.setHorizontalGroup(
            updateUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );
        updateUserPanelLayout.setVerticalGroup(
            updateUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 273, Short.MAX_VALUE)
        );

        systemAdministratorTabbedPane.addTab("Update Users", updateUserPanel);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(systemAdministratorTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(systemAdministratorTabbedPane)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void usernameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameTextFieldActionPerformed

    /**
     * Saves the <code>User</code> object when the saveButton is pressed.
     *
     * @param evt the event to be processed
     */
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        Component currentPanel = systemAdministratorTabbedPane.getSelectedComponent();

        String username = usernameTextField.getText();
        String password = String.copyValueOf(passwordField.getPassword());
        String role = roleComboBox.getSelectedItem().toString();
        String idText = idTextField.getText();
        //mentre non è pronta la factory fai così
        int id = idText.isEmpty() ? -1 : Integer.valueOf(idText);
        //create a factory
        User user;

        if (role.equals(MAINTAINER)) {
            user = new Maintainer(id, username, password);
        } else {
            user = new Planner(id, username, password);
        }

        Runnable saver = () -> {
            if (currentPanel.equals(insertUserPanel)) {

                int result = userBO.insert(user);
                checkInsert(result);

            } else if (currentPanel.equals(updateUserPanel)) {
                boolean success = userBO.update(user);
                checkUpdate(success, user.getId());
            }

        };

        new Thread(saver).start();
    }//GEN-LAST:event_saveButtonActionPerformed

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
            java.util.logging.Logger.getLogger(SystemAdministratorFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SystemAdministratorFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SystemAdministratorFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SystemAdministratorFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SystemAdministratorFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JPanel insertUserPanel;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JComboBox<String> roleComboBox;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JTabbedPane systemAdministratorTabbedPane;
    private javax.swing.JPanel updateUserPanel;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameTextField;
    private javax.swing.JTable usersTable;
    private javax.swing.JPanel viewUsersPanel;
    // End of variables declaration//GEN-END:variables
}
