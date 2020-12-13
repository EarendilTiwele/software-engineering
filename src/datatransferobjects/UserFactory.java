/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatransferobjects;

/**
 *
 * @author alexd
 */
public class UserFactory {

    /**
     * Factory method to create a <code>User</code> object given his
     * <code>username</code>, <code>password</code> and <code>role</code>.
     * Returns the <code>User</code> object created with given data;
     * <code>null</code> if the given <code>userRole</code> is not defined.
     *
     * @param username the username of the user to create
     * @param password the password of the user to create
     * @param userRole the role of the user to create
     * @return the <code>User</code> object created with given data;
     * <code>null</code> if the given <code>userRole</code> is not defined
     */
    public User createUser(String username, String password, User.Role userRole) {
        switch (userRole) {
            case MAINTAINER:
                return new Maintainer(username, password);
            case PLANNER:
                return new Planner(username, password);
            default:
                return null;
        }
    }
}
