/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guilayer;

import businesslogiclayer.Activity;
import businesslogiclayer.Competency;
import businesslogiclayer.Maintainer;
import businesslogiclayer.PlannedActivity;
import businesslogiclayer.Procedure;
import businesslogiclayer.Site;
import businesslogiclayer.Typology;
import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Alfonso
 */
public class VerificationScreenFrame extends javax.swing.JFrame {

    private Activity activity;
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
     * @param activityToAssign
     */
    public VerificationScreenFrame(Activity activityToAssign) {
        initComponents();
        this.activity = activityToAssign;
        initGUI();
    }

    private void initGUI() {
        weekNumberLabel.setText(activity.getWeek() + "");
        String activityString = activity.getId() + " - " + activity.getSite() + " - "
                + activity.getTipology() + " - " + activity.getInterventionTime() + "'";
        activityLabel.setText(activityString);
        String skillsString = "<ul>\n";
        for (Competency competency : activity.getProcedure().getCompetencies()) {
            skillsString += "<li>" + competency + "</li>\n";
        }
        skillsString += "</ul>";
        skillsNeededEditorPane.setText(skillsString);
        initTable();
    }

    private void initTable() {

        Runnable loader = (() -> {
            Map<Maintainer, String[]> agenda = getAgenda();
            Object[][] data = convertToObjectMatrix(agenda);
            SwingUtilities.invokeLater(() -> setTableData(tableColumnNames, data));
        });

        new Thread(loader).start();
    }

    private Map<Maintainer, String[]> getAgenda() {
        Maintainer m1 = new Maintainer("pippo", "pass");
        Maintainer m2 = new Maintainer("Pluto", "pass");

        m1.addCompetency(new Competency("competenza2"));
        m1.addCompetency(new Competency("competenza3"));

        String[] d1 = new String[]{"80%", "100%", "20%", "100%", "50%", "20%", "100%"};
        String[] d2 = new String[]{"20%", "50%", "80%", "50%", "100%", "50%", "80%"};

        Map<Maintainer, String[]> agenda = new HashMap<>();
        agenda.put(m1, d1);
        agenda.put(m2, d2);

        return agenda;
    }

    private Object[][] convertToObjectMatrix(Map<Maintainer, String[]> agenda) {
        int numCol = tableColumnNames.length;
        int numRow = agenda.size();

        Object[][] data = new Object[numRow][numCol];

        int i = 0;
        int mondayIndex = Arrays.asList(tableColumnNames).indexOf(MONDAY);
        int skillsIndex = Arrays.asList(tableColumnNames).indexOf(SKILLS_COLUMN);
        int maintainerIndex = Arrays.asList(tableColumnNames).indexOf(MAINTAINER_COLUMN);

        for (Map.Entry<Maintainer, String[]> entry : agenda.entrySet()) {
            Maintainer maintainer = entry.getKey();
            String[] availabilities = entry.getValue();
            int totalRequiredSkills = activity.getProcedure().getCompetencies().size();
            Set<Competency> remainingCompetencies = new HashSet<>(activity.getProcedure().getCompetencies());
            remainingCompetencies.removeAll(maintainer.getCompetencies());
            int availableSkills = totalRequiredSkills - remainingCompetencies.size();

            data[i][skillsIndex] = availableSkills + "/" + totalRequiredSkills;
            data[i][maintainerIndex] = maintainer.getUsername();
            for (int j = 0; j < availabilities.length; j++) {
                data[i][j + mondayIndex] = availabilities[j];
            }
            i++;
        }

        return data;

    }

    private void setTableData(String[] tableColumnNames, Object[][] data) {
        CustomTableModel model = new CustomTableModel(tableColumnNames, data) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
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
            public Color getColor(double power){
                double H = power * 0.4; // Hue (note 0.4 = Green, see huge chart below)
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
                }
                return comp;
            }
        };
        jScrollPane3 = new javax.swing.JScrollPane();
        skillsNeededEditorPane = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        weekLabel.setText("Week n°");

        activityToAssignLabel.setText("Activity to assign");

        skillsNeededLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        skillsNeededLabel.setText("Skills needed ");

        maintainerAvailabilityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        maintainerAvailabilityLabel.setText("Maintainer availability");

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
        skillsNeededEditorPane.setText("<html>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      \r\n    </p>\r\n  </body>\r\n</html>\r\n");
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
                    .addComponent(skillsNeededLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(weekLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(weekNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(activityToAssignLabel))
                    .addComponent(jScrollPane3))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(activityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addComponent(maintainerAvailabilityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weekLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weekNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(activityToAssignLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(activityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(skillsNeededLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maintainerAvailabilityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
                .addContainerGap(139, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(VerificationScreenFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VerificationScreenFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VerificationScreenFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VerificationScreenFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        int activityId = 1;
        int interventionTime = 10;
        int week = 3;
        boolean interruptible = true;
        Procedure procedure = new Procedure("procedure name", "smp.pdf");
        procedure.addCompetency(new Competency("competenza1"));
        procedure.addCompetency(new Competency("competenza2"));
        procedure.addCompetency(new Competency("competenza3"));
        procedure.addCompetency(new Competency("competenza4"));
        Activity activity = new PlannedActivity(
                activityId,
                new Site("factory", "area"),
                new Typology("typology"),
                "description",
                interventionTime,
                interruptible,
                week,
                procedure);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VerificationScreenFrame(activity).setVisible(true);
            }
        });
    }

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
