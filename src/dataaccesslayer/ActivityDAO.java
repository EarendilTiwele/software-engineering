/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Activity;
import java.util.Set;

/**
 *
 * @author alexd
 */
public interface ActivityDAO {

    /**
     * Inserts a <code>Activity</code> object in a persistent storage. Returns
     * the id of the inserted <code>activity</code> if the operation is
     * successful; -1 otherwise.
     *
     * @param activity the <code>Activity</code> object to insert
     * @return the id of the inserted <code>Activity</code> if the operation is
     * successful; -1 otherwise
     */
    public int insert(Activity activity);

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
    public boolean update(Activity activity);

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
    public boolean delete(int id);

    /**
     * Retrieves a <code>Set</code> of <code>Activity</code> objects from a
     * persistent storage. Returns the <code>Set</code> of <code>Activity</code>
     * objects if the operation is successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>Activity</code> objects from the
     * persistent storage if the operation is successful; <code>null</code>
     * otherwise
     */
    public Set<Activity> getAll();

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
    public Activity get(int id);

    /**
     * Retrieves a <code>Set</code> of <code>Activity</code> objects with a
     * specific <code>week</code> from a persistent storage. Returns the
     * <code>Set</code> of <code>Activity</code> objects if the operation is
     * successful; <code>null</code> otherwise.
     *
     * @param week
     * @return the <code>Set</code> of <code>Activity</code> objects with a
     * specific week from the persistent storage if the operation is successful;
     * <code>null</code> otherwise
     */
    public Set<Activity> getAllOfWeek(int week);

    /**
     * Retrieves a <code>Set</code> of <code>PlannedActivity</code> objects with
     * a specific <code>week</code> from a persistent storage. Returns the
     * <code>Set</code> of <code>PlannedActivity</code> objects if the operation
     * is successful; <code>null</code> otherwise.
     *
     * @param week
     * @return the <code>Set</code> of <code>Activity</code> objects with a
     * specific week from the persistent storage if the operation is successful;
     * <code>null</code> otherwise
     */
    public Set<Activity> getAllPlannedOfWeek(int week);

}
