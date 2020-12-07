/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatransferobjects;

import java.util.Objects;

/**
 * User in the system.
 * 
 * @author carbo
 */
public abstract class User {
    
    private int id;
    private String username;
    private String password;

    private static int DEFAULT_ID = -1;
    
    public enum Role {PLANNER, MAINTAINER};
    
    /**
     * Construct an user specifying his id, username and password.
     * 
     * @param id            the id of this user
     * @param username      the username of this user
     * @param password      the password of this user
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    
    /**
     * Construct an user specifying his username and password.
     * The id associated with this user will not be significant.
     * 
     * @param username  the username of this user
     * @param password  the password of this user
     */
    public User(String username, String password){
        this(DEFAULT_ID, username, password);
    }

    /**
     * Returns the id associated with this user.
     * 
     * @return the user id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the username associated with this user.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password associated with this user.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the hash code for this user.
     * The hash code is computed based on the id, username and password.
     * 
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.id;
        hash = 43 * hash + Objects.hashCode(this.username);
        hash = 43 * hash + Objects.hashCode(this.password);
        return hash;
    }

    /**
     * Compares this user to the specified object.
     * The result is <code>true</code> if and only if the argument is not <code>null</code>
     * and is a <code>User</code> object that represents a user with
     * the same id, username, password and role as this object.
     * 
     * @param obj the object to compare this <code>User</code> against
     * @return <code>true</code> if the given object represents a <code>User</code>
     *         equivalent to this user, <code>false</code> otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.getRole(), other.getRole())) {
            return false;
        }
        return true;
    }
    
    /**
     * Returns the role of this user.
     * The role can be <code>User.Role.PLANNER</code> or
     * <code>User.Role.MAINTAINER</code>.
     * 
     * @return the role of this user
     */
    public abstract Role getRole();

    /**
     * Set the username for this user.
     * 
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set the password for this user.
     * 
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
    
    
    
}
