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
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class UserBO {

    private final UserDAO userDAO;
    private final MaintainerSkillsBO mhcBO;

    public UserBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        userDAO = postgresFactory.getUserDAO();
        mhcBO = new MaintainerSkillsBO();
    }

    /**
     * Retrieves the <code>User</code> object with given <code>id</code> from a
     * persistent storage. Returns the <code>User</code> object with given
     * <code>id</code> if it exists in the persistent storage; <code>null</code>
     * if the <code>User</code> object with given <code>id</code> doesn't exist
     * in the persistent storage or if the operation fails.
     *
     * @param id the id which identifies the site
     * @return the <code>User</code> object with given <code>id</code> if it
     * exists in the persistent storage, returns <code>null</code> if the
     * <code>User</code> object with given <code>id</code> doesn't exist in the
     * persistent storage or if the operation fails
     */
    public User get(int id) {
        return userDAO.get(id);
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
        for (User user : userDAO.getAllMaintainers()) {
            Maintainer maintainer = new Maintainer(user.getId(),
                    user.getUsername(), user.getPassword());
            Set<Competency> competencies = mhcBO.getAllCompetencies(maintainer);
            for (Competency competency : competencies) {
                maintainer.addCompetency(competency);
            }
            maintainers.add(maintainer);
        }
        return maintainers;
    }

}
