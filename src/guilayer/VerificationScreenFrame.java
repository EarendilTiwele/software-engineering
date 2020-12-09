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
import static businesslogiclayer.AssignmentBO.*;
import datatransferobjects.Assignment;
import java.awt.event.MouseAdapter;
import java.util.function.BiPredicate;
import java.util.function.Function;
import javax.swing.JTable;

/**
 * Verification screen Frame allows a planner to view maintainers'
 * availabilities in the week.
 *
 * @author Alfonso
 */
public class VerificationScreenFrame extends javax.swing.JFrame {

    private Activity activity;
    private Map<Maintainer, Integer[]> agenda;
    private Set<Assignment> assignments;
    private String selectedDay;
    private Integer[] dailyAgenda;

    private AssignmentBO assignmentBO = new AssignmentBO();
    //table: column header
    private static final String MAINTAINER_COLUMN = "Maintainer";
    private static final String SKILLS_COLUMN = "Skills";

    private static final String H8_COLUMN = "Avaliab 8-9";
    private static final String H9_COLUMN = "Avaliab 9-10";
    private static final String H10_COLUMN = "Avaliab 10-11";
    private static final String H11_COLUMN = "Avaliab 11-12";
    private static final String H14_COLUMN = "Avaliab 14-15";
    private static final String H15_COLUMN = "Avaliab 15-16";
    private static final String H16_COLUMN = "Avaliab 16-17";

    private static final String[] DAILY_AGENDA_HEADER = new String[]{
        MAINTAINER_COLUMN, SKILLS_COLUMN, H8_COLUMN, H9_COLUMN, H10_COLUMN,
        H11_COLUMN, H14_COLUMN, H15_COLUMN, H16_COLUMN
    };
    private static final String[] AGENDA_HEADER = new String[]{
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
        assignationPanel.setVisible(false);
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
        String dot = String.valueOf("\u2022");
        for (Competency competency : activity.getProcedure().getCompetencies()) {
            skillsString += dot + competency + "<br>";
        }

        skillsNeededEditorPane.setText(skillsString);

        initAgendaTable();
        initDailyAgendaTableMouseListener();
    }

    /**
     * Load the agenda in a new thread and complete the table.
     */
    private void initAgendaTable() {
        initAgendaTableMouseListener();
        // Cursor indicates to user the wait needed to load agenda from
        // the database and to update the table.
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Runnable loader = (() -> {
            agenda = getAgenda();
            Object[][] data = convertToObjectMatrix(agenda);
            SwingUtilities.invokeLater(() -> {
                setTableData(agendaTable, AGENDA_HEADER, data);
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            });
        });
        new Thread(loader).start();
    }

    /**
     * Adds mouse listener to day's column such that assign activity to
     * maintainer in that day
     */
    private void initAgendaTableMouseListener() {
        agendaTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = agendaTable.rowAtPoint(evt.getPoint());
                int col = agendaTable.columnAtPoint(evt.getPoint());
                List<String> headers = Arrays.asList(AGENDA_HEADER);
                int mondayCol = headers.indexOf(MONDAY);
                int maintainerCol = headers.indexOf(MAINTAINER_COLUMN);
                int complianceCol = headers.indexOf(SKILLS_COLUMN);
                if (row >= 0 && col >= mondayCol) {
                    Maintainer maintainer = (Maintainer) agendaTable.getValueAt(row, maintainerCol);
                    selectedDay = AGENDA_HEADER[col];
                    int week = activity.getWeek();
                    String compliance = agendaTable.getValueAt(row, complianceCol).toString();
                    //init dailyAgendaTable
                    dailyAgenda = assignmentBO.getDailyAgenda(maintainer, assignments, week, selectedDay);
                    String[] dailyAgendaString = Arrays.stream(dailyAgenda).map(e -> e + " min").toArray(String[]::new);
                    Object[][] data = convertDailyAgendaToMatrix(dailyAgendaString, maintainer, compliance);
                    setTableData(dailyAgendaTable, DAILY_AGENDA_HEADER, data);
                    workspaceNotesEditorPane.setText(activity.getWorkspaceNotes());
                    maintainerNameLabel.setText(maintainer.toString());
                    String percentage = agendaTable.getValueAt(row, col).toString();
                    percentageLabel.setText(percentage);
                    selectedDayLabel.setText(selectedDay);
                    verificationPanel.setVisible(false);
                    assignationPanel.setVisible(true);

                }
            }
        });
    }

    private void initDailyAgendaTableMouseListener() {
        dailyAgendaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                List<String> headers = Arrays.asList(DAILY_AGENDA_HEADER);
                int maintainerCol = headers.indexOf(MAINTAINER_COLUMN);
                int row = dailyAgendaTable.rowAtPoint(evt.getPoint());
                int col = dailyAgendaTable.columnAtPoint(evt.getPoint());
                if (row == 0 && col >= headers.indexOf(H8_COLUMN)) {
                    int hour = dailyAgendaColumnToHour(col);
                    Maintainer maintainer = (Maintainer) dailyAgendaTable.getValueAt(row, maintainerCol);
                    Assignment assignment = new Assignment(maintainer, activity, selectedDay, hour);
                    insertAssignment(assignment, dailyAgenda);
                }

            }

        });
    }

    private void insertAssignment(Assignment assignment, Integer[] dailyAgenda) {

        if (assignmentBO.validate(assignment, dailyAgenda)) {
            Runnable saver = () -> {
                boolean inserted = assignmentBO.insert(assignment);
                if (!inserted) {
                    SwingUtilities.invokeLater(() -> {
                        String msg = "Insert assignment failed";
                        showErrorMessage(msg);
                    });
                } else {

                    SwingUtilities.invokeLater(() -> {
                        String msg = "Assignment correctly inserted";
                        String title = "Success";
                        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    });
                }
            };

            new Thread(saver).start();
        } else {
            String msg = "Invalid assignment";
            showErrorMessage(msg);
        }

    }

    /**
     * Pop up error message.
     *
     * @param msg the error message
     */
    private void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private int dailyAgendaColumnToHour(int column) {
        String columnName = DAILY_AGENDA_HEADER[column];
        switch (columnName) {
            case H8_COLUMN:
                return 8;
            case H9_COLUMN:
                return 9;
            case H10_COLUMN:
                return 10;
            case H11_COLUMN:
                return 11;
            case H14_COLUMN:
                return 14;
            case H15_COLUMN:
                return 15;
            case H16_COLUMN:
                return 16;
            default:
                return -1;
        }

    }

    private Object[][] convertDailyAgendaToMatrix(String[] dailyAgenda,
            Maintainer maintainer, String compliance) {

        Object[][] data = new Object[1][DAILY_AGENDA_HEADER.length];
        int row = 0;
        List<String> header = Arrays.asList(DAILY_AGENDA_HEADER);
        int maintainerCol = header.indexOf(MAINTAINER_COLUMN);
        int complianceCol = header.indexOf(SKILLS_COLUMN);

        data[row][maintainerCol] = maintainer;
        data[row][complianceCol] = compliance;

        for (int i = 0; i < dailyAgenda.length; i++) {
            data[row][i + complianceCol + 1] = dailyAgenda[i];
        }

        return data;

    }

    /**
     * Returns the agenda of the maintainers. The agenda is represented as a
     * Map. The key is a Maintainer and the value is an array of integer
     * availabilities for each day of the week.
     *
     * @return the agenda of the maintainers
     */
    private Map<Maintainer, Integer[]> getAgenda() {
        UserBO userBLL = new UserBO();
        try {
            assignments = assignmentBO.getAllforWeek(activity.getWeek());
            return assignmentBO.getAgenda(
                    assignments,
                    userBLL.getAllMaintainers());
        } catch (SQLException ex) {
            SwingUtilities.invokeLater(()
                    -> JOptionPane.showMessageDialog(
                            this, ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE));
        }
        return new HashMap<>();
    }

    /**
     * Convert an agenda into a matrix suitable for the table model.
     *
     * @param agenda the agenda
     * @return the matrix representing the agenda
     */
    private Object[][] convertToObjectMatrix(Map<Maintainer, Integer[]> agenda) {
        int numCol = AGENDA_HEADER.length;
        int numRow = agenda.size();
        Object[][] data = new Object[numRow][numCol];

        int i = 0;
        List<String> columnsAsList = Arrays.asList(AGENDA_HEADER);
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
            data[i][maintainerIndex] = maintainer;

            //insert availabilities
            for (int j = 0; j < availabilities.length; j++) {
                data[i][j + mondayIndex] = availabilities[j] + "%";
            }
            i++;
        }

        return data;

    }

    /**
     * Fill the table spcefied table with given data and sets. The table will be
     * non-editable.
     *
     * @param table the table
     * @param tableColumnNames the column header
     * @param data the matrix of data
     */
    private void setTableData(JTable table, String[] tableColumnNames, Object[][] data) {
        CustomTableModel model = new CustomTableModel(tableColumnNames, data);
        table.setModel(model);
    }

    private Color convertColor(double power) {
        double H = power * 0.4; // Hue (0.4 = Green)
        double S = 0.9; // Saturation
        double B = 0.9; // Brightness
        return Color.getHSBColor((float) H, (float) S, (float) B);
    }

    private double convertToPercent(String value, String substringToRemove, int fraction) {
        String number = value.replace(substringToRemove, "");
        return Double.valueOf(number) / fraction;

    }

    private Function<Object, Color> getColorConverter(String substringToRemove, int fraction) {
        Function<Object, Color> colorConverter = (value) -> {
            String val = value.toString();
            double percent = convertToPercent(val, substringToRemove, fraction);
            return convertColor(percent);

        };
        return colorConverter;
    }

    private Component agendaChangeColor(JTable table, Component comp, int row, int col) {
        Function<Object, Color> colorConverter = getColorConverter("%", 100);
        int mondayIndex = Arrays.asList(AGENDA_HEADER).indexOf(MONDAY);
        BiPredicate<Integer, Integer> cellPredicate = (r, c) -> c >= mondayIndex;
        return tableChangeColor(table, comp, row, col, colorConverter, cellPredicate);

    }

    private Component dailyAgendaChangeColor(JTable table, Component comp, int row, int col) {
        Function<Object, Color> colorConverter = getColorConverter(" min", 60);
        int eightIndex = Arrays.asList(DAILY_AGENDA_HEADER).indexOf(H8_COLUMN);
        BiPredicate<Integer, Integer> cellPredicate = (r, c) -> c >= eightIndex;
        return tableChangeColor(table, comp, row, col, colorConverter, cellPredicate);

    }

    private Component tableChangeColor(JTable table, Component comp, int row, int col, Function<Object, Color> colorConverter, BiPredicate<Integer, Integer> cellPredicate) {
        Object value = table.getModel().getValueAt(row, col);
        if (cellPredicate.test(row, col)) {
            comp.setBackground(colorConverter.apply(value));
        } else {
            comp.setBackground(new Color(0.9f, 0.9f, 0.9f, 1));
        }
        return comp;

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
        assignationPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dailyAgendaTable = new JColoredTable(this::dailyAgendaChangeColor);
        jScrollPane4 = new javax.swing.JScrollPane();
        workspaceNotesEditorPane = new javax.swing.JEditorPane();
        availabilityLabel = new javax.swing.JLabel();
        maintainerNameLabel = new javax.swing.JLabel();
        percentageLabel = new javax.swing.JLabel();
        dayLabel = new javax.swing.JLabel();
        selectedDayLabel = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        verificationPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        agendaTable = new JColoredTable(this::agendaChangeColor);
        jScrollPane3 = new javax.swing.JScrollPane();
        skillsNeededEditorPane = new javax.swing.JEditorPane();
        maintainerAvailabilityLabel = new javax.swing.JLabel();
        activityLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        weekLabel.setText("Week n°");

        weekNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        weekNumberLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        activityToAssignLabel.setText("Activity to assign");

        dailyAgendaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Maintainer", "Skills", "Avalilab 8-9", "Avalilab 9-10", "Avalilab 10-11", "Avalilab11-12", "Avalilab14-15", "Avalilab15-16", "Avalilab16-17"
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
        dailyAgendaTable.setCellSelectionEnabled(true);
        dailyAgendaTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(dailyAgendaTable);

        workspaceNotesEditorPane.setEditable(false);
        workspaceNotesEditorPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Workspace notes"));
        jScrollPane4.setViewportView(workspaceNotesEditorPane);

        availabilityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        availabilityLabel.setText("Availability");
        availabilityLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        maintainerNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        maintainerNameLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        percentageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        percentageLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        dayLabel.setText("Day");
        dayLabel.setToolTipText("");

        selectedDayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectedDayLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout assignationPanelLayout = new javax.swing.GroupLayout(assignationPanel);
        assignationPanel.setLayout(assignationPanelLayout);
        assignationPanelLayout.setHorizontalGroup(
            assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(assignationPanelLayout.createSequentialGroup()
                .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(assignationPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(dayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selectedDayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(assignationPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(backButton)
                            .addGroup(assignationPanelLayout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 697, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(assignationPanelLayout.createSequentialGroup()
                                        .addComponent(availabilityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(maintainerNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(percentageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        assignationPanelLayout.setVerticalGroup(
            assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(assignationPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectedDayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(assignationPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(assignationPanelLayout.createSequentialGroup()
                        .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(assignationPanelLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(availabilityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(maintainerNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, assignationPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(percentageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(backButton)
                        .addGap(33, 33, 33))))
        );

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
        jScrollPane2.setViewportView(agendaTable);
        if (agendaTable.getColumnModel().getColumnCount() > 0) {
            agendaTable.getColumnModel().getColumn(0).setHeaderValue("Maintainer");
            agendaTable.getColumnModel().getColumn(1).setHeaderValue("Skills");
            agendaTable.getColumnModel().getColumn(2).setHeaderValue("Mon");
            agendaTable.getColumnModel().getColumn(3).setHeaderValue("Tue");
            agendaTable.getColumnModel().getColumn(4).setHeaderValue("Wed");
            agendaTable.getColumnModel().getColumn(5).setHeaderValue("Thu");
            agendaTable.getColumnModel().getColumn(6).setHeaderValue("Fri");
            agendaTable.getColumnModel().getColumn(7).setHeaderValue("Sat");
            agendaTable.getColumnModel().getColumn(8).setHeaderValue("Sun");
        }

        skillsNeededEditorPane.setEditable(false);
        skillsNeededEditorPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Skills needed"));
        skillsNeededEditorPane.setContentType("text/html"); // NOI18N
        skillsNeededEditorPane.setAlignmentX(0.0F);
        skillsNeededEditorPane.setAlignmentY(0.2F);
        jScrollPane3.setViewportView(skillsNeededEditorPane);

        maintainerAvailabilityLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        maintainerAvailabilityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        maintainerAvailabilityLabel.setText("Maintainer AVAILABILITY");
        maintainerAvailabilityLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout verificationPanelLayout = new javax.swing.GroupLayout(verificationPanel);
        verificationPanel.setLayout(verificationPanelLayout);
        verificationPanelLayout.setHorizontalGroup(
            verificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(verificationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(129, 129, 129)
                .addGroup(verificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(maintainerAvailabilityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        verificationPanelLayout.setVerticalGroup(
            verificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, verificationPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(verificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(verificationPanelLayout.createSequentialGroup()
                        .addComponent(maintainerAvailabilityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(66, 66, 66))
        );

        activityLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(weekLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(weekNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(activityToAssignLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(activityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(verificationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(36, 36, 36)
                    .addComponent(assignationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
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
                .addComponent(verificationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(44, 44, 44)
                    .addComponent(assignationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(17, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        assignationPanel.setVisible(false);
        verificationPanel.setVisible(true);
    }//GEN-LAST:event_backButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel activityLabel;
    private javax.swing.JLabel activityToAssignLabel;
    private javax.swing.JTable agendaTable;
    private javax.swing.JPanel assignationPanel;
    private javax.swing.JLabel availabilityLabel;
    private javax.swing.JButton backButton;
    private javax.swing.JTable dailyAgendaTable;
    private javax.swing.JLabel dayLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel maintainerAvailabilityLabel;
    private javax.swing.JLabel maintainerNameLabel;
    private javax.swing.JLabel percentageLabel;
    private javax.swing.JLabel selectedDayLabel;
    private javax.swing.JEditorPane skillsNeededEditorPane;
    private javax.swing.JPanel verificationPanel;
    private javax.swing.JLabel weekLabel;
    private javax.swing.JLabel weekNumberLabel;
    private javax.swing.JEditorPane workspaceNotesEditorPane;
    // End of variables declaration//GEN-END:variables
}
