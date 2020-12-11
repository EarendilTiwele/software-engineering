/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Maintainer;
import datatransferobjects.User;
import dataaccesslayer.UserDAO;
import datatransferobjects.Planner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexd
 */
public class PostgresUserDAO extends PostgresAbstractDAO<User> implements UserDAO {

    /**
     * Returns the <code>User</code> object builded on the current row of the
     * ResultSet <code>rs</code>.
     *
     * @param rs the ResultSet with which to build the <code>User</code> object
     * @return the <code>User</code> object builded on the current row of the
     * ResultSet <code>rs</code>
     * @throws SQLException if a database access error occurs
     */
    @Override
    User convertToEntity(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        User dbUser = null;
        if ("planner".equals(role)) {
            dbUser = new Planner(rs.getInt("id"), rs.getString("username"),
                    rs.getString("password"));
        } else if ("maintainer".equals(role)) {
            dbUser = new Maintainer(rs.getInt("id"), rs.getString("username"),
                    rs.getString("password"));
        }
        return dbUser;
    }

    /**
     * Retrieves the <code>User</code> object with given <code>id</code> from
     * the Postgres Database. Returns the <code>User</code> object with given
     * <code>id</code> if it exists in the Postgres Database; <code>null</code>
     * if the <code>User</code> object with given <code>id</code> doesn't exist
     * in the Postgres Database or if the operation fails.
     *
     * @param id the id which identifies the user
     * @return the <code>User</code> object with given <code>id</code> if it
     * exists in the Postgres Database, returns <code>null</code> if the
     * <code>User</code> object with given <code>id</code> doesn't exist in the
     * Postgres Database or if the operation fails
     */
    @Override
    public User get(int id) {
        String query = String.format("select * from users "
                + "where id = %d;", id);
        try {
            return executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresUserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retrieves a <code>Set</code> of <code>User</code> objects with maintainer
     * role from the Postgres Database. Returns the <code>Set</code> of
     * <code>User</code> objects with maintainer role if the operation is
     * successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>User</code> objects with maintainer
     * role from the Postgres Database if the operation is successful;
     * <code>null</code> otherwise
     */
    @Override
    public Set<User> getAllMaintainers() {
        String query = "select * from users where role = 'maintainer';";
        try {
            return executeSetQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresUserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retrieves a <code>Set</code> of <code>User</code> objects from the
     * Postgres Database. Returns the <code>Set</code> of <code>User</code>
     * objects if the operation is successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>User</code> objects from the
     * Postgres Database if the operation is successful; <code>null</code>
     * otherwise
     */
    @Override
    public Set<User> getAll() {
        String query = "select * from users;";
        try {
            return executeSetQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresUserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
