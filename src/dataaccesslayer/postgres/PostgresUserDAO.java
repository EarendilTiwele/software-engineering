/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Maintainer;
import datatransferobjects.User;
import dataaccesslayer.UserDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class PostgresUserDAO extends PostgresAbstractDAO<User> implements UserDAO {

    @Override
    public User convertToEntity(ResultSet rs) throws SQLException {
        // rs.getString("role") to control which user to return
        User dbUser = new Maintainer(rs.getInt("id"), rs.getString("username"), 
                                     rs.getString("password"));
        return dbUser;
    }
    
    /** 
     * Retrieve a user with given id from a database
     * @param id the id which identifies the user
     * @return the user retrieved
     * @throws java.sql.SQLException
     */
    @Override
    public User get(int id) throws SQLException {
        String query = String.format("select * from users "
                                   + "where id = %d;", id);
        return executeQuery(query);
    }
    
    /**
     * Retrieve all the users with maintainer role from a database
     * @return the set of users retrieved
     * @throws java.sql.SQLException
     */
    @Override
    public Set<User> getAllMaintainers() throws SQLException {
        String query = "select * from users where role = 'maintainer';";
        return executeSetQuery(query);
    }
    
}
