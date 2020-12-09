/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Assignment;
import datatransferobjects.Activity;
import datatransferobjects.Maintainer;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import dataaccesslayer.AssignmentDAO;
import dataaccesslayer.DAOFactory;
import java.util.stream.Stream;

/**
 *
 * @author Alfonso
 */
public class AssignmentBO {

    private final AssignmentDAO assignmentDAO;

    public static final int WORKING_DAYS_PER_WEEK = 7;
    public static final int WORKING_HOURS_PER_DAY = 7;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int WORKING_MINUTES_PER_DAY
            = WORKING_HOURS_PER_DAY * MINUTES_PER_HOUR;

    //constants representing the days of week
    public static final String MONDAY = "Mon";
    public static final String TUESDAY = "Tue";
    public static final String WEDNESDAY = "Wed";
    public static final String THURSDAY = "Thu";
    public static final String FRIDAY = "Fri";
    public static final String SATURDAY = "Sat";
    public static final String SUNDAY = "Sun";

    //costants representing indexes of hours
    //used to retrieve availabilities from the daily agenda
    public static final int H8 = 0;
    public static final int H9 = 1;
    public static final int H10 = 2;
    public static final int H11 = 3;
    public static final int H14 = 4;
    public static final int H15 = 5;
    public static final int H16 = 6;

    public AssignmentBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        assignmentDAO = postgresFactory.getAssignmentDAO();
    }

    public Set<Assignment> getAllforWeek(int week) {
        return assignmentDAO.getAllForWeek(week);
    }

    public boolean insert(Assignment assignment) {
        return true;
    }

    public boolean validate(Assignment assignment, Integer[] dailyAgenda) {
        return true;
    }

    /**
     * Returns in a map the percentage of daily availability of each maintainer
     * depending on the assignments.
     *
     * @param assignments the set of assignments in a specific week
     * @param maintainers the set of all the maintainers in the system
     * @return
     *
     */
    public Map<Maintainer, Integer[]> getAgenda(Set<Assignment> assignments,
            Set<Maintainer> maintainers) {
        // Association between a maintainer and his percentage of availability
        Map<Maintainer, Integer[]> agenda = new HashMap<>();

        // associates at each maintainer the array of availabilities, initially all
        // maintainers have all days free
        for (Maintainer maintainer : maintainers) {
            Integer[] availabilities = new Integer[WORKING_DAYS_PER_WEEK];
            Arrays.fill(availabilities, WORKING_MINUTES_PER_DAY);
            agenda.put(maintainer, availabilities);

        }

        List<String> days = Arrays.asList(new String[]{MONDAY, TUESDAY,
            WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY});

        for (Assignment assignment : assignments) {
            Maintainer maintainer = assignment.getMaintainer();
            Activity activity = assignment.getActivity();
            int dayIndex = days.indexOf(assignment.getDay());
            int interventionTime = activity.getInterventionTime();

            // Update the array of disponibilities of maintainer
            agenda.get(maintainer)[dayIndex] -= interventionTime;

        }

        // Converts in percentage the daily availabilities of maintaners
        for (Integer[] availability : agenda.values()) {
            for (int i = 0; i < availability.length; i++) {
                availability[i] = (int) (availability[i] * 100 / ((float) WORKING_MINUTES_PER_DAY));
            }
        }

        return agenda;
    }

    /**
     * Returns an array of integers representing the maintaiener's availability
     * in a specified week and day. The availability is expressed in available
     * minutes in a hour. To retrieve the availability at a certain hour use the
     * appropriate constants. For example, <code>getDailyAgenda(...)[H8]</code>
     * returns the number of availabile minutes for the specified maintainer
     * from 8 o' clock to 9 o' clock.
     *
     * @param maintainer the maintainer to assign the activity to
     * @param assignments the set of all assignments
     * @param week the week of the scheduled activity
     * @param day the day of the assignment: MONDAY, TUESDAY, WEDNESDAY,
     * THURSDAY, FRIDAY, SATURADAY, SUNDAY
     * @return the array representing the maintaiener's availability
     */
    public Integer[] getDailyAgenda(Maintainer maintainer, Set<Assignment> assignments, int week, String day) {
        Integer[] dailyAgenda = new Integer[WORKING_HOURS_PER_DAY];
        Arrays.fill(dailyAgenda, MINUTES_PER_HOUR);

        //discard useless assignments
        Stream<Assignment> filterdAssignment = assignments
                .stream()
                .filter(assignment
                        -> assignment.getActivity().getWeek() == week
                && assignment.getDay().equals(day)
                && assignment.getMaintainer().equals(maintainer)
                );

        //update the daily agenda
        filterdAssignment.forEach(assignment -> {
            int hour = assignment.getHour();
            int interventionTime = assignment.getActivity().getInterventionTime();
            int index = hourToIndex(hour);

            //set fully busy hours to zero available minutes
            for (int i = 0; i < interventionTime / MINUTES_PER_HOUR; i++) {
                dailyAgenda[index + i] = 0;
            }

            //set remaining available minutes
            if ((index + interventionTime / MINUTES_PER_HOUR) <= H16) {
                dailyAgenda[index + interventionTime / MINUTES_PER_HOUR]
                        -= interventionTime % MINUTES_PER_HOUR;
            }
        });
        return dailyAgenda;
    }

    /**
     * Convert an hour (8, 9, 10, 11, 14, 15, 16) to an index (H8, H9, H10, H11,
     * H14, H15, H16). Returns -1 if hour is not valid.
     *
     * @param hour the hour
     * @return the related constant (H8, H9, H10, H11, H14, H15, H16)
     */
    private int hourToIndex(int hour) {
        switch (hour) {
            case 8:
                return H8;
            case 9:
                return H9;
            case 10:
                return H10;
            case 11:
                return H11;
            case 14:
                return H14;
            case 15:
                return H15;
            case 16:
                return H16;
            default:
                return -1;
        }
    }

}
