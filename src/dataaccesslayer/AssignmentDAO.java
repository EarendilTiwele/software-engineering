/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Assignment;
import java.util.Set;

/**
 *
 * @author avall
 */
public interface AssignmentDAO {

    /**
     * Gets all Assignment with a specific <code>week</code> from the persistent
     * storage. Returns the set of the assignments in the persistent storage;
     * otherwise null.
     *
     * @param week
     * @return the <code>Set</code> of <code>Assignment</code> objects from the
     * persistent storage if the operation is successful; <code>null</code>
     * otherwise
     *
     */
    public Set<Assignment> getAllForWeek(int week);

    /**
     * Inserts a <code>Assignment</code> object in the persistent storage.
     * Returns <code>true</code> if the operation is successful;
     * <code>false</code> otherwise.
     *
     * @param assignment the <code>Assignment</code> object to insert.
     * @return <code>true</code> if the operation is successful;
     * <code>false</code> otherwise.
     */
    public boolean insert(Assignment assignment);
}
