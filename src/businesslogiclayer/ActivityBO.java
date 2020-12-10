/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Activity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dataaccesslayer.ActivityDAO;
import dataaccesslayer.DAOFactory;
import datatransferobjects.Assignment;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class ActivityBO {

    private final ActivityDAO activityDAO;
    private final AssignmentBO assignmentBO;

    public ActivityBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        activityDAO = postgresFactory.getActivityDAO();
        assignmentBO = new AssignmentBO();
    }

    public int insert(Activity activity) {
        return activityDAO.insert(activity);
    }

    public boolean update(Activity activity) {
        return activityDAO.update(activity);
    }

    public boolean delete(int id) {
        return activityDAO.delete(id);
    }

    public List<Activity> getAll() {
        return new ArrayList<>(activityDAO.getAll());
    }

    public Activity get(int id) {
        return activityDAO.get(id);
    }

    public List<Activity> getAllOfWeek(int week) {
        Set<Activity> activities = activityDAO.getAllOfWeek(week);
        //error while loading activities
        if (activities == null) {
            return null;
        }

        Set<Assignment> assignments = assignmentBO.getAllForWeek(week);
        //error while loading assignments
        if (assignments == null) {
            return null;
        }

        for (Assignment assignment : assignments) {
            activities.remove(assignment.getActivity());
        }
        return new ArrayList<>(activities);
    }

    public List<Activity> getAllPlannedOfWeek(int week) {
        return new ArrayList<>(activityDAO.getAllPlannedOfWeek(week));
    }

}
