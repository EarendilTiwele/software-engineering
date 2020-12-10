/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Procedure;
import java.util.Set;

/**
 *
 * @author avall
 */
public interface ProcedureDAO {

    /**
     * Inserts a <code>Procedure</code> object in a persistent storage. Returns
     * the id of the inserted <code>procedure</code> if the operation is
     * successful; -1 otherwise.
     *
     * @param procedure the <code>Procedure</code> object to insert
     * @return the id of the inserted <code>procedure</code> if the operation is
     * successful; -1 otherwise
     */
    public int insert(Procedure procedure);

    /**
     * Updates a <code>Procedure</code> object in a persistent storage, if the
     * <code>procedure</code> is not present in the persistent storage it is not
     * created. Returns <code>true</code> if the operation is successful, that
     * is both when the <code>procedure</code> is updated and when the
     * <code>procedure</code> doesn't exist in the persistent storage;
     * <code>false</code> otherwise.
     *
     * @param procedure the <code>Procedure</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the procedure is updated and when the procedure doesn't exist in the
     * persistent storage; <code>false</code> otherwise
     */
    public boolean update(Procedure procedure);

    /**
     * Deletes the procedure with given <code>id</code> from a persistent
     * storage. Returns <code>true</code> if the operation is successful, that
     * is both when the procedure with given <code>id</code> is deleted and when
     * the procedure with given <code>id</code> doesn't exist in the persistent
     * storage; <code>false</code> otherwise.
     *
     * @param id the <code>id</code> which identifies the procedure
     * @return <code>true</code> if the operation is successful, that is both
     * when the procedure with given <code>id</code> is deleted and when the
     * procedure with given <code>id</code> doesn't exist in the persistent
     * storage; <code>false</code> otherwise
     */
    public boolean delete(int id);

    /**
     * Retrieves a <code>Set</code> of <code>Procedure</code> objects from a
     * persistent storage. Returns the <code>Set</code> of
     * <code>Procedure</code> objects if the operation is successful;
     * <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>Procedure</code> objects from the
     * persistent storage if the operation is successful; <code>null</code>
     * otherwise
     */
    public Set<Procedure> getAll();

    /**
     * Retrieves the <code>Procedure</code> object with given <code>id</code>
     * from a persistent storage. Returns the <code>Procedure</code> object with
     * given <code>id</code> if it exists in the persistent storage;
     * <code>null</code> if the <code>Procedure</code> object with given
     * <code>id</code> doesn't exist in the persistent storage or if the
     * operation fails.
     *
     * @param id the id which identifies the procedure
     * @return the <code>Procedure</code> object with given <code>id</code> if
     * it exists in the persistent storage, returns <code>null</code> if the
     * <code>Procedure</code> object with given <code>id</code> doesn't exist in
     * the persistent storage or if the operation fails
     */
    public Procedure get(int id);
}
