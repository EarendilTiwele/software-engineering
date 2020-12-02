/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.UserDALDatabase;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class UserBLL {
    
    private final UserDALDatabase userDAL;
    private final MaintainerHasCompetenciesBLL mhcBLL;
    
    public UserBLL() {
        userDAL = new UserDALDatabase();
        mhcBLL = new MaintainerHasCompetenciesBLL();
    }
    
    public User get(int id) throws SQLException {
        return userDAL.get(id);
    }
    
    public Set<Maintainer> getAllMaintainers() throws SQLException {
        Set<Maintainer> maintainers = new HashSet<>();
        for (User user: userDAL.getAllMaintainers()) {
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
