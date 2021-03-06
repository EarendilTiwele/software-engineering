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
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * PostgresUserDAO class test.
 * 
 * @author alexd
 */
public class PostgresUserDAOTest {

    private static PostgresUserDAO postgresUserDAO;
    private static Connection connection;
    private static PostgresUserUtils userUtils;

    public PostgresUserDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        postgresUserDAO = new PostgresUserDAO();
        connection = PostgresDAOFactory.createConnection();
        connection.setAutoCommit(false);
        userUtils = new PostgresUserUtils(connection);
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
        connection.rollback();
        //PreparedStatement preparedStatement = connection.prepareStatement("delete from users;");
        //preparedStatement.execute();
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
    private void insertUser(int id, String username, String password, String role) throws SQLException {
        userUtils.insertUser(id, username, password, role);
    }

    /**
     * Insert a maintainer in the database.
     *
     * @param id the id of this maintainer
     * @param username the username of this maintainer
     * @param password the password of this maintainer
     * @throws SQLException if an error occurs
     */
    private void insertMaintainer(int id, String username, String password) throws SQLException {
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
    private void insertPlanner(int id, String username, String password) throws SQLException {
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
    private User retrieveUser(int id) throws SQLException {
        return userUtils.retrieveUser(id);
    }

    /**
     * Test of get method, of class PostgresUserDAO.
     * <ul>
     * <li>Insert a maintainer in the database with <code>id = 1</code></li>
     * <li>Create a local maintainer with same fields of the one inserted
     * before</li>
     * <li>Check if the maintainer retrieved from the database with
     * <code>id = 1</code> is equals to the local maintainer</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetOneMaintainer() throws SQLException {
        int id = 1;
        insertMaintainer(id, "test", "test");
        User localUser = new Maintainer(id, "test", "test");
        assertEquals(postgresUserDAO.get(id), localUser);
    }

    /**
     * Test of get method, of class PostgresUserDAO.
     * <ul>
     * <li>Insert a planner in the database with <code>id = 1</code></li>
     * <li>Create a local planner with same fields of the one inserted
     * before</li>
     * <li>Check if the planner retrieved from the database with
     * <code>id = 1</code> is equals to the local planner</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetOnePlanner() throws SQLException {
        int id = 1;
        insertPlanner(id, "test", "test");
        User localUser = new Planner(id, "test", "test");
        assertEquals(postgresUserDAO.get(id), localUser);
    }

    /**
     * Test of get method, of class PostgresUserDAO. Test case: search for user
     * whose id is 1 and the database contains a maintainer with id=2,
     * name='name', password='password'. The method should return null.
     *
     * @throws SQLException
     */
    @Test
    public void testGetNoMatch() throws SQLException {
        int id = 1;
        insertMaintainer(2, "name", "password");
        assertNull(postgresUserDAO.get(id));
    }

    /**
     * Test of get method, of class PostgresUserDAO. Test case: search for user
     * whose id is 1 and the database is empty. The method should return null.
     *
     * @throws SQLException
     */
    @Test
    public void testGetEmpty() throws SQLException {
        int id = 1;
        assertNull(postgresUserDAO.get(id));
    }

    /**
     * Test of getAllMaintainers method, of class PostgresUserDAO.
     * <ul>
     * <li>Insert one maintainer in the database with <code>id = 1</code></li>
     * <li>Create a local set of users adding the same maintainer inserted
     * before</li>
     * <li>Check if the set of users retrieved from the database is equals to
     * the local set of users</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllMaintainersOneMaintainer() throws SQLException {
        insertMaintainer(1, "test", "test");
        Set<User> localMaintainers = new HashSet<>();
        localMaintainers.add(new Maintainer(1, "test", "test"));
        assertEquals(localMaintainers, postgresUserDAO.getAllMaintainers());
    }

    /**
     * Test of getAllMaintainers method, of class PostgresUserDAO.
     * <ul>
     * <li>Insert three maintainers in the database with
     * <code>id = 1, 2, 3</code></li>
     * <li>Create a local set of users adding the same maintainers inserted
     * before</li>
     * <li>Check if the set of users retrieved from the database is equals to
     * the local set of users</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllMaintainersMoreMaintainers() throws SQLException {
        insertMaintainer(1, "test", "test");
        insertMaintainer(2, "test2", "test2");
        insertMaintainer(3, "test3", "test3");
        Set<User> localMaintainers = new HashSet<>();
        localMaintainers.add(new Maintainer(1, "test", "test"));
        localMaintainers.add(new Maintainer(2, "test2", "test2"));
        localMaintainers.add(new Maintainer(3, "test3", "test3"));
        assertEquals(localMaintainers, postgresUserDAO.getAllMaintainers());
    }

    /**
     * Test of getAllMaintainers method, of class PostgresUserDAO.
     * <ul>
     * <li>Insert three maintainers and three planners in the database with
     * <code>id = 1, 2, 3 - 4, 5, 6</code></li>
     * <li>Create a local set of users adding the same users inserted
     * before</li>
     * <li>Check if the set of users retrieved from the database is equals to
     * the local set of users (that contains only the maintainers inserted
     * before)</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllMaintainersMixedUsers() throws SQLException {
        insertMaintainer(1, "test", "test");
        insertMaintainer(2, "test2", "test2");
        insertMaintainer(3, "test3", "test3");
        insertPlanner(4, "test4", "test4");
        insertPlanner(5, "test5", "test5");
        insertPlanner(6, "test6", "test6");

        Set<User> localMaintainers = new HashSet<>();
        localMaintainers.add(new Maintainer(1, "test", "test"));
        localMaintainers.add(new Maintainer(2, "test2", "test2"));
        localMaintainers.add(new Maintainer(3, "test3", "test3"));
        assertEquals(localMaintainers, postgresUserDAO.getAllMaintainers());
    }

    /**
     * Test of getAllMaintainers method, of class PostgresUserDAO. Test case:
     * database empty. The method should return an empty set.
     */
    @Test
    public void testGetAllMaintainersEmpty() {
        assertTrue(postgresUserDAO.getAllMaintainers().isEmpty());
    }

    /**
     * Test of getAll method, of class PostgresUserDAO. Test case: no users in
     * the database.
     *
     * @throws SQLException
     */
    @Test
    public void testGetAllEmpty() throws SQLException {
        Set<User> actualUsers = postgresUserDAO.getAll();
        assertTrue(actualUsers.isEmpty());
    }

    /**
     * Test of getAll method, of class PostgresUserDAO. Test case: one
     * maintainer in the database with id=1, username="username",
     * password="password".
     *
     * @throws SQLException
     */
    @Test
    public void testGetAllOneMaintainer() throws SQLException {
        insertMaintainer(1, "username", "password");
        Set<User> expectedUsers = new HashSet<>();
        expectedUsers.add(new Maintainer(1, "username", "password"));
        Set<User> actualUsers = postgresUserDAO.getAll();
        assertEquals(expectedUsers, actualUsers);
    }

    /**
     * Test of getAll method, of class PostgresUserDAO. Test case: one planner
     * in the database with id=1, username="username", password="password".
     *
     * @throws SQLException
     */
    @Test
    public void testGetAllOnePlanner() throws SQLException {
        insertPlanner(1, "username", "password");
        Set<User> expectedUsers = new HashSet<>();
        expectedUsers.add(new Planner(1, "username", "password"));
        Set<User> actualUsers = postgresUserDAO.getAll();
        assertEquals(expectedUsers, actualUsers);
    }

    /**
     * Test of getAll method, of class PostgresUserDAO. Test case: three
     * planners (ids = 1, 2, 3) and three maintainers (ids = 4, 5, 6)
     *
     * @throws SQLException
     */
    @Test
    public void testGetAllMoreUsers() throws SQLException {
        insertPlanner(1, "planner1", "password");
        insertPlanner(2, "planner2", "password");
        insertPlanner(3, "planner3", "password");
        insertMaintainer(4, "maintainer1", "password");
        insertMaintainer(5, "maintainer2", "password");
        insertMaintainer(6, "maintainer3", "password");

        Set<User> expectedUsers = new HashSet<>();
        expectedUsers.add(new Planner(1, "planner1", "password"));
        expectedUsers.add(new Planner(2, "planner2", "password"));
        expectedUsers.add(new Planner(3, "planner3", "password"));
        expectedUsers.add(new Maintainer(4, "maintainer1", "password"));
        expectedUsers.add(new Maintainer(5, "maintainer2", "password"));
        expectedUsers.add(new Maintainer(6, "maintainer3", "password"));

        Set<User> actualUsers = postgresUserDAO.getAll();
        assertEquals(expectedUsers, actualUsers);
    }

    /**
     * Inserts a user in the database and asserts that the retrived one is equal
     * to the inserted one.
     *
     * @param user the user to insert
     * @throws SQLException
     */
    private void insertUserSuccess(User user) throws SQLException {
        int id = postgresUserDAO.insert(user);
        user.setId(id);
        User dbUser = retrieveUser(id);
        assertEquals(user, dbUser);
    }

    /**
     * Test of insert method, of class PostgresUserDAO. Test case: insert a
     * maintainer with username='username', password='password'. The id will be
     * automatically generated. The user retrieved from the database should be
     * equal to the inserted user.
     */
    @Test
    public void testInsertMaintainerSuccess() throws SQLException {
        User user = new Maintainer("username", "password");
        insertUserSuccess(user);
    }

    /**
     * Test of insert method, of class PostgresUserDAO. Test case: insert a
     * planner with username='username', password='password'. The id will be
     * automatically generated. The user retrieved from the database should be
     * equal to the inserted user.
     *
     * @throws SQLException
     */
    @Test
    public void testInsertPlannerSuccess() throws SQLException {
        User user = new Planner("username", "password");
        insertUserSuccess(user);
    }

    /**
     * Test of insert method, of class PostgresUserDAO. Test case: insert a
     * planner with username='username', password='password' and auto generated
     * id twice. Inserting two users with the same username, the second insert
     * operation should return -1.
     *
     * @throws SQLException
     */
    @Test
    public void testInsertPlannerFailure() throws SQLException {
        User user = new Planner("username", "password");
        int id = postgresUserDAO.insert(user);
        assertNotEquals(-1, id);
        id = postgresUserDAO.insert(user);
        assertEquals(-1, id);
    }

    /**
     * Test of insert method, of class PostgresUserDAO. Test case: insert a
     * maintainer with username='username', password='password' and auto
     * generated id twice. Inserting two users with the same username, the
     * second insert operation should return -1.
     *
     * @throws SQLException
     */
    @Test
    public void testInsertMaintainerFailure() throws SQLException {
        User user = new Maintainer("username", "password");
        int id = postgresUserDAO.insert(user);
        assertNotEquals(-1, id);
        id = postgresUserDAO.insert(user);
        assertEquals(-1, id);
    }

    /**
     * Test of update method, of class PostgresUserDAO. Test case: empty
     * database, update maintainer with id=1, username="name",password="pass".
     * The update operation should return true and the user should not be
     * inserted in the database.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateEmpty() throws SQLException {
        int id = 1;
        boolean success
                = postgresUserDAO.update(new Maintainer(id, "name", "pass"));
        assertTrue(success);
        assertNull(retrieveUser(id));
    }

    /**
     * Test of update method, of class PostgresUserDAO. Test case: non empty
     * database, update maintainer with id=1, username="name",password="pass"
     * that does not exist in the database. The update operation should return
     * true and the user should not be inserted.
     *
     * @throws SQLException
     */
    @Test
    public void testUpdateNonEmptyNoMatch() throws SQLException {
        int id = 1;
        insertMaintainer(2, "username", "password");
        boolean success
                = postgresUserDAO.update(new Maintainer(id, "name", "pass"));
        assertTrue(success);
        assertNull(retrieveUser(id));
    }

    /**
     * Test of update method, of class PostgresUserDAO. Test case: non empty
     * database, update maintainer with id=1, username="name",password="pass"
     * that exist in the database, changing the name="name2" and the
     * password="pass2". The update operation should return true and the user
     * should be correctly updated.
     *
     * @throws SQLException
     */
    @Test
    public void testUpdateSuccess() throws SQLException {
        int id = 1;
        insertMaintainer(id, "name", "pass");
        User newUser = new Maintainer(id, "name2", "pass2");
        boolean success
                = postgresUserDAO.update(newUser);
        assertTrue(success);
        User dbUser = retrieveUser(id);
        assertEquals(dbUser, newUser);
    }

    /**
     * Test of update method, of class PostgresUserDAO. Test case: non empty
     * database, update maintainer with id=1, username="name",password="pass"
     * that exist in the database, changing the name="name2" and the
     * password="pass2" when there is another user with this name. The update
     * operation should return false.
     *
     * @throws SQLException
     */
    @Test
    public void testUpdateFailure() throws SQLException {
        int id = 1;
        insertMaintainer(id, "name", "pass");
        insertMaintainer(id + 1, "name2", "pass");
        User newUser = new Maintainer(id, "name2", "pass2");
        boolean success
                = postgresUserDAO.update(newUser);
        assertFalse(success);
    }

    /**
     * Test of update method, of class PostgresUserDAO. Test case: change the
     * role of a maintainer (id=1, name='name', password='pass') when he is
     * associated to an activity. The update operation should fail.
     *
     * @throws SQLException
     */
    @Test
    public void testUpdateFailureToPlanner() throws SQLException {
        int id = 1;
        insertMaintainer(id, "name", "pass");
        assignFakeActivityToMaintainer(id);
        Planner planner = new Planner(id, "name", "pass");
        assertFalse(postgresUserDAO.update(planner));
    }

    /**
     * Assign a fake activity to the maintainer with the specified id.
     *
     * @param maintainerId the id of the maintainer
     *
     * @throws SQLException if an error occurs
     */
    private void assignFakeActivityToMaintainer(int maintainerId) throws SQLException {
        PreparedStatement statement
                = connection.prepareStatement("insert into assignment values (?, ?, ?, ?);");
        statement.setInt(1, maintainerId);
        int activityId = 2;
        //drop constraints if needed
        statement.setInt(2, activityId);
        int hour = 15;
        statement.setInt(3, hour);
        statement.setString(4, "Mon");
    }
}
