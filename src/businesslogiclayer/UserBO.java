/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import datatransferobjects.User;
import dataaccesslayer.DAOFactory;
import dataaccesslayer.UserDAO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class UserBO {

    private final UserDAO userDAO;
    private final MaintainerSkillsBO mhcBO;
    private final Cypher cypher;

    public UserBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        userDAO = postgresFactory.getUserDAO();
        mhcBO = new MaintainerSkillsBO();
        cypher = new Cypher();
    }
    
    /**
     * Encrypts the password of the specified user.
     * 
     * @param user the user
     * @return the same user passed in
     */
    private User encryptUser(User user){
        String password = user.getPassword();
        String encodedPassword = cypher.encode(password);
        user.setPassword(encodedPassword);
        return user;
    }
    
    /**
     * Decrypts the password of the specified user.
     * 
     * @param user the user
     * @return the same user passed in
     */
    private User decryptUser(User user){
        String encodedPassword = user.getPassword();
        String password = cypher.decode(encodedPassword);
        user.setPassword(password);
        return user;
    }

    /**
     * Inserts a <code>User</code> object in a persistent storage. The password
     * of the user will be encripted. Returns the id of the inserted
     * <code>user</code> if the operation is successful; -1 otherwise.<br>
     * <strong>NOTE</strong>: the password encryption will be performed on the
     * specified user. Thus, the password of the passed User object will change.
     * The specified user must have a non-encripted password.
     *
     * @param user the <code>User</code> object to insert
     * @return the id of the inserted <code>user</code> if the operation is
     * successful; -1 otherwise
     */
    public int insert(User user) {
        return userDAO.insert(encryptUser(user));
    }

    /**
     * Updates a <code>User</code> object in a persistent storage, if the
     * <code>user</code> is not present in the persistent storage it is not
     * created. The password of the user will be encripted. Returns
     * <code>true</code> if the operation is successful, that is both when the
     * <code>user</code> is updated and when the <code>user</code> doesn't exist
     * in the persistent storage; <code>false</code> otherwise.<br>
     * <strong>NOTE</strong>: the password encryption will be performed on the
     * specified user. Thus, the password of the passed User object will change.
     * The specified user must have a non-encripted password.
     *
     * @param user the <code>User</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the user is updated and when the user doesn't exist in the
     * persistent storage; <code>false</code> otherwise
     */
    public boolean update(User user) {
        return userDAO.update(encryptUser(user));
    }

    /**
     * Retrieves the <code>User</code> object with given <code>id</code> from a
     * persistent storage. Returns the <code>User</code> object with given
     * <code>id</code> if it exists in the persistent storage; <code>null</code>
     * if the <code>User</code> object with given <code>id</code> doesn't exist
     * in the persistent storage or if the operation fails.
     *
     * @param id the id which identifies the user
     * @return the <code>User</code> object with given <code>id</code> if it
     * exists in the persistent storage, returns <code>null</code> if the
     * <code>User</code> object with given <code>id</code> doesn't exist in the
     * persistent storage or if the operation fails
     */
    public User get(int id) {
        User user = userDAO.get(id);
        if (user == null) {
            return null;
        }
        return decryptUser(user);
    }

    /**
     * Retrieves a <code>List</code> of <code>User</code> objects from a
     * persistent storage. Returns the <code>List</code> of <code>User</code>
     * objects if the operation is successful; <code>null</code> otherwise.
     *
     * @return the <code>List</code> of <code>User</code> objects from the
     * persistent storage if the operation is successful; <code>null</code>
     * otherwise
     */
    public List<User> getAll() {
        Set<User> usersSet = userDAO.getAll();
        if (usersSet == null) {
            return null;
        }
        List<User> users = new ArrayList<>(usersSet);
        for (User user : users) {
            decryptUser(user);
        }
        return users;
    }

    /**
     * Retrieves a <code>Set</code> of <code>User</code> objects with maintainer
     * role from a persistent storage. Returns the <code>Set</code> of
     * <code>User</code> objects with maintainer role if the operation is
     * successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>User</code> objects with maintainer
     * role from the persistent storage if the operation is successful;
     * <code>null</code> otherwise
     */
    public Set<Maintainer> getAllMaintainers() {
        Set<Maintainer> maintainers = new HashSet<>();
        Set<User> users = userDAO.getAllMaintainers();
        if (users == null) {
            return null;
        }
        for (User user : users) {
            user = decryptUser(user);
            Maintainer maintainer = new Maintainer(user.getId(),
                    user.getUsername(), user.getPassword());

            Set<Competency> competencies = mhcBO.getAllCompetencies(maintainer);
            if (competencies != null) {
                for (Competency competency : competencies) {
                    maintainer.addCompetency(competency);
                }
            } else {
                return null;
            }
            maintainers.add(maintainer);
        }
        return maintainers;
    }

}
