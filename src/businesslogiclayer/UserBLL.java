/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.UserDALDatabase;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class UserBLL {
    
    private final UserDALDatabase userDAL;
    
    public UserBLL() {
        userDAL = new UserDALDatabase();
    }
    
    public User get(int id) throws SQLException {
        return userDAL.get(id);
    }
    
    public Set<User> getAllMaintainers() throws SQLException {
        return userDAL.getAllMaintainers();
    }
    
    
}
