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
     * Retrieves the <code>User</code> object with given <code>id</code> from a
     * persistent storage. Returns the <code>User</code> object with given
     * <code>id</code> if it exists in the persistent storage; <code>null</code>
     * if the <code>User</code> object with given <code>id</code> doesn't exist
     * in the persistent storage or if the operation fails.
     *
     * @param id the id which identifies the site
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

}
