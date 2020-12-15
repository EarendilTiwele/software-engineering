/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatransferobjects;

/**
 * Planner class for a planner in the system.
 *
 * @author carbo
 */
public class Planner extends User {

    /**
     * Construct a planner specifying his id, username and password.
     *
     * @param id the id of this planner
     * @param username the username of this planner
     * @param password the password of this planner
     */
    public Planner(int id, String username, String password) {
        super(id, username, password);
    }

    /**
     * Construct a planner specifying his username and password. The id
     * associated with this planner will not be significant.
     *
     * @param username the username of this planner
     * @param password the password of this planner
     */
    public Planner(String username, String password) {
        super(username, password);
    }

    /**
     * Returns the role of this planner. The role is
     * <code>User.Role.PLANNER</code>.
     *
     * @return the role of this planner
     */
    @Override
    public Role getRole() {
        return User.Role.PLANNER;
    }
}
