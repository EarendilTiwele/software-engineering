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
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class UserBO {
    
    private final UserDAO userDAO;
    private final MaintainerSkillsBO mhcBLL;
    
    public UserBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        userDAO = postgresFactory.getUserDAO();
        mhcBLL = new MaintainerSkillsBO();
    }
    
    public User get(int id) throws SQLException {
        return userDAO.get(id);
    }
    
    public Set<Maintainer> getAllMaintainers() throws SQLException {
        Set<Maintainer> maintainers = new HashSet<>();
        for (User user: userDAO.getAllMaintainers()) {
            Maintainer maintainer = new Maintainer(user.getId(), 
                                    user.getUsername(), user.getPassword());
            Set<Competency> competencies = mhcBLL.getAllCompetencies(maintainer);
            for (Competency competency: competencies) {
                maintainer.addCompetency(competency);
            }
            maintainers.add(maintainer);
        }
        return maintainers;
    }
    
    
}
