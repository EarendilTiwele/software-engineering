/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Activity;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author alexd
 */
public interface ActivityDAL {

    /**
     * Insert an activity
     *
     * @param activity
     * @return the version of the activity presents after the insert operation.
     */
    public Activity insert(Activity activity);

    /**
     * Update an activity
     *
     * @param activity
     * @return the version of the activity presents after the update operation.
     */
    public Activity update(Activity activity);

    /**
     * Delete an activity
     *
     * @param id
     * @return the version of the activity presents before the delete operation.
     */
    public Activity delete(int id);

    /**
     * Retrieve all activities present
     *
     * @return the list of activities
     */
    public List<Activity> getAll();

    /**
     * Retrieve the activity with a specified id
     *
     * @param id
     * @return the activity with the specified id
     */
    public Activity get(int id);

    /**
     * Retrieve the activities with a specified week
     *
     * @param week
     * @return the activities with the specified week
     */
    public List<Activity> getAllOfWeek(int week);

    /**
     * Retrieve the planned activities with a specified week
     *
     * @param week
     * @return
     */
    public List<Activity> getAllPlannedOfWeek(int week);

    /**
     * Delete all activities
     * @return the list of activities before the delete operation
     */
    public List<Activity> deleteAll();

}
