/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Maintainer;
import datatransferobjects.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alexd
 */
public class PostgresUserDAOTest {

    private static PostgresUserDAO postgresUserDAO;
    private static Connection connection;

    public PostgresUserDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        postgresUserDAO = new PostgresUserDAO();
        connection = PostgresDAOFactory.createConnection();
        connection.setAutoCommit(false);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Before
    public void setUp() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from users;");
        preparedStatement.execute();
    }

    @After
    public void tearDown() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from users;");
        preparedStatement.execute();
    }

    private void insertMaintainer(int id, String username, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into users values (?, ?, ?, 'maintainer');");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, username);
        preparedStatement.setString(3, password);
        preparedStatement.execute();
    }

    /**
     * Test of get method, of class PostgresUserDAO.
     * <ul>
     * <li>Insert a user in the database with <code>id = 1</code></li>
     * <li>Create a local user with same fields of the one inserted before</li>
     * <li>Check if the user retrieved from the database with
     * <code>id = 1</code> is equals to the local user</li>
     * <li>Check if the user retrieved from the database with
     * <code>id = 2</code> is null</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGet() throws SQLException {
        insertMaintainer(1, "test", "test");
        User localUser = new Maintainer(1, "test", "test");
        assertEquals(postgresUserDAO.get(1), localUser);
        assertNull(postgresUserDAO.get(2));
    }

    /**
     * Test of getAllMaintainers method, of class PostgresUserDAO.
     * <ul>
     * <li>Insert three users in the database with
     * <code>id = 1, 2, 3</code></li>
     * <li>Create a local set of users adding the same users inserted
     * before</li>
     * <li>Check if the set of users retrieved from the database is equals to
     * the local set of users</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllMaintainers() throws SQLException {
        insertMaintainer(1, "test", "test");
        insertMaintainer(2, "test2", "test2");
        insertMaintainer(3, "test3", "test3");
        Set<User> localMaintainers = new HashSet<>();
        localMaintainers.add(new Maintainer(1, "test", "test"));
        localMaintainers.add(new Maintainer(2, "test2", "test2"));
        localMaintainers.add(new Maintainer(3, "test3", "test3"));
        assertEquals(localMaintainers, postgresUserDAO.getAllMaintainers());
    }

}
