/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guilayer;

import datatransferobjects.Activity;
import businesslogiclayer.AssignmentBO;
import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import businesslogiclayer.UserBO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;


/**
 * Verification screen Frame allows a planner to view maintainers'
 * availabilities in the week.
 *
 * @author Alfonso
 */
public class VerificationScreenFrame extends javax.swing.JFrame {
    
    private Activity activity;
    private Map<Maintainer,Integer[]> agenda;
    
    //table: column header
    private static final String MONDAY = "Mon";
    private static final String TUESDAY = "Tue";
    private static final String WEDNESDAY = "Wed";
    private static final String THURSDAY = "Thu";
    private static final String FRIDAY = "Fri";
    private static final String SATURDAY = "Sat";
    private static final String SUNDAY = "Sun";
    private static final String MAINTAINER_COLUMN = "Maintainer";
    private static final String SKILLS_COLUMN = "Skills";
    
    private final String[] tableColumnNames = new String[]{
        MAINTAINER_COLUMN, SKILLS_COLUMN, MONDAY, TUESDAY, WEDNESDAY, THURSDAY,
        FRIDAY, SATURDAY, SUNDAY
    };

    /**
     * Creates new form VerificationScreenFrame
     *
     * @param activityToAssign the activity to assign
     */
    public VerificationScreenFrame(Activity activityToAssign) {
        initComponents();
        this.activity = activityToAssign;
        setTitle("Verification screen - Activity id: " + activityToAssign.getId());
        initGUI();
        
    }

    /**
     * Initialize components according to the activity to assign.
     */
    private void initGUI() {
        weekNumberLabel.setText(activity.getWeek() + "");
        String activityString
                = activity.getId() + " - "
                + activity.getSite() + " - "
                + activity.getTipology() + " - "
                + activity.getInterventionTime() + "'";
        
        activityLabel.setText(activityString);
        
        String skillsString = "";
        for (Competency competency : activity.getProcedure().getCompetencies()) {
            skillsString += String.valueOf("\u2022") + competency + "<br>";
        }
        
        skillsNeededEditorPane.setText(skillsString);
        
        initTable();
    }

    /**
     * Load the agenda in a new thread and complete the table.
     */
    private void initTable() {
        initTableMouseListener();
        // Cursor indicates to user the wait needed to load agenda from 
        // the database and to update the table.
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Runnable loader = (() -> {
            agenda = getAgenda();
            Object[][] data = convertToObjectMatrix(agenda);
            SwingUtilities.invokeLater(() ->{ 
                setTableData(tableColumnNames, data);
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            
            });
        });
        new Thread(loader).start();
    }

    /**
     * Adds mouse listener to day's column such that assign activity to maintainer
     * in that day
     */
    private void initTableMouseListener(){
        agendaTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = agendaTable.rowAtPoint(evt.getPoint());
                int col = agendaTable.columnAtPoint(evt.getPoint());
                int mondayCol = Arrays.asList(tableColumnNames).indexOf(MONDAY);
                int maintainerCol = Arrays.asList(tableColumnNames).indexOf(MAINTAINER_COLUMN);
                if (row >= 0 && col >= mondayCol) {
                    String username = (String)agendaTable.getValueAt(row, maintainerCol);
                    String day = tableColumnNames[col];
                    String msg = "Activity " + activity.getId()+ " assigned to "+ username + " on "+ day;
                    JOptionPane.showMessageDialog(VerificationScreenFrame.this, msg, "Activity assigned", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
    

    /**
     * Returns the agenda of the maintainers. The agenda is represented as a
     * Map. The key is a Maintainer and the value is an array of integer
     * availabilities for each day of the week.
     *
     * @return the agenda of the maintainers 
     */
    private Map<Maintainer, Integer[]> getAgenda(){
        AssignmentBO assignmentBLL = new AssignmentBO();
        UserBO userBLL = new UserBO();
        try {
            return assignmentBLL.getAgenda(assignmentBLL.getAllforWeek(activity.getWeek()),userBLL.getAllMaintainers());
        } catch (SQLException ex) {
            SwingUtilities.invokeLater(()->JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
        }
        return new HashMap<Maintainer, Integer[]>();
    }
    
    /**
     * Convert an agenda into a matrix suitable for the table model.
     *
     * @param agenda the agenda
     * @return the matrix representing the agenda
     */
    private Object[][] convertToObjectMatrix(Map<Maintainer, Integer[]> agenda) {
        int numCol = tableColumnNames.length;
        int numRow = agenda.size();
        Object[][] data = new Object[numRow][numCol];
        
        int i = 0;
        List<String> columnsAsList = Arrays.asList(tableColumnNames);
        int mondayIndex = columnsAsList.indexOf(MONDAY);
        int skillsIndex = columnsAsList.indexOf(SKILLS_COLUMN);
        int maintainerIndex = columnsAsList.indexOf(MAINTAINER_COLUMN);

        //fill the matrix
        for (Map.Entry<Maintainer, Integer[]> entry : agenda.entrySet()) {
            Maintainer maintainer = entry.getKey();
            Integer[] availabilities = entry.getValue();
            int totalRequiredSkills = activity
                    .getProcedure()
                    .getCompetencies()
                    .size();
            Set<Competency> remainingCompetencies
                    = new HashSet<>(activity.getProcedure().getCompetencies());
            //set difference
            remainingCompetencies.removeAll(maintainer.getCompetencies());
            int availableSkills = totalRequiredSkills - remainingCompetencies.size();
            
            data[i][skillsIndex] = availableSkills + "/" + totalRequiredSkills;
            data[i][maintainerIndex] = maintainer.getUsername();

            //insert availabilities
            for (int j = 0; j < availabilities.length; j++) {
                data[i][j + mondayIndex] = availabilities[j] + "%";
            }
            i++;
        }
        
        return data;
        
    }

    /**
     * Fill the table and sets its cell as non-editable.
     *
     * @param tableColumnNames the column header
     * @param data the matrix of data
     */
    private void setTableData(String[] tableColumnNames, Object[][] data) {
        CustomTableModel model = new CustomTableModel(tableColumnNames, data) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                //this is a non-editable table
                return false;
            }
        };
        agendaTable.setModel(model);
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
        weekNumberLabel = new javax.swing.JLabel();
        activityToAssignLabel = new javax.swing.JLabel();
        activityLabel = new javax.swing.JLabel();
        skillsNeededLabel = new javax.swing.JLabel();
        maintainerAvailabilityLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        agendaTable = new javax.swing.JTable(){
            private Color getColor(double power){
                double H = power * 0.4; // Hue (0.4 = Green)
                double S = 0.9; // Saturation
                double B = 0.9; // Brightness
                return Color.getHSBColor((float)H, (float)S, (float)B);
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                Object value = getModel().getValueAt(row, col);
                int mondayIndex=Arrays.asList(tableColumnNames).indexOf(MONDAY);
                if (col>=mondayIndex){
                    String val = (String)value;
                    val=val.replace("%","");
                    double percent = Double.valueOf(val)/100;
                    Color color=getColor(percent);
                    comp.setBackground(color);
                }else{
                    comp.setBackground(new Color(0.9f,0.9f,0.9f,1));
                }
                return comp;
            }
        };
        jScrollPane3 = new javax.swing.JScrollPane();
        skillsNeededEditorPane = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        weekLabel.setText("Week n°");

        weekNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        weekNumberLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        activityToAssignLabel.setText("Activity to assign");

        activityLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        skillsNeededLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        skillsNeededLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        skillsNeededLabel.setText("Skills needed ");

        maintainerAvailabilityLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        maintainerAvailabilityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        maintainerAvailabilityLabel.setText("Maintainer AVAILABILITY");
        maintainerAvailabilityLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        agendaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Maintainer", "Skills", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        agendaTable.setCellSelectionEnabled(true);
        jScrollPane2.setViewportView(agendaTable);

        skillsNeededEditorPane.setEditable(false);
        skillsNeededEditorPane.setContentType("text/html"); // NOI18N
        skillsNeededEditorPane.setAlignmentX(0.0F);
        skillsNeededEditorPane.setAlignmentY(0.2F);
        jScrollPane3.setViewportView(skillsNeededEditorPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(skillsNeededLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                            .addComponent(jScrollPane3))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(weekLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(weekNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(activityToAssignLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maintainerAvailabilityLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(activityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(weekLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(weekNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(activityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(activityToAssignLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(skillsNeededLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maintainerAvailabilityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                .addGap(138, 138, 138))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel activityLabel;
    private javax.swing.JLabel activityToAssignLabel;
    private javax.swing.JTable agendaTable;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel maintainerAvailabilityLabel;
    private javax.swing.JEditorPane skillsNeededEditorPane;
    private javax.swing.JLabel skillsNeededLabel;
    private javax.swing.JLabel weekLabel;
    private javax.swing.JLabel weekNumberLabel;
    // End of variables declaration//GEN-END:variables
}
