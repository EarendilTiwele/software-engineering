/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Site;
import java.util.Set;

/**
 *
 * @author alexd
 */
public interface SiteDAO {

    /**
     * Inserts a <code>Site</code> object in a persistent storage. Returns the
     * id of the inserted <code>site</code> if the operation is successful; -1
     * otherwise.
     *
     * @param site the <code>Site</code> object to insert
     * @return the id of the inserted <code>site</code> if the operation is
     * successful; -1 otherwise
     */
    public int insert(Site site);

    /**
     * Updates a <code>Site</code> object in a persistent storage, if the
     * <code>site</code> is not present in the persistent storage it is not
     * created. Returns <code>true</code> if the operation is successful, that
     * is both when the <code>site</code> is updated and when the
     * <code>site</code> doesn't exist in the persistent storage;
     * <code>false</code> otherwise.
     *
     * @param site the <code>Site</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the site is updated and when the site doesn't exist in the
     * persistent storage; <code>false</code> otherwise
     */
    public boolean update(Site site);

    /**
     * Deletes the site with given <code>id</code> from a persistent storage.
     * Returns <code>true</code> if the operation is successful, that is both
     * when the site with given <code>id</code> is deleted and when the site
     * with given <code>id</code> doesn't exist in the persistent storage;
     * <code>false</code> otherwise.
     *
     * @param id the <code>id</code> which identifies the site
     * @return <code>true</code> if the operation is successful, that is both
     * when the site with given <code>id</code> is deleted and when the site
     * with given <code>id</code> doesn't exist in the persistent storage;
     * <code>false</code> otherwise
     */
    public boolean delete(int id);

    /**
     * Retrieves the <code>Site</code> object with given <code>id</code> from a
     * persistent storage. Returns the <code>Site</code> object with given
     * <code>id</code> if it exists in the persistent storage; <code>null</code>
     * if the <code>Site</code> object with given <code>id</code> doesn't exist
     * in the persistent storage or if the operation fails.
     *
     * @param id the id which identifies the site
     * @return the <code>Site</code> object with given <code>id</code> if it
     * exists in the persistent storage, returns <code>null</code> if the
     * <code>Site</code> object with given <code>id</code> doesn't exist in the
     * persistent storage or if the operation fails
     */
    public Site get(int id);

    /**
     * Retrieves a <code>Set</code> of <code>Site</code> objects from a
     * persistent storage. Returns the <code>Set</code> of <code>Site</code>
     * objects if the operation is successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>Site</code> objects from the
     * persistent storage if the operation is successful; <code>null</code>
     * otherwise
     */
    public Set<Site> getAll();

}
