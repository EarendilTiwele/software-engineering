/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.User;
import dataaccesslayer.UserDAO;
import datatransferobjects.UserFactory;
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

    private final UserFactory userFactory = new UserFactory();

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
        String roleString = rs.getString("role");
        User.Role role = User.Role.valueOf(roleString.toUpperCase());
        return userFactory.createUser(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                role);
    }

    /**
     * Inserts a <code>User</code> object in the Postgres Database. Returns the
     * id of the inserted <code>user</code> if the operation is successful; -1
     * otherwise.
     *
     * @param user the <code>User</code> object to insert
     * @return the id of the inserted <code>user</code> if the operation is
     * successful; -1 otherwise
     */
    @Override
    public int insert(User user) {
        String query = String.format("insert into users "
                + "(username, password, role)"
                + "values ('%s', '%s', '%s') returning *;",
                user.getUsername(), user.getPassword(),
                user.getRole().toString().toLowerCase());
        try {
            return executeQuery(query).getId();
        } catch (SQLException ex) {
            Logger.getLogger(PostgresUserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    /**
     * Updates a <code>User</code> object in the Postgres Database, if the
     * <code>user</code> is not present in the Postgres Database it is not
     * created. Returns <code>true</code> if the operation is successful, that
     * is both when the <code>user</code> is updated and when the
     * <code>user</code> doesn't exist in the Postgres Database;
     * <code>false</code> otherwise.
     * <strong>NOTE</strong>: the update operation will fail trying to change
     * the role of a user from planner to maintainer if the user is associated
     * to an activity.
     *
     * @param user the <code>User</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the user is updated and when the user doesn't exist in the Postgres
     * Database; <code>false</code> otherwise
     */
    @Override
    public boolean update(User user) {
        String query = String.format("update users "
                + "set username = '%s', password = '%s', role = '%s'"
                + "where id = %d;",
                user.getUsername(), user.getPassword(),
                user.getRole().toString().toLowerCase(), user.getId());
        try {
            executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresUserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
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
