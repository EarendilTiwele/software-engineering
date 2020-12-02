/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.AbstractDAL;
import dataaccesslayer.AssignmentDAL;
import dataaccesslayer.AssignmentDALDatabase;
import dataaccesslayer.UserDALDatabase;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Alfonso
 */
public class AssignmentBLL {

    private AssignmentDAL assignmentDAL;
    private UserBLL userBLL;

    private static final int NUM_WORKING_DAYS = 7;
    private static final int DAILY_WORKING_MINUTES = 7 * 60;

    private static final String MONDAY = "Mon";
    private static final String TUESDAY = "Tue";
    private static final String WEDNESDAY = "Wed";
    private static final String THURSDAY = "Thu";
    private static final String FRIDAY = "Fri";
    private static final String SATURDAY = "Sat";
    private static final String SUNDAY = "Sun";

    
    public AssignmentBLL() {
        this.assignmentDAL = new AssignmentDALDatabase();
        this.userBLL = new UserBLL();
    }

    


    /**
     * Returns in a map the percentage of daily availability of each maintenance
     * into specified week. 
     * 
     * @param week          
     * @return
     * @throws SQLException 
     */
    public Map<Maintainer, Integer[]> getAgenda(int week) throws SQLException {
        // Load  the whole assigments for the specified week
        Set<Assignment> assignments = assignmentDAL.getAllForWeek(week);
        // Load the set of all maintainers 
        Set<Maintainer> maintainers = userBLL.getAllMaintainers();
        // Association between a maintainer and his percentage of disponibility
        Map<Maintainer, Integer[]> agenda = new HashMap<>();
        
        // associates at each maintainer the array of disponabilities, initialy all
        // maintainers have all days free 
        for (Maintainer maintainer : maintainers) {
            Integer[] availabilities = new Integer[NUM_WORKING_DAYS];
            Arrays.fill(availabilities, DAILY_WORKING_MINUTES);
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
        
        // Converts in percentage the daily disponibility of maintaners
        for (Integer[] availability : agenda.values()) {
            for (int i = 0; i < availability.length; i++) {
                availability[i] = (int) (availability[i] * 100 / ((float) DAILY_WORKING_MINUTES));
            }
        }

        return agenda;
    }

    
}
