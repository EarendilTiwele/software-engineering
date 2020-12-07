/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public interface MaintainerSkillsDAO {
    
    /**
     * Retrieve all the competencies associated to a maintainer from a persistent storage
     * @param maintainer the maintainer with the associated competencies
     * @return the set of the competencies retrieved
     * @throws java.sql.SQLException
     */
    public Set<Competency> getAllCompetencies(Maintainer maintainer) throws SQLException;
    
}
