/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.User;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public interface UserDAO {
    
    /** 
     * Retrieve a user with given id from a persistent storage
     * @param id the id which identifies the user
     * @return the user retrieved
     * @throws java.sql.SQLException
     */
    public User get(int id) throws SQLException;
    
    /**
     * Retrieve all the users with maintainer role from a persistent storage
     * @return the set of users retrieved
     * @throws java.sql.SQLException
     */
    public Set<User> getAllMaintainers() throws SQLException;
    
}
