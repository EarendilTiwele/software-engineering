/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Maintainer;
import businesslogiclayer.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author alexd
 */
public class UserDALDatabaseTest {
    
    private static UserDALDatabase userDAL;
    private static Connection conn;
    private static final int ID = 1;
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";
    private static final String ROLE = "maintainer";
    
    public UserDALDatabaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException {
        userDAL = new UserDALDatabase();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        Statement stm = conn.createStatement();
        stm.executeUpdate("delete from users where true;");
        stm.executeUpdate("alter table users drop constraint users_username_key;");
        stm.executeUpdate(String.format("insert into users "
                                      + "(id, username, password, role) "
                                      + "values (%d, '%s', '%s', '%s');"
                                      , ID, USERNAME, PASSWORD, ROLE));  
        stm.executeUpdate(String.format("insert into users "
                                      + "(id, username, password, role) "
                                      + "values (%d, '%s', '%s', '%s');"
                                      , ID + 1, USERNAME, PASSWORD, ROLE));  
        stm.executeUpdate(String.format("insert into users "
                                      + "(id, username, password, role) "
                                      + "values (%d, '%s', '%s', '%s');"
                                      , ID + 2, USERNAME, PASSWORD, ROLE));  
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.rollback();
        conn.close();
    }
    
    /**
     * Create a local user with same data of the one inserted in setUpClass
     * Get the user from db and compare it with the local user
     * @throws java.sql.SQLException
     */
    @Test
    public void testGet () throws SQLException {
        User localUser = new Maintainer(ID, USERNAME, PASSWORD);
        User dbUser = userDAL.get(1);
        assertEquals(localUser, dbUser);
        assertNull(userDAL.get(932109));
    }
    
    /**
     * Create a set of local users with same data of the ones inserted in setUpClass
     * Get the users from db and compare them with the local users
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllMaintainers () throws SQLException {
        Set<User> localUsers = new HashSet<>();
        localUsers.add(new Maintainer(ID, USERNAME, PASSWORD));
        localUsers.add(new Maintainer(ID + 1, USERNAME, PASSWORD));
        localUsers.add(new Maintainer(ID + 2, USERNAME, PASSWORD));
        Set<User> dbUsers = userDAL.getAllMaintainers();
        assertTrue(dbUsers.containsAll(localUsers));
    }
    
}
