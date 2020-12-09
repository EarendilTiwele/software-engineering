/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Site;
import dataaccesslayer.SiteDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexd
 */
public class PostgresSiteDAO extends PostgresAbstractDAO<Site> implements SiteDAO {

    /**
     * Returns the <code>Site</code> object builded on the current row of the
     * ResultSet <code>rs</code>.
     *
     * @param rs the ResultSet with which to build the <code>Site</code> object
     * @return the <code>Site</code> object builded on the current row of the
     * ResultSet <code>rs</code>
     * @throws SQLException if a database access error occurs
     */
    @Override
    Site convertToEntity(ResultSet rs) throws SQLException {
        Site dbSite = new Site(rs.getInt("id"), rs.getString("factory"),
                rs.getString("area"));
        return dbSite;
    }

    /**
     * Inserts a <code>Site</code> object in the Postgres Database. Returns the
     * id of the inserted <code>site</code> if the operation is successful; -1
     * otherwise.
     *
     * @param site the <code>Site</code> object to insert
     * @return the id of the inserted <code>site</code> if the operation is
     * successful; -1 otherwise
     */
    @Override
    public int insert(Site site) {
        String query = String.format("insert into site "
                + "(factory, area)"
                + "values ('%s', '%s') returning *;",
                site.getFactory(), site.getArea());
        try {
            return executeQuery(query).getId();
        } catch (SQLException ex) {
            Logger.getLogger(PostgresSiteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    /**
     * Updates a <code>Site</code> object in the Postgres Database, if the
     * <code>site</code> is not present in the Postgres Database it is not
     * created. Returns <code>true</code> if the operation is successful, that
     * is both when the <code>site</code> is updated and when the
     * <code>site</code> doesn't exist in the Postgres Database;
     * <code>false</code> otherwise.
     *
     * @param site the <code>Site</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the site is updated and when the site doesn't exist in the Postgres
     * Database; <code>false</code> otherwise
     */
    @Override
    public boolean update(Site site) {
        String query = String.format("update site "
                + "set factory = '%s', area = '%s' "
                + "where id = %d;",
                site.getFactory(), site.getArea(), site.getId());
        try {
            executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresSiteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Deletes the site with given <code>id</code> from the Postgres Database.
     * Returns <code>true</code> if the operation is successful, that is both
     * when the site with given <code>id</code> is deleted and when the site
     * with given <code>id</code> doesn't exist in the Postgres Database;
     * <code>false</code> otherwise.
     *
     * @param id the <code>id</code> which identifies the site
     * @return <code>true</code> if the operation is successful, that is both
     * when the site with given <code>id</code> is deleted and when the site
     * with given <code>id</code> doesn't exist in the Postgres Database;
     * <code>false</code> otherwise
     */
    @Override
    public boolean delete(int id) {
        String query = String.format("delete from site "
                + "where id = %d;", id);
        try {
            executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresSiteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Retrieves the <code>Site</code> object with given <code>id</code> from
     * the Postgres Database. Returns the <code>Site</code> object with given
     * <code>id</code> if it exists in the Postgres Database; <code>null</code>
     * if the <code>Site</code> object with given <code>id</code> doesn't exist
     * in the Postgres Database or if the operation fails.
     *
     * @param id the id which identifies the site
     * @return the <code>Site</code> object with given <code>id</code> if it
     * exists in the Postgres Database, returns <code>null</code> if the
     * <code>Site</code> object with given <code>id</code> doesn't exist in the
     * Postgres Database or if the operation fails
     */
    @Override
    public Site get(int id) {
        String query = String.format("select * from site "
                + "where id = %d;", id);
        try {
            return executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresSiteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retrieves a <code>Set</code> of <code>Site</code> objects from the
     * Postgres Database. Returns the <code>Set</code> of <code>Site</code>
     * objects if the operation is successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>Site</code> objects from the
     * Postgres Database if the operation is successful; <code>null</code>
     * otherwise
     */
    @Override
    public Set<Site> getAll() {
        String query = "select * from site;";
        try {
            return executeSetQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresSiteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
