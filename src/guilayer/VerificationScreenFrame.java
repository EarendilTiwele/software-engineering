/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guilayer;

import businesslogiclayer.ActivityBO;
import datatransferobjects.Activity;
import businesslogiclayer.AssignmentBO;
import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import businesslogiclayer.UserBO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import static businesslogiclayer.AssignmentBO.*;
import datatransferobjects.Assignment;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
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

    //the main activity of this frame
    private Activity activity;
    //the week agenda of all the maintainers in the system
    private Map<Maintainer, Integer[]> agenda;
    //the set of assignements of the specific week
    private Set<Assignment> assignments;
    //the day selected in the verification screen
    private String selectedDay;
    //the daily agenda of the selected maintainer
    private Integer[] dailyAgenda;

    private AssignmentBO assignmentBO = new AssignmentBO();
    private ActivityBO activityBO = new ActivityBO();

    //table: column header
    private static final String MAINTAINER_COLUMN = "Maintainer";
    private static final String SKILLS_COLUMN = "Skills";

    private static final String H8_COLUMN = "Availab 8-9";
    private static final String H9_COLUMN = "Availab 9-10";
    private static final String H10_COLUMN = "Availab 10-11";
    private static final String H11_COLUMN = "Availab 11-12";
    private static final String H14_COLUMN = "Availab 14-15";
    private static final String H15_COLUMN = "Availab 15-16";
    private static final String H16_COLUMN = "Availab 16-17";

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
        this.activity = activityToAssign;
        initGUI();
        setLocationRelativeTo(null);
        getContentPane().setBackground(mainPanel.getBackground());
    }

    /**
     * Initialize components according to the activity to assign.
     */
    private void initGUI() {
        informationPanel.setVisible(false);
        verificationPanel.setVisible(false);
        assignationPanel.setVisible(false);
        initInformationPanel();

        //initialize the header of the frame
        weekNumberLabel.setText(activity.getWeek() + "");
        String activityString
                = activity.getId() + " - "
                + activity.getSite() + " - "
                + activity.getTipology() + " - "
                + activity.getInterventionTime() + "'";

        activityLabel.setText(activityString);

        initDailyAgendaTableMouseListener();
        initAgendaTableMouseListener();
    }

    /**
     * Initialize components for 'information screen'.
     */
    private void initInformationPanel() {
        workspaceNotesInformationScreenPane.setText(activity.getWorkspaceNotes());
        interventionDescriptionPane.setText(activity.getDescription());
        smpLabel.setText(activity.getProcedure().getName());
        skillsNeededInformationPane.setText(
                getListString(activity.getProcedure().getCompetencies()));
        String url = activity.getProcedure().getSmp();
        openSMPButton.addActionListener(e -> browseToPage(url));
        showInformationPanel();
        pack();
    }

    private void showInformationPanel() {
        assignationPanel.setVisible(false);
        informationPanel.setVisible(true);
        verificationPanel.setVisible(false);
        setTitle("Information screen activity: " + activity.getId());
        refreshLabels();
    }

    /**
     * Initialize components for 'verification screen'.
     */
    private void initVerificationScreen() {
        skillsNeededVerificationPane.setText(
                getListString(activity.getProcedure().getCompetencies()));
        initAgendaTable();
        showVerificationPanel();
        pack();
    }

    private void showVerificationPanel() {
        assignationPanel.setVisible(false);
        informationPanel.setVisible(false);
        verificationPanel.setVisible(true);
        setTitle("Verification screen activity: " + activity.getId());
        refreshLabels();
    }

    /**
     * Initialize components for 'assignation screen'.
     *
     * @param maintainer the selected maintainer
     * @param percentage the percentage availability of the selected maintainer
     * @param week the week of the selected activity
     * @param compliance the compliance of the selected maintainer
     */
    private void initAssignationScreen(Maintainer maintainer, int percentage,
            int week, String compliance) {
        workspaceNotesAssignationScreenPane.setText(activity.getWorkspaceNotes());
        maintainerNameLabel.setText(maintainer.toString());
        percentageLabel.setOpaque(true);
        percentageLabel.setBackground(convertColor(percentage / 100.0));
        percentageLabel.setText(percentage + " %");
        selectedDayLabel.setText(selectedDay);
        showAssignationScreen();
        initDailyAgendaTable(maintainer, week, compliance);
        pack();
    }

    private void showAssignationScreen() {
        verificationPanel.setVisible(false);
        informationPanel.setVisible(false);
        assignationPanel.setVisible(true);
        setTitle("Assignation screen activity: " + activity.getId());
        refreshLabels();
    }

    /**
     * Workaround to change the opacity of labels.
     */
    private void refreshLabels() {
        informationScreenLabel.setOpaque(
                informationPanel.isVisible()
        );
        verificationScreenLabel.setOpaque(
                verificationPanel.isVisible()
        );
        assignationScreenLabel.setOpaque(
                assignationPanel.isVisible()
        );
        informationScreenLabel.setText("Information screen");
        informationScreenLabel.setText("Information screen ");
        verificationScreenLabel.setText("Verification screen");
        verificationScreenLabel.setText("Verification screen ");
        assignationScreenLabel.setText("Assignation screen");
        assignationScreenLabel.setText("Assignation screen ");
    }

    /**
     * Complete the daily agenda table according to the maintainer's
     * availabiliy.
     *
     * @param maintainer the selected maintainer
     * @param week the week of the selected activity
     * @param compliance the compliance of the selected maintainer
     */
    private void initDailyAgendaTable(Maintainer maintainer, int week, String compliance) {
        dailyAgenda
                = assignmentBO.getDailyAgenda(maintainer, assignments, week, selectedDay);
        String[] dailyAgendaString
                = Arrays.stream(dailyAgenda).map(e -> e + " min").toArray(String[]::new);
        Object[][] data
                = convertDailyAgendaToMatrix(dailyAgendaString, maintainer, compliance);
        setTableData(dailyAgendaTable, DAILY_AGENDA_HEADER, data);

    }

    /**
     * Returns a dotted list string of all the items in the specified
     * collection.
     *
     * @param <T> the type of the items
     * @param items the colletion of items
     * @return the dotted list of items as String
     */
    private <T> String getListString(Collection<T> items) {
        String result = "";
        String dot = String.valueOf("\u2022");
        for (T item : items) {
            result += dot + item + "<br>";
        }
        return result;
    }

    /**
     * Opens the browser and go to the specified url.
     *
     * @param url the url
     */
    private void browseToPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (URISyntaxException | IOException ex) {
            browserLoadingError(url);
        }
    }

    /**
     * Loads the agenda in a new thread and complete the agenda table.
     */
    private void initAgendaTable() {
        //initAgendaTableMouseListener();
        // Cursor indicates to user the wait needed to load agenda from
        // the database and to update the table.
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Runnable loader = (() -> {
            agenda = getAgenda();

            SwingUtilities.invokeLater(() -> {
                if (agenda == null) {
                    //Error loading agenda
                    agendaLoadingError();
                } else {
                    Object[][] data = convertAgendaToObjectMatrix(agenda);
                    setTableData(agendaTable, AGENDA_HEADER, data);
                }
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
                    String percentage = agendaTable
                            .getValueAt(row, col)
                            .toString().replace("%", "");
                    initAssignationScreen(maintainer, Integer.valueOf(percentage), week, compliance);
                }
            }
        });
    }

    /**
     * Adds mouse listener to hour's column such that complete the assignment in
     * that hour.
     */
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

    /**
     * Stores the assignment if it is valid according to the specified
     * dailyAgenda.
     *
     * @param assignment the assignment to store
     * @param dailyAgenda the daily agenda to validate the assignment
     */
    private void insertAssignment(Assignment assignment, Integer[] dailyAgenda) {

        if (assignmentBO.validate(assignment, dailyAgenda)) {
            Runnable saver = () -> {
                boolean inserted = assignmentBO.insert(assignment);

                SwingUtilities.invokeLater(() -> {
                    if (!inserted) {
                        assignmentInsertError();
                    } else {
                        assignmentInsertSuccess();
                        dispose();
                    }
                });

            };

            new Thread(saver).start();
        } else {
            invalidAssignmentError(assignment.getMaintainer());
        }

    }

    /**
     * Convert a column index of the daily agenda table to an integer hour, 8,
     * 9, 10, 11, 14, 15, 16.
     *
     * @param column the column index
     * @return the hour
     */
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

    /**
     * Converts a daily agenda into a matrix suitable for the table model.
     *
     * @param dailyAgenda the daily agenda
     * @param maintainer the selected maintaner
     * @param compliance the compliance of the selected maintainer
     * @return the matrix of data
     */
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
     * @return the agenda of the maintainers, or null if an error occurs
     */
    private Map<Maintainer, Integer[]> getAgenda() {
        UserBO userBO = new UserBO();
        assignments = assignmentBO.getAllForWeek(activity.getWeek());
        Set<Maintainer> maintainers = userBO.getAllMaintainers();
        if (assignments == null || maintainers == null) {
            return null;
        } else {
            return assignmentBO.getAgenda(assignments, maintainers);
        }

    }

    /**
     * Converts an agenda into a matrix suitable for the table model.
     *
     * @param agenda the agenda
     * @return the matrix representing the agenda
     */
    private Object[][] convertAgendaToObjectMatrix(Map<Maintainer, Integer[]> agenda) {
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
     * Fill the specified table with given data. The table will be non-editable.
     *
     * @param table the table
     * @param tableColumnNames the column header
     * @param data the matrix of data
     */
    private void setTableData(JTable table, String[] tableColumnNames, Object[][] data) {
        CustomTableModel model = new CustomTableModel(tableColumnNames, data);
        table.setModel(model);
    }

    /**
     * Converts a color in range [0;1] to a color [red; green].
     *
     * @param power the value to convert
     * @return the color
     */
    private Color convertColor(double power) {
        double H = power * 0.4; // Hue (0.4 = Green)
        double S = 0.9; // Saturation
        double B = 0.9; // Brightness
        return Color.getHSBColor((float) H, (float) S, (float) B);
    }

    /**
     * Converts the numeric part of a string to a number and fractionates it.
     * NOTE: the string must consist of a numeric part and at most could have
     * one or more equal substrings
     *
     * @param value the value string
     * @param substringToRemove the no numeric part of value to remove
     * @param fraction the fraction
     * @return the fractionated number
     */
    private double convertToFraction(String value, String substringToRemove, int fraction) {
        String number = value.replace(substringToRemove, "");
        return Double.valueOf(number) / fraction;

    }

    /**
     * Gets the color converter able to remove the specified substring from a
     * string and fractioned, trough the specified fraction, the numeric part of
     * that and so associated a color to the fractionated number.
     *
     * @param substringToRemove the no numeric part of value to remove
     * @param fraction the fraction
     * @return the color converter
     */
    private Function<Object, Color> getColorConverter(String substringToRemove, int fraction) {
        Function<Object, Color> colorConverter = (value) -> {
            String val = value.toString();
            double percent = convertToFraction(val, substringToRemove, fraction);
            return convertColor(percent);
        };
        return colorConverter;
    }

    /**
     * Defines the algorithm needed to paint the specified table and returns the
     * <code>Component</code> under the event location.
     *
     * NOTE: the table's cells must have values like those defined for the
     * agenda table.
     *
     * @param table the table similar to the agenda table
     * @param comp the compomponet under the event location
     * @param row the row of the cell to paint, where 0 is the first row
     * @param col the column of the cell to paint, where 0 is the first column
     * @return the colored Component under the event location
     */
    private Component agendaChangeColor(JTable table, Component comp, int row, int col) {
        Function<Object, Color> colorConverter = getColorConverter("%", 100);
        int mondayIndex = Arrays.asList(AGENDA_HEADER).indexOf(MONDAY);
        BiPredicate<Integer, Integer> cellPredicate = (r, c) -> c >= mondayIndex;
        return tableChangeColor(table, comp, row, col, colorConverter, cellPredicate);

    }

    /**
     * Defines the algorithm needed to paint the specified table and returns the
     * <code>Component</code> under the event location.
     *
     * NOTE: the table's cells must have values like those defined for the daily
     * agenda table.
     *
     * @param table the table similar to the daily agenda table
     * @param comp the compomponet under the event location
     * @param row the row of the cell to paint, where 0 is the first row
     * @param col the column of the cell to paint, where 0 is the first column
     * @return the colored Component under the event location
     */
    private Component dailyAgendaChangeColor(JTable table, Component comp, int row, int col) {
        Function<Object, Color> colorConverter = getColorConverter(" min", 60);
        int eightIndex = Arrays.asList(DAILY_AGENDA_HEADER).indexOf(H8_COLUMN);
        BiPredicate<Integer, Integer> cellPredicate = (r, c) -> c >= eightIndex;
        return tableChangeColor(table, comp, row, col, colorConverter, cellPredicate);

    }

    /**
     * Paints the <code>Component</code> under the event location. If
     * cellPrecicate test on the specified <code>row</code> and <code>col</code>
     * is <code>true</code> the component is painted according to the value at
     * (row,column), of the specified table, using the specified color
     * converter, otherwise a default light grey color is used.
     *
     * @param table the table to be colored
     * @param comp the compomponet under the event location
     * @param row the row of the cell to paint, where 0 is the first row
     * @param col the column of the cell to paint, where 0 is the first column
     * @param colorConverter the color converted
     * @param cellPredicate the cell predicate
     * @return the colored Component under the event location
     */
    private Component tableChangeColor(JTable table, Component comp, int row, int col, Function<Object, Color> colorConverter, BiPredicate<Integer, Integer> cellPredicate) {
        Object value = table.getModel().getValueAt(row, col);
        if (cellPredicate.test(row, col)) {
            comp.setBackground(colorConverter.apply(value));
        } else {
            comp.setBackground(new Color(0.9f, 0.9f, 0.9f, 1));
        }
        return comp;

    }

    /*----------------------------User Messages------------------------------*/
    private void agendaLoadingError() {
        String msg = "Error while loading agenda";
        String title = "Loading error";
        JOptionPane.showMessageDialog(this, msg, title,
                JOptionPane.ERROR_MESSAGE);
    }

    private void activityUpdateError() {
        String msg = "Error while updating activity";
        String title = "Update error";
        JOptionPane.showMessageDialog(this, msg, title,
                JOptionPane.ERROR_MESSAGE);
    }

    private void assignmentInsertError() {
        String msg = "Error while assigning the activity to the maintainer";
        String title = "Insert error";
        JOptionPane.showMessageDialog(this, msg, title,
                JOptionPane.ERROR_MESSAGE);
    }

    private void assignmentInsertSuccess() {
        String msg = "Activity is assignes successfully to the maintainer";
        String title = "Insert success";
        JOptionPane.showMessageDialog(this, msg, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void invalidAssignmentError(Maintainer maintainer) {
        String msg = "This assignment is not compatible with " + maintainer + "'s "
                + "daily agenda";
        String title = "Invalid assigment";
        JOptionPane.showMessageDialog(this, msg, title,
                JOptionPane.ERROR_MESSAGE);
    }

    private void browserLoadingError(String url) {
        String msg = "Error opening this url: " + url;
        String title = "Error opening the browser";
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

        mainPanel = new javax.swing.JPanel();
        weekLabel = new javax.swing.JLabel();
        weekNumberLabel = new javax.swing.JLabel();
        activityToAssignLabel = new javax.swing.JLabel();
        activityLabel = new javax.swing.JLabel();
        verificationPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        agendaTable = new JColoredTable(this::agendaChangeColor);
        jScrollPane3 = new javax.swing.JScrollPane();
        skillsNeededVerificationPane = new javax.swing.JEditorPane();
        maintainerAvailabilityLabel = new javax.swing.JLabel();
        backToInformationScreenButton = new javax.swing.JButton();
        assignationPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dailyAgendaTable = new JColoredTable(this::dailyAgendaChangeColor);
        jScrollPane4 = new javax.swing.JScrollPane();
        workspaceNotesAssignationScreenPane = new javax.swing.JEditorPane();
        availabilityLabel = new javax.swing.JLabel();
        maintainerNameLabel = new javax.swing.JLabel();
        percentageLabel = new javax.swing.JLabel();
        dayLabel = new javax.swing.JLabel();
        selectedDayLabel = new javax.swing.JLabel();
        backToVerificationScreenButton = new javax.swing.JButton();
        informationPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        workspaceNotesInformationScreenPane = new javax.swing.JEditorPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        interventionDescriptionPane = new javax.swing.JEditorPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        skillsNeededInformationPane = new javax.swing.JEditorPane();
        forwardButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        openSMPButton = new javax.swing.JButton();
        smpLabel = new javax.swing.JLabel();
        leftPanel = new javax.swing.JPanel();
        informationScreenLabel = new javax.swing.JLabel();
        verificationScreenLabel = new javax.swing.JLabel();
        assignationScreenLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(0, 51, 102));

        weekLabel.setFont(new java.awt.Font("Segoe UI Symbol", 0, 14)); // NOI18N
        weekLabel.setForeground(new java.awt.Color(255, 255, 255));
        weekLabel.setText("Week n°");

        weekNumberLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        weekNumberLabel.setForeground(new java.awt.Color(255, 255, 255));
        weekNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        weekNumberLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        activityToAssignLabel.setFont(new java.awt.Font("Segoe UI Symbol", 0, 14)); // NOI18N
        activityToAssignLabel.setForeground(new java.awt.Color(255, 255, 255));
        activityToAssignLabel.setText("Activity to assign:");

        activityLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        activityLabel.setForeground(new java.awt.Color(255, 255, 255));
        activityLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        verificationPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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

        skillsNeededVerificationPane.setEditable(false);
        skillsNeededVerificationPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Skills needed"));
        skillsNeededVerificationPane.setContentType("text/html"); // NOI18N
        skillsNeededVerificationPane.setAlignmentX(0.0F);
        skillsNeededVerificationPane.setAlignmentY(0.2F);
        jScrollPane3.setViewportView(skillsNeededVerificationPane);

        maintainerAvailabilityLabel.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        maintainerAvailabilityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        maintainerAvailabilityLabel.setText("Maintainer AVAILABILITY");
        maintainerAvailabilityLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        backToInformationScreenButton.setText("Back");
        backToInformationScreenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToInformationScreenButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout verificationPanelLayout = new javax.swing.GroupLayout(verificationPanel);
        verificationPanel.setLayout(verificationPanelLayout);
        verificationPanelLayout.setHorizontalGroup(
            verificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(verificationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(verificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(verificationPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(backToInformationScreenButton))
                    .addGroup(verificationPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addGroup(verificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(verificationPanelLayout.createSequentialGroup()
                                .addComponent(maintainerAvailabilityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(110, 110, 110)))))
                .addContainerGap())
        );
        verificationPanelLayout.setVerticalGroup(
            verificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, verificationPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(verificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(verificationPanelLayout.createSequentialGroup()
                        .addComponent(maintainerAvailabilityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backToInformationScreenButton)
                .addGap(2, 2, 2))
        );

        assignationPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        dailyAgendaTable.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
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

        workspaceNotesAssignationScreenPane.setEditable(false);
        workspaceNotesAssignationScreenPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Workspace notes"));
        jScrollPane4.setViewportView(workspaceNotesAssignationScreenPane);

        availabilityLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        availabilityLabel.setText("Availability");

        maintainerNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        maintainerNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        maintainerNameLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        percentageLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        percentageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        percentageLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        dayLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        dayLabel.setText("Day");
        dayLabel.setToolTipText("");

        selectedDayLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        selectedDayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectedDayLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        backToVerificationScreenButton.setText("Back");
        backToVerificationScreenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToVerificationScreenButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout assignationPanelLayout = new javax.swing.GroupLayout(assignationPanel);
        assignationPanel.setLayout(assignationPanelLayout);
        assignationPanelLayout.setHorizontalGroup(
            assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, assignationPanelLayout.createSequentialGroup()
                .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(assignationPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(dayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selectedDayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(113, 113, 113))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, assignationPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, assignationPanelLayout.createSequentialGroup()
                        .addComponent(availabilityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(maintainerNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(percentageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(352, 352, 352))
                    .addGroup(assignationPanelLayout.createSequentialGroup()
                        .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(assignationPanelLayout.createSequentialGroup()
                                .addGap(626, 626, 626)
                                .addComponent(backToVerificationScreenButton))
                            .addComponent(jScrollPane1))
                        .addGap(15, 15, 15))))
        );
        assignationPanelLayout.setVerticalGroup(
            assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(assignationPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(assignationPanelLayout.createSequentialGroup()
                        .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(selectedDayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4))
                    .addGroup(assignationPanelLayout.createSequentialGroup()
                        .addGroup(assignationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(availabilityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(maintainerNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(percentageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(backToVerificationScreenButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        workspaceNotesInformationScreenPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Workspace notes"));
        jScrollPane5.setViewportView(workspaceNotesInformationScreenPane);

        interventionDescriptionPane.setEditable(false);
        interventionDescriptionPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Intervention description"));
        jScrollPane6.setViewportView(interventionDescriptionPane);

        skillsNeededInformationPane.setEditable(false);
        skillsNeededInformationPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Skills needed"));
        skillsNeededInformationPane.setContentType("text/html"); // NOI18N
        skillsNeededInformationPane.setAlignmentX(0.0F);
        skillsNeededInformationPane.setAlignmentY(0.2F);
        jScrollPane8.setViewportView(skillsNeededInformationPane);

        forwardButton.setText("Forward");
        forwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forwardButtonActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Standard maintainance procedure"));

        openSMPButton.setText("Open");

        smpLabel.setText("SMP label");
        smpLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(smpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(openSMPButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(openSMPButton)
                    .addComponent(smpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout informationPanelLayout = new javax.swing.GroupLayout(informationPanel);
        informationPanel.setLayout(informationPanelLayout);
        informationPanelLayout.setHorizontalGroup(
            informationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(informationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(informationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(informationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(forwardButton))
                .addContainerGap())
        );
        informationPanelLayout.setVerticalGroup(
            informationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(informationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(informationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(informationPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(informationPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(forwardButton))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(weekLabel)
                        .addGap(18, 18, 18)
                        .addComponent(weekNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(activityToAssignLabel)
                        .addGap(18, 18, 18)
                        .addComponent(activityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(verificationPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(informationPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(assignationPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(39, 39, 39))))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(activityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weekLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weekNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(activityToAssignLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(informationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(verificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(assignationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        leftPanel.setBackground(new java.awt.Color(0, 0, 153));

        informationScreenLabel.setBackground(new java.awt.Color(0, 102, 204));
        informationScreenLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        informationScreenLabel.setForeground(new java.awt.Color(255, 255, 255));
        informationScreenLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        informationScreenLabel.setText("Information screen ");
        informationScreenLabel.setOpaque(true);

        verificationScreenLabel.setBackground(new java.awt.Color(0, 102, 204));
        verificationScreenLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        verificationScreenLabel.setForeground(new java.awt.Color(255, 255, 255));
        verificationScreenLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        verificationScreenLabel.setText("Verification screen ");

        assignationScreenLabel.setBackground(new java.awt.Color(0, 102, 204));
        assignationScreenLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        assignationScreenLabel.setForeground(new java.awt.Color(255, 255, 255));
        assignationScreenLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        assignationScreenLabel.setText("Assignation screen ");

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(informationScreenLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(verificationScreenLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
            .addComponent(assignationScreenLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(informationScreenLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(verificationScreenLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(assignationScreenLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backToVerificationScreenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToVerificationScreenButtonActionPerformed
        showVerificationPanel();
    }//GEN-LAST:event_backToVerificationScreenButtonActionPerformed

    private void forwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forwardButtonActionPerformed
        String newWorkspaceNotes
                = workspaceNotesInformationScreenPane.getText();
        String oldWorkspaceNotes = activity.getWorkspaceNotes();
        if (!newWorkspaceNotes.equals(oldWorkspaceNotes)) {
            //workspace edited
            //update activity in database and locally
            Runnable saver = () -> {
                activity.setWorkspaceNotes(newWorkspaceNotes);
                boolean success = activityBO.update(activity);
                SwingUtilities.invokeLater(() -> {
                    if (!success) {
                        activityUpdateError();
                        activity.setWorkspaceNotes(oldWorkspaceNotes);
                    } else {
                        forwardActivity();
                    }
                });
            };
            new Thread(saver).start();
        } else {
            forwardActivity();
        }
    }//GEN-LAST:event_forwardButtonActionPerformed

    private void backToInformationScreenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToInformationScreenButtonActionPerformed
        showInformationPanel();
    }//GEN-LAST:event_backToInformationScreenButtonActionPerformed

    /**
     * Change panel to show the 'verification screen'.
     */
    private void forwardActivity() {
        initVerificationScreen();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel activityLabel;
    private javax.swing.JLabel activityToAssignLabel;
    private javax.swing.JTable agendaTable;
    private javax.swing.JPanel assignationPanel;
    private javax.swing.JLabel assignationScreenLabel;
    private javax.swing.JLabel availabilityLabel;
    private javax.swing.JButton backToInformationScreenButton;
    private javax.swing.JButton backToVerificationScreenButton;
    private javax.swing.JTable dailyAgendaTable;
    private javax.swing.JLabel dayLabel;
    private javax.swing.JButton forwardButton;
    private javax.swing.JPanel informationPanel;
    private javax.swing.JLabel informationScreenLabel;
    private javax.swing.JEditorPane interventionDescriptionPane;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel maintainerAvailabilityLabel;
    private javax.swing.JLabel maintainerNameLabel;
    private javax.swing.JButton openSMPButton;
    private javax.swing.JLabel percentageLabel;
    private javax.swing.JLabel selectedDayLabel;
    private javax.swing.JEditorPane skillsNeededInformationPane;
    private javax.swing.JEditorPane skillsNeededVerificationPane;
    private javax.swing.JLabel smpLabel;
    private javax.swing.JPanel verificationPanel;
    private javax.swing.JLabel verificationScreenLabel;
    private javax.swing.JLabel weekLabel;
    private javax.swing.JLabel weekNumberLabel;
    private javax.swing.JEditorPane workspaceNotesAssignationScreenPane;
    private javax.swing.JEditorPane workspaceNotesInformationScreenPane;
    // End of variables declaration//GEN-END:variables
}
