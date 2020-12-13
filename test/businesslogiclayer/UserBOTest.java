/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.postgres.PostgresDAOFactory;
import dataaccesslayer.postgres.PostgresUserUtils;
import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import datatransferobjects.Planner;
import datatransferobjects.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * UserBO class test.
 *
 * @author carbo
 */
public class UserBOTest {

    private static UserBO userBO;
    private static Connection connection;
    //required to perform test operations on the database
    private static PostgresUserUtils userUtils;

    private final Cypher cypher = new Cypher();

    @BeforeClass
    public static void setUpClass() throws SQLException {
        userBO = new UserBO();
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
        //clear some tables
        String[] queries = {"delete from users;", "delete from competency;",
            "delete from maintainerhascompetencies;"};
        PreparedStatement preparedStatement;
        for (String query : queries) {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        }
    }

    @After
    public void tearDown() throws SQLException {
        connection.rollback();
        //PreparedStatement preparedStatement = connection.prepareStatement("delete from users;");
        //preparedStatement.execute();
    }

    /**
     * Stores in the persistent storage the specified competency associated to
     * the maintainer with the specified id.
     *
     * @param maintainerId the id of the maintainer
     * @param competency the competency
     * @param competencyId the id of the competency
     */
    private void addCompetencyToMaintainer(int maintainerId, String competency, int competencyId)
            throws SQLException {
        PreparedStatement preparedStatement
                = connection.prepareStatement("insert into competency values (?, ?);");
        preparedStatement.setInt(1, competencyId);
        preparedStatement.setString(2, competency);
        preparedStatement.execute();

        preparedStatement
                = connection.prepareStatement("insert into maintainerhascompetencies"
                        + " values (?, ?);");
        preparedStatement.setInt(1, maintainerId);
        preparedStatement.setInt(2, competencyId);
        preparedStatement.execute();
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
        User user = userUtils.retrieveUser(id);
        if (user == null) {
            return null;
        }
        String decodedPassword = cypher.decode(user.getPassword());
        user.setPassword(decodedPassword);
        return user;
    }

    /**
     * Insert a user with the specified parameters and asserts that it is
     * correctly stored.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param role the role of the user
     *
     * @throws SQLException if an error occurs
     */
    private void insertUserSuccess(String username, String password, User.Role role) throws SQLException {
        User user = null;
        if (role == User.Role.MAINTAINER) {
            user = new Maintainer(username, password);
        } else if (role == User.Role.PLANNER) {
            user = new Planner(username, password);
        }
        int id = userBO.insert(user);
        user.setId(id);
        user.setPassword(password);
        User storedUser = retrieveUser(id);
        assertEquals(storedUser, user);

    }

    /**
     * Test of insert method, of class UserBO. Test case: insert a Maintainer
     * with id=1, name='name', password='pass'. The user should be correctly
     * inserted in the persistent storage with an encripted password.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsertMaintainerSuccess() throws SQLException {
        insertUserSuccess("name", "pass", User.Role.MAINTAINER);
    }

    /**
     * Test of insert method, of class UserBO. Test case: insert a Planner with
     * id=1, name='name', password='pass'. The user should be correctly inserted
     * in the persistent storage with an encripted password.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsertPlannerSuccess() throws SQLException {
        insertUserSuccess("name", "pass", User.Role.PLANNER);
    }

    /**
     * Test of insert method, of class UserBO. Test case: insert a maintainer
     * with username='username', password='password' and auto generated id
     * twice. Inserting two users with the same username, the second insert
     * operation should return -1.
     *
     */
    @Test
    public void testInsertMaintainerFailure() {
        User user = new Maintainer("username", "password");
        userBO.insert(user);
        user = new Maintainer("username", "password");
        int res = userBO.insert(user);
        assertEquals(-1, res);
    }

    /**
     * Test of insert method, of class UserBO. Test case: insert a planner with
     * username='username', password='password' and auto generated id twice.
     * Inserting two users with the same username, the second insert operation
     * should return -1.
     *
     */
    @Test
    public void testInsertPlannerFailure() {
        User user = new Planner("username", "password");
        userBO.insert(user);
        user = new Planner("username", "password");
        int res = userBO.insert(user);
        assertEquals(-1, res);
    }

    /**
     * Test of update method, of class UserBO. Test case: empty storage, update
     * maintainer with id=1, username="name",password="pass". The update
     * operation should return true and the user should not be inserted in the
     * persistent storage.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testUpdateEmpty() throws SQLException {
        int id = 1;
        User user = new Maintainer(id, "name", "pass");
        boolean success = userBO.update(user);
        assertTrue(success);
        assertNull(retrieveUser(id));
    }

    /**
     * Test of update method, of class UserBO. Test case: non empty storage,
     * update maintainer with id=1, username="name",password="pass" that does
     * not exist in the storage. The update operation should return true and the
     * user should not be inserted.
     *
     * @throws SQLException
     */
    @Test
    public void testUpdateNonEmptyNoMatch() throws SQLException {
        int id = 1;
        insertMaintainer(2, "username", "password");
        boolean success
                = userBO.update(new Maintainer(id, "name", "pass"));
        assertTrue(success);
        assertNull(retrieveUser(id));
    }

    /**
     * Test of update method, of class UserBO. Test case: non empty storage,
     * update maintainer with id=1, username="name",password="pass" that exist
     * in the storage, changing the name="name2" and the password="pass2". The
     * update operation should return true and the user should be correctly
     * updated.
     *
     * @throws SQLException
     */
    @Test
    public void testUpdateSuccess() throws SQLException {
        int id = 1;
        insertMaintainer(id, "name", "pass");
        String newPassword = "pass2";
        String newName = "name2";
        User newUser = new Maintainer(id, newName, newPassword);
        boolean success = userBO.update(newUser);
        assertTrue(success);
        newUser.setPassword(newPassword);
        User storedUser = retrieveUser(id);
        assertEquals(storedUser, newUser);
    }

    /**
     * Test of update method, of class UserBO. Test case: non empty storage,
     * update maintainer with id=1, username="name",password="pass" that exist
     * in the storage, changing the name="name2" and the password="pass2" when
     * there is another user with this name. The update operation should return
     * false.
     *
     * @throws SQLException
     */
    @Test
    public void testUpdateFailure() throws SQLException {
        int id = 1;
        insertMaintainer(id, "name", "pass");
        insertMaintainer(id + 1, "name2", "pass");
        User newUser = new Maintainer(id, "name2", "pass2");
        boolean success = userBO.update(newUser);
        assertFalse(success);
    }

    //--------------------------------------------------------------------------
    /**
     * Test of get method, of class UserBO.
     * <ul>
     * <li>Insert a maintainer in the storage with <code>id = 1</code></li>
     * <li>Create a local maintainer with same fields of the one inserted
     * before</li>
     * <li>Check if the maintainer retrieved from the storage with
     * <code>id = 1</code> is equals to the local maintainer</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetOneMaintainer() throws SQLException {
        int id = 1;
        insertMaintainer(id, "test", cypher.encode("test"));
        User localUser = new Maintainer(id, "test", "test");
        assertEquals(localUser, userBO.get(id));
    }

    /**
     * Test of get method, of class UserBO.
     * <ul>
     * <li>Insert a planner in the storage with <code>id = 1</code></li>
     * <li>Create a local planner with same fields of the one inserted
     * before</li>
     * <li>Check if the planner retrieved from the storage with
     * <code>id = 1</code> is equals to the local planner</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetOnePlanner() throws SQLException {
        int id = 1;
        insertPlanner(id, "test", cypher.encode("test"));
        User localUser = new Planner(id, "test", "test");
        assertEquals(userBO.get(id), localUser);
    }

    /**
     * Test of get method, of class UserBO. Test case: search for user whose id
     * is 1 and the storage contains a maintainer with id=2, name='name',
     * password='password'. The method should return null.
     *
     * @throws SQLException
     */
    @Test
    public void testGetNoMatch() throws SQLException {
        int id = 1;
        insertMaintainer(2, "name", "password");
        assertNull(userBO.get(id));
    }

    /**
     * Test of get method, of class UserBO. Test case: search for user whose id
     * is 1 and the storage is empty. The method should return null.
     *
     * @throws SQLException
     */
    @Test
    public void testGetEmpty() throws SQLException {
        int id = 1;
        assertNull(userBO.get(id));
    }

    //--------------------------------------------------------------------------
    /**
     * Test of getAllMaintainers method, of class UserBO.
     * <ul>
     * <li>Insert one maintainer in the storage with <code>id = 1</code></li>
     * <li>Create a local set of users adding the same maintainer inserted
     * before</li>
     * <li>Check if the set of users retrieved from the storage is equals to the
     * local set of users</li>
     * </ul>
     *
     * Perform the test with and without competencies for the maintainer.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllMaintainersOneMaintainer() throws SQLException {
        int id = 1;
        insertMaintainer(1, "test", cypher.encode("test"));
        //no competency
        Maintainer maintainer = new Maintainer(1, "test", "test");
        Set<Maintainer> storedMaintainers = userBO.getAllMaintainers();
        assertEquals(1, storedMaintainers.size());
        assertEquals(maintainer, storedMaintainers.iterator().next());

        //add competency
        String competency = "competency";
        int competencyId = 1;
        addCompetencyToMaintainer(id, competency, competencyId);
        maintainer.addCompetency(new Competency(competencyId, competency));
        storedMaintainers = userBO.getAllMaintainers();
        assertEquals(1, storedMaintainers.size());
        assertEquals(maintainer, storedMaintainers.iterator().next());
    }

    /**
     * Test of getAllMaintainers method, of class UserBO.
     * <ul>
     * <li>Insert three maintainers in the storage with
     * <code>id = 1, 2, 3</code></li>
     * <li>Create a local set of users adding the same maintainers inserted
     * before</li>
     * <li>Check if the set of users retrieved from the storage is equals to the
     * local set of users</li>
     * </ul>
     * Perform the test with and without competencies for the maintainer.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllMaintainersMoreMaintainers() throws SQLException {
        insertMaintainer(1, "test1", cypher.encode("test1"));
        insertMaintainer(2, "test2", cypher.encode("test2"));
        insertMaintainer(3, "test3", cypher.encode("test3"));
        Maintainer maintainer1 = new Maintainer(1, "test1", "test1");
        Maintainer maintainer2 = new Maintainer(2, "test2", "test2");
        Maintainer maintainer3 = new Maintainer(3, "test3", "test3");
        Set<Maintainer> localMaintainers = new HashSet<>();
        localMaintainers.add(maintainer1);
        localMaintainers.add(maintainer2);
        localMaintainers.add(maintainer3);
        assertEquals(localMaintainers, userBO.getAllMaintainers());

        //add competencies
        Competency competency1 = new Competency(1, "competency1");
        Competency competency2 = new Competency(2, "competency2");
        Competency competency3 = new Competency(3, "competency3");

        maintainer1.addCompetency(competency1);
        maintainer2.addCompetency(competency2);
        maintainer3.addCompetency(competency3);

        addCompetencyToMaintainer(1, competency1.getDescription(), competency1.getId());
        addCompetencyToMaintainer(2, competency2.getDescription(), competency2.getId());
        addCompetencyToMaintainer(3, competency3.getDescription(), competency3.getId());

        Set<Maintainer> actualMaintainers = userBO.getAllMaintainers();
        localMaintainers = new HashSet<>();
        localMaintainers.add(maintainer1);
        localMaintainers.add(maintainer2);
        localMaintainers.add(maintainer3);
        assertEquals(localMaintainers, actualMaintainers);
    }

    /**
     * Test of getAllMaintainers method, of class UserBO.
     * <ul>
     * <li>Insert three maintainers and three planners in the storage with
     * <code>id = 1, 2, 3 - 4, 5, 6</code></li>
     * <li>Create a local set of users adding the same users inserted
     * before</li>
     * <li>Check if the set of users retrieved from the storage is equals to the
     * local set of users (that contains only the maintainers inserted
     * before)</li>
     * </ul>
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllMaintainersMixedUsers() throws SQLException {
        insertMaintainer(1, "test", cypher.encode("test"));
        insertMaintainer(2, "test2", cypher.encode("test2"));
        insertMaintainer(3, "test3", cypher.encode("test3"));
        insertPlanner(4, "test4", cypher.encode("test4"));
        insertPlanner(5, "test5", cypher.encode("test5"));
        insertPlanner(6, "test6", cypher.encode("test6"));

        Set<User> localMaintainers = new HashSet<>();
        localMaintainers.add(new Maintainer(1, "test", "test"));
        localMaintainers.add(new Maintainer(2, "test2", "test2"));
        localMaintainers.add(new Maintainer(3, "test3", "test3"));
        assertEquals(localMaintainers, userBO.getAllMaintainers());
    }

    /**
     * Test of getAllMaintainers method, of class UserBO. Test case: storage
     * empty. The method should return an empty set.
     */
    @Test
    public void testGetAllMaintainersEmpty() {
        assertTrue(userBO.getAllMaintainers().isEmpty());
    }

    //--------------------------------------------------------------------------
    /**
     * Test of getAll method, of class UserBO. Test case: no users in the
     * storage. The method should return an empty list.
     *
     * @throws SQLException
     */
    @Test
    public void testGetAllEmpty() throws SQLException {
        assertTrue(userBO.getAll().isEmpty());
    }

    /**
     * Test of getAll method, of class UserBO. Test case: one maintainer in the
     * storage with id=1, username="username", password="password".
     *
     * @throws SQLException
     */
    @Test
    public void testGetAllOneMaintainer() throws SQLException {
        insertMaintainer(1, "username", cypher.encode("password"));
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new Maintainer(1, "username", "password"));
        List<User> actualUsers = userBO.getAll();
        assertEquals(expectedUsers, actualUsers);
    }

    /**
     * Test of getAll method, of class UserBO. Test case: one planner in the
     * storage with id=1, username="username", password="password".
     *
     * @throws SQLException
     */
    @Test
    public void testGetAllOnePlanner() throws SQLException {
        insertPlanner(1, "username", cypher.encode("password"));
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new Planner(1, "username", "password"));
        List<User> actualUsers = userBO.getAll();
        assertEquals(expectedUsers, actualUsers);
    }

    /**
     * Test of getAll method, of class UserBO. Test case: three planners (ids =
     * 1, 2, 3) and three maintainers (ids = 4, 5, 6)
     *
     * @throws SQLException
     */
    @Test
    public void testGetAllMoreUsers() throws SQLException {
        String password = cypher.encode("password");
        insertPlanner(1, "planner1", password);
        insertPlanner(2, "planner2", password);
        insertPlanner(3, "planner3", password);
        insertMaintainer(4, "maintainer1", password);
        insertMaintainer(5, "maintainer2", password);
        insertMaintainer(6, "maintainer3", password);

        Set<User> expectedUsers = new HashSet<>();
        expectedUsers.add(new Planner(1, "planner1", "password"));
        expectedUsers.add(new Planner(2, "planner2", "password"));
        expectedUsers.add(new Planner(3, "planner3", "password"));
        expectedUsers.add(new Maintainer(4, "maintainer1", "password"));
        expectedUsers.add(new Maintainer(5, "maintainer2", "password"));
        expectedUsers.add(new Maintainer(6, "maintainer3", "password"));

        List<User> actualUsers = userBO.getAll();
        assertEquals(expectedUsers, new HashSet<>(actualUsers));
    }
}
