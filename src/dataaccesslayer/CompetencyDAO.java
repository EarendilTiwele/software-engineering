/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Competency;
import java.util.Set;

/**
 *
 * @author alexd
 */
public interface CompetencyDAO {

    /**
     * Retrieves the <code>Competency</code> object with given <code>id</code>
     * from a persistent storage. Returns the <code>Competency</code> object
     * with given <code>id</code> if it exists in the persistent storage;
     * <code>null</code> if the <code>Competency</code> object with given
     * <code>id</code> doesn't exist in the persistent storage or if the
     * operation fails.
     *
     * @param id the id which identifies the competency
     * @return the <code>Competency</code> object with given <code>id</code> if
     * it exists in the persistent storage, returns <code>null</code> if the
     * <code>Competency</code> object with given <code>id</code> doesn't exist
     * in the persistent storage or if the operation fails
     */
    public Competency get(int id);

    /**
     * Retrieves a <code>Set</code> of <code>Competency</code> objects from a
     * persistent storage. Returns the <code>Set</code> of
     * <code>Competency</code> objects if the operation is successful;
     * <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>Competency</code> objects from the
     * persistent storage if the operation is successful; <code>null</code>
     * otherwise
     */
    public Set<Competency> getAll();

}
