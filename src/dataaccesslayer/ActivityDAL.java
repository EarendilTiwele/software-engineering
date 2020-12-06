/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Activity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

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
    public Activity insert(Activity activity) throws SQLException;

    /**
     * Update an activity
     *
     * @param activity
     * @return the version of the activity presents after the update operation.
     */
    public Activity update(Activity activity) throws SQLException;

    /**
     * Delete an activity
     *
     * @param id
     * @return the version of the activity presents before the delete operation.
     */
    public Activity delete(int id) throws SQLException;

    /**
     * Retrieve all activities present
     *
     * @return the list of activities
     */
    public Set<Activity> getAll() throws SQLException;

    /**
     * Retrieve the activity with a specified id
     *
     * @param id
     * @return the activity with the specified id
     */
    public Activity get(int id) throws SQLException;

    /**
     * Retrieve the activities with a specified week
     *
     * @param week
     * @return the activities with the specified week
     * @throws java.sql.SQLException
     */
    public Set<Activity> getAllOfWeek(int week) throws SQLException;

    /**
     * Retrieve the planned activities with a specified week
     *
     * @param week
     * @return
     * @throws java.sql.SQLException
     */
    public Set<Activity> getAllPlannedOfWeek(int week) throws SQLException;

    /**
     * Delete all activities
     * @return the list of activities before the delete operation
     * @throws java.sql.SQLException
     */
    public Set<Activity> deleteAll() throws SQLException;

}
