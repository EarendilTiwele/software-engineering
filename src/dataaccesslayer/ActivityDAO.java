/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Activity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public interface ActivityDAO {

    /**
     * Insert an <code>activity</code> in the database
     *
     * @param activity
     * @return the id of activity if the activity is inserted correctly;
     * otherwise - 1 if the insert operation is failed
     * @throws SQLException
     */
    public int insert(Activity activity);

    /**
     * Update an <code>activity</code> in the database
     *
     * @param activity
     * @return true if the activity has been updated or if there was not the
     * activity; otherwise false in case of error.
     */
    public boolean update(Activity activity);

    /**
     * Delete an <code>activity</code> in the database
     *
     * @param id
     * @return true if the activity has been deleted or if there was not the
     * activity in the database; otherwise false in case of error.
     */
    public boolean delete(int id);

    /**
     * Return the Set of the activities present in the database
     *
     * @return the set of the activities; otherwise null in case of error.
     */
    public Set<Activity> getAll();

    /**
     *
     * @param id
     * @return
     */
    public Activity get(int id);

    /**
     * Return the Set of the activities present in the database for a specified
     * <code>week</code>
     *
     * @return the set of the activities with the <code>week</code> specified;
     * otherwise null in case of error.
     */
    public Set<Activity> getAllOfWeek(int week);

    /**
     * Return the Set of the activities present in the database for a specified
     * <code>week</code>
     *
     * @return the set of the planned activities with the <code>week</code>
     * specified; otherwise null in case of error.
     */
    public Set<Activity> getAllPlannedOfWeek(int week);

}
