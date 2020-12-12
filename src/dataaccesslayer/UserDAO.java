/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.User;
import java.util.Set;

/**
 *
 * @author alexd
 */
public interface UserDAO {

    /**
     * Inserts a <code>User</code> object in a persistent storage. Returns the
     * id of the inserted <code>user</code> if the operation is successful; -1
     * otherwise.
     *
     * @param user the <code>User</code> object to insert
     * @return the id of the inserted <code>user</code> if the operation is
     * successful; -1 otherwise
     */
    public int insert(User user);
    
    /**
     * Updates a <code>User</code> object in a persistent storage, if the
     * <code>user</code> is not present in the persistent storage it is not
     * created. Returns <code>true</code> if the operation is successful, that
     * is both when the <code>user</code> is updated and when the
     * <code>user</code> doesn't exist in the persistent storage;
     * <code>false</code> otherwise.
     *
     * @param user the <code>User</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the user is updated and when the user doesn't exist in the
     * persistent storage; <code>false</code> otherwise
     */
    public boolean update(User user);
    
    /**
     * Retrieves the <code>User</code> object with given <code>id</code> from a
     * persistent storage. Returns the <code>User</code> object with given
     * <code>id</code> if it exists in the persistent storage; <code>null</code>
     * if the <code>User</code> object with given <code>id</code> doesn't exist
     * in the persistent storage or if the operation fails.
     *
     * @param id the id which identifies the user
     * @return the <code>User</code> object with given <code>id</code> if it
     * exists in the persistent storage, returns <code>null</code> if the
     * <code>User</code> object with given <code>id</code> doesn't exist in the
     * persistent storage or if the operation fails
     */
    public User get(int id);

    /**
     * Retrieves a <code>Set</code> of <code>User</code> objects with maintainer
     * role from a persistent storage. Returns the <code>Set</code> of
     * <code>User</code> objects with maintainer role if the operation is
     * successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>User</code> objects with maintainer
     * role from the persistent storage if the operation is successful;
     * <code>null</code> otherwise
     */
    public Set<User> getAllMaintainers();

    /**
     * Retrieves a <code>Set</code> of <code>User</code> objects from a
     * persistent storage. Returns the <code>Set</code> of <code>User</code>
     * objects if the operation is successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>User</code> objects from the
     * persistent storage if the operation is successful; <code>null</code>
     * otherwise
     */
    public Set<User> getAll();
    
    
}
