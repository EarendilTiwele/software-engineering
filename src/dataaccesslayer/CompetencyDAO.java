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
     * Inserts a <code>Competency</code> object in a persistent storage. Returns
     * the id of the inserted <code>competency</code> if the operation is
     * successful; -1 otherwise.
     *
     * @param competency the <code>Competency</code> object to insert
     * @return the id of the inserted <code>competency</code> if the operation
     * is successful; -1 otherwise
     */
    public int insert(Competency competency);

    /**
     * Updates a <code>Competency</code> object in a persistent storage, if the
     * <code>competency</code> is not present in the persistent storage it is
     * not created. Returns <code>true</code> if the operation is successful,
     * that is both when the <code>competency</code> is updated and when the
     * <code>competency</code> doesn't exist in the persistent storage;
     * <code>false</code> otherwise.
     *
     * @param competency the <code>Competency</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the competency is updated and when the competency doesn't exist in
     * the persistent storage; <code>false</code> otherwise
     */
    public boolean update(Competency competency);

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
