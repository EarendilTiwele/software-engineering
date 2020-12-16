/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Activity;
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

    /**
     * Inserts a <code>Activity</code> object in a persistent storage. Returns
     * the id of the inserted <code>activity</code> if the operation is
     * successful; -1 otherwise.
     *
     * @param activity the <code>Activity</code> object to insert
     * @return the id of the inserted <code>Activity</code> if the operation is
     * successful; -1 otherwise
     */
    public int insert(Activity activity) {
        return activityDAO.insert(activity);
    }

    /**
     * Updates a <code>Activity</code> object in a persistent storage, if the
     * <code>Activity</code> is not present in the persistent storage it is not
     * created. Returns <code>true</code> if the operation is successful, that
     * is both when the <code>activity</code> is updated and when the
     * <code>activity</code> doesn't exist in the persistent storage;
     * <code>false</code> otherwise.
     *
     * @param activity the <code>Activity</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the activity is updated and when the activity doesn't exist in the
     * persistent storage; <code>false</code> otherwise
     */
    public boolean update(Activity activity) {
        return activityDAO.update(activity);
    }

    /**
     * Deletes the activity with given <code>id</code> from a persistent
     * storage. Returns <code>true</code> if the operation is successful, that
     * is both when the activity with given <code>id</code> is deleted and when
     * the activity with given <code>id</code> doesn't exist in the persistent
     * storage; <code>false</code> otherwise.
     *
     * @param id the <code>id</code> which identifies the activity
     * @return <code>true</code> if the operation is successful, that is both
     * when the activity with given <code>id</code> is deleted and when the
     * activity with given <code>id</code> doesn't exist in the persistent
     * storage; <code>false</code> otherwise
     */
    public boolean delete(int id) {
        return activityDAO.delete(id);
    }

    /**
     * Retrieves a <code>List</code> of <code>Activity</code> objects from a
     * persistent storage. Returns the <code>List</code> of
     * <code>Activity</code> objects if the operation is successful;
     * <code>null</code> otherwise.
     *
     * @return the <code>List</code> of <code>Activity</code> objects from the
     * persistent storage if the operation is successful; <code>null</code>
     * otherwise
     */
    public List<Activity> getAll() {
        Set<Activity> activities = activityDAO.getAll();
        return activities != null ? new ArrayList<>(activities) : null;
    }

    /**
     * Retrieves the <code>Activity</code> object with given <code>id</code>
     * from a persistent storage. Returns the <code>Activity</code> object with
     * given <code>id</code> if it exists in the persistent storage;
     * <code>null</code> if the <code>Activity</code> object with given
     * <code>id</code> doesn't exist in the persistent storage or if the
     * operation fails.
     *
     * @param id the id which identifies the activity
     * @return the <code>Activity</code> object with given <code>id</code> if it
     * exists in the persistent storage, returns <code>null</code> if the
     * <code>Activity</code> object with given <code>id</code> doesn't exist in
     * the persistent storage or if the operation fails
     */
    public Activity get(int id) {
        return activityDAO.get(id);
    }

    /**
     * Retrieves a <code>List</code> of <code>Activity</code> objects with a
     * specific <code>week</code> from a persistent storage. Returns the
     * <code>List</code> of <code>Activity</code> objects if the operation is
     * successful; <code>null</code> otherwise.
     *
     * @param week
     * @return the <code>List</code> of <code>Activity</code> objects with a
     * specific week from the persistent storage if the operation is successful;
     * <code>null</code> otherwise
     */
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

    /**
     * Retrieves a <code>List</code> of <code>PlannedActivity</code> objects
     * with a specific <code>week</code> from a persistent storage. Returns the
     * <code>List</code> of <code>PlannedActivity</code> objects if the
     * operation is successful; <code>null</code> otherwise.
     *
     * @param week
     * @return the <code>List</code> of <code>Activity</code> objects with a
     * specific week from the persistent storage if the operation is successful;
     * <code>null</code> otherwise
     */
    public List<Activity> getAllPlannedOfWeek(int week) {
        Set<Activity> activities = activityDAO.getAllPlannedOfWeek(week);
        return activities != null ? new ArrayList<>(activities) : null;
    }

}
