/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Typology;
import java.util.Set;

/**
 *
 * @author alexd
 */
public interface TypologyDAO {

    /**
     * Inserts a <code>Typology</code> object in a persistent storage. Returns
     * the id of the inserted <code>typology</code> if the operation is
     * successful; -1 otherwise.
     *
     * @param typology the <code>Typology</code> object to insert
     * @return the id of the inserted <code>typology</code> if the operation is
     * successful; -1 otherwise
     */
    public int insert(Typology typology);

    /**
     * Updates a <code>Typology</code> object in a persistent storage, if the
     * <code>typology</code> is not present in the persistent storage it is not
     * created. Returns <code>true</code> if the operation is successful, that
     * is both when the <code>typology</code> is updated and when the
     * <code>typology</code> doesn't exist in the persistent storage;
     * <code>false</code> otherwise.
     *
     * @param typology the <code>Typology</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the typology is updated and when the typology doesn't exist in the
     * persistent storage; <code>false</code> otherwise
     */
    public boolean update(Typology typology);

    /**
     * Deletes the typology with given <code>id</code> from a persistent
     * storage. Returns <code>true</code> if the operation is successful, that
     * is both when the typology with given <code>id</code> is deleted and when
     * the typology with given <code>id</code> doesn't exist in the persistent
     * storage; <code>false</code> otherwise.
     *
     * @param id the <code>id</code> which identifies the typology
     * @return <code>true</code> if the operation is successful, that is both
     * when the typology with given <code>id</code> is deleted and when the
     * typology with given <code>id</code> doesn't exist in the persistent
     * storage; <code>false</code> otherwise
     */
    public boolean delete(int id);

    /**
     * Retrieves the <code>Typology</code> object with given <code>id</code>
     * from a persistent storage. Returns the <code>Typology</code> object with
     * given <code>id</code> if it exists in the persistent storage;
     * <code>null</code> if the <code>Typology</code> object with given
     * <code>id</code> doesn't exist in the persistent storage or if the
     * operation fails.
     *
     * @param id the id which identifies the typology
     * @return the <code>Typology</code> object with given <code>id</code> if it
     * exists in the persistent storage, returns <code>null</code> if the
     * <code>Typology</code> object with given <code>id</code> doesn't exist in
     * the persistent storage or if the operation fails
     */
    public Typology get(int id);

    /**
     * Retrieves a <code>Set</code> of <code>Typology</code> objects from a
     * persistent storage. Returns the <code>Set</code> of <code>Typology</code>
     * objects if the operation is successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>Typology</code> objects from the
     * persistent storage if the operation is successful; <code>null</code>
     * otherwise
     */
    public Set<Typology> getAll();

}
