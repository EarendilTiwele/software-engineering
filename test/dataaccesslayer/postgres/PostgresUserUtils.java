/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Maintainer;
import datatransferobjects.Planner;
import datatransferobjects.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is for test purpose only. 
 * @author carbo
 */
public class PostgresUserUtils {

    private final Connection connection;

    public PostgresUserUtils(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a user in the database.
     *
     * @param id the id of this user
     * @param username the username of this user
     * @param password the password of this user
     * @param role the role of this user: "planner" or "maintainer"
     * @throws SQLException if an error occurs
     */
    public void insertUser(int id, String username, String password, String role) throws SQLException {
        PreparedStatement preparedStatement
                = connection.prepareStatement("insert into users values (?, ?, ?, ?);");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, username);
        preparedStatement.setString(3, password);
        preparedStatement.setString(4, role);
        preparedStatement.execute();
    }

    /**
     * Insert a maintainer in the database.
     *
     * @param id the id of this maintainer
     * @param username the username of this maintainer
     * @param password the password of this maintainer
     * @throws SQLException if an error occurs
     */
    public void insertMaintainer(int id, String username, String password) throws SQLException {
        insertUser(id, username, password, "maintainer");
    }

    /**
     * Inserts a planner in the database.
     *
     * @param id the id of this planner
     * @param username the username of this planner
     * @param password the password of this planner
     * @throws SQLException if an error occurs
     */
    public void insertPlanner(int id, String username, String password) throws SQLException {
        insertUser(id, username, password, "planner");
    }

    /**
     * Returns the User in the database with the specified id or null if no user
     * has the specified id.
     *
     * @param id the id of the user to retrieve
     * @return the user, or null if no user has the specified id
     * @throws SQLException
     */
    public User retrieveUser(int id) throws SQLException {
        PreparedStatement preparedStatement
                = connection.prepareStatement("select * from users where id = ?;");
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        User user = null;
        while (rs.next()) {
            user = convertToEntity(rs);
        }
        return user;
    }

    /**
     * Converts a row of the passed result set into a User.
     * 
     * @param rs the result set
     * @return the user
     * 
     * @throws SQLException if an error occurs
     */
    public User convertToEntity(ResultSet rs) throws SQLException {
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
}
